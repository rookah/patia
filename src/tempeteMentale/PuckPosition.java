package tempeteMentale;

import java.util.HashMap;
import java.util.Map.Entry;


public class PuckPosition {

	public PuckPosition() {
	}

	/**
	 * Pucks on the map
	 */
	private HashMap<String, Point> pucks;

	/**
	 * Static coordinates for associting pucks/nodes with the right label
	 */
	static final HashMap<String, Point> listCordonnees = 
			new HashMap<String, Point>()
	{
		private static final long serialVersionUID = 1L;
		{
			put ("a1", new Point(50, 217));
			put ("a2", new Point(105, 217));
			put ("a3", new Point(161, 217));
			put ("b1", new Point(50, 151));
			put ("b2", new Point(103, 151));
			put ("b3", new Point(159, 149));
			put ("c1", new Point(50, 87));
			put ("c2", new Point(103, 89));
			put ("c3", new Point(159, 89));
			put ("start", new Point(103,288));
			put ("goal", new Point(103, 26));
		}
	};

/**
 * Position update
 */
	public void updatePos() {
		int[] tab  = EV3Client.getPalets();
		float coord1;
		float coord2;
		HashMap<String, Point> listCordonneesExiste = new HashMap<String, Point>();

		if(tab.length > 0) {
			for(int i = 0; i < tab.length; i=i+2) {	
				coord1 = tab[i];
				coord2 = tab[i+1];
				for (Entry<String, Point>  j : listCordonnees.entrySet()) {
					if((j.getValue().getCoord1()-20<coord1)&&(coord1<j.getValue().getCoord1()+20)) {
						if((j.getValue().getCoord2()-20<coord2)&&(coord2<j.getValue().getCoord2()+20)){
							listCordonneesExiste.put(j.getKey(), new Point(coord1 ,coord2));
						}
					}
				}
			}	
		}else {
			System.out.println("warning: no pucks on the field");
		}
		pucks = listCordonneesExiste;
	}
	
	/**
	 * Get the list of pucks present on the board
	 * @return HashMap of pucks on the board
	 */
	public HashMap<String, Point> getPucks() {
		return this.pucks;
	}
	
	/**
	 * Get the position on the board of a given node
	 * @param node_label node to get the position of
	 * @return Point on the board
	 */
	public Point getNode(String node_label) {
		return listCordonnees.get(node_label);
	}
}
