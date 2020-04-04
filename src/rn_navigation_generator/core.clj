(ns rn-navigation-generator.core
  (:gen-class)
  (:use [clojure.pprint]
        [rn-navigation-generator.node]
        [rn-navigation-generator.stringifiers]
        [rn-navigation-generator.file-writers]))

; ----------- ;
; Sample Data
; ----------- ;

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

; ----------- ;

(defn -main [& args]
  (->> sample-route-def
       flatten-nodes
       flattened-nodes->root-navigator-file
       write-root-navigator!))
