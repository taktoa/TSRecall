(defproject ts-api-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
		             [com.datomic/datomic-free "0.9.4880"]
                 [ring "1.3.1"]
                 [ring-cors "0.1.4"]
                 [ring-transit "0.1.2"]]
  :main ^:skip-aot ts-api-server.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
