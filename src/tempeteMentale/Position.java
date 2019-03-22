package tempeteMentale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class Position {
	static ArrayList<Point> listCordonnees = new ArrayList<Point>(Arrays.asList(new Point("a1", 42, 218),
																			new Point("a2", 97, 218),
																			new Point("a3", 152, 218),
																			new Point("b1", 46, 153),
																			new Point("b2", 97, 153),
																			new Point("b3", 152, 153),
																			new Point("c1", 46, 84),
																			new Point("c2", 97, 84),
																			new Point("c3", 152, 84))												
			
															);
	public static void main(String[] args) {
		// Recuperer la liste des position 
		ArrayList<Point> listePositions = ListePos();
		for (Point i : listePositions) {
		      System.out.println(i.id + " "+i.coord1 + "  "+i.coord2);
		    }
	}
	
	
	static ArrayList<Point> ListePos() {
		int[] tab  = EV3Client.getPalets();
		float coord1;
		float coord2;
		ArrayList<Point> listCordonneesExiste = new ArrayList<Point>();
	
		if(tab.length >0) {
			for(int i = 0 ; i<1;i=i+2) {	
				coord1 = tab[i];
				coord2 = tab[i+1];
				System.out.println("  ");	
				
				for (Point j : listCordonnees) {
					if((j.getCoord1()-20<coord1)&&(coord1<j.getCoord1()+20)) {
						if((j.getCoord2()-20<coord2)&&(coord2<j.getCoord2()+20)){
							listCordonneesExiste.add(new Point(j.getId(),coord1 ,coord2));
						}
					}
				}
			}	
		}else {
			System.out.println("pas de palets");
		}
		return listCordonneesExiste;
		
	}
	

}
