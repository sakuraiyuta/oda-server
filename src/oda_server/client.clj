(ns oda-server.client
  (:import [java.io StringReader BufferedReader])
  (:require [pl.danieljanus.tagsoup :as tagsoup]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [oda-server.selector :as selector]
            [http.async.client :as http]))

(def server-url "http://image.oml.city.osaka.lg.jp/archive/list.do")
(def req-params {:region "region__slug"
                 :name "name"
                 :format "format"})

(defn get-page
  [href & query]
  (with-open [client (http/create-client)]
    (let [query (first query)
          q (->> (map #(str (name (first %)) "=" (second %))
                      query)
                 (string/join "&"))
          url (str href "?" q)]
    (->> (http/GET client url)
         http/await
         http/body
         str tagsoup/parse-string))))

(defn search
  [options]
  (let [res (get-page server-url options)]
    (-> res
        (selector/by-fn #(and (= (tagsoup/tag %) :table)
                              (= (:class (tagsoup/attributes %)) "tbl05"))))))

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

