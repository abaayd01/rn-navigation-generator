(ns rn-navigation-generator.renderer
  (:require [rn-navigation-generator.nav-graph :as nav-graph]
            [rn-navigation-generator.io :refer [read-template]]
            ["handlebars" :as handlebars]))

(defn routes-file [nav-graph]
  (let [route-names (nav-graph/route-names nav-graph)
        template (. handlebars compile (read-template "routes"))]

    (template (clj->js {:route-names route-names}))))

(defn root-navigator-file [nav-graph]
  (let [prepare-stack (fn [stack]
                        (hash-map
                          :stack-name (:name stack)
                          :route-names (nav-graph/node-route-names stack)))
        stacks (map prepare-stack (nav-graph/stacks nav-graph))
        template (. handlebars compile (read-template "root-navigator"))]

    (template (clj->js {:stacks stacks}))))

(defn page-file [page]
  (let [template (. handlebars compile (read-template "page"))]

    (template (clj->js
                {:page-name   (:name page)
                 :layout      "BaseLayout"
                 :route-names (nav-graph/node-route-names page)}))))
