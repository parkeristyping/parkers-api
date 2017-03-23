(ns parkers-api.core
  (:require [clojure.string :as str]
            [clojure.pprint :as pprint]
            [clj-time.core :as t]
            [clj-time.format :as tf])
  (:gen-class))

(defn determine-delimiter
  "Scans a string for a ' ' (space), '|' (pipe), or ',' (comma) and returns
   the first of those that it finds. Throws AssertionError if none found."
  [s]
  (let [delimiter (re-find #"[ |,]" s)]
    (assert (some #{delimiter} [" ", "|", ","])
            "No delimiter was found!")
    delimiter))

(defn parse-delimited-string
  "Given a delimiter, parses a string into a vector of vectors. E.g.

   (parse-delimited-string 'one|two|three\nfour|five|six' '|')
   => [['one', 'two', 'three'], ['four', 'five', 'six']]"
  [s delimiter]
  (mapv
   ;; Below we escape delimiter before converting to regex to avoid
   ;; conflicts with regex special chars
   #(str/split % (re-pattern (str "\\" delimiter)))
   (str/split-lines s)))

(defn vec->map
  "Given a vector and an ordered list of keys, converts the vector
   to a map with corresponding keys. E.g.

   (vec->maps ['Joe' 28] [:name :age])
   => {:name 'Joe'
       :age 28}"
  [v ks]
  (into {} (map vector ks v)))

(defn apply-conversion-map
  "Applies a 'conversion-map' (a map of keys and functions) to a map,
   returning the resulting map. E.g.

   (apply-conversion-map {:one 1 :two 2} {:two inc})
   => {:one 2 :two 2}"
  [m conversion-map]
  (into
   {}
   (map
    (fn [[k v]] [k ((or (get conversion-map k) identity) v)])
    m)))

(defn compare-by
  "Function for generating sequential comparators that can be used
   by `sort`. I don't actually need this for the given specs, but that
   seemed coincidental so I decided to look into it (I would need it,
   e.g., to sort by age asc and then last name desc).

   (sort (compare-by :a compare :b #(compare %2 %1)) [{:a 1 :b 1}{:a 1 :b 2}])
   => [{:a 1 :b 2}{:a 1 :b 1}]

   Note also that I stole this function from a Google Groups discussion on the
   topic: https://groups.google.com/d/msg/clojure/VVVa3TS15pU/pT3iG_W2VroJ

   I was surprised to find `sort-by` doesn't support this behavior so went
   Googling. After finding this I saw no reason to try to write my own."
  [& key-cmp-pairs]
  (fn [x y]
    (loop [[k cmp & more] key-cmp-pairs]
      (let [result (cmp (k x) (k y))]
        (if (and (zero? result) more)
          (recur more)
          result)))))

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

(defn clojurify-record
  "Does needed type conversions when reading values from string"
  [record]
  (apply-conversion-map
   record
   {:birth-date #(tf/parse time-format %)}))

(defn printify-record
  "Converts record values to be more easily human-readable, where needed"
  [record]
  (apply-conversion-map
   record
   {:birth-date #(tf/unparse time-format %)}))

(defn print-records
  "Prepares records for printing and prints them using `print-table`'"
  [records]
  (pprint/print-table
   (map
    printify-record
    records)))

(defn -main
  [path]
  (let [file-string (slurp path)
        delimiter (determine-delimiter file-string)
        records-vec (parse-delimited-string file-string delimiter)
        records (map #(clojurify-record
                       (vec->map % [:last-name :first-name :gender :favorite-color :birth-date]))
                     records-vec)]
    (println "OUTPUT 1: Sorted by gender (females before males) then by last name ascending.")
    (print-records (sort-by-gender records))
    (println "\n----\nOUTPUT 2: Sorted by birth date, ascending.")
    (print-records (sort-by-birth-date records))
    (println "\n----\nOUTPUT 3: Sorted by last name, descending.")
    (print-records (sort-by-last-name records))))
