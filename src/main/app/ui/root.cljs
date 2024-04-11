(ns app.ui.root
  (:require
    [com.fulcrologic.fulcro.dom :as dom :refer [div ul li p h3 button b]]
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.fulcro.mutations :as m :refer [defmutation]]
    [taoensso.timbre :as log]))

(defsc Player [this {:player/keys [id hand show-cards?]}]
  {:query         [:player/id
                   :player/hand
                   :player/show-cards?]
   :ident         :player/id
   :initial-state (fn [{:keys [role]}]
                    (if (= role :computer)
                      {:player/id          :computer
                       :player/show-cards? false}
                      {:player/id          :human
                       :player/show-cards? true}))}
  (div
    (dom/h2
      (if (= id :computer) "Dealer" "You"))
    "TODO"))

(def ui-player (comp/factory Player {:keyfn :player/id}))

(defsc Root [this {:keys [game-state deck computer human]}]
  {:query         [:game-state
                   :deck
                   {:computer (comp/get-query Player)}
                   {:human (comp/get-query Player)}]

   :initial-state {:game-state :not-started
                   :computer   {:role :computer}
                   :human      {:role :human}}}
  (case game-state
    :not-started (div
                   "Ready? Press play to begin"
                   (button {}
                     "Play!"))
    (div
      (dom/h2 "Blackjack!")
      (ui-player computer)
      (ui-player human))))
