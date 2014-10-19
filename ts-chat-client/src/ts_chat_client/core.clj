(ns ts-chat-client.core
  (:require [instaparse.core :as insta])
  (:use [datomic.api :only [db q] :as d]
        [ts-chat-client.telnet :as telnet]
        )
  (:import (java.net Socket)
           (java.io PrintWriter InputStreamReader BufferedReader))
  (:gen-class))

(def uri
  ;"datomic:free://localhost:4334")
  "datomic:mem://127.0.0.1:4334")

(defn make-db []
  (d/create-database uri))

(make-db)

(defn delete-db []
  (d/delete-database uri))

(def conn (d/connect uri))

(defn add-person-attribute []
  (d/transact conn [{:db/id #db/id[:db.part/db]
                     :db/ident :person/name
                     :db/valueType :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/doc "A person's name"
                     :db.install/_attribute :db.part/db}]))


(defn add-address-attribute []
  (d/transact conn [{:db/id #db/id[:db.part/db]
                     :db/ident :person/address
                     :db/valueType :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/doc "A person's address"
                     :db.install/_attribute :db.part/db}]))

(defn add-chat-attribute []
  (d/transact conn [{:db/id #db/id[:db.part/db]
                     :db/ident :msg/text
                     :db/valueType :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/doc "a chat's log"
                     :db.install/_attribute :db.part/db}]))


(add-person-attribute)
(add-address-attribute)
(add-chat-attribute)


(defn add-a-person [name]
  (d/transact conn [{:db/id #db/id[:db.part/user] :person/name name}]))

(defn add-mesg [message]
  (d/transact conn [{:db/id #db/id[:db.part/user] :msg/text message}]))

(defn get-all-people []
  (q '[:find ?n :where [?c person/name ?n ]] (db conn)))

(defn get-all-chats []
  (q '[:find ?n :where [?c msg/text ?n ]] (db conn)))

(defn find-person [name dbc]
  (first (first (q `[:find ?c :where [?c person/name ~name]] dbc))))

(defn find-address [address dbc]
  (first (first (q `[:find ?c :where [?c person/name ~name]] dbc))))

(defn illustrators-that-live-with [name]
  (q `[:find ?n1 :where
       [?c person/name ~name]
       [?c person/address ?a]
       [?n person/name ?n1]]  (db conn)))

(defn add-address [name address]
  (let [dbc (db conn)
        id (find-person name dbc)]
    (d/transact conn [{:db/id id :person/msg address}])))

(defn shit [m]
  (if (= "notifytalkstatuschange" (get m "command")) (add-mesg (str (get m "invokername") (get m "msg"))) nil))

(defn -main
  [& args]
   (def tn (telnet/connect fuspr shit))
   (telnet/write tn "clientnotifyregister schandlerid=0 event=any")
  )

