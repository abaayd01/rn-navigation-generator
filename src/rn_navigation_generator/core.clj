(ns rn-navigation-generator.core
  (:gen-class)
  (:require [rn-navigation-generator.node :as node]
            [rn-navigation-generator.stringifiers :as stringifiers]
            [rn-navigation-generator.file-writers :as file-writers]))

(defn gen-page-files [route-def]
  (let [pages (node/pages route-def)]
    (doseq [page pages]
      (let [page-content (stringifiers/page->page-content page)]
        (file-writers/write-page! (:page-name page) page-content)))))

(defn gen-root-navigator-file [route-def]
  (->> route-def
       stringifiers/route-def->root-navigator-content
       file-writers/write-root-navigator!))

(defn gen-routes-file [route-def]
  (->> route-def
       stringifiers/route-def->routes-content
       file-writers/write-routes-file!))

(gen-routes-file node/sample-route-def)
(gen-page-files node/sample-route-def)
(gen-root-navigator-file node/sample-route-def)

(defn -main [& args]
  (println "do something..."))

