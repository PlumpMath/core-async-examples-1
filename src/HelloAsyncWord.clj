(ns user)

(require '[clojure.core.async :as async :refer :all])

(def mychan (chan 0))

;; (close! mychan)

(go
  (>! mychan "Hello World" ))

(println (<!! mychan))


(defn download [c url]
  (go (>! c (slurp url))))

(def site (chan))

(download site "http://localhost")


(def timeout_channel (timeout (* 4 1000)))

(go
  (let [[res ch] (alts! [site timeout_channel])]
    (if (= ch timeout_channel)
        (println ":-( timed out.")
        (println res))))