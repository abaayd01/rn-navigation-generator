(ns rn-navigation-generator.nav-graph)

(defn route-names [nav-graph]
  (map :name nav-graph))

(defn stack? [{type :type}] (= type "stack"))
(defn page? [{type :type}] (= type "page"))

(defn stacks [nav-graph]
  (filter stack? nav-graph))

(defn pages [nav-graph]
  (filter page? nav-graph))

(defn node-route-names [node]
  (let [children (:adjacent-nodes node)]
    (map :name children)))
