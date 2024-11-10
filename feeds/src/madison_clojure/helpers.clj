(ns madison-clojure.helpers)

(def ^java.time.ZoneId madison-tz (java.time.ZoneId/of "America/Chicago"))

(defn madison-time [^String local-dt]
  (.atZone (java.time.LocalDateTime/parse local-dt) madison-tz))

(defn madison-time? [^java.time.ZonedDateTime t]
  (= madison-tz (.getZone t)))

(defn format-day-of-month [^java.time.ZonedDateTime t]
  (let [d (.getDayOfMonth t)]
    (str d (case d
             (1 21 31) "st"
             (2 22) "nd";
             (3 23) "rd";
             "th"))))
