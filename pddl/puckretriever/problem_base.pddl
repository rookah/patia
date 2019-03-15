(define (problem puck-retrieving-1)
 (:domain puck-retriever)
 (:objects
  start - node
  goal - node
  a1 - node
  a2 - node
  a3 - node
  b1 - node
  b2 - node
  b3 - node
  c1 - node
  c2 - node
  c3 - node
 )
 (:init
  (gripper-empty)
  (goal-node goal)
  (at start)
  (link start a1)
  (link start a2)
  (link start a3)
  (link a1 b1)
  (link a2 b2)
  (link a3 b3)
  (link b1 c1)
  (link b2 c2)
  (link b3 c3)
  (link c1 goal)
  (link c2 goal)
  (link c3 goal)
 )
 (:goal
  (at goal)
 ))
