(ns parkers-api.core-test
  (:require [clojure.test :refer :all]
            [parkers-api.core :refer :all]
            [clj-time.core :as t]))

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

(deftest test-sort-by-gender
  (testing "sorts by gender (female, then male) then
            last name, ascending"
    (is (= [{:gender "female" :last-name "W"}
            {:gender "female" :last-name "Y"}
            {:gender "male" :last-name "V"}
            {:gender "male" :last-name "X"}
            {:gender "male" :last-name "Z"}]
           (sort-by-gender
            [{:gender "male" :last-name "Z"}
             {:gender "female" :last-name "Y"}
             {:gender "male" :last-name "X"}
             {:gender "female" :last-name "W"}
             {:gender "male" :last-name "V"}])))))

(deftest test-sort-by-birth-date
  (testing "sorts by birth date, ascending"
    (let [one (t/date-time 1999 10 4)
          two (t/date-time 2000 9 1)
          three (t/date-time 2001 12 31)
          four (t/date-time 2002 1 1)]
      (is (= [{:birth-date one}
              {:birth-date two}
              {:birth-date three}
              {:birth-date four}]
             (sort-by-birth-date
              [{:birth-date two}
               {:birth-date one}
               {:birth-date four}
               {:birth-date three}]))))))

(deftest test-sort-by-last-name
  (testing "sorts by last name, descending"
    (is (= [{:last-name "D"}
            {:last-name "C"}
            {:last-name "B"}
            {:last-name "A"}]
           (sort-by-last-name
            [{:last-name "C"}
             {:last-name "B"}
             {:last-name "D"}
             {:last-name "A"}])))))

(deftest test-clojurify-record
  (testing "converts birth-date to joda time"
    (is (= {:birth-date (t/date-time 1990 4 30)}
         (clojurify-record {:birth-date "4/30/1990"})))))

(deftest test-printify-record
  (testing "converts birth-date to M/D/YYYY string"
    (is (= {:birth-date "4/30/1990"}
           (printify-record {:birth-date (t/date-time 1990 4 30)})))))
