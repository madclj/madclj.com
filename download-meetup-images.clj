#!/bin/bash

bb -e '(let [i (atom 0)]
         (run! (fn [{{eventId "id" :strs [images]} "node"}]
                 ;(prn "event" eventId)
                 (run! 
                   (fn [{imageId "id" :strs [id baseUrl]}]
                     (let [f (io/file "meetup-export" eventId "images" #_imageId)
                           url (str baseUrl imageId)
                           i (swap! i inc)]
                       (println)
                       (println (str i ": " "Open: " url "#" i))
                       (println "Save to:" (.getAbsolutePath f))
                       (io/make-parents f)
                       #_(spit f (:body (curl/get url)))))
                   images))
               (get-in (json/decode (slurp "meetup-export.json")) ["data" "groupByUrlname" "pastEvents" "edges"])))'
