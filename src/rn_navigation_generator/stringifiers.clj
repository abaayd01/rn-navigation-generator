(ns rn-navigation-generator.stringifiers
  (:require [rn-navigation-generator.templater :as templater]
            [rn-navigation-generator.node :refer :all]))

(defn- stack->route-config [stack]
  (->> (:children stack)
       (map #(format "\t\t%s,\n" (route-name %)))
       (reduce str)
       (format "{\n%s\t}")))

(defn- stack->stack-config [stack]
  (let [mode (if (modal-stack? stack) "modal" "card")]
    (format "{\n\t\tmode: '%s',\n\t}" mode)))

(defn stack->stack-navigator [stack]
  (let [route-config (stack->route-config stack)
        stack-config (stack->stack-config stack)]
    (templater/stack->stack-navigator {:stack-name   (:stack-name stack)
                                       :route-config route-config
                                       :stack-config stack-config})))

(defn page->page-import [page]
  (let [page-name (:page-name page)]
    (format "import %s from '../pages/%s';\n" page-name page-name)))

(defn stack-navigators->root-navigator-file [pages stack-navigators]
  (let [page-imports (->> pages
                          (map page->page-import)
                          (reduce str))]
    (->> stack-navigators
         (map #(str % "\n\n"))
         (reduce str)
         (format "%s\n%s" page-imports))))

(defn page->page-file [page]
  (templater/parse-page page))
