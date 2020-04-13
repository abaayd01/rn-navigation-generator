(ns rn-navigation-generator.core
  (:gen-class)
  (:require [rn-navigation-generator.adapters.draw-io :as draw-io]))

(def sample-nav-graph (draw-io/make-nav-graph "resources/sample_file.xml"))

(defn -main [& args]
  (println "do something..."))

