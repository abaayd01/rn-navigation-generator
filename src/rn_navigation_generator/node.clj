(ns rn-navigation-generator.node)

(defrecord Page [page-name layout])

(defrecord Stack [stack-name children])

(defprotocol Node (route-name [node]))

(extend-protocol Node
  Page
  (route-name [node] (:page-name node))

  Stack
  (route-name [node] (:stack-name node)))

(defn- stack? [x] (= Stack (type x)))
(defn- page? [x] (= Page (type x)))

(defn- apply-stack-nodes [nodes node-op]
  "Apply's some fn node-op to each stack node recursively in nodes."
  (->> nodes
       (doseq [node nodes]
         (when (stack? node)
           (do
             (node-op node)
             (apply-stack-nodes (:children node) node-op))))))

;; Public
(defn flatten-nodes [nodes]
  (letfn [(push-stack-node! [a stack] (swap! a #(conj % stack)))]
    (let [result (atom '())]
      (apply-stack-nodes nodes (partial push-stack-node! result))
      @result)))

(defn pages [nodes]
  (let [flattened-nodes (flatten-nodes nodes)]
    (->> flattened-nodes
         (map :children)
         flatten
         (filter #(page? %)))))

;; Sample Data
(def login-page (->Page "LoginPage" "BaseLayout"))
(def register-page (->Page "RegisterPage" "BaseLayout"))
(def privacy-policy-page (->Page "PrivacyPolicy" "BaseLayout"))

(def legal-stack
  (->Stack "LegalStack" [privacy-policy-page]))

(def sample-stack
  (->Stack "LoginStack" [login-page
                         register-page
                         legal-stack]))

(def sample-route-def
  [(->Stack "RootStack"
            [(->Page "HomePage" "BaseLayout")
             (->Page "SettingsPage" "BaseLayout")
             sample-stack])])
