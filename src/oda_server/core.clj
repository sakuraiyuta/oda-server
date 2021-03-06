(ns oda-server.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer :all]
            [environ.core :refer [env]]
            [compojure.core :refer [routes]]
            [oda-server.middleware :refer [wrap-dev]]
            [oda-server.handler.top :refer [top-routes]]
            [oda-server.handler.search :refer [search-routes]]
            [oda-server.handler.regist :refer [regist-routes]]
            [oda-server.handler.scrape :refer [scrape-routes]]
            [ring.util.response :as response])
  (:gen-class main true))

(defonce server (atom nil))

(defn- wrap
  [handler middleware opt]
  (if (= opt "true")
    (middleware handler)
    handler))

(def app
  (-> (routes search-routes
              regist-routes
              scrape-routes
              top-routes)
      (wrap-resource "public")
      wrap-params
      (wrap wrap-dev (:dev env))))

(defn start-server
  []
  (when-not @server
    (reset! server
            (jetty/run-jetty #'app
                             {:port 3000
                              :join? false}))))

(defn stop-server
  []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart-server
  []
  (when @server
    (stop-server)
    (start-server)))

(defn -main
  [& args]
  (start-server))

