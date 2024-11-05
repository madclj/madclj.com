(ns madison-clojure.events
  (:require [clojure.string :as str]
            [madison-clojure.helpers :refer [madison-time]]))

(defn text [& strs]
  (str/join "\n" strs))

(def startingblock "StartingBlock Madison, 821 E Washington Ave 2nd floor, Madison, WI")

;; :uid should be stable and unique for each event
(def events
  [
   {:full-title "Clojure Conference Takeaways and Inspirations"
    :summary "Clojure Conference Takeaways and Inspirations"
    :uid "https://github.com/orgs/madclj/discussions/6"
    :rsvp "https://github.com/orgs/madclj/discussions/6"
    :description
    (text
      "The Clojure community held two large conferences over the last two months."
      "Several of our regulars attended and/or spoke, and last meetup we phoned in to catch the last day of Clojure/conj."
      ""
      "A lot happened at these conferences. This meetup will provide space to discuss our experience, takeaways and inspirations."
      ""
      "Topics:"
      ""
      "- hallway track reports"
      "- favorite talks"
      "- conference followups (new projects/collaborations, ideas, initiatives)"
      "- industry and community trends"
      ""
      "No online stream is planned."
      ""
      "Agenda:"
      ""
      "- 6:30pm arrive, food & drink provided (pizza, bottled water, sparkling water, candy, fruit)"
      "- 7pm start structured discussion"
      "- 8pm end structured discussion"
      "- 9pm end meetup")
    :location startingblock
    :start (madison-time "2024-11-13T18:30")
    :end   (madison-time "2024-11-13T21:00")}
   {:full-title "Celebrating A Year of (Madison) Clojure"
    :summary "Celebrating A Year of (Madison) Clojure"
    :uid "https://github.com/orgs/madclj/discussions/7"
    :description
    (text
      "It's been a busy year! This will be our 11th and final meetup of 2024."
      ""
      "Let's wind down and celebrate the year together. We'll have a loosely structured discussion about our year with an eye towards Madison Clojure in 2025."
      ""
      "Topics:"
      ""
      "- what characterized Clojure in 2024 for you?"
      "- feedback on Madison Clojure in 2024"
      "- planning 2025"
      ""
      "Whether this is streamed will be determined closer to the event."
      ""
      "Agenda:"
      ""
      "- 6:30pm arrive, food & drink provided (TBD)"
      "- 7pm, start structured discussion"
      "- 8pm end structured discussion"
      "- 9pm end meetup")
    :rsvp "https://github.com/orgs/madclj/discussions/7"
    :location startingblock
    :start (madison-time "2024-12-11T18:30")
    :end   (madison-time "2024-12-11T21:00")}
   ])

(assert (every? :uid events))
(assert (apply distinct? (map :rsvp events)))
