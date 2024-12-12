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
   {:full-title "January Meetup (TBD)"
    :summary "January Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/10"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/10"
    :location startingblock
    :start (madison-time "2025-01-08T18:30")
    :end   (madison-time "2025-01-08T21:00")}
   {:full-title "February Meetup (TBD)"
    :summary "February Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/11"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/11"
    :location startingblock
    :start (madison-time "2025-02-12T18:30")
    :end   (madison-time "2025-02-12T21:00")}
   {:full-title "March Meetup (TBD)"
    :summary "March Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/12"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/12"
    :location startingblock
    :start (madison-time "2025-03-12T18:30")
    :end   (madison-time "2025-03-12T21:00")}
   {:full-title "April Meetup (TBD)"
    :summary "April Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/13"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/13"
    :location startingblock
    :start (madison-time "2025-04-09T18:30")
    :end   (madison-time "2025-04-09T21:00")}
   {:full-title "May Meetup (TBD)"
    :summary "May Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/14"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/14"
    :location startingblock
    :start (madison-time "2025-05-14T18:30")
    :end   (madison-time "2025-05-14T21:00")}
   {:full-title "June Meetup (TBD)"
    :summary "June Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/15"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/15"
    :location startingblock
    :start (madison-time "2025-06-11T18:30")
    :end   (madison-time "2025-06-11T21:00")}
   {:full-title "July Meetup (TBD)"
    :summary "July Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/16"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/16"
    :location startingblock
    :start (madison-time "2025-07-09T18:30")
    :end   (madison-time "2025-07-09T21:00")}
   {:full-title "August Meetup (TBD)"
    :summary "August Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/17"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/17"
    :location startingblock
    :start (madison-time "2025-08-13T18:30")
    :end   (madison-time "2025-08-13T21:00")}
   {:full-title "September Meetup (TBD)"
    :summary "September Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/18"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/18"
    :location startingblock
    :start (madison-time "2025-09-10T18:30")
    :end   (madison-time "2025-09-10T21:00")}
   {:full-title "October Meetup (TBD)"
    :summary "October Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/19"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/19"
    :location startingblock
    :start (madison-time "2025-10-08T18:30")
    :end   (madison-time "2025-10-08T21:00")}
   {:full-title "November Meetup (TBD)"
    :summary "November Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/20"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/20"
    :location startingblock
    :start (madison-time "2025-11-12T18:30")
    :end   (madison-time "2025-11-12T21:00")}
   {:full-title "December Meetup (TBD)"
    :summary "December Meetup (TBD)"
    :uid "https://github.com/orgs/madclj/discussions/21"
    :description
    (text
      "TBD")
    :rsvp "https://github.com/orgs/madclj/discussions/21"
    :location startingblock
    :start (madison-time "2025-12-10T18:30")
    :end   (madison-time "2025-12-10T21:00")}
   ])

(assert (every? :uid events))
(assert (apply distinct? (map :rsvp events)))
