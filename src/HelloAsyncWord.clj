(ns user)

(require '[clojure.core.async :as async :refer :all])

(def mychan (chan 0))

(go
  (>! mychan "Hello World" ))

(println (<!! mychan))