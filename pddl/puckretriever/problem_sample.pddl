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
	p1 - puck
	p2 - puck
	p3 - puck
	p4 - puck
	p5 - puck
	p6 - puck
	p7 - puck
	p8 - puck
	p9 - puck
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
	(puck-at p1 a1)
	(puck-at p2 a2)
	(puck-at p3 a3)
	(puck-at p4 b1)
	(puck-at p5 b2)
	(puck-at p6 b3)
	(puck-at p7 c1)
	(puck-at p8 c2)
	(puck-at p9 c3)
   )
   (:goal
	(and 
	 (puck-at p1 goal)
	 (puck-at p2 goal)
	 (puck-at p3 goal)
	 (puck-at p4 goal)
	 (puck-at p5 goal)
	 (puck-at p6 goal)
	 (puck-at p7 goal)
	 (puck-at p8 goal)
	 (puck-at p9 goal))))
