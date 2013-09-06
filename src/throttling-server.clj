(ns user
  (:import [java.net InetAddress ServerSocket Socket SocketException]
           [java.io InputStreamReader BufferedReader PrintWriter OutputStreamWriter BufferedWriter]))

(require '[clojure.core.async :as async :refer :all])

(defn strip [message]
  (clojure.string/replace (clojure.string/replace-first message #"/" "") #"%20" " "))

(def queue (chan 1024))

(defn handle [connection]
  (go
    (let [inFromClient (BufferedReader. (InputStreamReader. (.getInputStream connection)))
          outToClient (PrintWriter. (BufferedWriter. (OutputStreamWriter. (.getOutputStream connection))))
          request (.readLine inFromClient)
          message (when (not (nil? request))(strip (second (clojure.string/split request #" "))))
          result-chan (chan)]
      (>! queue (list message result-chan))
      (let [val (<! result-chan)
            detailed-message (str message " - Served by go block #" val " - "(.toString (java.util.Date.)))]
        (.write outToClient (str "HTTP/1.1 200 OK\r\n" "Content-length: " (count detailed-message) "\r\n" "Content-type: text/html\r\n" "\r\n" detailed-message))
        (.flush outToClient)
        (.close inFromClient)
        (.close outToClient)))))

(thread
  (let [socket (ServerSocket. 80)]
    (while true
    (let [connection (.accept socket)]
      (handle connection)))))

(thread
  (doseq [i (range 1e3)]
    (go
      (while true
        (let [[message result-chan] (<! queue)]
          (println (str "Go block " i " serving: " message ))
          (alts! [(timeout 5000)])   ;;fake a long process by parking go block
          (println (str "Go block " i " served: " message ))
          (>! result-chan i))))))