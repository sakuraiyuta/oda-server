(ns oda-server.handler.top
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [oda-server.view.top :refer [top-view]]
            [oda-server.response :as res]))

(defn top
  [req]
  (-> (top-view req)
      res/response
      res/html))

(defroutes top-routes
  (GET "/" req top)
  (route/not-found "not found!"))

