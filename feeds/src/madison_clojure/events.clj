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
   {:full-title "Round-table: Native Clojure"
    :summary "Round-table: Native Clojure"
    :uid "https://github.com/orgs/madclj/discussions/10"
    :description
    (text
      "Clojure was designed to be hosted on virtual machines like Java and CLR where"
      "low-level concerns are handled by the virtual machine. This week our meetup"
      "is all about connecting Clojure with native code and executables."
      ""
      "Take a look at the suggested topics below. If you'd like to lead or participate"
      "in a discussion about them, follow some of the links and prepare brief presentation"
      "(any format). We can have multiple presenters on the same topic, feel free to coordinate"
      "in the GitHub Discussion."
      ""
      "Please suggest any related topics you'd like for us to cover or just bring them up"
      "during the meetup."
      ""
      "Topics:"
      ""
      "- Original design of Clojure on JVM"
      "  - advantages + disadvantages"
      "- Compiling Clojure to native executables with GraalVM"
      "  - differences from JVM"
      "- Babashka + SCI"
      "  - how is startup so fast"
      "  - what's involved for library compatibility"
      "- Jank on LLVM"
      "  - what is LLVM"
      "  - current jank roadmap and progress"
      "- Clang"
      "  - how clang is relevant to Clojure"
      "  - what are clang plugins"
      "- JNI vs JNA vs FFI (new in JVM 22, aka Project Panama)"
      ""
      "Links:"
      ""
      "- [https://github.com/phronmophobic/clong]()"
      "  - A wrapper for libclang and a generator that can turn c header files into clojure apis."
      "- [https://github.com/babashka/babashka]()"
      "  - Babashka is a native Clojure interpreter for scripting with fast startup."
      "- [https://github.com/babashka/SCI]()"
      "  - Clojure interpreter compilable with GraalVM native"
      "- [https://github.com/clj-easy/graal-docs]()"
      "  - scripts and tips on natively compiling Clojure programs with GraalVM."
      "- [https://github.com/jank-lang/jank]()"
      "  - jank is a Clojure dialect on LLVM with C++ interop."
      "- [https://github.com/pfeodrippe/vybe/blob/develop/src/vybe/c.clj]()"
      "  - C compiler written in Clojure to extend Supercollider (Overtone)"
      "  - [commentary](https://clojurians.slack.com/archives/C053Q1QSG/p1732687749027129?thread_ts=1731877266.734569&cid=C053Q1QSG)"
      "  - [example usage](https://github.com/pfeodrippe/vybe/blob/641c00d07606c4cae126cee02930026e3d055f7f/src/vybe/experimental/audio/overtone.clj#L308)"
      "- [https://openjdk.org/jeps/454]()"
      "  - an API by which Java programs can interoperate with code and data outside of the Java runtime."
      "- [https://github.com/openjdk/jextract]()"
      "  - jextract is a tool which mechanically generates Java bindings from native library headers."
      "  - via clang C API"
      "- [https://github.com/pfeodrippe/vybe]()"
      "  - Clojure wrapper using Java's new Foreign Function & Memory API (JVM 22)"
      ""
      "Agenda:"
      ""
      "- 6:30pm arrive, food & drink provided"
      "- 7pm, start structured discussion"
      "- 8:30pm end structured discussion"
      "- 9:30pm end meetup")
    :rsvp "https://github.com/orgs/madclj/discussions/10"
    :location startingblock
    :start (madison-time "2025-01-08T18:30")
    :end   (madison-time "2025-01-08T21:00")}
   {:full-title "CANCELLED Planning Lunch: Causal Profiling"
    :summary "CANCELLED Planning Lunch: Causal Profiling"
    :uid "https://github.com/orgs/madclj/discussions/22"
    :description
    (text
      "NOTE: This event has been cancelled due to weather."
      ""
      "Want to be involved in our [next meetup on Causal Profiling (Feb 12th)](https://github.com/orgs/madclj/discussions/11)? Let's have lunch and plan something cool!"
      ""
      "Causal profiling is a benchmarking strategy that runs experiments by slowing parts of your program down until it can determine which lines would improve overall performance if improved."
      ""
      "Clojure doesn't have a causal profiler yet. Naively, it doesn't seem that hard! Perhaps we can divide and conquer, and present our results at the meetup."
      ""
      "It might be fun to plan another lunch before the meetup to check in."
      ""
      "Ideas:"
      "- write causal profiler based on clj-async-profiler"
      "- write pure clojure causal profiler based on https://github.com/taoensso/tufte"
      "- write smallest and simplest clojure causal profiler displaying essence"
      "- adapt JCoz to clojure https://github.com/Byte-Lab/JCoz/issues/23"
      "- bench https://github.com/tonsky/fast-edn/ using JCoz (already implemented in Java)"
      "  - can we predict where clojure.edn reader is slow? (also Java)"
      "- summarize theory behind causal profiling"
      "  - https://www.sigops.org/s/conferences/sosp/2015/current/2015-Monterey/printable/090-curtsinger.pdf"
      "- existing discussion"
      "  - https://www.reddit.com/r/Clojure/comments/kstuvl/anyone_tried_using_jcoz_java_causal_profiler_with/"
      "  - https://clojureverse.org/t/avoiding-recursive-function-replacement-with-redefs-fn/5399/11")
    :rsvp "https://github.com/orgs/madclj/discussions/22"
    :location startingblock
    :start (madison-time "2025-01-21T12:00")
    :end   (madison-time "2025-01-21T14:00")}
   {:full-title "Meetup: Causal Profiling"
    :summary "Meetup: Causal Profiling"
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
