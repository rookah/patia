(define (domain puck-retriever)
 (:requirements :strips :typing)
 (:types node puck - object)
 (:predicates
  (at ?n - node)
  (goal-node ?n - node)
  (puck-at ?p - puck ?n - node)
  (link ?n1 - node ?n2 - node)
  (holding ?p - puck)
  (gripper-empty)
 )

 (:action move
  :parameters (?from - node ?to - node)
  :precondition (and
	  (at ?from)
	  (link ?from ?to))
  :effect (and
	  (not (at ?from))
	  (at ?to))
 )

 (:action pick-up
  :parameters (?p - puck ?n - node)
  :precondition (and
	  (at ?n)
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
	  (at ?n)
	  (goal-node ?n)
	  (holding ?p))
  :effect (and
	  (gripper-empty)
	  (not (holding ?p))
	  (puck-at ?p ?n))
 ))
