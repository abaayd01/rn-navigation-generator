(ns rn-navigation-generator.sandbox
  (:require [clojure.xml :as xml]
            [rn-navigation-generator.node :as node]))

(def data (xml/parse "resources/sample_file.xml"))

(defn- extract-cells [m]
  (if-not (or (= (:tag m) :mxCell)
              (= (:tag m) :object))
    (->> (:content m)
         (map extract-cells)
         flatten)
    m))

(defn- node-type [{{:keys [node_type]} :attrs}] node_type)

(defn- page? [cell] (= (node-type cell) "page"))

(defn- stack? [cell] (= (node-type cell) "stack"))

(defn- vertex? [cell] (or (stack? cell) (page? cell)))

(defn- edge? [{{:keys [edge]} :attrs}] (= edge "1"))

(defn- vertex-id [{{:keys [id]} :attrs}] id)

(defn- edge-source-id [edge]
  (let [{{source :source} :attrs} edge] source))

(defn- edge-target-id [edge]
  (let [{{target :target} :attrs} edge] target))

(defn- extract-vertices [xml-data]
  (let [cells (extract-cells xml-data)]
    (->> cells
         (filter vertex?))))

(defn- extract-edges [xml-data]
  (let [cells (extract-cells xml-data)]
    (->> cells
         (filter edge?))))

(defn- assoc-edges [grouped-edges {{id :id} :attrs :as vertex}]
  (let [edges (get grouped-edges id)]
    (assoc vertex :edges edges)))

(defn- group-by-vertex-id [vertices]
  (into {} (map #(vector (vertex-id %) %) vertices)))

(defn make-graph [data]
  (let [vertices (extract-vertices data)
        grouped-edges (group-by edge-source-id (extract-edges data))]
    (->> vertices
         (map #(assoc-edges grouped-edges %))
         group-by-vertex-id)))

(defn- get-vertex [graph id] (get graph id))

(defn- assoc-adjacent-vertices [graph vertex]
  (let [adjacent-vertices
        (->> (:edges vertex)
             (map #(get-vertex graph (edge-target-id %))))]
    (assoc vertex :adjacent-vertices adjacent-vertices)))

(defn dereference-vertices [graph]
  "Dereferences the edges attached to each vertex by 1 layer."
  (->> graph
       (map (fn [[_ vertex]] (assoc-adjacent-vertices graph vertex)))
       group-by-vertex-id))

