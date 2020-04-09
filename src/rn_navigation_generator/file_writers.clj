(ns rn-navigation-generator.file-writers
  (:require [clojure.java.io :refer [make-parents]]))

(defn- write-file! [filename s]
  (make-parents filename)
  (spit filename s))

(def write-root-navigator! (partial write-file! "./gen/rootNavigator.js"))

(defn write-page! [page-name page-content]
  (write-file! (format "./gen/pages/%s.js" page-name) page-content))

(def write-routes-file! (partial write-file! "./gen/routes.js"))
