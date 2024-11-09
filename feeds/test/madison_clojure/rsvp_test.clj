(ns madison-clojure.rsvp-test
  (:require [clojure.test :refer [deftest is]]
            [madison-clojure.rsvp :as sut]))

(def dummy-events
  [{:summary "1", :uid "1", :rsvp "https://github.com/orgs/madclj/discussions/6", :description "desc1"}
   {:summary "2", :uid "2", :rsvp "https://github.com/orgs/madclj/discussions/7", :description "desc2"}])

(def dummy-rsvps
  {"https://github.com/orgs/madclj/discussions/6" [{:name "Ambrose Bonnaire-Sergeant",
                                                    :url "https://github.com/frenchy64",
                                                    :avatar-url "https://avatars.githubusercontent.com/u/287396?u=2aa22e9ddcc23256939aa36dbd3ca60f3e260e69&v=4"}],
   "https://github.com/orgs/madclj/discussions/7" [{:name "User 2",
                                                    :url "https://github.com/user2",
                                                    :avatar-url "avatar-url"}]})

(deftest add-rsvps-to-event-test
  (is (= [{:summary "1", :uid "1", :rsvp "https://github.com/orgs/madclj/discussions/6",
           :description "desc1\n\nAttendees:\n- [Ambrose Bonnaire-Sergeant](https://github.com/frenchy64)"
           :attendees [{:name "Ambrose Bonnaire-Sergeant",
                        :url "https://github.com/frenchy64",
                        :avatar-url "https://avatars.githubusercontent.com/u/287396?u=2aa22e9ddcc23256939aa36dbd3ca60f3e260e69&v=4"}]}
          {:summary "2", :uid "2", :rsvp "https://github.com/orgs/madclj/discussions/7",
           :description "desc2\n\nAttendees:\n- [User 2](https://github.com/user2)"
           :attendees [{:name "User 2",
                        :url "https://github.com/user2",
                        :avatar-url "avatar-url"}]}]
         (sut/add-rsvps-to-events dummy-events dummy-rsvps))))
