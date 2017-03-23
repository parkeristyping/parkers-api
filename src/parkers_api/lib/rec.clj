(ns parkers-api.lib.rec
  (:require [clojure.pprint :as pprint]
            [clj-time.core :as t]
            [clj-time.format :as tf]
            [parkers-api.lib.utils :as u]))

(def records (atom []))

(def desc
  "Descending comparator"
  #(compare %2 %1))

(defn sort-by-gender
  "Sorts records by gender, female first then male, and then by
   last name in ascending alphabetical order"
  [records]
  (sort-by (juxt :gender :last-name) records))

(defn sort-by-birth-date
  "Sorts records by birth date, ascending (oldest people first)"
  [records]
  (sort-by :birth-date records))

(defn sort-by-last-name
  "Sorts records by last name in descending alphabetical order"
  [records]
  (sort-by :last-name desc records))

(def time-format
  (tf/formatter "M/d/YYYY"))

(defn clojurify
  "Does needed type conversions when reading values from string"
  [record]
  (u/apply-conversion-map
   record
   {:birth-date #(tf/parse time-format %)}))

(defn printify
  "Converts record values to be more easily human-readable, where needed"
  [record]
  (u/apply-conversion-map
   record
   {:birth-date #(tf/unparse time-format %)}))

(defn print-records
  "Prepares records for printing and prints them using `print-table`'"
  [records]
  (pprint/print-table
   (map
    printify
    records)))

(defn extract-records-from-string
  [records-string]
  (let [delimiter (u/determine-delimiter records-string)
        records-vec (u/parse-delimited-string records-string delimiter)]
    (-> records-vec
        (u/vec->map [:last-name :first-name :gender :favorite-color :birth-date])
        clojurify)))
