(ns rn-navigation-generator.core
  (:gen-class)
  (:require [rn-navigation-generator.node :as node]
            [rn-navigation-generator.stringifiers :as stringifiers]
            [rn-navigation-generator.file-writers :as file-writers]))

(defn gen-page-files [route-def]
  (let [make-page-data-map (fn [page]
                             (hash-map
                               :page-name (:page-name page)
                               :parsed-page (stringifiers/page->page-file page)))
        page-data-map-coll (->> route-def
                            node/pages
                            (map make-page-data-map))]
    (doseq [{:keys [page-name parsed-page]} page-data-map-coll]
      (file-writers/write-page! page-name parsed-page))))

(defn gen-root-navigator-file [route-def]
  (let [stacks (node/stacks route-def)
        pages (node/pages route-def)]
    (->> stacks
         (map stringifiers/stack->stack-navigator)
         (stringifiers/stack-navigators->root-navigator-file pages)
         file-writers/write-root-navigator!)))

(defn gen-routes-file [route-def]
  (let [stacks (node/stacks route-def)]
    (->> stacks
         (map node/stack->route-names)
         flatten
         distinct
         stringifiers/route-names->routes-file
         file-writers/write-routes-file!)))

(gen-routes-file node/sample-route-def)
(gen-page-files node/sample-route-def)
(gen-root-navigator-file node/sample-route-def)

(defn -main [& args]
  (println "do something..."))

