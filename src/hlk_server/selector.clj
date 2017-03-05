(ns hlk-server.selector
  (:require [pl.danieljanus.tagsoup :refer :all]))

(defn- map-contains-map? [m x]
  (= (select-keys m (keys x)) x))

(defn by-fn [html-tree pred?]
  (vec
    (if-not (vector? html-tree)
    []
    (lazy-cat
      (if (pred? html-tree)
        [html-tree]
        [])
      (mapcat #(by-fn %1 pred?) (children html-tree))))))

(defn by-attribute [html-tree x]
  (by-fn html-tree #(map-contains-map? (attributes %) x)))

(defn by-tag [html-tree x]
  (by-fn html-tree #(= (tag %) x)))

