(ns rn-navigation-generator.stringifiers
  (:require [selmer.parser :refer [render-file render]]
            [selmer.util :refer [without-escaping]]
            [rn-navigation-generator.node :as node]))

(defn page->page-content [page]
  (without-escaping
    (render-file
      "templates/page.template"
      {:page-name   (:page-name page)
       :layout      (:layout page)
       :route-names (:links page)})))

(defn route-def->root-navigator-content [route-def]
  (let [prepare-stack (fn [stack] (assoc stack :route-names (node/stack->route-names stack)))
        stacks (map prepare-stack (node/stacks route-def))]
    (render-file
      "templates/root-navigator.template"
      {:stacks stacks})))

(defn route-def->routes-content [route-def]
  (let [route-names (node/route-names route-def)]
    (render-file
      "templates/routes.template"
      {:route-names route-names})))

