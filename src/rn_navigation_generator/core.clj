(ns rn-navigation-generator.core
  (:gen-class)
  (:require [rn-navigation-generator.node :as node]
            [rn-navigation-generator.stringifiers :as stringifiers]
            [rn-navigation-generator.file-writers :as file-writers]
            [rn-navigation-generator.templating-engine :as templater]))

(defn gen-page-files [route-def]
  (let [page-data-list (->> route-def
                            node/pages
                            (map #(hash-map :parsed-page (templater/parse-page %) :page-name (:page-name %))))]
    (doseq [{:keys [page-name parsed-page]} page-data-list]
      (file-writers/write-page! page-name parsed-page))))

(defn gen-root-navigator-file [route-def]
  (->> route-def
       node/flatten-nodes
       stringifiers/flattened-nodes->root-navigator-file
       file-writers/write-root-navigator!))

(defn -main [& args]
  (println "do something..."))
