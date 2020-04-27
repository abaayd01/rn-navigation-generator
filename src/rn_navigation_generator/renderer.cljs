(ns rn-navigation-generator.renderer
  (:require [rn-navigation-generator.nav-graph :as nav-graph]
            [rn-navigation-generator.io :refer [read-template]]
            [rn-navigation-generator.templates :as templates]
            ["handlebars" :as handlebars]))

(def routes-template
  ^{:private true}
  (. handlebars compile templates/routes-template))

(def root-navigator-template
  ^{:private true}
  (. handlebars compile templates/root-navigator-template))

(def page-template
  ^{:private true}
  (. handlebars compile templates/page-template))

(defn routes-file [nav-graph]
  (let [route-names (nav-graph/route-names nav-graph)]

    (routes-template (clj->js {:route-names route-names}))))

(defn root-navigator-file [nav-graph]
  (let [prepare-stack (fn [stack]
                        (hash-map
                          :stack-name (:name stack)
                          :route-names (nav-graph/node-route-names stack)))
        stacks (map prepare-stack (nav-graph/stacks nav-graph))]

    (root-navigator-template (clj->js {:stacks stacks}))))

(defn page-file [page]
  (page-template (clj->js
                   {:page-name   (:name page)
                    :layout      "BaseLayout"
                    :route-names (nav-graph/node-route-names page)})))
