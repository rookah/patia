package tempeteMentale;

import java.util.HashMap;
import java.util.Map.Entry;


public class PuckPosition {

	public PuckPosition() {
	}

	private HashMap<String, Point> pucks;

	static final HashMap<String, Point> listCordonnees = 
			new HashMap<String, Point>()
	{
		private static final long serialVersionUID = 1L;
		{
			put ("a1", new Point(42, 218));
			put ("a2", new Point(97, 218));
			put ("a3", new Point(152, 218));
			put ("b1", new Point(46, 153));
			put ("b2", new Point(97, 153));
			put ("b3", new Point(152, 153));
			put ("c1", new Point(46, 84));
			put ("c2", new Point(97, 84));
			put ("c3", new Point(152, 84));
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
 * Get pucks 
 * @return
 */
	public HashMap<String, Point> getPucks() {
		return this.pucks;
	}
}
