(ns madison-clojure.events
  (:require [clojure.string :as str]
            [madison-clojure.helpers :refer [madison-time]]))

(def startingblock "StartingBlock Madison, 821 E Washington Ave 2nd floor, Madison, WI")

(def events
  [
   {:full-title "Clojure Conference Takeaways and Inspirations"
    :summary "Clojure Conference Takeaways and Inspirations"
    :rsvp "https://github.com/orgs/madclj/discussions/6"
    :location startingblock
    :start (madison-time "2024-11-13T18:30")
    :end   (madison-time "2024-11-13T21:00")}
   {:full-title "Celebrating A Year of (Madison) Clojure"
    :summary "Celebrating A Year of (Madison) Clojure"
    :rsvp "https://github.com/orgs/madclj/discussions/7"
    :location startingblock
    :start (madison-time "2024-12-11T18:30")
    :end   (madison-time "2024-12-11T21:00")}
   ])

(assert (apply distinct? (map :rsvp events)))
