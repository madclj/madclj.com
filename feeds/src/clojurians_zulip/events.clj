(ns clojurians-zulip.events
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [clojure.string :as str]
            [tick.core :as t]
            [clojurians-zulip.events.scraper :refer [scrape]]))


(defn- post-event [{:keys [auth topic msg to]}]
  (http/post "https://clojurians.zulipchat.com/api/v1/messages"
             {:basic-auth  auth
              :form-params {:type    "stream"
                            :to      to
                            :topic   topic
                            :content msg}}))

(defn- ->time-is-url [dt]
  (let [dt-utc (t/in dt "UTC")]
    (format "https://time.is/%s_%d_%s_%s_in_UTC" (t/time dt-utc) (t/day-of-month dt-utc) (t/month dt-utc) (t/year dt-utc))))


(defn- validate [data atts]
  (reduce (fn [acc att]
            (if (seq (get acc att))
              acc
              (update acc ::errors assoc att :missing))) data atts))

(comment
  (validate {:start "foo"} [:start])
  (update {} ::errors assoc :b :missing))

(defn ^#:inclined{:option.start       {:desc "Zoned date time e.g. '2021-01-13T13:00-07:00' or for UTC '2021-01-13T13:00Z'"}
                  :option.duration    {:desc "Duration in hours"
                                       :type :int}
                  :option.title       {:desc "Title (max length: 45)"}
                  :option.description {:desc "Description"}
                  :option.url         {:desc "Url for more info/rsvp"}
                  :option.zulip-auth! {:desc "e.g. 'your@email.org:<API-token-from-Zulip-settings>'"}
                  :option.dev?        {:default false}
                  :option.confirm?    {:desc    "Show preview and confirm before posting"
                                       :default true}}
  create
  "Post new event to Zulip event-stream

- You'll be asked for confirmation before posting. Add flag '--no-confirm' to suppress this.
- Any corrections after creation can be made via Zulip (see the link that the create-command echos).
- All events will (eventually) appear on the caldendar-feed: https://www.clojurians-zulip.org/feeds/events.ics
- When providing a url of a meetup.com or clojureverse.org event (e.g. \"https://www.meetup.com/london-clojurians/events/288373088/\") you don't need to provide `title`, `description` and `start` (though you _can_ in order to override them).
"
  [{:keys [start duration title description url dev? confirm? zulip-auth] :as _all}]
  (let [{:keys  [url start till title description duration]
         errors ::errors}
        (cond-> {}
          url         (merge (scrape url) {:url url})
          start       (assoc :start start)
          title       (assoc :title title)
          duration    (assoc :duration duration)
          description (assoc :description description)
          :always     (validate [:start :title :description]))
        _                       (when (seq errors) (println (str "Error: Please provide flags " (keys errors))) (System/exit 1))
        zoned-start             (t/zoned-date-time start)
        zoned-till              (if (or (not till) duration)
                                  (t/+ zoned-start (t/new-duration (or duration 2) :hours))
                                  (t/zoned-date-time till))
        formatted-date          (t/format :iso-local-date zoned-start)
        topic                   (str formatted-date " " title)
        msg                     (cond-> (str "**Title**: " title \newline
                                             "**Start**: " (str zoned-start) " ([local time](" (->time-is-url zoned-start) "))" \newline
                                             "**End**: " (str zoned-till) \newline
                                             "**Description**: " description)
                                  (seq url) (str \newline "**URL**: " url))
        [stream-name stream-id] (if dev?
                                  ["events-dev" 264019]
                                  ["events" 262224])]
    (when confirm?
      (println (format "The following topic will be created in stream '%s':" stream-name))
      (println (str "Topic: " \newline topic))
      (println (str "Message: " \newline msg))
      (print "Continue? [y/N] ")
      (flush)
      (when-not (= "y" (read-line)) (System/exit 1)))
    (let [{:keys [id]} (json/read-json (:body (post-event {:auth zulip-auth :to stream-id :topic topic :msg msg})))
          msg-url      (str "https://clojurians.zulipchat.com/#narrow/stream/" stream-id "-" stream-name "/near/" id)]
      (println msg-url))))
