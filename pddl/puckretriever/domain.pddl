(define (domain puck-retriever)
 (:requirements :strips :typing)
 (:types node puck)
 (:predicates
  (at ?n - node)
  (start-node ?n - node)
  (goal-node ?n - node)
  (puck-at ?p - puck ?n - node)
  (link ?n1 - node ?n2 - node)
  (holding ?p)
  (gripper-empty)
 )

 (:action move
  :parameters (?from - node ?to - node)
  :precondition (and
	  (not (start-node ?to))
	  (at ?from)
	  (or (link ?from ?to) (link ?to ?from)))
  :effect (and
	  (not (at ?from))
	  (at ?to))
 )

 (:action pick-up
  :parameters (?p - puck ?n - node)
  :precondition (and
	  (not (start-node ?n))
	  (not (goal-node ?n))
	  (puck-at ?p ?n)
	  (gripper-empty))
  :effect (and
	  (not (puck-at ?p ?n))
	  (not (gripper-empty))
	  (holding ?p))
 )

 (:action drop-down
  :parameters (?p - puck ?n - node)
  :precondition (and
	  (goal-node ?n)
	  (not (gripper-empty))
	  (holding ?p))
  :effect (and
	  (gripper-empty)
	  (not (holding ?puck))
	  (puck-at ?puck ?p))
 ))
