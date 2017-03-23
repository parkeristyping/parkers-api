(ns parkers-api.core
  (:require [parkers-api.lib.rec :as rec]
            [parkers-api.lib.utils :as u]
            [parkers-api.server :as server]
            [clojure.tools.nrepl.server :as nrepl]
            [cider.nrepl :refer [cider-nrepl-handler]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(defn start-server
  "Starts the API server on port 3000. Additionally starts an nREPL
   server on port 9998, which I find helpful for development."
  []
  (nrepl/start-server :port 9998
                      :handler cider-nrepl-handler)
  (run-jetty server/app {:port 3000}))

(defn start-cli
  "Runs the command line application, which reads the file at the provided
   path, parses it into records, and displays that data sorted 3 different
   ways: by gender, by birth date, and then by last name"
  [path]
  (let [records-string (slurp path)
        records (rec/extract-records-from-string records-string)]
    (println "OUTPUT 1: Sorted by gender (females before males) then by last name ascending.")
    (rec/print-records (rec/sort-by-gender records))
    (println "\n----\nOUTPUT 2: Sorted by birth date, ascending.")
    (rec/print-records (rec/sort-by-birth-date records))
    (println "\n----\nOUTPUT 3: Sorted by last name, descending.")
    (rec/print-records (rec/sort-by-last-name records))
    (System/exit 0)))

(defn -main
  "Invoked by `lein run`, this triggers either the CLI or the web
   server. Example usage below:

   $ lein run cli /some/file/with/delimited/data.csv
   $ lein run server"
  ([command] (-main command nil))
  ([command path]
   (case command
     "server" (start-server)
     "cli" (start-cli path))))
