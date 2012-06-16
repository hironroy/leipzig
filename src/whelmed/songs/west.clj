(ns whelmed.songs.west
  (:use
    [whelmed.scale]
    [whelmed.canon]
    [whelmed.melody]
    [whelmed.instrument]
    [overtone.live :only [midi->hz now stop]]))

(defn => [value & fs] (reduce #(%2 %1) value fs))
(defn lower [f] (comp #(- % 7) f))
(def progression (map #(map % seventh) [i (lower v) (lower vi) (lower iii)]))
(defn with-bass [chords] (map #(conj % (- (first %) 7)) chords))

(def backing
    (map
      #(map (partial vector %1) %2 (repeat 1/2))
      [0 4 8 12]
      (with-bass progression)))

(defn after [wait] (shift [wait 0 0])) 

(defn follow [first gap second]
    (let [[timing _ duration] (last first)
                  shifted ((after (+ duration gap timing)) second)]
          (concat first shifted))) 

(def ill-run-away [[-1/2 3 1/2] [0 4 1/4] [1/4 3 1/4] [1/2 4 1/2]])
(def ill-get-away (assoc ill-run-away 2 [1/4 6 1/4]))
(def my-heart-will-go-west-with-the-sun
  [[-1/2 3 1/2]
   [0 4 3/4] [3/4 3 3/4] [3/2 2 1/4]
   [8/4 4 3/4] [11/4 3 1/4] [14/4 2 1/4]
   [15/4 -1 4]
   ])

(defn west#
  [tempo scale melody backing]
  (let [start (+ (now) 1000)
        in-time (comp (skew timing tempo) (skew duration tempo))
        in-key (skew pitch scale)
        midify (fn [instrument#] (comp instrument# midi->hz))]
  (=> backing in-time (after start) in-key (partial play-on# (midify shudder#)))
  (=> melody in-time (after start) in-key (partial play-on# (midify sawish#)))))

(def melody (concat
             (follow
               (follow ill-run-away 3 ill-get-away)
               3 my-heart-will-go-west-with-the-sun)))

;(west# (bpm 80) (comp E aeolian) melody (apply concat backing))
