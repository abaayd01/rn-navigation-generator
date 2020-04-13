(ns rn-navigation-generator.core
  (:gen-class)
  (:require [clojure.tools.cli :refer [parse-opts]]
            [rn-navigation-generator.adapters.draw-io :as draw-io]
            [rn-navigation-generator.generators :as generators]))

(def cli-options
  [["-f" "--filename FILENAME" "Filename"
    :default "data.xml"]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{{filename :filename} :options} (parse-opts args cli-options)
        nav-graph (draw-io/make-nav-graph filename)]
    (println "generating...")
    (generators/gen-routes-file nav-graph)
    (generators/gen-root-navigator-file nav-graph)
    (generators/gen-page-files nav-graph)
    (println "done.")))
