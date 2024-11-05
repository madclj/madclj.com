(ns madison-clojure.ical-test
  (:require [clojure.test :refer [deftest is]]
            [clojure.string :as str]
            [tick.core :as t]
            [madison-clojure.ical :as sut]))

(def example-event-1 {:full-title "Foo"
                      :rsvp "https://github.com/orgs/madclj/discussions/6"
                      :location " Foo"
                      :start (t/zoned-date-time "2020-12-04T12:30+01:00")
                      :end (t/zoned-date-time "2020-12-04T13:30+01:00")})

(deftest ical-doc-test
  (is (= [["BEGIN" "VCALENDAR"]
          ["VERSION" "2.0"]
          ["PRODID" "-//hacksw/handcal//NONSGML v1.0//EN"]
          ["X-WR-CALNAME" "Madison Clojure Events"]
          [[["BEGIN" "VEVENT"]
            ["UID" "https://github.com/orgs/madclj/discussions/6"]
            ["DTSTART" "20201204T123000Z"]
            ["DTEND" "20201204T133000Z"]
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
          "UID:https://github.com/orgs/madclj/discussions/6"
          "DTSTART:20201204T123000Z"
          "DTEND:20201204T133000Z"
          "SUMMARY:"
          "LOCATION: Foo"
          "DESCRIPTION:Foo\\n\\n\\n\\nRSVP: https://github.com/orgs/madclj/discussions/6"
          "URL:https://github.com/orgs/madclj/discussions/6" "END:VEVENT" "END:VCALENDAR"]
         (str/split-lines
           (sut/render-ical-doc
             (sut/ical-doc [example-event-1]))))))
