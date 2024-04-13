(ns app.draft-baby-version)

;; Add alternative names that could be typed in instead of the specified name


(def draft-state (atom [{:name "Aatrox"}
                   {:name "Ahri"}
                   {:name "Akali"}
                   {:name "Akshan"}
                   {:name "Alistar"}
                   {:name "Amumu"}
                   {:name "Anivia"}
                   {:name "Annie"}]))

@draft-state

;; (swap! draft-state update-in [:name "Alistar"] assoc :Aliases "Ali")

;; WRONG - cannot use 'update-in' in this context. It is used to update nested values within maps or vectors
;; but it expects keys or indexes. This is a vector of maps. Have to specify the index of map to update
;; instead of providing a key-value pair directly.

(swap! draft-state update-in [4] assoc :Aliases "Ali")
(swap! draft-state #(update % 4 assoc :Alias "Ali"))

;; tldr = cannot modify map keys, they only have one value, have to change it to a vector or make an alias


  ;;________________________________________________________________________________


  ;; Show that Ahri has been selected from the champion-list
  (def draft-state (atom {:selected-champion nil
                          :champion-list     [{{:name "Aatrox"}
                                               {:name "Ahri"}
                                               {:name "Akali"}
                                               {:name "Akshan"}
                                               {:name "Alistar"}
                                               {:name "Amumu"}
                                               {:name "Anivia"}
                                               {:name "Annie"}}]}))

  @draft-state

  (defn red-pick-champion [c]
    (swap! draft-state (fn [old] (assoc old :selected-champion c))))

  (red-pick-champion "Ahri")


  ;;________________________________________________________________________________
  ;; Show that after selection, Ahri is not available for selection for future picks
  ;; Show the decreasing number in the champion-list with more picks

  (def draft-state (atom {:selected-champion nil
                          :champion-list     [{:name "Aatrox"}
                                              {:name "Ahri"}
                                              {:name "Akali"}
                                              {:name "Akshan"}
                                              {:name "Alistar"}
                                              {:name "Amumu"}
                                              {:name "Anivia"}
                                              {:name "Annie"}]}))

  @draft-state

  (defn red-pick-champion [c]
    (swap! draft-state (fn [old] {:selected-champion c
                                  :champion-list     (remove #(= c (:name %)) (:champion-list old))})))

  ;; 'remove' takes 2 arguments: a predicate function and a sequence to filter
  ;; the predicate function '#(= c (:name %))' compares the name of each champion in the list with
  ;; the newly selected champion 'c'

  (red-pick-champion "Ahri")
  (red-pick-champion "Anivia")
  (red-pick-champion "Amumu")

  ;________________________________________________________________________________
  ;; Create a function that can tell you at any point what champions have been selected
  ;; or are unavailable from the list

  (def draft-state (atom {:selected-champion nil
                          :champion-list     [{:name "Aatrox"}
                                              {:name "Ahri"}
                                              {:name "Akali"}
                                              {:name "Akshan"}
                                              {:name "Alistar"}
                                              {:name "Amumu"}
                                              {:name "Anivia"}
                                              {:name "Annie"}]}))

  @draft-state

  (defn champions-selected [])



  (defn red-pick-champion [c]
    (swap! draft-state (fn [old] {:selected-champion c
                                  :champion-list     (remove #(= c (:name %)) (:champion-list old))})))

  (red-pick-champion "Ahri")
  (champions-selected)

  (red-pick-champion "Anivia")
  (champions-selected)

  (red-pick-champion "Amumu")
  (champions-selected)


  ;;________________________________________________________________________________
  ;; Show that TWO teams are selecting and then removing champions from the list.
  ;; Show which team selected which champions.

