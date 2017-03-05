(ns oda-server.handler.regist
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [oda-server.view.regist :refer [regist-view]]
            [oda-server.response :as res]))

(defn regist
  [req]
  (-> (regist-view req)
      res/html))

(defroutes regist-routes
  (context "/regist" _
           (GET "/" req regist)))

