(ns madison-clojure.helpers)

(defn madison-time [^String local-dt]
  (.atZone (java.time.LocalDateTime/parse local-dt)
           (java.time.ZoneId/of "America/Chicago")))
