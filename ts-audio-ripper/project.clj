(defproject ts-audio-ripper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [gloss "0.2.3"]]
  :main ^:skip-aot ts-audio-ripper.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})