(ns rn-navigation-generator.utils)

(defn hook
  {:shadow.build/stage :compile-prepare}
  [build-state & args]
  (let [templates-file-path "./src/rn_navigation_generator/templates.cljs"
        preamble "(ns rn-navigation-generator.templates)\n\n"
        page-template (slurp "./src/templates/page.template")
        root-navigator-template (slurp "./src/templates/root-navigator.template")
        routes-template (slurp "./src/templates/routes.template")]

    (spit templates-file-path preamble)
    (spit templates-file-path (str "(def page-template " "\n\"" page-template "\")\n\n") :append true)
    (spit templates-file-path (str "(def root-navigator-template " "\n\"" root-navigator-template "\")\n\n") :append true)
    (spit templates-file-path (str "(def routes-template " "\n\"" routes-template "\")\n\n") :append true))

  build-state)

