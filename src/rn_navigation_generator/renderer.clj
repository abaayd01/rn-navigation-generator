(ns rn-navigation-generator.renderer
  (:require [selmer.parser :refer [render-file]]
            [selmer.util :refer [without-escaping]]
            [rn-navigation-generator.nav-graph :as nav-graph]))

(defn routes-file [nav-graph]
  (let [route-names (nav-graph/route-names nav-graph)]
    (render-file
      "templates/routes.template"
      {:route-names route-names})))

(defn root-navigator-file [nav-graph]
  (let [prepare-stack (fn [stack]
                        (hash-map
                          :stack-name (:name stack)
                          :route-names (nav-graph/node-route-names stack)))
        stacks (map prepare-stack (nav-graph/stacks nav-graph))]

    (render-file
      "templates/root-navigator.template"
      {:stacks stacks})))

(defn page-file [page]
  (without-escaping
    (render-file
      "templates/page.template"
      {:page-name   (:name page)
       :layout      "SpaceLayout"
       :route-names (nav-graph/node-route-names page)})))
