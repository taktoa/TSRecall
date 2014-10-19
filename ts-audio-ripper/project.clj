(defproject ts-audio-ripper "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.datomic/datomic-free "0.9.4894"]
                 [edu.cmu.sphinx/sphinx4-core "1.0-SNAPSHOT"]
                 [edu.cmu.sphinx/sphinx4-data "1.0-SNAPSHOT"]
                 [org.clojure/data.codec "0.1.0"]]
  :repositories [["sonartype snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots"}]]
  :main ^:skip-aot ts-audio-ripper.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
