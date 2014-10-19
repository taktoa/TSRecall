(defproject frontend "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}

  :min-lein-version "2.3.4"

  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2322"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.cognitect/transit-cljs "0.8.188"]
                 [om "0.8.0-alpha1"]
                 [com.facebook/react "0.11.1"]
                 [cljs-ajax "0.3.3"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :hooks [leiningen.cljsbuild]

  :cljsbuild
  {:builds {:frontend
            {:source-paths ["src/cljs"]
             :compiler
             {:output-to "dev-resources/public/js/frontend.js"
              :optimizations :advanced
              :pretty-print false}}}})
