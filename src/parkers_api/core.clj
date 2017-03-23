(ns parkers-api.core
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

(defn vecs->maps
  "Given a 2d vector and an ordered list of keys, converts vectors to
   maps with corresponding keys. E.g.

   (vecs->maps [['Joe' 26]['Jane' 28]] [:name :age])
   => [{:name 'Joe'
        :age 26}
       {:name 'Jane'
        :age 28}]"
  [vecs keys]
  (map
   #(into {} (map vector keys %))
   vecs))
