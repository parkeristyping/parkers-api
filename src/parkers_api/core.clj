(ns parkers-api.core
  (:require [parkers-api.lib.rec :as rec]
            [parkers-api.lib.utils :as u])
  (:gen-class))

(defn -main
  [path]
  (let [file-string (slurp path)
        delimiter (u/determine-delimiter file-string)
        records-vec (u/parse-delimited-string file-string delimiter)
        records (map #(rec/clojurify-record
                       (u/vec->map % [:last-name :first-name :gender :favorite-color :birth-date]))
                     records-vec)]
    (println "OUTPUT 1: Sorted by gender (females before males) then by last name ascending.")
    (rec/print-records (rec/sort-by-gender records))
    (println "\n----\nOUTPUT 2: Sorted by birth date, ascending.")
    (rec/print-records (rec/sort-by-birth-date records))
    (println "\n----\nOUTPUT 3: Sorted by last name, descending.")
    (rec/print-records (rec/sort-by-last-name records))))
