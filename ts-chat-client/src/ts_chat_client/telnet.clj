(ns ts-chat-client.telnet
  (:require [instaparse.core :as insta])
  (:import (java.net Socket)
           (java.io PrintWriter InputStreamReader BufferedReader))
  (:gen-class))

(def fuspr
  {:name "localhost" :port 25639})

(declare conn-handler)
(def msg-parse  (insta/parser (clojure.java.io/resource "msgparse.bnf")))

(defn connect [server handler]
  (let [socket (Socket. (:name server) (:port server))
        in (BufferedReader. (InputStreamReader. (.getInputStream socket)))
        out (PrintWriter. (.getOutputStream socket))
        conn (ref {:in in :out out})]
    (doto (Thread. #(conn-handler conn handler)) (.start))
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

(defn conn-handler [conn handler]
  (while (nil? (:exit @conn))
    (let [msg (.readLine (:in @conn))
          parsed (msg-parse msg)]
      (handler (if (insta/failure? parsed) {} parsed))
      (cond
       (re-find #"^PING" msg)
       (write conn (str "PONG "  (re-find #":.*" msg)))))))

(defn byte-seq [rdr]
  "create a lazy seq of bytes in a file and close the file at the end"
  (let [result (. rdr read)]
    (if (= result -1)
     (do (. rdr close) nil)
     (lazy-seq (cons result (byte-seq rdr))))))