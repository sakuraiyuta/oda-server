(ns hlk-server.view.scrape
  (:require [compojure.core :refer [defroutes context GET]]
            [pl.danieljanus.tagsoup :as tagsoup]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.java.io :as io]
            [selmer.parser :as selmer]
            [hlk-server.localwiki :refer :all]
            [hlk-server.turtle :as turtle]
            [hlk-server.selector :as selector]
            [hlk-server.response :as res]))


(defn scrape-view
  [req]
  (-> (selmer/render-file "templates/test.html" {})
      res/response
      res/html))

(defn scrape-url
  [region word]
  (let [links (-> (get-page-links region word)
                      (selector/by-fn #(and (= (tagsoup/tag %) :div)
                                            (= ((tagsoup/attributes %) :id) "content")))
                      first get-links)
        links (distinct (filter #(re-matches #"/hirakata/%.*" (:href %)) links))]
    (for [link links]
      (when-not (.exists (io/as-file (str (:text link) ".ttl")))
        (with-open [fout (io/output-stream (str "./" (:text link) ".ttl"))]
          (let [parsed (get-page region (:href link))
                turtle-links (get-links parsed)
                turtle (turtle/create-turtle {:search-word word :region region} links)]
            (.write fout (.getBytes turtle))
            (scrape-url region (:text link))))))))

;(scrape-url "hirakata" "Front%20Page")
