#!/usr/bin/env bb

(ns madison-clojure.gen
  (:require [clojure.string :as str]
            [madison-clojure.events :as e]
            [madison-clojure.ical :as ical]
            [madison-clojure.rsvp :as rsvp]
            [madison-clojure.helpers :as h])
  (:import [java.util Locale]
           [java.time ZonedDateTime]
           [java.time.format DateTimeFormatter]))

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

(defn format-event-time [^ZonedDateTime start]
  (assert (h/madison-time? start) (class start))
  (format (.format start (DateTimeFormatter/ofPattern "MMM '%s' yyyy" Locale/US))
          (let [d (.getDayOfMonth start)]
            (str d (case d
                     (1 21 31) "st"
                     (2 22) "nd";
                     (3 23) "rd";
                     "th")))))

(defn md-table [events]
  (let [extra-events ["| Jan 15th 2025 | [TBD](https://www.meetup.com/madison-clojure-meetup/events/304256375) |"]]
    (-> ["| Date | Event RSVP | Attendees |"
         "| ------------- | ------------- | ------------- |"]
        (into (map (fn [{:keys [start full-title summary rsvp attendees]}]
                     (assert (not (str/includes? rsvp "(")))
                     (assert (not (str/includes? rsvp ")")))
                     (str "|"
                          (str/join "|"
                                    (eduction (map #(str/escape % {\| "\\|"}))
                                              [(format-event-time start)
                                               (format "[%s](%s)"
                                                       (str/escape full-title {\[ "\\[" \] "\\]"})
                                                       rsvp)
                                               (str/join (mapcat
                                                           (fn [{:keys [name url avatar-url]}]
                                                             (assert (not (str/includes? name "\"")))
                                                             (assert (not (str/includes? avatar-url "\"")))
                                                             (assert (not (str/includes? url "\"")))
                                                             [(format "<a href=\"%s\" title=\"%s\">" url name)
                                                              (format "<img src=\"%s\" alt=\"%s\" style=\"%s\"/>"
                                                                      avatar-url
                                                                      name
                                                                      "height:3em;display: inline-block; position: relative; overflow: hidden; border-radius: 50%;")
                                                              "</a>"])
                                                           attendees))]))
                          "|")))
              (sort-by (juxt :start :end :full-title :uid) events))
        (into extra-events)
        (->> (str/join "\n")))))

(defn gen-table [events]
  (-> index-md
      slurp
      (replace-between "◊(events-table" "events-table)◊" (md-table events))
      (->> (spit index-md))))

(defn -main []
  (let [events (rsvp/add-rsvps-to-events e/events (rsvp/rsvps-for-pinned-discussions))]
    (gen-ics events)
    (gen-table events)))

(comment
  (-main)
  )

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
