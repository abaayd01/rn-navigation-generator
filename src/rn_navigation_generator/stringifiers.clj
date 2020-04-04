(ns rn-navigation-generator.stringifiers
  (:require [rn-navigation-generator.templater :as templater]
            [rn-navigation-generator.node :refer :all]))

(defn stack->route-config [stack]
  (->> (:children stack)
       (map #(format "\t\t%s,\n" (route-name %)))
       (reduce str)
       (format "{\n%s\t}")))

(defn stack->stack-config [stack]
  (let [mode (if (modal-stack? stack) "modal" "card")]
    (format "{\n\t\tmode: '%s',\n\t}" mode)))

(defn stack->stack-navigator [stack]
  (let [route-config (stack->route-config stack)
        stack-config (stack->stack-config stack)]
    (templater/stack->stack-navigator {:stack-name   (:stack-name stack)
                                       :route-config route-config
                                       :stack-config stack-config})))

(defn stack-navigators->root-navigator-file [stack-navigators]
  (->> stack-navigators
       (map #(str % "\n\n"))
       (reduce str)))
