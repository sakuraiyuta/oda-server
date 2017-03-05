(ns oda-server.handler.search
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [oda-server.view.search :refer [search-view js-view]]
            [oda-server.response :as res]))


(defroutes search-routes
  (context "/search" _
           (GET "/" req search-view)
           (GET "/js" req js-view)))

