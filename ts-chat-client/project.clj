(defproject ts-chat-client "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [aleph "0.2.0"]
                 [instaparse "1.3.4"]
                 [com.datomic/datomic-free "0.9.4894"]]
  :main ^:skip-aot ts-chat-client.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
