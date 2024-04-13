(ns app.demo
  (:require
    [com.fulcrologic.fulcro.dom :as dom]
    ["react-dom" :as react.dom]
    [clojure.pprint :refer [pprint]]))

(defonce state (atom {:count1 0
                      :hello  "world"
                      :champs [{:name "Foo"}
                               {:name "Bar"}]}))

(defn ui-app-state [current-state]
  ;; HTM
  (dom/div {}
    (dom/hr {})
    ;; HTML: <h5>Your app state is currently:</h5>
    (dom/h5 {} "Your app state is currently:")
    (dom/pre {}
      (with-out-str
        (pprint current-state)))
    (dom/hr {})))

(defn Root []
  (let [current-state @state
        current-count (get current-state :count1)]
    (dom/div {:style {:color "red"}}
      (dom/button
        {:onClick (fn [_]
                    (swap! state update :champs conj {:name (str "Champion" current-count)})
                    (swap! state update :count1 inc))}
        (str "Clicked:" current-count))
      (map
        (fn [champ]
          (dom/div {} (get champ :name)))
        (get current-state :champs))
      (ui-app-state current-state))))

;; HTML: <button onClick=... >Label</button>
;; (dom/button {:onClick (fn [] ..)} "Label")


;; IGNORE THE GLUE BELOW THIS LINE

(defn factory [cls]
  (fn [props]
    (dom/create-element cls #js {:props props})))

(def ui-root (factory Root))

(defn refresh []
  (let [dom-node (js/document.getElementById "app")]
    (react.dom/render (ui-root @state) dom-node)))

(defn init []
  (add-watch state :render (fn [_ _ _ _] (refresh)))
  (refresh))

