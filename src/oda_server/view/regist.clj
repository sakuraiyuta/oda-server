(ns oda-server.view.regist
  (:require [compojure.core :refer [defroutes context GET]]
            [pl.danieljanus.tagsoup :as tagsoup]
            [oda-server.selector :as selector]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.data.json :as json]
            [selmer.util :refer :all]
            [selmer.parser :refer :all]
            [oda-server.client :refer :all]
            [oda-server.turtle :as turtle]
            [oda-server.response :as res]))

(defn regist-view
  [req]
  (let [params (-> req :params keywordize-keys)]
    (-> (render-file "templates/regist.html" params)
        res/response
        res/html)))

