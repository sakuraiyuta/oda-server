(ns hlk-server.view.search
  (:require [compojure.core :refer [defroutes context GET]]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.data.json :as json]
            [selmer.util :refer :all]
            [selmer.parser :refer :all]
            [hlk-server.localwiki :refer :all]
            [hlk-server.turtle :as turtle]
            [hlk-server.response :as res]))

(defn search-view
  [req]
  (let [params (if (zero? (count (:params req)))
                 {:search-word "Front Page"
                  :region "hirakata"}
                 (:params req))]
    (-> (render-file "templates/test.html" params)
        res/response
        res/html)))

(defn js-view
  [req]
  (let [{:strs [search-word region] :as params} (:params req)
        parsed-content (get-page-links region search-word)
        data (reduce concat (map get-links parsed-content))
        nodes (->> data
                   (map-indexed
                     (fn [idx itm]
                       (let [[region search-word] (rest
                                                    (re-find #"^/([a-zA-Z0-9]*)/(.*)"
                                                             (:href itm)))]
                         {:id idx
                          :name (:text itm)
                          :href (str "/search?search-word=" search-word
                                     "&region=" region)}))))
        links (->> data
                   (map-indexed (fn [idx _] {:source 0 :target idx})))]
    (without-escaping
      (render-file "templates/hlk.js"
                   (conj params
                         {:nodes (json/write-str
                                   nodes
                                   :escape-unicode false)
                          :links (json/write-str
                                   links
                                   :escape-unicode false)})
                   res/response
                   res/js))))

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

