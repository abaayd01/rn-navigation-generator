(ns rn-navigation-generator.templater
  (:gen-class)
  (:require [clojure.string :as str]
            [rn-navigation-generator.node :as node]))

(def rgx #"(\$\$(.+?)\$\$)")
(def page-template (slurp "./resources/templates/page-template.js"))
(def stack-navigator-template (slurp "./resources/templates/create-stack-navigator-template.js"))
(def button-template (slurp "./resources/templates/button-template.js"))
(def routes-template (slurp "./resources/templates/routes-template.js"))

(defn get-other-chunks [template] (str/split template rgx))

(defn refd-chunks [template] (re-seq rgx template))

(defn deref-chunk [data [_ _ kw-str]]
  ((keyword kw-str) data))

(defn get-derefd-chunks [template data]
  (->> (refd-chunks template)
       (map #(deref-chunk data %))))

(defn assemble [template data]
  (let [d (get-derefd-chunks template data)
        o (get-other-chunks template)
        r (nthrest o (count d))]
    (->> (concat (interleave o d) r)
         (reduce str))))

(defn parse-page [page] (assemble page-template page))

(defn parse-button [m] (assemble button-template m))

(defn parse-routes-file [m] (assemble routes-template m))

(defn stack->stack-navigator [stack] (assemble stack-navigator-template stack))

