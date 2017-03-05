(ns hlk-server.response
  (:require [ring.util.response :as res]))

(def response #'res/response)
(alter-meta! #'response #(merge % (meta #'res/response)))

(defn html [res]
  (res/content-type res "text/html; charset=utf-8"))

(defn js [res]
  (res/content-type res "text/javascript; charset=utf-8"))
