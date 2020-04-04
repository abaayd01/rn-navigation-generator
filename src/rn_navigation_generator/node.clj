(ns rn-navigation-generator.node)

(defrecord Page [page-name layout])

(defrecord Stack [stack-name children config])

(defprotocol Node (route-name [node]))

(extend-protocol Node
  Page
  (route-name [node] (:page-name node))

  Stack
  (route-name [node] (:stack-name node)))

(defn- stack? [x] (= Stack (type x)))
(defn- page? [x] (= Page (type x)))

(defn modal-stack? [s]
  (let [{{:keys [modal]} :config} s]
    (= true modal)))

(defn- apply-stack-nodes [nodes node-op]
  "Apply some fn node-op to each stack node recursively in nodes."
  (->> nodes
       (doseq [node nodes]
         (when (stack? node)
           (do
             (node-op node)
             (apply-stack-nodes (:children node) node-op))))))

;; Public
(defn- push-stack-node! [a stack] (swap! a #(conj % stack)))

(defn flatten-nodes [nodes]
  (let [result (atom '())]
    (apply-stack-nodes nodes (partial push-stack-node! result))
    @result))

(defn stacks [nodes]
  (->> nodes
       flatten-nodes))

(defn pages [nodes]
  (->> nodes
       flatten-nodes
       (map :children)
       flatten
       (filter #(page? %))))

;; Sample Data
(def privacy-policy-page (->Page "PrivacyPolicy" "BaseLayout"))
(def legal-stack
  (map->Stack {:stack-name "LegalStack"
               :children   [privacy-policy-page]}))

(def login-page (->Page "LoginPage" "BaseLayout"))
(def register-page (->Page "RegisterPage" "BaseLayout"))
(def login-stack
  (map->Stack {:stack-name "LoginStack"
               :children   [login-page
                            register-page
                            legal-stack]
               :config     {:modal true}}))

(def home-page (->Page "HomePage" "BaseLayout"))
(def settings-page (->Page "SettingsPage" "BaseLayout"))
(def root-stack
  (map->Stack {:stack-name "RootStack"
               :children   [home-page
                            settings-page
                            login-stack]}))

(def sample-route-def (list root-stack))
