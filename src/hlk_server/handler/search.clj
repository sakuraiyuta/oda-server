(ns hlk-server.handler.search
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [hlk-server.view.search :refer [search-view js-view]]
            [hlk-server.response :as res]))


(defroutes search-routes
  (context "/search" _
           (GET "/" req search-view)
           (GET "/js" req js-view)))

