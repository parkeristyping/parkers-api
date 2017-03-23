(ns parkers-api.server-test
  (:require [parkers-api.server :refer :all]
            [clojure.test :refer :all]
            [ring.mock.request :as mock]))

(deftest test-app
  (testing "GET /records/gender"
    (is (= (app (mock/request :get "/records/gender"))
           {:status 200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body "[]"})))
  (testing "GET /records/birthdate"
    (is (= (app (mock/request :get "/records/birthdate"))
           {:status 200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body "[]"})))
  (testing "GET /records/name"
    (is (= (app (mock/request :get "/records/name"))
           {:status 200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body "[]"})))
  (testing "POST /records"
    (is (= (app (body (mock/request :post "/records")) "Lawrence,Parker,male,blue,4/30/1990")
           {:status 200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body "[]"}))))
