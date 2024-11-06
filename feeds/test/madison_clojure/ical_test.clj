(ns madison-clojure.ical-test
  (:require [clojure.test :refer [deftest is]]
            [clojure.string :as str]
            [tick.core :as t]
            [madison-clojure.helpers :as h]
            [madison-clojure.ical :as sut]))

(def example-event-1 {:full-title "Foo"
                      :uid "uid"
                      :rsvp "https://github.com/orgs/madclj/discussions/6"
                      :location " Foo"
                      :start (h/madison-time "2020-12-04T18:30")
                      :end   (h/madison-time "2020-12-04T21:00")})

(deftest render-datetime-test
  (is (= "20201205T003000Z"
         (sut/render-datetime
           (h/madison-time "2020-12-04T18:30")))))

(deftest ical-doc-test
  (is (= [["BEGIN" "VCALENDAR"]
          ["VERSION" "2.0"]
          ["PRODID" "-//hacksw/handcal//NONSGML v1.0//EN"]
          ["X-WR-CALNAME" "Madison Clojure Events"]
          [[["BEGIN" "VEVENT"]
            ["UID" "uid"]
            ["DTSTART" "20201205T003000Z"] ["DTEND" "20201205T030000Z"]
            ["SUMMARY" nil]
            ["LOCATION" " Foo"]
            ["DESCRIPTION" "Foo\\n\\n\\n\\nRSVP: https://github.com/orgs/madclj/discussions/6"]
            ["URL" "https://github.com/orgs/madclj/discussions/6"]
            ["END" "VEVENT"]]]
          ["END" "VCALENDAR"]]
         (sut/ical-doc [example-event-1]))))

(deftest render-ical-doc-test
  (is (= ["BEGIN:VCALENDAR"
          "VERSION:2.0"
          "PRODID:-//hacksw/handcal//NONSGML v1.0//EN"
          "X-WR-CALNAME:Madison Clojure Events"
          "BEGIN:VEVENT"
          "UID:uid"
          "DTSTART:20201205T003000Z"
          "DTEND:20201205T030000Z"
          "SUMMARY:"
          "LOCATION: Foo"
          "DESCRIPTION:Foo\\n\\n\\n\\nRSVP: https://github.com/orgs/madclj/discussions/6"
          "URL:https://github.com/orgs/madclj/discussions/6" "END:VEVENT" "END:VCALENDAR"]
         (str/split-lines
           (sut/render-ical-doc
             (sut/ical-doc [example-event-1]))))))
