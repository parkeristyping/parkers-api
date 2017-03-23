(ns parkers-api.server
  (:require [compojure.core :refer :all]
            [clojure.data.json :as json]
            [ring.middleware.defaults :refer :all]
            [ring.middleware.json :refer [wrap-json-response]]
            [ring.util.response :refer [response]]
            [parkers-api.lib.rec :as rec :refer [records]]
            [parkers-api.lib.utils :as u]))

(defn recs->resp
  "Helper to prepare record collection to be sent as JSON"
  [records]
  (response (map rec/printify records)))

(defroutes app-routes
  (POST "/records" {body :body}
        (let [new-record (first (extract-records-from-string (slurp body)))]
          (swap! records conj new-record)
          (response (rec/printify new-record))))
  (GET "/records/gender" [] (-> @records rec/sort-by-gender recs->resp))
  (GET "/records/birthdate" [] (-> @records rec/sort-by-birth-date recs->resp))
  (GET "/records/name" [] (-> @records rec/sort-by-last-name recs->resp)))

(def app
  (-> #'app-routes
      (wrap-json-response :pretty true)
      (wrap-defaults api-defaults)))
