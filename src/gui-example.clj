(ns user)
(require '[clojure.core.async :as async :refer :all])
(use 'seesaw.core)

(native!)
(def f (frame :title "core.async example"))

(def countButton (button :text "Count to 100"))
(def YC (button :text "Load Hacker News"))
(def Yahoo (button :text "Load Yahoo"))
(def Google (button :text "Load Google"))
(def progress (progress-bar :min 0 :max 100 :value 0))

;;create a huge buffer for user input
(def gui-command (chan 1000))

;;long running function that updates a progress bar
(defn timer-fn [time]
  (doseq [i (range (+ time 1))]
    (do (java.lang.Thread/sleep 100)
      (config! progress :value i))))

;;time how long it takes to retrieve a URL
(defn slurp-timer [name url]
  (alert (str name " Time: " (with-out-str (time (slurp url))))))

;;dispatch to appropriate event handler based on which button clicked
(defn command-handler[cmd]
  (case cmd
    "timer"  (timer-fn 100)
    "YC"     (slurp-timer "Hacker News" "https://news.ycombinator.com")
    "Yahoo"  (slurp-timer "Yahoo!" "http://www.yahoo.com")
    "Google" (slurp-timer "Google" "http://www.google.com")))

;;handle any button clicks
(thread
  (while true
    (command-handler (<!! gui-command))))

;;put a button click into the channel to be handled
(defn button-handler[source]
  (>!! gui-command source))

;;add handlers to buttons
(listen countButton :action (fn [e] (button-handler "timer")))
(listen YC :action (fn [e] (button-handler "YC")))
(listen Yahoo :action (fn [e] (button-handler "Yahoo")))
(listen Google :action (fn [e] (button-handler "Google")))

;;display GUI
(config! f :content (border-panel
  :north countButton
  :west YC
  :center Yahoo
  :east Google
  :south progress))

(-> f pack! show!)