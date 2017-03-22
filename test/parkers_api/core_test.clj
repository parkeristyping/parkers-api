(ns parkers-api.core-test
  (:require [clojure.test :refer :all]
            [parkers-api.core :refer :all]))

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
           (parse-delimited-string "x x\nx\nx" "|")))))
