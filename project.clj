(defproject async-examples "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]

;; seems to be the most used version
                 [org.clojure/core.async "0.1.0-SNAPSHOT"]
                 [seesaw "1.4.4"]]

;; core.async is not available on clojars, so add this repository
  :repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  )


