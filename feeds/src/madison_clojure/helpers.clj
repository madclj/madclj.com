(ns madison-clojure.helpers)

(def ^java.time.ZoneId madison-tz (java.time.ZoneId/of "America/Chicago"))

(defn madison-time [^String local-dt]
  (.atZone (java.time.LocalDateTime/parse local-dt) madison-time))

(defn madison-time? [^java.time.ZonedDateTime t]
  (= madison-tz (.getZone t)))
