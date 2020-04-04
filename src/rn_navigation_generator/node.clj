(ns rn-navigation-generator.node)

(defrecord Page [page-name layout])

(defrecord Stack [stack-name children])

(defn stack? [x] (= Stack (type x)))

(defprotocol Node (route-name [node]))

(extend-protocol Node
  Page
  (route-name [node] (:page-name node))

  Stack
  (route-name [node] (:stack-name node)))

(defn apply-stack-nodes [nodes node-op]
  "Apply's some fn node-op to each stack node recursively in nodes."

  (->> nodes
       (doseq [node nodes]
         (when (stack? node)
           (do
             (node-op node)
             (apply-stack-nodes (:children node) node-op))))))

(defn push-stack-node! [a stack]
  (swap! a #(conj % stack)))

(defn flatten-nodes [nodes]
  (let [result (atom '())]
    (apply-stack-nodes nodes (partial push-stack-node! result))
    @result))

