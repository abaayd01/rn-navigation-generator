(ns rn-navigation-generator.core
  (:require [rn-navigation-generator.adapters.draw-io :refer [make-nav-graph]]
            [rn-navigation-generator.generators :as generators]))

(defn -main [filename]
  (println "Generating from file:" filename)
  (generators/gen-routes-file (make-nav-graph filename))
  (generators/gen-root-navigator-file (make-nav-graph filename))
  (generators/gen-page-files (make-nav-graph filename)))
