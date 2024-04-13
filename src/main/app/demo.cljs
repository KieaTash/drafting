(ns app.demo
  (:require
    [com.fulcrologic.fulcro.dom :as dom]
    ["react-dom" :as react.dom]
    [clojure.pprint :refer [pprint]]))

(defonce state (atom {:count1 0
                      :champion [{{:name "Aatrox"
                                   :class "Bruiser"}
                                  {:name "Ahri"
                                   :class "Utility Assassin"}
                                  {:name "Akali"
                                   :class "Assassin"}
                                  {:name "Akshan"
                                   :class "Assassin"}
                                  {:name "Alistar"
                                   :class "Tank"}
                                  {:name "Amumu"
                                   :class "Tank"}
                                  {:name "Anivia"
                                   :class "Control Mage"}
                                  {:name "Annie"
                                   :class "Burst Utility Mage"}}]}))

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
    (dom/div {:style {:color "dark green"}}
      "Champion Selected"
      (dom/button
        {:onClick (fn [_]
                    (swap! state update :champion conj {:name (str "Champion" current-count)})
                    (swap! state update :count1 inc))}
        (str "Clicked:" current-count))
      (dom/button
        {:onClick (fn [_]
                    (swap! state update :champion #(sort-by :class %)))}
        (str "Class Sort:"))
      (dom/button
        {:onClick (fn [_]
                    (swap! state update :champion #(sort-by :name %)))}
        (str "Name Sort:"))



      (map
        (fn [champion]
          (dom/div {} (get champion :name)))
        (get current-state :champion))
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

