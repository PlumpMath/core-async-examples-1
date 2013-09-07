(ns user)

(require '[clojure.core.async :as async :refer :all])

(defn slow-function []
  "A dummy function that takes a couple of seconds to execute"
  (reduce + (range 100000000)))

;; launch a new thread to execute slow function, put its result onto "result-channel"
(def result-channel
    (thread
      (slow-function)
      ))

;; block until either result-channel gets a value, or timeout occurs
(let [[result ch] (alts!! [result-channel (timeout 2150)])]
  (if (= ch result-channel)
      result
      ":-( TIMED OUT "))