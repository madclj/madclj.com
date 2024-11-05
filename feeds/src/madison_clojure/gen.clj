(ns madison-clojure.gen
  (:require [madison-clojure.events :refer [events]]
            [madison-clojure.ical :as ical]))

(defn -main []
  (->> events
       ical/ical-doc
       ical/render-ical-doc
       (spit "../events.ical")))

(comment
  (-main)
  )

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
