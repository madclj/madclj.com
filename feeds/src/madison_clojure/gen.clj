#!/usr/bin/env bb

(ns madison-clojure.gen
  (:require [clojure.string :as str]
            [madison-clojure.events :as e]
            [madison-clojure.ical :as ical]
            [madison-clojure.rsvp :as rsvp]))

(def index-md "../content/_index.markdown")
(def events-ics "../events.ics")

(defn gen-ics [events]
  (->> events
       ical/ical-doc
       ical/render-ical-doc
       (spit events-ics)))

(defn replace-between [s start end replace]
  (let [lines (str/split-lines s)
        [before [start-tag & within]] (split-with #(not (str/includes? % start)) lines)
        after (drop-while #(not (str/includes? % end)) within)]
    (str/join "\n" (concat before [start-tag] (str/split-lines replace) after))))
(do 
  (-> e/events first keys)
  )

(defn md-table [events]
  (prn "events" events)
  (let [extra-events ["| Jan 15th 2025 | [TBD](https://www.meetup.com/madison-clojure-meetup/events/304256375) |"]]
    (-> ["| Date | Event RSVP | Attendees |"
         "| ------------- | ------------- | ------------- |"]
        (into (map (fn [{:keys [start full-title summary rsvp attendees]}]
                     (str/join "|"
                               (into []
                                     (mapv #(str/escape % {\| "\\|"})
                                           )
                                     (interpose))
                               [(pr-str start) ;;TODO print madison time
                                (format "[%s](%s)" full-title rsvp)
                                ])))
              (sort-by (juxt :start :end :full-title :uid) events)
              )
        (into extra-events)
        (doto prn)
        (->> (str/join "\n")))))

(defn gen-table [events]
  (-> index-md
      slurp
      (replace-between "◊(events-table" "events-table◊)" (md-table events))
      (->> (prn #_spit index-md))))

(defn -main []
  (let [events (rsvp/add-rsvps-to-events e/events (rsvp/rsvps-for-pinned-discussions))]
    (gen-ics events)
    (gen-table events)))

(comment
  (-main)
  )

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
