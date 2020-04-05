(ns rn-navigation-generator.file-writers
  (:require [clojure.java.io :refer [make-parents]]))

(defn- to-bytes [s]
  (bytes (byte-array (map byte s))))

(defn- write-file! [filename s]
  (make-parents filename)
  (spit filename (String. (to-bytes s))))

(def write-root-navigator! (partial write-file! "./gen/rootNavigator.js"))

(defn write-page! [page-name parsed-page]
  (write-file! (format "./gen/pages/%s.js" page-name) parsed-page))

(def write-routes-file! (partial write-file! "./gen/routes.js"))
