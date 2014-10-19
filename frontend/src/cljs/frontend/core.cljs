(ns frontend.core
  (:require-macros [cljs.core.async.macros :refer [go-loop]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [put! chan <!]]
            [goog.events :as events])
  (:import [goog.events EventType]
           [goog.async Throttle]))

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
                      :output "Die in a fire."
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

(defn output [app owner] (reify
                           om/IRender
                           (render [_]
                             (dom/p nil (:output app)))))

(defn search-database
  [app]
  (swap! app-state assoc :text "Something completely different."))

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
                 (om/build output app)))))

(om/root
  search-app
  app-state
  {:target (. js/document (getElementById "app"))})
