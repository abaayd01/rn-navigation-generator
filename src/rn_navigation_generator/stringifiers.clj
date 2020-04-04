(ns rn-navigation-generator.stringifiers
  (:require [rn-navigation-generator.node :refer :all]))

(defn stack->route-config [stack]
  (->> (:children stack)
       (map #(format "\t%s,\n" (route-name %)))
       (reduce str)
       (format "{\n%s}")))

(defn stack->stack-navigator [stack]
  (let [route-config (stack->route-config stack)]
    (format
      "// %s\ncreateStackNavigator(%s);"
      (:stack-name stack)
      route-config)))

(defn flattened-nodes->root-navigator-file [flattened-nodes]
  (->> flattened-nodes
       (map stack->stack-navigator)
       (map #(str % "\n\n"))
       (reduce str)))
