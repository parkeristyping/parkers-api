(ns parkers-api.lib.rec-test
  (:require [clojure.test :refer :all]
            [parkers-api.lib.rec :refer :all]
            [clj-time.core :as t]
            [clojure.string :as str]
            [parkers-api.lib.utils :as u]
            [parkers-api.lib.rec :as rec]))

(def test-input-comma
"Lawrence,Parker,male,blue,4/30/1990
Brooks,Sofie,female,green,1/10/1990
Shields,Brooke,female,yellow,4/30/1994
Hardesty,Ben,male,blue,4/3/2007
Glass,Philip,male,blue,2/9/1987")

(def test-input-pipe
  (str/replace test-input-comma #"," "|"))

(def test-input-space
  (str/replace test-input-comma #"," " "))

(def test-records
  [{:last-name "Lawrence" :first-name "Parker" :gender "male"
    :favorite-color "blue" :birth-date (t/date-time 1990 4 30)}
   {:last-name "Brooks" :first-name "Sofie" :gender "female"
    :favorite-color "green" :birth-date (t/date-time 1990 1 10)}
   {:last-name "Shields" :first-name "Brooke" :gender "female"
    :favorite-color "yellow" :birth-date (t/date-time 1994 4 30)}
   {:last-name "Hardesty" :first-name "Ben" :gender "male"
    :favorite-color "blue" :birth-date (t/date-time 2007 4 3)}
   {:last-name "Glass" :first-name "Philip" :gender "male"
    :favorite-color "blue" :birth-date (t/date-time 1987 2 9)}])

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
