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

   one|two|three
   four|five|six

   [['one', 'two', 'three'], ['four', 'five', 'six']]"
  [s delimiter]
  (mapv
   ;; Below we escape delimiter before converting to regex to avoid
   ;; conflicts with regex special chars
   #(str/split % (re-pattern (str "\\" delimiter)))
   (str/split-lines s)))
