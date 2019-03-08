(define (problem puck-retrieving-1)
   (:domain puck-retriever)
   (:objects
	start - node
	goal - node
	a1 - node
	a2 - node
	a3 - node
	b1 - node
	b1 - node
	b2 - node
	c3 - node
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
	(start-node start)
	(goal-node goal)
	(puck-at a1 p1)
	(puck-at a2 p2)
	(puck-at a3 p3)
	(puck-at b1 p4)
	(puck-at b2 p5)
	(puck-at b3 p6)
	(puck-at c1 p7)
	(puck-at c2 p8)
	(puck-at c3 p9)
   )
   (:goal
	(and 
	 (at p1 goal)
	 (at p2 goal)
	 (at p3 goal)
	 (at p4 goal)
	 (at p5 goal)
	 (at p6 goal)
	 (at p7 goal)
	 (at p8 goal)
	 (at p9 goal))))
