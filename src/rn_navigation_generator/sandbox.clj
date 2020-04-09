(ns rn-navigation-generator.sandbox
  (:require [clojure.xml :as xml]
            [rn-navigation-generator.node :as node]))

(def data (xml/parse "resources/sample_file.xml"))

(defn extract-cells [m]
  (if-not (or (= (:tag m) :mxCell)
              (= (:tag m) :object))
    (->> (:content m)
         (map extract-cells)
         flatten)
    m))

(defn- node-type [{{:keys [node_type]} :attrs}] node_type)

(defn cells->pages [cells]
  (let [page? #(= (node-type %) "page")
        cell->page (fn [{{page-name :label} :attrs}]
                     (node/map->Page {:page-name page-name}))]

    (->> cells
         (filter page?)
         (map cell->page))))

(defn cells->stacks [cells]
  (let [stack? #(= (node-type %) "stack")
        cell->stack (fn [{{stack-name :label} :attrs}]
                      (node/map->Stack {:stack-name stack-name}))]

    (->> cells
         (filter stack?)
         (map cell->stack))))

(cells->pages (extract-cells data))
(cells->stacks (extract-cells data))
