(ns parkers-api.lib.rec-test
  (:require [clojure.test :refer :all]
            [parkers-api.lib.rec :refer :all]
            [clj-time.core :as t]))

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
