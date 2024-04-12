(ns app.draft-logic)

(def a 1)
(def b 2)

(def state (atom 0))

(deref state)
@state

;;  state [ ]----> 0

(reset! state 4)
;;  state [ ]----> 4

;;  state [ ]----> 4 -> FN -> NEW VALUE
(swap! state (fn [old-value] 8))
(reset! state 8)

(def draft-state (atom {:selected-champion nil
                        :champion-list     []}))

@draft-state

(defn pick-champion [c]
  (swap! draft-state (fn [old] (assoc old :selected-champion c))))

(pick-champion "Foo")

(comment

  (swap! draft-state (fn [old]
                       (update-in old ["Aatrox" :role] (fn [old-role] (conj old-role "mid")))
                       ))
  (add-watch draft-state :make-up (fn [k a old new]
                                    (println "The atom changed "))))



(def team-list
  {"Red"
   "Blue"}
  )


(def champion-list
  {"Aatrox"  {:name      "Aatrox"
              :role      ["top"]
              :class     "juggernaut"
              :direction "forward"
              :range     "melee"}
   "Ahri"    {:name      "Ahri"
              :role      ["mid"]
              :class     ["assassin" "utility"]
              :direction "pick"
              :range     "ranged"}
   "Akali"   {:name      "Akali"
              :role      ["mid" "top"]
              :class     "assassin"
              :direction "pick"
              :range     "melee"}
   "Akshan"  {:name      "Akshan"
              :role      ["mid" "top"]
              :class     "assassin"
              :direction "pick"
              :range     "ranged"}
   "Alistar" {:name      "Alistar"
              :role      ["support"]
              :class     "tank"
              :direction ["forward" "backward" "hold"]
              :range     "melee"}
   "Amumu"   {:name      "Amumu"
              :role      ["jungle" "support" "top"]
              :class     "tank"
              :direction ["forward" "backward" "hold"]
              :range     "melee"}
   "Anivia"  {:name      "Anivia"
              :role      ["mid" "support"]
              :class     "control mage"
              :direction "hold"
              :range     "ranged"}
   "Annie"   {:name      "Annie"
              :role      ["mid" "support" "top"]
              :class     ["assassin" "utility"]
              :direction ["forward" "hold"]
              :range     "ranged"}}
  )

;; Add another  value to  one of the elements in  the champion list

;; attempt 1
;; (def champion-list2 (assoc-in champion-list "Alistar" [:role] "top"))
;; attempt 2
(def champion-list2 (update-in champion-list ["Alistar" :role] conj "top"))

;;Replace a value of one of the elements in the champion list
(def champion-list2 (assoc-in champion-list ["Alistar" :role] "top"))
(def champion-list2)
(keys champion-list2)

(def L [{:name "A" :n 1} {:name "B" :n 2}])
(:name {:name "a"})
(zipmap (map :name L) L)

(def L [{:name "Aatrox :"}])



;; always fixed
;; champion
;; number of picks and bans: 5 bans and 5 picks for each team
;; once a champion is picked or banned it is unavailable from the champion pool list
;; must have one of each role: top, jungle, mid, bot, support on each team


;; variables = champions picked and banned


;; more difficult:
;; blue side always picks and bans first
;; blue and red take turns in ban phase 1:1



