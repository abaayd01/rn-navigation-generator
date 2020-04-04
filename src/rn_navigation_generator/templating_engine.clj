(ns rn-navigation-generator.templating-engine
  (:gen-class)
  (:require [clojure.string :as str]))

(def rgx #"(\$\$(.+?)\$\$)")
(def template (slurp "./resources/templates/page-template.js"))

(def other-chunks
  (str/split
   template
   rgx))

(def refd-chunks (re-seq rgx template))

(defn deref-chunk [page [_ _ kw-str]]
  ((keyword kw-str) page))

(defn get-derefd-chunks [page]
  (->> refd-chunks
       (map (partial deref-chunk page))))

(defn parse-page [page]
  (->> page
       get-derefd-chunks
       (interleave other-chunks)
       (reduce str)))
