#!/usr/bin/env bb

(ns madison-clojure.gen
  (:require [madison-clojure.events :refer [events]]
            [madison-clojure.ical :as ical]
            [madison-clojure.rsvp :as rsvp]))

(defn add-rsvps-to-event [rsvps events]
  (mapv (fn [{:keys [rsvp] :as event}]
          (if-some [users (get rsvps rsvp)]
            (update event :description str "\n\nAttendees:"
                    (apply str (map (fn [{:keys [name url avatarUrl]}]
                                      (format "\n- [%s](%s)" name url))
                                    (sort-by :name users))))
            event))
        events))

(comment
  (add-rsvps-to-event  (rsvp/rsvps-for-pinned-discussions) (do events))
  )

(defn -main []
  (->> events
       (add-rsvps-to-event (rsvp/rsvps-for-pinned-discussions))
       ical/ical-doc
       ical/render-ical-doc
       (spit "../events.ics")))

(comment
  (-main)
  )

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
