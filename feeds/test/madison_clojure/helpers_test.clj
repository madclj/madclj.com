(ns madison-clojure.helpers-test
  (:require [clojure.test :refer [deftest is]]
            [madison-clojure.helpers :as sut]))

(defn dom [d]
  (-> "2020-12-04T18:30"
      sut/madison-time
      (.withDayOfMonth d)
      sut/format-day-of-month))

(deftest format-day-of-month-test
  (doseq [[expected actual] {1 "1st"
                             2 "2nd"
                             3 "3rd"
                             4 "4th"
                             5 "5th"
                             6 "6th"
                             7 "7th"
                             8 "8th"
                             9 "9th"
                             10 "10th"
                             11 "11th"
                             12 "12th"
                             13 "13th"
                             14 "14th"
                             15 "15th"
                             16 "16th"
                             17 "17th"
                             18 "18th"
                             19 "19th"
                             20 "20th"
                             21 "21st"
                             22 "22nd"
                             23 "23rd"
                             24 "24th"
                             25 "25th"
                             26 "26th"
                             27 "27th"
                             28 "28th"
                             29 "29th"
                             30 "30th"
                             31 "31st"}]
    (is (= (dom expected) actual))))

(deftest unpost-event?-test
  (is (not (sut/unpost-event? (sut/madison-time "2024-12-10T21:00") {:end (sut/madison-time "2024-12-11T21:00")})))
  (is (not (sut/unpost-event? (sut/madison-time "2024-12-11T21:00") {:end (sut/madison-time "2024-12-11T21:00")})))
  (is (not (sut/unpost-event? (sut/madison-time "2024-12-12T09:00") {:end (sut/madison-time "2024-12-11T21:00")})))
  (is (sut/unpost-event? (sut/madison-time "2024-12-12T10:00") {:end (sut/madison-time "2024-12-11T21:00")}))
  (is (sut/unpost-event? (sut/madison-time "2024-12-11T21:00") {:end (sut/madison-time "2024-12-10T21:00")})))

(deftest second-wednesday-test
  (is (= 14 (sut/second-wednesday 2026 1)))
  (is (= 11 (sut/second-wednesday 2026 2)))
  (is (= 11 (sut/second-wednesday 2026 3)))
  (is (= 8 (sut/second-wednesday 2026 4))))

(deftest init-discussion-body-test
  (is (= "👍 to RSVP\n\nhttps://madclj.com/\n\nWednesday, January 14th, 2026\n6:30 PM to 9:00 PM CT\n\nStartingBlock Madison, 821 E Washington Ave 2nd floor · Madison · WI\n\nTBD"
         (sut/init-discussion-body {:full-title "January Meetup",
                                    :summary "January Meetup",
                                    :uid "https://github.com/orgs/madclj/discussions/32",
                                    :description "TBD",
                                    :rsvp "https://github.com/orgs/madclj/discussions/32",
                                    :location "StartingBlock Madison, 821 E Washington Ave 2nd floor, Madison, WI"
                                    :start (sut/madison-time "2026-01-14T18:30"),
                                    :end (sut/madison-time "2026-01-14T21:00")}))))
