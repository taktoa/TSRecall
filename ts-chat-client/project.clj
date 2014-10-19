(defproject ts-recall "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [aleph "0.2.0"]
                 [instaparse "1.3.4"]
                 ;;[datomic ""]
                 ]
  :main ^:skip-aot ts-recall.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
