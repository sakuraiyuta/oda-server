(ns oda-server.handler.scrape
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [oda-server.view.scrape :refer [scrape-view]]
            [oda-server.response :as res]))

(defn scrape
  [req]
  (-> (scrape-view req)
      res/html))

(defroutes scrape-routes
  (context "/scrape" _
           (GET "/" req scrape)))

