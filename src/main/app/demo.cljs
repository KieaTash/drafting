(ns app.demo
  (:require
    [com.fulcrologic.fulcro.dom :as dom]
    ["react-dom" :as react.dom]
    [clojure.pprint :refer [pprint]]))


(def champions [{:name  "Annie"
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
                 :class "Bruiser"}])
(def champions-by-name (zipmap (map :name champions) champions))

(defonce state (atom {:selected  nil
                      :champions champions-by-name}))

(defn banned-champions [team]
  (let [c-by-name (get @state champions)
        all       (vals c-by-name)
        banned    (filterv
                    (fn [c]
                      (and
                        (= team (:team c))
                        (contains? c :ban)))
                    all)]
    (sort-by :ban banned)))




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

(defn ui-ban-list [bans]
  (dom/div
    :.flex {}
    (mapv
      (fn [champion]
        (dom/div :.w-10.h-10.bg-gray-200.m-2.mt-10.rounded-lg {:style {:overflow "hidden"}}
          (dom/img {:src   "https://via.placeholder.com/48"
                    :style {:width "100%" :height "100%"}})))
      bans)))

(defn ui-body-layout [col1 col2 col3]
  (dom/div
    :.flex {:style {:width "100%"
                    :justify-content "space-between"
                    :overflow "wrap"}}
    (dom/div :.flex.items-top.ml-8 col1)
    (dom/div :.flex.mt-28.flex-grow-1 col2)
    (dom/div :.flex.items-top.mr-8.ml-1 col3)))

(defn ui-pick-list [picks]
  (dom/div
    :.flex-col {}
    (mapv
      (fn [champion]
        (dom/div :.w-20.h-20.bg-gray-200.m-2.mt-10.rounded-lg {:style {:overflow "hidden"}}
          (dom/img {:src   "https://via.placeholder.com/100"
                    :width 100
                    })))
      picks)))

(def scroll-styles
  (dom/style {} "
    ::-webkit-scrollbar {
      width: 8px;
    }
    ::-webkit-scrollbar-track {
      background-color: #f1f1f1;
    }
    ::-webkit-scrollbar-thumb {
      background-color: #888;
    }
    ::-webkit-scrollbar-thumb:hover {
      background-color: #555;
    }"))

(defn ui-champion-list [list]
  (dom/div
    {:style {:maxHeight "calc(58vh)",                       ;; Adjust the max-height as needed
             :overflow  "auto"}}
    (dom/div scroll-styles)
    (dom/div
      :.flex.flex-row.flex-wrap.items-left.justify-center.overflow-x-auto
      (mapv
        (fn [champion]
          (let [nm      (get champion :name)
                img-url (str "https://ddragon.leagueoflegends.com/cdn/12.4.1/img/champion/" nm ".png")]
            (dom/div :.w-16.h-16.m-4
              {:onClick (fn []

                          )}
              (dom/img {:src   img-url                      ; "https://via.placeholder.com/64"
                        :style {:width "100%" :height "100%"
                                :border-radius "30%"}}))))
        list))))

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

(defn mark-champion [old name]
  (assoc-in old [:champions name :used?] true))

(defn clear-champion [old name]
  (assoc-in old [:champions name :used?] false))



(defn Root []
  (let [current-state @state
        all           (vals (get @state :champions))
        champions     (sort-by :name all)
        selection     (get @state :selected)]
    (dom/div
      #_(mapv
          (fn [c]
            (let [nm    (:name c)
                  used? (:used? c)]

              (dom/div {:style   {:color (if :used?
                                           "grey"
                                           (if
                                             (and selection (= nm selection))
                                             "red" "yellow"))}
                        :onClick (fn [] (when-not used?
                                          (swap! state assoc :selected (:name c))))}
                (get c :name))))
          champions)

      #_(map-indexed
          (fn [idx c]
            (dom/div {:onClick (fn []
                                 (let [current (get-in @state [:teamB :bans idx])
                                       who     (get @state :selected)]
                                   (if current
                                     (do
                                       (swap! state update-in [:teamB :bans] dissoc idx)
                                       (swap! state clear-champion current))
                                     (when who
                                       (swap! state assoc-in [:teamB :bans idx] who)))))}
              (str "B Banned:" (get-in @state [:teamB :bans idx]))))
          (range 5))




      (dom/div {}
        (ui-header-layout
          (dom/div
            (dom/div "Team A")

            (ui-ban-list ["A" "B" "C" "D" "E"]))
          (dom/div "Time Remaining")
          (dom/div
            (dom/div "Team B")
            (ui-ban-list ["A" "B" "C" "D" "E"])))
        (ui-body-layout
          (dom/div {:style {:color "blue"}}
            (dom/div "Team A Picks")
            (ui-pick-list ["1" "2" "3" "4" "5"]))
          (ui-champion-list
            champions)
          (dom/div {:style {:color "red"}}
            (dom/div "Team B Picks")
            (ui-pick-list ["1" "2" "3" "4" "5"])))


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

