(ns app.demo
  (:require
    [com.fulcrologic.fulcro.dom :as dom]
    ["react-dom" :as react.dom]
    [clojure.pprint :refer [pprint]]))

(defonce state (atom {:count1 0
                      :champion [{:name "Annie"
                                  :class "Burst Utility Mage"}
                                 {:name "Alistar"
                                  :class "Tank"}
                                 {:name "Anivia"
                                  :class "Control Mage"}
                                 {:name "Ahri"
                                  :class "Utility Assassin"}
                                 {:name "Akshan"
                                  :class "Assassin"}
                                 {:name "Akali"
                                  :class "Assassin"}
                                 {:name "Amumu"
                                  :class "Assassin"}
                                 {:name "Aatrox"
                                  :class "Bruiser"}]}))

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
      (dom/button
        {:onClick (fn [_]
                    (swap! state update :champion #(sort-by (juxt :class :name) %)))}
         (str "Alphabetical Class Sort"))
      ;; 'juxt' creates a function that returns a vector containing the values of :class and :name for each champion
      ;; the function is pasted to 'sort-by' which sorts the champions based on this vector
      (map
        (fn [champion]
          (dom/div {} (get champion :name)))
        (get current-state :champion))
      (ui-app-state current-state))))


;; THIS ALSO WORKS - but uses threading
;;                    (swap! state update :champion #(sort-by (fn [champion] [(-> champion :class) (-> champion :name)]) %)))}
;;        (str "Alphabetical Class Sort"))
;; this extracts the values of ':class' and 'name' using the '->' threading macro and constructs a vector with these values
;; this function is passed to 'sort-by' for sorting




;;sort-by takes a function
;; that function will recieve each item and it's supposed to return a kay by which they will be sorted
;; vectors will sort based on the order of their first elements, and then their second elements
;;if you tell sort by to use a function that returns a vector whose first member is the class, and the second member is the name then it should work


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

