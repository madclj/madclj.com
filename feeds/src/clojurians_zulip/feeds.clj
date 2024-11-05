(ns clojurians-zulip.feeds
  (:require [clojure.data.json :as json]
            [clojure.data.xml :as xml]
            [clojure.java.io :as io]
            [clojure.set :as set]
            [clojure.string :as str]
            [markdown.core :as md]
            [tick.core :as t]))

(defn err [& msgs]
  (binding [*out* *err*]
    (apply prn msgs)))

(defn- linkify [content]
  (str/replace content  #"(?<![>])(?<!\]\()(?!https://[-\w/.-\\#]*[\w/-]+(?:<|\]\())https://[-\w/.-\\#]*[\w/-]+" "<$0>"))

(defn- timestamp->instant [ts]
  (t/instant (* 1000 ts)))


(defn- msg->title [msg]
  (let [str-truncate (fn [s {:keys [max-length]}]
                       (if (< (count s) max-length)
                         s
                         (str (str/join (take max-length s)) "...")))]
    (str-truncate (first (str/split-lines (str/replace msg #"^[^ ]+ " ""))) {:max-length 75})))


(defn- msg->author [msg]
  (nth (re-find #"^\*\*([^:]+)" msg) 1))


(defn- index-by [by-fn coll]
  (->> coll
       (map (juxt by-fn identity))
       (into {})))


(defn- read-messages [json-file {:keys [limit older-than] :or {limit 10 older-than (t/now)}}]
  (-> json-file
      (io/file)
      (io/reader)
      (json/read :key-fn #(keyword (str/replace % "_" "-")))
      :messages
      (->> (sort-by :timestamp >)
           (map #(update % :timestamp timestamp->instant))
           (map #(assoc % :html-content (-> % :content linkify md/md-to-html-string)))
           (filter #(-> % :timestamp (t/< older-than)))
           (take limit))))

(def event-time-re #"(\d{4}-\d{2}-\d{2}T\d{2}:\d{2})(?:--(\d{2}:\d{2}))?")

(def event-announcement (every-pred (comp #(re-find #"\d{4}-\d{2}-\d{2}" %) :subject)
                                    (comp #(re-find #"^\*\*Title\*\*" %) :content)))

(defmulti assoc-event-start-end (fn [event]
                                  (cond
                                    (contains? event :time) :time
                                    :else :start-end)))
(comment
  (def dt "2020-12-08T18:30Z ([local time](https://time.is/18:30_8_DECEMBER_2020_in_UTC))")
  (def dt2 "2020-12-08T18:30+01:00 ([local time](https://time.is/18:30_8_DECEMBER_2020_in_UTC))")
  (def dt3 "2020-12-08T18:30-01:00 ([local time](https://time.is/18:30_8_DECEMBER_2020_in_UTC))")


  (t/zoned-date-time "2020-12-04T12:30+01:00")
  )
(defmethod assoc-event-start-end :time
  [{time :time :as event}]
  (let [[_ from to]      (re-find event-time-re time)
        default-duration (t/new-duration 2 :hours)
        start            (t/parse-time from)
        end              (if to (t/on (t/time to) start) (t/+ start default-duration))
        end              (if (t/< end start) (t/+ end (t/new-duration 1 :days)) end)]
    (assoc event :start start :end end)))

(defmethod assoc-event-start-end :start-end
  [{start-str :start end-str :end :as event}]
  (let [start-end-re #"(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}(?:Z|[+-]\d{2}:\d{2}))"
        parse-dt     #(as-> %  $ (re-find start-end-re $) (second $) (t/zoned-date-time $) (t/in $ "UTC"))
        start        (parse-dt start-str)
        end          (parse-dt end-str)]
    (assoc event :start start :end end)))


(defn- msg->event [{:keys [id content subject] :as _msg}]
 (set/rename-keys
  (->> content
       (re-seq #"\*\*(Title|Time|Start|End|Description|Location|RSVP|URL)\*\*: +([\s\S]*?)(?=\n\*\*|$)")
       (map (juxt second last))
       (into {:id id :summary (-> subject (str/split #"\s" 2) last)}))
  {"Title"       :full-title
   "Description" :description
   "RSVP"        :rsvp
   "URL"         :url
   "Time"        :time
   "Start"       :start
   "End"         :end
   "Location"    :location}))


(defn- render-datetime [dt]
  (let [formatter (fn [fmt] #(t/format (t/formatter fmt) %))
        [date time] ((juxt (formatter "uuuuMMdd") (formatter "HHmm")) dt)]
    (format "%sT%s00Z" date time)))

(defn- ical-doc [events]
  [["BEGIN" "VCALENDAR"]
   ["VERSION" "2.0"]
   ["PRODID" "-//hacksw/handcal//NONSGML v1.0//EN"]
   ["X-WR-CALNAME" "Madison Clojure Events"]
   (for [{:keys [full-title start end summary description url rsvp id location]} events
         :let [zulip-url        (str "https://clojurians.zulipchat.com/#narrow/stream/262224-events/near/" id)
               ical-description (cond-> ""
                                  (not= full-title summary) (str full-title "\n\n")
                                  :always (str description "\n\n" "Zulip: " zulip-url)
                                  :escape (str/replace #"\n" "\\\\n"))]]
     [["BEGIN" "VEVENT"]
      ["UID" (str "message_" id "@clojurians.zulipchat.com")]
      ["DTSTART" (render-datetime start)]
      ["DTEND" (render-datetime end)]
      ["SUMMARY" summary]
      ["LOCATION" (or location "online")]
      ["DESCRIPTION" ical-description]
      (when (or url rsvp)
        ["URL" (or url rsvp)])
      ["END" "VEVENT"]])
   ["END" "VCALENDAR"]])

(defn- render-ical-doc [doc]
  (str/join "\n"
            (map #(str/join ":" %)
                 (filter some?
                         (reduce (fn [acc doc]
                                   (if (seq? doc)
                                     (reduce into acc doc)
                                     (conj acc doc))) [] doc)))))


(defn events
  "Create events-feed"
  [{:keys [messages-file]}]
  (let [valid?         (some-fn
                         (every-pred #(every? % #{:summary :description :start :end})
                                     (comp #(re-find event-time-re (str %)) :start)
                                     (comp #(re-find event-time-re (str %)) :end))
                         (every-pred #(every? % #{:summary :description :time})
                                     (comp #(re-find event-time-re (str %)) :time)))
        events         (map msg->event
                            (filter event-announcement
                                    (read-messages messages-file {:limit 1000})))
        ical-events    (filter valid? events)
        invalid-events (filter (complement valid?) events)]
    (when (seq invalid-events)
      (err :error/invalid-events invalid-events))
    (println  (render-ical-doc (ical-doc (map assoc-event-start-end ical-events))))))


(defn announcements
  "Create announce-feed"
  [{:keys [messages-file]}]
  (let [messages (read-messages messages-file {:limit      40
                                               :older-than (t/ago (t/new-duration 2 :hours))})]
    (println (xml/emit-str
               (xml/sexp-as-element
                 [:feed {:xmlns "http://www.w3.org/2005/Atom"}
                  [:link {:rel "self" :href "https://www.clojurians-zulip.org/feeds/announcements.rss"}]
                  [:id "https://clojurians.zulipchat.com/#narrow/stream/150792-announce/topic/announcements.40slack"]
                  [:title "Clojurians Announcements"]
                  [:updated (t/instant)]
                  (for [{:keys [timestamp id content html-content]} messages]
                    [:entry
                     [:id (str "https://clojurians.zulipchat.com/#narrow/stream/150792-announce/topic/announcements.40slack/near/" id)]
                     [:title (msg->title content)]
                     [:link {:href (str "https://clojurians.zulipchat.com/#narrow/stream/180378-slack-archive/topic/announcements/near/" id)}]
                     [:content {:type "html"} html-content]
                     [:updated timestamp]
                     [:author [:name (msg->author content)]]])])))))
