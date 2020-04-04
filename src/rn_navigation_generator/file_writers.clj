(ns rn-navigation-generator.file-writers
  (:require [clojure.java.io :refer [make-parents]]
    #_[rn-navigation-generator.templating-engine :refer [parse-page]]))

(defn to-bytes [s]
  (bytes (byte-array (map byte s))))

(defn write-file! [filename s]
  (make-parents filename)
  (spit filename (String. (to-bytes s))))

(def write-root-navigator! (partial write-file! "./gen/rootNavigator.js"))

;(defn write-pages! [pages]
;  (doseq [page pages]
;    (write-file!
;      (format "./gen/pages/%s.js" (:page-name page))
;      (parse-page page))))
