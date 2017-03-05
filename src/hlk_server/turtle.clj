(ns hlk-server.turtle
  (:use [clj-turtle.core])
  (:require [compojure.core :refer [defroutes context GET]]
            [hlk-server.localwiki :refer [localwiki-url]]
            [clojure.walk :refer [keywordize-keys]]))

(defns dbpedia-owl "http://dbpedia.org/ontology/")
(defns category-ja "http://ja.dbpedia.org/resource/Category:")

(defns dc "http://purl.org/dc/elements/1.1/")
(defns cc "http://creativecommons.org/ns#")

(def my-url "http://localhost:3000/")

(defn create-turtle
  [{:keys [search-word region] :or {search-word "光善寺駅周辺" region "hirakata"} :as params}
   links & parent]
  (let [relations (map #(let [link %]
                          (turtle (str "<" my-url search-word "> ")
                                  (dc :relation)
                                  (literal (str localwiki-url
                                                region "/"
                                                (:href link)))))
                       links)]
    (str (turtle (str "<" my-url search-word "> ") (dc :title) (literal search-word))
         (apply str relations))))


(create-turtle
  {:search-word "枚方市" :region "hirakata"}
  '({:tag :a, :href "%E4%BA%AC%E9%98%AA%E5%85%89%E5%96%84%E5%AF%BA%E9%A7%85", :text "京阪光善寺駅"} {:tag :a, :text "光善寺駅前商店街", :href "%E5%85%89%E5%96%84%E5%AF%BA%E9%A7%85%E5%89%8D%E5%95%86%E5%BA%97%E8%A1%97"} {:tag :a, :text "遠州屋", :href "%E9%81%A0%E5%B7%9E%E5%B1%8B"} {:tag :a, :href "%E9%81%93%E6%A8%99%EF%BC%88%E5%85%89%E5%96%84%E5%AF%BA%EF%BC%89", :text "道標（光善寺）"} {:tag :a, :text "国道1号線横断個所", :href "%E5%9B%BD%E9%81%931%E5%8F%B7%E7%B7%9A%E6%A8%AA%E6%96%AD%E6%A8%AA%E7%AE%87%E6%89%80"}))
