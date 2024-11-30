(ns madison-clojure.helpers
  (:import [java.time ZoneId LocalDateTime ZonedDateTime]
           [java.time.temporal ChronoUnit]))

(def ^ZoneId madison-tz (ZoneId/of "America/Chicago"))

(defn madison-time
  (^ZonedDateTime [] (ZonedDateTime/now madison-tz))
  (^ZonedDateTime [^String local-dt]
   (.atZone (LocalDateTime/parse local-dt) madison-tz)))

(defn madison-time? [^ZonedDateTime t]
  (= madison-tz (.getZone t)))

(defn unpost-event?
  "Unpost event from homepage 12 hours after ending."
  ([event] (unpost-event? (madison-time) event))
  ([now {:keys [^ZonedDateTime end]}]
   {:pre [(madison-time? now)
          (madison-time? end)]}
   (< 12 (.between ChronoUnit/HOURS end now))))

(defn format-day-of-month [^ZonedDateTime t]
  (let [d (.getDayOfMonth t)]
    (str d (case d
             (1 21 31) "st"
             (2 22) "nd";
             (3 23) "rd";
             "th"))))
