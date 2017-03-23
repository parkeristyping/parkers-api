(ns parkers-api.lib.utils
  (:require [clojure.string :as str]))

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
