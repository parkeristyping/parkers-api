(ns parkers-api.lib.rec-test
  (:require [clojure.test :refer :all]
            [parkers-api.lib.rec :refer :all]
            [clj-time.core :as t]
            [clojure.string :as str]
            [parkers-api.lib.utils :as u]
            [parkers-api.lib.rec :as rec]))

(def test-input-comma
  (slurp "resources/comma-example.csv"))

(def test-input-pipe
  (str/replace test-input-comma #"," "|"))

(def test-input-space
  (str/replace test-input-comma #"," " "))

(def test-records
  [{:last-name "Bryant" :first-name "Alan" :gender "male"
    :favorite-color "blue" :birth-date (t/date-time 1580 12 9)}
   {:last-name "Dearborn" :first-name "Carol" :gender "female"
    :favorite-color "green" :birth-date (t/date-time 1990 1 10)}
   {:last-name "Fields" :first-name "Elle" :gender "female"
    :favorite-color "yellow" :birth-date (t/date-time 1994 5 19)}
   {:last-name "Hardesty" :first-name "Gareth" :gender "male"
    :favorite-color "blue" :birth-date (t/date-time 1994 5 20)}
   {:last-name "Junkins" :first-name "Igor" :gender "male"
    :favorite-color "blue" :birth-date (t/date-time 1993 1 1)}])

(deftest test-sort-by-gender
  (testing "sorts by gender (female, then male) then last name, ascending"
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
             {:gender "male" :last-name "V"}])))
    (is (= ["Dearborn" "Fields" "Bryant" "Hardesty" "Junkins"]
           (map :last-name (sort-by-gender test-records))))))

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
               {:birth-date three}]))))
    (is (= ["Bryant" "Dearborn" "Junkins" "Fields" "Hardesty"]
           (map :last-name (sort-by-birth-date test-records))))))

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
             {:last-name "A"}])))
    (is (= ["Junkins" "Hardesty" "Fields" "Dearborn" "Bryant"]
           (map :last-name (sort-by-last-name test-records))))))

(deftest test-clojurify
  (testing "converts birth-date to joda time"
    (is (= {:birth-date (t/date-time 1990 4 30)}
           (clojurify {:birth-date "4/30/1990"}))))
  (testing "lower-cases gender and favorite color to standardize"
    (is (= {:gender "male" :favorite-color "red"}
           (clojurify {:gender "Male" :favorite-color "RED"})))))

(deftest test-printify
  (testing "converts birth-date to M/D/YYYY string"
    (is (= {:birth-date "4/30/1990"}
           (printify {:birth-date (t/date-time 1990 4 30)})))))

(deftest test-extract-records-from-string
  (testing "comma delimited"
    (is (= test-records
           (extract-records-from-string test-input-comma))))
  (testing "pipe delimited"
    (is (= test-records
           (extract-records-from-string test-input-pipe))))
  (testing "space delimited"
    (is (= test-records
           (extract-records-from-string test-input-space)))))
