(ns rn-navigation-generator.io
  (:require ["fs" :as fs]
            goog.string.format))

(defn- read-file [filename]
  (. fs readFileSync filename "utf8"))

(defn read-template [template-name]
  (read-file (str "src/templates/" template-name ".template")))

(defn- path-exists? [path] (. fs existsSync path))

(defn- create-gen-dir! []
  (when (not (path-exists? "./gen"))
    (. fs mkdirSync "./gen")))

(defn- create-gen-pages-dir! []
  (when (not (path-exists? "./gen/pages"))
    (. fs mkdirSync "./gen/pages")))

(defn- write-file! [filename s]
  (create-gen-dir!)
  (. fs writeFileSync filename s))

(def write-routes-file! (partial write-file! "./gen/routes.js"))

(def write-root-navigator! (partial write-file! "./gen/rootNavigator.js"))

(defn write-page! [page-name page-content]
  (create-gen-pages-dir!)
  (write-file! (goog.string.format "./gen/pages/%s.js" page-name) page-content))
