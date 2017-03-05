(ns hlk-server.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :refer [wrap-params]]
            [environ.core :refer [env]]
            [compojure.core :refer [routes]]
            [hlk-server.middleware :refer [wrap-dev]]
            [hlk-server.handler.top :refer [top-routes]]
            [hlk-server.handler.search :refer [search-routes]]
            [hlk-server.handler.scrape :refer [scrape-routes]]
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
              scrape-routes
              top-routes)
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

