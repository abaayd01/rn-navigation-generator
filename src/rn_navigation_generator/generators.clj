(ns rn-navigation-generator.generators
  (:require [rn-navigation-generator.file-writers :as file-writers]
            [rn-navigation-generator.nav-graph :as nav-graph]
            [rn-navigation-generator.renderer :as renderer]))

(defn gen-routes-file [nav-graph]
  (->> nav-graph
       renderer/routes-file
       file-writers/write-routes-file!))

(defn gen-root-navigator-file [nav-graph]
  (->> nav-graph
       renderer/root-navigator-file
       file-writers/write-root-navigator!))

(defn gen-page-files [nav-graph]
  (let [pages (nav-graph/pages nav-graph)]
    (doseq [page pages]
      (let [p (renderer/page-file page)]
        (file-writers/write-page! (:name page) p)))))
