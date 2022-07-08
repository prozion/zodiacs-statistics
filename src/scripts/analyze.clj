(ns scripts.analyze
  (:require
    [clojure.string :as s]
    [org.clojars.prozion.odysseus.utils :refer :all]
    [org.clojars.prozion.odysseus.debug :refer :all]
    [org.clojars.prozion.odysseus.time :as time]
    [org.clojars.prozion.odysseus.stat :as stat]
    [cheshire.core :as cheshire]
    ))


(defn load-compatibility-table []
  (->> "data/distribution.json"
      slurp
      cheshire/parse-string
      (map-hash (fn [[k1 v1]]
                  {(keyword k1)
                    (map-hash
                      (fn [[k2 v2]]
                        {(keyword k2)
                         v2})
                      v1)}))
      ))

(def TABLE (load-compatibility-table))

(defn get-birthdays []
  (->> "data/couples.json"
       slurp
       cheshire/parse-string
       (reduce
         (fn [acc {he "he" she "she"}]
            (merge-with conj acc {:men (he "birthdate") :women (she "birthdate")}))
          {:men [] :women []})
       ))

; (defn get-count-by-zodiacs [data]
;   (reduce
;     (fn [acc [k v]]
;       (conj acc
;             {k
;              (->> v vals (apply +))}))
;     {}
;     data))
;
; (defn validate []
;   (let [m1 (get-count-by-zodiacs TABLE)
;         m2 (->> TABLE vals (apply merge-with +))
;         total1 (->> m1 vals (apply +))
;         total2 (->> m2 vals (apply +))
;         ]
;     (--- m1 m2 total1 total2)
;     false))

(defn get-zodiacs [birthdays]
  (let [all-birthdays (concat (:men birthdays) (:women birthdays))
        zodiacs (reduce
                  (fn [acc birthday]
                    (let [zodiac-sign (time/get-zodiac-sign birthday)]
                      (update acc zodiac-sign #(if % (inc %) 1))))
                  {}
                  all-birthdays)]
    zodiacs))

(defn sorted-distribution []
  (let [
        distribution (get-zodiacs (get-birthdays))]
    (->>
      distribution
      (into [])
      (sort (fn [a b] (compare (second a) (second b))))
      )))

(defn get-probability-of-randomness []
  ; TODO: как определить вероятность заданного распределения по знакам Зодиака, при условии, что вероятность попадания в тот или иной знак Зодиака одинакова?
  (let [values (vals (sorted-distribution))]
    (stat/confidence-interval values)))
