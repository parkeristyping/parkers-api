(defproject parkers-api "0.1.0-SNAPSHOT"
  :description "Just an example API"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.13.0"]
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-jetty-adapter "1.4.0"]
                 [ring/ring-defaults "0.2.1"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-mock "0.3.0"]
                 [compojure "1.5.1"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [cider/cider-nrepl "0.14.0"]]
  :resource-paths ["resources"]
  :main parkers-api.core
  :plugins [[lein-ring "0.10.0"]]
  :ring {:handler parkers-api.server/app})
