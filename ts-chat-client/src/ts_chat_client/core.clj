(ns ts-chat-client.core
  (:require [instaparse.core :as insta])
  (:import (java.net Socket)
           (java.io PrintWriter InputStreamReader BufferedReader))
  (:gen-class))

(def fuspr {:name "localhost" :port 25639})

(declare conn-handler)

(def msg-parse  (insta/parser (clojure.java.io/resource "msgparse.bnf")))

(defn connect [server]
  (let [socket (Socket. (:name server) (:port server))
        in (BufferedReader. (InputStreamReader. (.getInputStream socket)))
        out (PrintWriter. (.getOutputStream socket))
        conn (ref {:in in :out out})]
    (doto (Thread. #(conn-handler conn)) (.start))
    conn))

(defn parsed-to-hash [parsed]
  (let [processed-parsed (map rest parsed)]
    (conj (reduce #(assoc %1 (first %2) (last %2))
                  {}
                  (map #(map last %) (rest processed-parsed)))
          {"command" (first (first processed-parsed))})))

(defn write [conn msg]
  (doto (:out @conn)
    (.println (str msg "\r"))
    (.flush)))

(defn handle-msg [msg]
  (println msg))

(defn conn-handler [conn]
  (while (nil? (:exit @conn))
    (let [msg (.readLine (:in @conn))]
      (handle-msg msg)
      (cond
       (re-find #"^ERROR :Closing Link:=" msg)
       (dosync (alter conn merge {:exit true}))
       (re-find #"^PING" msg)
       (write conn (str "PONG "  (re-find #":.*" msg)))))))

(defn byte-seq [rdr]
  "create a lazy seq of bytes in a file and close the file at the end"
  (let [result (. rdr read)]
    (if (= result -1)
     (do (. rdr close) nil)
     (lazy-seq (cons result (byte-seq rdr))))))

(defn -main
  [& args]
  (def tn (connect fuspr))
  (write tn "clientnotifyregister schandlerid=0 event=any"))

