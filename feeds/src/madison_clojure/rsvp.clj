(ns madison-clojure.rsvp
  (:require
    [babashka.process :as proc]
    [cheshire.core :as json]
    [clojure.set :as set]))

(def rsvp-emojis #{"THUMBS_UP"})

(defn rsvps-for-pinned-discussions []
  {:post [(or (assert (and (map? %)
                           (every? string? (keys %))
                           (every? #(every? (every-pred (comp string? :name)
                                                        (comp string? :url)
                                                        (comp string? :avatar-url))
                                            %)
                                   (vals %)))
                      (pr-str %))
              true)]}
  (-> (:out (proc/shell
              {:out :string
               :err :string}
              "gh api graphql -H 'GraphQL-Features: discussions_api' -F owner='madclj' -F name='madclj.com'
              -f query='query($name: String!, $owner: String!) {
                repository(owner: $owner, name: $name) {
                  pinnedDiscussions(first: 10) {
                   nodes {discussion {url reactions(first: 10) {nodes {user {name url avatarUrl} content}}}}
                  }
                }
              }'"))
      (json/parse-string true)
      ;;TODO figure out how to filter and select in graphql
      (get-in [:data :repository :pinnedDiscussions :nodes])
      (->> (into {} (map (fn [{{:keys [url reactions]} :discussion}]
                           [url (into [] (keep #(when (rsvp-emojis (:content %))
                                                  (-> (:user %)
                                                      (set/rename-keys {:avatarUrl :avatar-url})
                                                      ;; name is optional
                                                      (update :name #(or % "")))))
                                      (:nodes reactions))]))))))

(comment
  (rsvps-for-pinned-discussions)
  ;=>
  {"https://github.com/orgs/madclj/discussions/6" [{:name "Ambrose Bonnaire-Sergeant",
                                                    :url "https://github.com/frenchy64",
                                                    :avatar-url "https://avatars.githubusercontent.com/u/287396?u=2aa22e9ddcc23256939aa36dbd3ca60f3e260e69&v=4"}],
   "https://github.com/orgs/madclj/discussions/7" [{:name "Ambrose Bonnaire-Sergeant",
                                                    :url "https://github.com/frenchy64",
                                                    :avatar-url "https://avatars.githubusercontent.com/u/287396?u=2aa22e9ddcc23256939aa36dbd3ca60f3e260e69&v=4"}]}
  )

(defn add-rsvps-to-events [events rsvps]
  (mapv (fn [{:keys [rsvp] :as event}]
          (if-some [attendees (get rsvps rsvp)]
            (let [attendees (sort-by (juxt :name :url) attendees)]
              (-> event
                  (assoc :attendees attendees)
                  (update :description
                          str "\n\nRSVPs:"
                          (str " " (count attendees))
                          #_(apply str (map (fn [{:keys [name url avatar-url]}]
                                              (format "\n- [%s](%s)" name url))
                                            attendees)))))
            event))
        events))
