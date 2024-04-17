(ns app.demo
  (:require
    [com.fulcrologic.fulcro.dom :as dom]
    ["react-dom" :as react.dom]
    [clojure.pprint :refer [pprint]]))

(defonce state (atom {:count1   0
                      :champion [{:name  "Annie"
                                  :class "Burst Utility Mage"}
                                 {:name  "Alistar"
                                  :class "Tank"}
                                 {:name  "Anivia"
                                  :class "Control Mage"}
                                 {:name  "Ahri"
                                  :class "Utility Assassin"}
                                 {:name  "Akshan"
                                  :class "Assassin"}
                                 {:name  "Akali"
                                  :class "Assassin"}
                                 {:name  "Amumu"
                                  :class "Assassin"}
                                 {:name  "Aatrox"
                                  :class "Bruiser"}]}))

(defn ui-button [props label]
  (dom/button
    :.bg-gray-500.hover:bg-gray-600.text-white.font-bold.py-2.px-4.rounded
    props
    label))


(defn ui-header-layout [col1 col2 col3]
  (dom/div
    :.flex
    (dom/div :.w-48.flex.items-center.justify-right.text-center.p-8.mr-20 col1)

    (dom/div :.flex-1.flex.items-center.flex-wrap.whitespace-nowrap.justify-center.text-center.p-10 col2)
    (dom/div :.w-48.flex.items-center.justify-center.text-center.p-8.mr-20 col3)))



(defn ui-pick-list [picks]
  (dom/div
    :.flex-col.w-100
    (mapv
      (fn [champion]
        (dom/div :.w-20.h-20.bg-gray-300.mb-4.ml-10 {}
          (dom/img {:src "https://via.placeholder.com/100"})))
      picks)))




(defn ui-ban-list [bans]
  (dom/div
    :.flex {}
    (mapv
      (fn [champion]
        (dom/div :.w-10.h-10.bg-gray-200.m-2.mt-10 {}
          (dom/img {:src "https://via.placeholder.com/48"})))
      bans)))



(defn ui-app-state [current-state]
  ;; HTM
  (dom/div :.fish.potato.cat {:style {:color "green"}}
    ;; can write this above as this also:
    ;; (dom/div {:className "fish potato cat"}
    ;; using the one stated = can drop all the optional empty maps
    (dom/hr)
    ;; HTML: <h5>Your app state is currently:</h5>
    (dom/h5 "Your app state is currently:")
    (dom/pre
      (with-out-str
        (pprint current-state)))
    (dom/hr)))





(defn Root []
  (let [current-state @state
        current-count (get current-state :count1)]
    (dom/div {}
      (ui-header-layout
        (dom/div
          (dom/div "Team A")
          (ui-ban-list ["A" "B" "C" "D" "E"]))
        (dom/div "Time Remaining")
        (dom/div
          (dom/div "Team B")
          (ui-ban-list ["A" "B" "C" "D" "E"])))
      (dom/div
        (dom/div {:style {:color "blue"}}
          (dom/div "Picks")
          (ui-pick-list ["1" "2" "3" "4" "5"])))
      (dom/div
        (dom/div {:style {:color "red" :flex-grow 20 :m1-auto true}}
          (dom/div "Picks")
          (ui-pick-list ["1" "2" "3" "4" "5"])))


      (dom/div {:style {:color "yellow"}}
        "Champion Selected"
        (ui-button
          {:onClick (fn [_]
                      (swap! state update :champion conj {:name (str "Champion" current-count)})
                      (swap! state update :count1 inc)
                      (swap! state update :button-color (fn [color] (if (= color "blue") "red" "blue"))))
           :style   {:color (get @state :button-color)}}
          (str "Clicked:" current-count))
        (ui-button
          {:onClick (fn [_]
                      (swap! state update :champion #(sort-by :class %)))}
          (str "Class Sort:"))
        (ui-button
          {:onClick (fn [_]
                      (swap! state update :champion #(sort-by :name %)))}
          (str "Name Sort:"))
        (ui-button
          {:onClick (fn [_]
                      (swap! state update :champion #(sort-by (juxt :class :name) %)))}
          (str "Alphabetical Class Sort"))
        ;; 'juxt' creates a function that returns a vector containing the values of :class and :name for each champion
        ;; the function is pasted to 'sort-by' which sorts the champions based on this vector
        (map
          (fn [champion]
            (dom/div {:style   {:color       "yellow"
                                :font-family "Verdana, sans-serif"}
                      :onClick {:color       "black"
                                :font-family "Verdana, sans-serif"}}
              ;; not sure if this onclick styling actually does anything here
              (dom/span {:style {:color "purple"}} (str "Name: " (get champion :name)))
              " "                                           ;can include this to give a space between the two stylings
              (dom/span {:style {:color "pink"}} (str "Class: " (get champion :class)))))
          ; if the two dom/spans above are included then no need to specify the :color "green" earlier in dom/div
          (get current-state :champion))
        (ui-app-state current-state)))))


;; THIS ALSO WORKS - but uses threading
;;                    (swap! state update :champion #(sort-by (fn [champion] [(-> champion :class) (-> champion :name)]) %)))}
;;        (str "Alphabetical Class Sort"))
;; this extracts the values of ':class' and 'name' using the '->' threading macro and constructs a vector with these values
;; this function is passed to 'sort-by' for sorting




;;sort-by takes a function
;; that function will receive each item, and it's supposed to return a kay by which they will be sorted
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

