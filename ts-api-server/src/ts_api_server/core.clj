(ns ts-api-server.core
  (use [ring.middleware.transit]
       [ring.util.response]
       [ring.util.io]
       [ring.adapter.jetty])
  (require [datomic.api :as d]
           [ring.middleware.cors :refer [wrap-cors]])
  (:gen-class))

;; {:start 1000543454
;;  :end 394082458}

;; [ {"vio" "hiiii!"}
;;   {"card" "I'll cut you"}
;;   {"tak" "science autism fp math"}
;;   {"psycho" "jenken"}]

;; (def uri "datomic:mem://192.168.78.16:4334")

;; (d/create-database uri)

;; (def conn (d/connect uri))

;; ;; transaction input is data
;; (def tx-result
;;   (d/transact
;;    conn(defn handler [request]
;;   (-> (response "Hello World")
;;       (content-type "text/plain")))

;; (run-jetty handler {:port 8080})
;;    [[:db/add
;;      (d/tempid :db.part/user)
;;      :db/doc
;;      "Hello world"]]))

;; ;; transaction result is data
;; tx-result

;; (def dbval (d/db conn))

;; ;; query input is data
;; (def q-result (d/q '[:find ?e
;;                      :where [?e :db/doc "Hello world"]]
;;                    dbval))

;; ;; query result is data
;; q-result

;; ;; entity is a navigable view over data
;; (def ent (d/entity dbval (ffirst q-result)))

;; ;; entities are lazy, so...
;; (d/touch ent)

;; ;; schema itself is data
;; (def doc-ent (d/entity dbval :db/doc))

;; (d/touch doc-ent)

;; handlefuck

(def base-data
  [{:name "Cardboardicus"
    :time 122222
    :text "goodnight"}
   {:name "VioletFox"
    :time 122255
    :text "no u"}])

;; (defn handler [request]
;;   (wrap-transit-response {:body {:a true :b false} :test "word" :another "dawg"}))

;; serverfuck

(defn handler [request]
  (response base-data))

(def app
  (->
   handler
   (wrap-transit-response {:encoding :json, :opts {}})
   (wrap-cors :access-control-allow-origin #"http://localhost:3000"
              :access-control-allow-methods [:get])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "this file is pretty much useless")
  (run-jetty app {:port 8080}))
