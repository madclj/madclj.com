(ns madison-clojure.ical
  (:require [clojure.string :as str]
            [clojure.edn :as edn]
            [tick.core :as t])
  (:import [java.time ZonedDateTime ZoneId]))

(def event-time-re #"(\d{4}-\d{2}-\d{2}T\d{2}:\d{2})(?:--(\d{2}:\d{2}))?")

(comment
  (def dt "2020-12-08T18:30Z ([local time](https://time.is/18:30_8_DECEMBER_2020_in_UTC))")
  (def dt2 "2020-12-08T18:30+01:00 ([local time](https://time.is/18:30_8_DECEMBER_2020_in_UTC))")
  (def dt3 "2020-12-08T18:30-01:00 ([local time](https://time.is/18:30_8_DECEMBER_2020_in_UTC))")

  (render-datetime (t/zoned-date-time "2020-12-04T12:30+01:00"))
  )

(defn render-datetime [^ZonedDateTime dt]
  (let [dt (.withZoneSameInstant dt (ZoneId/of "UTC"))
        formatter (fn [fmt] #(t/format (t/formatter fmt) %))
        [date time] ((juxt (formatter "uuuuMMdd") (formatter "HHmm")) dt)]
    (format "%sT%s00Z" date time)))

(defn ical-doc [events]
  [["BEGIN" "VCALENDAR"]
   ["VERSION" "2.0"]
   ["PRODID" "-//hacksw/handcal//NONSGML v1.0//EN"]
   ["X-WR-CALNAME" "Madison Clojure Events"]
   (for [{:keys [full-title start end summary description url rsvp id location]} events
         :let [ical-description (cond-> ""
                                  (not= full-title summary) (str full-title "\n\n")
                                  :always (str description "\n\n" "RSVP: " rsvp)
                                  :escape (str/replace #"\n" "\\\\n"))]]
     [["BEGIN" "VEVENT"]
      ["UID" rsvp]
      ["DTSTART" (render-datetime start)]
      ["DTEND" (render-datetime end)]
      ["SUMMARY" summary]
      ["LOCATION" (or location "online")]
      ["DESCRIPTION" ical-description]
      (when (or url rsvp)
        ["URL" (or url rsvp)])
      ["END" "VEVENT"]])
   ["END" "VCALENDAR"]])

(defn render-ical-doc [doc]
  (str/join "\n"
            (map #(str/join ":" %)
                 (filter some?
                         (reduce (fn [acc doc]
                                   (if (seq? doc)
                                     (reduce into acc doc)
                                     (conj acc doc))) [] doc)))))

(comment
  (render-ical-doc (ical-doc [{:full-title "Foo"
                               :rsvp "https://github.com/orgs/madclj/discussions/6"
                               :location " Foo"
                               :start (t/zoned-date-time "2018-03-25T03:05+02:00[America/Chicago]")
                               :end (t/zoned-date-time "2020-12-04T13:30+01:00")}]))
  (prn (t/zoned-date-time "2020-12-04T12:30+01:00"))
  (binding [*read-eval* false]
    (read-string (slurp "events.clj")))
  (filter #(.startsWith % "America/") (java.time.ZoneId/getAvailableZoneIds))
  (.atZone (t/date-time "2018-03-25T03:05")
           (java.time.ZoneId/of "America/Chicago"))

)
