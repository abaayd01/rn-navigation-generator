(ns rn-navigation-generator.node)

(defrecord Page [id page-name layout links])

(defrecord Stack [id stack-name children config])

(defprotocol Node (route-name [node]))

(extend-protocol Node
  Page
  (route-name [node] (:page-name node))

  Stack
  (route-name [node] (:stack-name node)))

(defn- stack? [x] (= Stack (type x)))
(defn- page? [x] (= Page (type x)))

(defn- apply-stack-nodes [nodes node-op]
  "Apply some fn node-op to each stack node recursively in nodes."
  (->> nodes
       (doseq [node nodes]
         (when (stack? node)
           (do
             (node-op node)
             (apply-stack-nodes (:children node) node-op))))))

(defn- push-stack-node! [a stack] (swap! a #(conj % stack)))

(defn- flatten-nodes [nodes]
  (let [result (atom '())]
    (apply-stack-nodes nodes (partial push-stack-node! result))
    @result))

;; Public
(defn stacks [nodes]
  (->> nodes
       flatten-nodes))

(defn pages [nodes]
  (->> nodes
       flatten-nodes
       (map :children)
       flatten
       (filter #(page? %))))

(defn stack->route-names [stack]
  (concat [(:stack-name stack)]
          (map route-name (:children stack))))

(defn route-names [nodes]
  (->> nodes
       stacks
       (mapcat stack->route-names)))

;; Sample Data
(def privacy-policy-page
  (map->Page {:page-name "PrivacyPolicy"
              :layout    "BaseLayout"}))

(def legal-stack
  (map->Stack {:stack-name "LegalStack"
               :children   [privacy-policy-page]}))

(def register-page
  (map->Page {:page-name "RegisterPage"
              :layout    "BaseLayout"}))

(def login-page
  (map->Page {:page-name "LoginPage"
              :layout    "BaseLayout"
              :links     ["PrivacyPolicy"
                          "RegisterPage"]}))

(def login-stack
  (map->Stack {:stack-name "LoginStack"
               :children   [login-page
                            register-page
                            legal-stack]
               :config     {:modal true}}))

(def settings-page
  (map->Page {:page-name "SettingsPage"
              :layout    "BaseLayout"}))

(def home-page
  (map->Page {:page-name "HomePage"
              :layout    "BaseLayout"}))

(def root-stack
  (map->Stack {:stack-name "RootStack"
               :children   [home-page
                            settings-page
                            login-stack]}))

(def sample-route-def (list root-stack))
