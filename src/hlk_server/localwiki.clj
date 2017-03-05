(ns hlk-server.localwiki
  (:import [java.io StringReader BufferedReader])
  (:require [pl.danieljanus.tagsoup :as tagsoup]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.data.json :as json]
            [hlk-server.selector :as selector]
            [http.async.client :as http]))

(def localwiki-api-url "https://ja.localwiki.org/api/v4/pages")
(def localwiki-url "https://ja.localwiki.org/")
(def req-params {:region "region__slug"
                 :name "name"
                 :format "format"})

(defn get-page
  [region href]
  (with-open [client (http/create-client)]
    (->> (http/GET client (str localwiki-url region "/" href))
         http/await
         http/body
         str tagsoup/parse-string)))

(defn get-page-links
  [region word]
  (let [res (with-open [client (http/create-client)]
              (->> (http/GET client (str localwiki-url "_links/" region "/" word "/"))
                   http/await
                   http/body))]
    (-> res str tagsoup/parse-string
        (selector/by-fn #(and (= (tagsoup/tag %) :div)
                              (= (:id (tagsoup/attributes %)) "content"))))))

(defn create-api-url
  [{:keys [search-word region] :or {search-word "光善寺駅周辺" region "hirakata"} :as params}]
  (str localwiki-api-url
       "/?" (:format req-params) "=json"
       "&" (:region req-params) "=" region
       "&" (:name req-params) "=" search-word))

(defn get-json
  [params]
  (let [res (with-open [client (http/create-client)]
              (->> (http/GET client (create-api-url params))
                   http/await
                   http/body))]
    (-> res
        str
        (StringReader.)
        (BufferedReader.)
        json/read
        keywordize-keys)))

(defn get-links
  [parsed]
  (let [elms (selector/by-fn parsed #(= (tagsoup/tag %) :a))]
    (map #(loop [elm %
                 result {}]
            (let [tag (tagsoup/tag elm)
                  attrs (tagsoup/attributes elm)
                  children (first (tagsoup/children elm))]
              (cond (vector? children)
                    (recur children
                           (conj result {:tag tag
                                         :text (:text result)
                                         :href (:href attrs)}))
                    (string? children)
                    (if (= tag :a)
                      (conj result {:tag tag
                                    :text (str (:text result) children)
                                    :href (:href attrs)})
                      (conj result {:text (str (:text result) children)})))))
         elms)))

