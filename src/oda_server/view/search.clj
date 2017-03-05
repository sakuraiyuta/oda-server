(ns oda-server.view.search
  (:require [compojure.core :refer [defroutes context GET]]
            [pl.danieljanus.tagsoup :as tagsoup]
            [oda-server.selector :as selector]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [selmer.util :refer :all]
            [selmer.parser :refer :all]
            [oda-server.client :refer :all]
            [oda-server.turtle :as turtle]
            [oda-server.response :as res]))

(defn search-view
  [req]
  (let [params (if (zero? (count (:params req)))
                 {:keyword1 "難波"
                  :smode 2
                  :d_cnt_l 100}
                 (:params req))
        results (-> (search params)
                    first
                    (selector/by-fn #(= (tagsoup/tag %) :td))
                    (selector/by-fn #(= (tagsoup/tag %) :img))
                    (as-> x (map #(-> % tagsoup/attributes :src) x)))]
    (-> (render-file "templates/result.html" {:params params
                                              :results results})
        res/response
        res/html)))

(defn js-view
  [req]
  nil)
;  (let [{:strs [search-word] :as params} (:params req)
;        parsed-content (get-page-links search-word)
;        data (reduce concat (map get-links parsed-content))]
;    (without-escaping
;      (render-file "templates/hlk.js"
;                   (conj params
;                         {:nodes (json/write-str
;                                   nodes
;                                   :escape-unicode false)
;                          :links (json/write-str
;                                   links
;                                   :escape-unicode false)})
;                   res/response
;                   res/js))))

;(let [{:keys [search-word region] :as params} (keywordize-keys {:search-word "枚方の神社" :region "hirakata"})
;      parsed-content (get-page-links region search-word)
;      data (reduce concat (map get-links parsed-content))
;      nodes (->> data
;                 (map-indexed
;                   (fn [idx itm]
;                     {:id idx
;                      :name (:text itm)
;                      :href (str "/search?search-word=" (:href itm) "&region=" region)})))
;      links (->> data
;                 (map-indexed (fn [idx _] {:source 0 :target idx})))]
;  nodes)


(let [results (-> (search {:smode 2 :keyword1 "難波"})
                  first
                  (selector/by-fn #(= (tagsoup/tag %) :img)))]
  (map #(-> % tagsoup/attributes :src) results))
