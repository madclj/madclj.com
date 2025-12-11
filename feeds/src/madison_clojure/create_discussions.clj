#!/usr/bin/env bb

(ns madison-clojure.create-discussions
  (:require [babashka.process :as proc]
            [cheshire.core :as json]
            [clojure.string :as str]
            [madison-clojure.events :as e]
            [madison-clojure.helpers :as h]))

(set! *warn-on-reflection* true)

(defn discussion-exists?
  "Check if a discussion with the given ID exists and is a discussion.
  Returns true if it exists, false if it doesn't exist, throws if the check fails."
  [discussion-id]
  {:pre [(int? discussion-id)]}
  (let [result (proc/shell
                 {:out :string
                  :err :string
                  :continue true}
                 (format "gh api graphql -H 'GraphQL-Features: discussions_api' -F owner='madclj' -F name='madclj.com' -F number='%d'
                         -f query='query($owner: String!, $name: String!, $number: Int!) {
                           repository(owner: $owner, name: $name) {
                             discussion(number: $number) {
                               id
                               number
                             }
                           }
                         }'" discussion-id))]
    (if (zero? (:exit result))
      (let [data (-> (:out result)
                     (json/parse-string true)
                     (get-in [:data :repository :discussion]))]
        (boolean data))
      ;; Non-zero exit: check if it's "discussion not found" vs real error
      (if (str/includes? (:err result) "Could not resolve to a Discussion")
        false
        (throw (ex-info (str "Failed to check discussion existence: " (:err result))
                        {:discussion-id discussion-id
                         :exit-code (:exit result)
                         :stderr (:err result)}))))))

(defn get-category-id
  "Get the Announcements category ID for the repository."
  []
  (let [result (proc/shell
                 {:out :string
                  :err :string}
                 "gh api graphql -H 'GraphQL-Features: discussions_api' -F owner='madclj' -F name='madclj.com'
                 -f query='query($owner: String!, $name: String!) {
                   repository(owner: $owner, name: $name) {
                     discussionCategories(first: 10) {
                       nodes {
                         id
                         name
                       }
                     }
                   }
                 }'")
        categories (-> (:out result)
                       (json/parse-string true)
                       (get-in [:data :repository :discussionCategories :nodes]))
        announcement-cat (first (filter #(= "Announcements" (:name %)) categories))]
    (when-not announcement-cat
      (throw (ex-info "Announcements category not found" {:categories categories})))
    (:id announcement-cat)))

(defn get-repository-id
  "Get the repository ID for madclj/madclj.com."
  []
  (let [result (proc/shell
                 {:out :string
                  :err :string}
                 "gh api graphql -F owner='madclj' -F name='madclj.com'
                 -f query='query($owner: String!, $name: String!) {
                   repository(owner: $owner, name: $name) {
                     id
                   }
                 }'")]
    (-> (:out result)
        (json/parse-string true)
        (get-in [:data :repository :id]))))

(defn create-discussion
  "Create a discussion with the given title and body."
  [repository-id category-id title body]
  {:pre [(string? repository-id)
         (string? category-id)
         (string? title)
         (string? body)]}
  (let [;; Escape double quotes and newlines in the body for GraphQL
        escaped-body (-> body
                         (str/replace "\\" "\\\\")
                         (str/replace "\"" "\\\"")
                         (str/replace "\n" "\\n"))
        escaped-title (-> title
                          (str/replace "\\" "\\\\")
                          (str/replace "\"" "\\\""))
        mutation (format "mutation {
                           createDiscussion(input: {
                             repositoryId: \"%s\",
                             categoryId: \"%s\",
                             title: \"%s\",
                             body: \"%s\"
                           }) {
                             discussion {
                               id
                               number
                               url
                             }
                           }
                         }"
                         repository-id
                         category-id
                         escaped-title
                         escaped-body)
        result (proc/shell
                 {:out :string
                  :err :string}
                 "gh" "api" "graphql"
                 "-H" "GraphQL-Features: discussions_api"
                 "-f" (str "query=" mutation))]
    (-> (:out result)
        (json/parse-string true)
        (get-in [:data :createDiscussion :discussion]))))

(defn -main []
  (println "Checking if discussion #32 exists...")
  (if (discussion-exists? 32)
    (println "Discussion #32 exists. Nothing to do.")
    (do
      (println "Discussion #32 does not exist. Creating discussions for 2026 events...")
      (let [repository-id (get-repository-id)
            category-id (get-category-id)]
        (println "Repository ID:" repository-id)
        (println "Category ID:" category-id)
        (doseq [event e/events-2026]
          (let [{:keys [full-title]} event
                body (h/init-discussion-body event)
                _ (println (str "Creating discussion for: " full-title))
                discussion (create-discussion repository-id category-id full-title body)]
            (println (str "  Created: " (:url discussion)))))
        (println "Done!")))))

(comment
  (-main)
  (discussion-exists? 32)
  (discussion-exists? 21)
  (get-category-id)
  (get-repository-id)
  )

(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
