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

(defn unpost-event? [{:keys [^ZonedDateTime end]}]
  (< 12 (.between ChronoUnit/HOURS (madison-time) end)))

(defn format-day-of-month [^ZonedDateTime t]
  (let [d (.getDayOfMonth t)]
    (str d (case d
             (1 21 31) "st"
             (2 22) "nd";
             (3 23) "rd";
             "th"))))
