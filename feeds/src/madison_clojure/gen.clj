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

(defn format-event-time [^ZonedDateTime t]
  (assert (h/madison-time? t) (class t))
  (-> t
      (.format (DateTimeFormatter/ofPattern "MMM '%s' yyyy" Locale/US))
      (format (h/format-day-of-month t))))

(defn md-table [events]
  (let [extra-events [#_"| <DATE> | <SUBJECT> | |"]]
    (-> ["| Date | Link | RSVPs |"
         "| ------------- | ------------- | ------------- |"]
        (into (comp
                (remove h/unpost-event?)
                (map (fn [{:keys [start full-title summary rsvp attendees]}]
                       (assert (not (str/includes? rsvp "(")))
                       (assert (not (str/includes? rsvp ")")))
                       (str "|"
                            (str/join "|"
                                      (eduction (map #(str/escape % {\| "\\|"}))
                                                [(format-event-time start)
                                                 (format "[%s](%s)"
                                                         (str/escape full-title {\[ "\\[" \] "\\]"})
                                                         rsvp)
                                                 (if (seq attendees)
                                                   (pr-str (count attendees))
                                                   "")
                                                 #_
                                                 (str/join (mapcat
                                                             (fn [{:keys [#_name #_url avatar-url]}]
                                                               (assert (not (str/includes? avatar-url "\"")))
                                                               ;(assert (not (str/includes? url "\"")))
                                                               [;(format "<a href=\"%s\" title=\"%s\">" url name)
                                                                (format "<img src=\"%s\" style=\"%s\"/>"
                                                                        avatar-url
                                                                        "height:3em;display: inline-block; position: relative; overflow: hidden; border-radius: 50%;")
                                                                ;"</a>"
                                                                ])
                                                             attendees))]))
                            "|"))))
              (sort-by (juxt :start :end :full-title :uid) events))
        (into extra-events)
        (->> (str/join "\n")))))

(defn gen-table [events]
  (-> index-md
      slurp
      (replace-between "◊(events-table" "events-table)◊" (md-table events))
      (->> (spit index-md))))

(defn -main []
  (let [rsvps (try (rsvp/rsvps-for-pinned-discussions)
                   (catch Exception e
                     (println "Error retreiving RSVP's!")
                     (prn e)
                     ;; events will still render without RSVP's
                     {}))
        events (rsvp/add-rsvps-to-events e/events rsvps)]
    (gen-ics events)
    (gen-table events)))

(comment
  (-main)
  )

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
