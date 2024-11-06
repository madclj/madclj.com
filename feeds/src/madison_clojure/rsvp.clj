(ns madison-clojure.rsvp
  (:require
    [babashka.process :as proc]
    [cheshire.core :as json]))

(defn rsvps-for-pinned-discussions []
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
              }'"
              ))
      (json/parse-string true)
      ;;TODO figure out how to filter and select in graphql
      (get-in [:data :repository :pinnedDiscussions :nodes])
      (->> (into {} (map (fn [{{:keys [url reactions]} :discussion}]
                           [url (into [] (keep #(when (= (:content %) "THUMBS_UP")
                                                  (:user %)))
                                      (:nodes reactions))]))))))

(comment
  (rsvps-for-pinned-discussions)
  ;=>
  {"https://github.com/orgs/madclj/discussions/6" [{:name "Ambrose Bonnaire-Sergeant",
                                                    :url "https://github.com/frenchy64",
                                                    :avatarUrl "https://avatars.githubusercontent.com/u/287396?u=2aa22e9ddcc23256939aa36dbd3ca60f3e260e69&v=4"}],
   "https://github.com/orgs/madclj/discussions/7" [{:name "Ambrose Bonnaire-Sergeant",
                                                    :url "https://github.com/frenchy64",
                                                    :avatarUrl "https://avatars.githubusercontent.com/u/287396?u=2aa22e9ddcc23256939aa36dbd3ca60f3e260e69&v=4"}]}
  )
