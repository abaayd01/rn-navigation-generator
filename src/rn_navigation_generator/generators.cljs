(ns rn-navigation-generator.generators
  (:require [rn-navigation-generator.renderer :as renderer]
            [rn-navigation-generator.nav-graph :as nav-graph]
            [rn-navigation-generator.io :as io]))

(defn gen-routes-file [nav-graph]
  (->> nav-graph
       renderer/routes-file
       io/write-routes-file!))

(defn gen-root-navigator-file [nav-graph]
  (->> nav-graph
       renderer/root-navigator-file
       io/write-root-navigator!))

(defn gen-page-files [nav-graph]
  (let [pages (nav-graph/pages nav-graph)]
    (doseq [page pages]
      (let [p (renderer/page-file page)]
        (io/write-page! (:name page) p)))))
