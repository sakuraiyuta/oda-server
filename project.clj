(defproject hlk-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [environ "1.1.0"]
                 [org.clojure/data.json "0.2.6"]
                 [selmer "1.10.6"]
                 [clj-turtle "0.1.3"]
                 [http.async.client "1.2.0"]
                 [clj-time "0.13.0"]
                 [clj-tagsoup/clj-tagsoup "0.3.0" :exclusions [org.clojure/clojure]]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [ring "1.5.1"]
                 [compojure "1.5.2"]]
  :target-path "target/%s"
  :main hlk-server.core
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[prone "1.1.4"]]
                   :env {:dev true}}})

