#!/usr/bin/env bb

(ns madison-clojure.gen
  (:require [madison-clojure.events :refer [events]]
            [madison-clojure.ical :as ical]
            [madison-clojure.rsvp :as rsvp]))


(comment
  (add-rsvps-to-events  (rsvp/rsvps-for-pinned-discussions) (do events))
  )

(defn -main []
  (-> events
      (rsvp/add-rsvps-to-events (rsvp/rsvps-for-pinned-discussions))
      ical/ical-doc
      ical/render-ical-doc
      (->> (spit "../events.ics"))))

(comment
  (-main)
  )

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
