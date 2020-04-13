(ns rn-navigation-generator.adapters.draw-io
  (:require [clojure.data.xml :as xml]))

(defn cell? [m] (or (= (:tag m) :mxCell)
                    (= (:tag m) :object)))

(defn- node-type [{{:keys [node_type]} :attrs}] node_type)
(defn- page? [cell] (= (node-type cell) "page"))
(defn- stack? [cell] (= (node-type cell) "stack"))

(defn- vertex? [cell] (or (stack? cell) (page? cell)))
(defn- vertex-id [{{:keys [id]} :attrs}] id)

(defn- edge? [{{:keys [edge]} :attrs}] (= edge "1"))
(defn- edge-source-id [edge]
  (let [{{source :source} :attrs} edge] source))
(defn- edge-target-id [edge]
  (let [{{target :target} :attrs} edge] target))

(defn- extract-cells [m]
  (if-not (cell? m)
    (->> (:content m)
         (map extract-cells)
         flatten)
    m))

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

(defn- get-vertex [graph id] (first (filter #(= (vertex-id %) id) graph)))
(defn- assoc-adjacent-vertices [graph vertex]
  (let [adjacent-vertices
        (->> (:edges vertex)
             (map #(get-vertex graph (edge-target-id %))))]
    (assoc vertex :adjacent-vertices adjacent-vertices)))

(defn- make-graph [xml-data]
  (let [vertices (extract-vertices xml-data)
        grouped-edges (group-by edge-source-id (extract-edges xml-data))]
    (->> vertices
         (map #(assoc-edges grouped-edges %)))))

(defn- dereference-vertices [graph]
  (->> graph
       (map (fn [vertex] (assoc-adjacent-vertices graph vertex)))))

(defn- vertex->node [{{id :id type :node_type name :label} :attrs
                      adjacent-vertices                    :adjacent-vertices}]
  {:id             id
   :name           name
   :type           type
   :adjacent-nodes (map vertex->node adjacent-vertices)})

(defn make-nav-graph [filename]
  (->> (clojure.java.io/reader filename)
       xml/parse
       make-graph
       dereference-vertices
       (map vertex->node)))

