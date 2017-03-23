(ns parkers-api.lib.utils-test
  (:require [clojure.test :refer :all]
            [parkers-api.lib.utils :refer :all]))

(deftest test-determine-delimiter
  (testing "works for pipe"
    (is (= "|" (determine-delimiter "1and234|76483"))))
  (testing "works for comma"
    (is (= "," (determine-delimiter ",\njudybloom"))))
  (testing "works for space"
    (is (= " " (determine-delimiter "hello@#$@#! what is up"))))
  (testing "throws assertion error if no delimiter found"
    (is (thrown? AssertionError (determine-delimiter "no\ndelimiter\nhere")))))

(deftest test-parse-delimited-string
  (testing "works for pipes"
    (is (= [["one" "two" "three"]["four" "five" "six"]]
           (parse-delimited-string "one|two|three\nfour|five|six" "|"))))
  (testing "works for commas"
    (is (= [["HEY" "___" "!@#!@"]]
           (parse-delimited-string "HEY,___,!@#!@" ","))))
  (testing "works for spaces"
    (is (= [["x" "x"]["x"]["x"]]
           (parse-delimited-string "x x\nx\nx" " "))))
  (testing "handles empty columns"
    (is (= [["" "what" "" "where" "when"]]
           (parse-delimited-string "|what||where|when" "|")))))

(deftest test-vec->map
  (testing "simple example with name and age"
    (is (= {:name "Joe" :age 28}
           (vec->map ["Joe" 28] [:name :age])))))

(deftest test-apply-conversion-map
  (testing "simple example using inc"
    (is (= {:one 2 :two 2}
           (apply-conversion-map {:one 1 :two 2} {:one inc})))))

(deftest test-compare-by
  (testing "simple example using inc"
    (is (= [{:a 1 :b 2}{:a 1 :b 1}]
           (sort (compare-by :a compare :b #(compare %2 %1))
                 [{:a 1 :b 1}{:a 1 :b 2}])))))
