(ns parkers-api.server-test
  (:require [parkers-api.data :refer [records]]
            [parkers-api.server :refer :all]
            [parkers-api.lib.rec :as rec]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]))

(defn setup-records
  [f]
  (reset!
   records
   [{:one 1}])
  (f))

(defn stub-fns
  [f]
  (with-redefs
    [rec/extract-records-from-string (fn [s] [{:input s}])
     rec/sort-by-gender identity
     rec/sort-by-birth-date identity
     rec/sort-by-last-name identity]
    (f)))

(use-fixtures :once setup-records stub-fns)

(deftest test-app
  (testing "GET /records/gender"
    (is (= (app (mock/request :get "/records/gender"))
           {:status 200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body "[{\"one\":1}]"})))
  (testing "GET /records/birthdate"
    (is (= (app (mock/request :get "/records/birthdate"))
           {:status 200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body "[{\"one\":1}]"})))
  (testing "GET /records/name"
    (is (= (app (mock/request :get "/records/name"))
           {:status 200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body "[{\"one\":1}]"})))
  (testing "POST /records"
    (is (= (app (assoc (mock/request :post "/records")
                       :body (into-array Byte/TYPE "foobar")))
           {:status 200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body "{\"input\":\"foobar\"}"}))
    (is (= @records
           [{:one 1}{:input "foobar"}]))))
