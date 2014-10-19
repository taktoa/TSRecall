(ns ts-audio-ripper.core
  (:import [edu.cmu.sphinx.api.Configuration]
           [edu.cmu.sphinx.api.LiveSpeechRecognizer]
           [edu.cmu.sphinx.api.StreamSpeechRecognizer]
           [edu.cmu.sphinx.api.SpeechResult])
  (:use   [clojure.java.io])
  (:gen-class))

(defn get-current-time
  "Get the current time in milliseconds"
  []
  (System/currentTimeMillis))

(def ac-model-path "resource:/en-us")
(def dict-path "resource:/cmudict.0.7a")
(def lang-model-path "resource:/cmusphinx-5.0-en-us.lm")

(defn -main
  "Main function"
  [& args]
  (def stream-conf
    (doto (new edu.cmu.sphinx.api.Configuration)
          (.setAcousticModelPath ac-model-path)
          (.setDictionaryPath dict-path)
          (.setLanguageModelPath lang-model-path)))

  (def stream-sr (new edu.cmu.sphinx.api.LiveSpeechRecognizer stream-conf))

  (.startRecognition stream-sr true)

  (def result (.getResult stream-sr))

  (.stopRecognition stream-sr)

  result)

;; (defn -main
;;   "Main function"
;;   [& args]
;;   (def stream-conf
;;     (doto (new edu.cmu.sphinx.api.Configuration)
;;           (.setAcousticModelPath ac-model-path)
;;           (.setDictionaryPath dict-path)
;;           (.setLanguageModelPath lang-model-path)))

;;   (def stream-sr (new edu.cmu.sphinx.api.StreamSpeechRecognizer stream-conf))

;;   (def test-file
;;     (doto (new java.io.File "/home/remy/test-ts3.wav")
;;           (.toURI)
;;           (.toURL)))

;;   (.startRecognition stream-sr (input-stream test-file))

;;   (def result (.getResult stream-sr))

;;   (.stopRecognition stream-sr)

;;   result)
