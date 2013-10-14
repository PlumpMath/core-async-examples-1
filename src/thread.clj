(ns user)

(require '[clojure.core.async :as async :refer :all])

(defn slow-function []
  "A dummy function that takes a couple of seconds to execute"
  (reduce + (range 1e8))
  "long function")

(defn medium-function []
  "A dummy function that takes a fraction of a second to execute"
  (reduce + (range 2e7))
       "medium function")

;; launch a new thread to execute slow function, put its result onto "result-channel"
(def result-channel
    (thread
      (slow-function)
      ))

;; execute the slow function in the go-block thread pool, put its result into "result-channel-go-style"
(def result-channel-go-style
  (go
    (medium-function)
    ))

;; create a channel that closes itself after the given number of milliseconds
(def timeout-channel (timeout 1000))

;; block until either result-channel gets a value, or timeout occurs
(let [[result ch] (alts!! [result-channel result-channel-go-style timeout-channel])]
  (if (= ch timeout-channel)
    ":-( TIMED OUT "
     result))

(java.lang.Thread/activeCount)

