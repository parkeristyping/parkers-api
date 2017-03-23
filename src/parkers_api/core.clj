(ns parkers-api.core
  (:require [parkers-api.lib.rec :as rec]
            [parkers-api.lib.utils :as u]
            [parkers-api.server :as server]
            [clojure.tools.nrepl.server :as nrepl]
            [cider.nrepl :refer [cider-nrepl-handler]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(defn start-server
  []
  (nrepl/start-server :port 9998
                      :handler cider-nrepl-handler)
  (run-jetty server/app {:port 3000}))

(defn start-cli
  [path]
  (let [records-string (slurp path)
        records (rec/extract-records-from-string records-string)]
    (println "OUTPUT 1: Sorted by gender (females before males) then by last name ascending.")
    (rec/print-records (rec/sort-by-gender records))
    (println "\n----\nOUTPUT 2: Sorted by birth date, ascending.")
    (rec/print-records (rec/sort-by-birth-date records))
    (println "\n----\nOUTPUT 3: Sorted by last name, descending.")
    (rec/print-records (rec/sort-by-last-name records))))

(defn -main
  ([command] (-main command nil))
  ([command path]
   (case command
     "server" (start-server)
     "cli" (start-cli path))))
