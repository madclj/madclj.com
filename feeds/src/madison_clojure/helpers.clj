(ns madison-clojure.helpers
  (:require [clojure.string :as str])
  (:import [java.time ZoneId LocalDate LocalDateTime ZonedDateTime]
           [java.time.format DateTimeFormatter]
           [java.util Locale]
           [java.time.temporal ChronoUnit]))

(set! *warn-on-reflection* true)

(def ^ZoneId madison-tz (ZoneId/of "America/Chicago"))

(defn second-wednesday [year month]
  {:pre [(int? year)
         (<= 1 month 12)]
   :post [(<= 1 % 31)]}
  (let [first-day (LocalDateTime/of (int year) (int month) 1 0 0)
        day-of-week (.getValue (.getDayOfWeek first-day))
        days-until-first-wednesday (mod (- 10 day-of-week) 7)
        first-wednesday-day (+ 1 days-until-first-wednesday)
        second-wednesday-day (+ first-wednesday-day 7)]
    second-wednesday-day))

(defn second-wednesdays [year]
  (mapv (fn [month]
          (LocalDate/parse (format "%d-%02d-%02d" year month (second-wednesday year month))))
        (range 1 13)))

(defn madison-time
  (^ZonedDateTime [] (ZonedDateTime/now madison-tz))
  (^ZonedDateTime [local-dt]
   (let [ldt (if (string? local-dt)
               (LocalDateTime/parse local-dt)
               (if (instance? LocalDateTime local-dt)
                 local-dt
                 (throw (ex-info "could not coerce to LocalDateTime" {:local-dt local-dt}))))]
     (LocalDateTime/.atZone ldt madison-tz))))

(defn madison-time? [^ZonedDateTime t]
  (= madison-tz (.getZone t)))

(defn unpost-event?
  "Unpost event from homepage 12 hours after ending."
  ([event] (unpost-event? (madison-time) event))
  ([now {:keys [^ZonedDateTime end]}]
   {:pre [(madison-time? now)
          (madison-time? end)]}
   (< 12 (.between ChronoUnit/HOURS end now))))

(defn format-day-of-month [^ZonedDateTime t]
  (let [d (.getDayOfMonth t)]
    (str d (case d
             (1 21 31) "st"
             (2 22) "nd";
             (3 23) "rd";
             "th"))))

(defn text [& strs]
  (str/join "\n" strs))

(defn init-discussion-body [event]
  {:post [(string? %)]}
  (let [{:keys [^ZonedDateTime start ^ZonedDateTime end location description]} event
        date-formatter (DateTimeFormatter/ofPattern "EEEE, MMMM '%s', yyyy" Locale/US)
        time-formatter (DateTimeFormatter/ofPattern "h:mm a" Locale/US)
        date-str (-> start
                     (.format date-formatter)
                     (format (format-day-of-month start)))
        start-time (.format start time-formatter)
        end-time (.format end time-formatter)
        time-zone "CT"
        [location-name location-address] (str/split location #",\s*" 2)
        formatted-address (str/replace location-address #",\s*" " · ")]
    (text "👍 to RSVP"
          ""
          "https://madclj.com/"
          ""
          date-str
          (format "%s to %s %s" start-time end-time time-zone)
          ""
          location-name
          formatted-address
          ""
          description)))
