(ns frontend.core
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cognitect.transit :as t]
            [cljs.core.async :refer [put! chan <!]]
            [ajax.core :as ajax]))

(enable-console-print!)

;; Implements ICloneable on string and js/string so that
;; React can handle these properly.

(extend-type string
    ICloneable
    (-clone [s] (js/String. s)))

(extend-type js/String
    ICloneable
    (-clone [s] (js/String. s))
    om/IValue
    (-value [s] (str s)))

(def app-state (atom {:input ""
                      :result-list ["And now," "for something" "completely" "different"]
                      :channel (chan)}))

(defn handle-input-change
    [event app]
    (let [new-input (.. event -target -value)]
          (om/update! app :title new-input)))

(defn input-field
  [app owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js
               {:id "input-field"}
               (dom/input #js
                          {:type "text"
                           :onChange #(handle-input-change % app)
                           :value (:input app)})))))

(defn search-button-click
  [app]
    (put! (:channel @app) [:search nil]))

(defn search-button
    "Component that renders the search button.
      Clicking the search button triggers the app state to update"
    [app owner]
    (reify
          om/IRender
          (render [_]
                  (dom/button #js {:id "search-button"
                                   :onClick #(search-button-click app)}
                              "search"))))

(defn results [app owner]
  (om/component
    (apply dom/ul #js {:className "keywords"}
           (map (fn [text] (dom/li nil text)) (:result-list app)))))

(defn search-database
  [app]
  nil)

(defn dispatch
  "Dispatches the incoming commands on the app channel."
  [command params app]
  (case command
    :search (search-database app)))

(defn search-app
    [app owner]
    (reify
      om/IWillMount
      (will-mount [_]
        (let [channel (:channel app)]
                  (go-loop []
                    (let [[command params] (<! channel)]
                      (dispatch command params app)
                      (recur)))))

      om/IRender
      (render [_]
        "Renders the app components in a container."
        (dom/div #js {:className "container"}
                 (om/build search-button app)
                 (om/build input-field (:input app))
                 (om/build results app)))))

(om/root
  search-app
  app-state
  {:target (. js/document (getElementById "app"))})

(defn roundtrip [x]
    (let [w (t/reader :json)
                  r (t/reader :json)]
          (t/read r (t/write w x))))

(def test-reader (t/reader :json))
;(println (t/read test-reader (ajax/GET "http://localhost:8080")))
(println (ajax/GET "http://localhost:8080"))
;(println (ajax/ajax-request {"localhost:8080" :get}))
(println (ajax/ajax-request "localhost:8080" :get
                            {:format (ajax/transit-request-format)
                             :response-format (ajax/transit-response-format {:keywords? true})}))
;(println (t/read test-reader "[[\"^ \",\"~:name\",\"Cardboardicus\",\"~:time\",122222,\"~:text\",\"goodnight\"],[\"^ \",\"^0\",\"VioletFox\",\"^1\",122255,\"^2\",\"no u\"]]"))
