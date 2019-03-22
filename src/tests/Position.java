package tests;

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
																			new Point("b4", 46, 84),
																			new Point("b5", 97, 84),
																			new Point("b6", 152, 84))												
			
															);
	static Hashtable<String, int[]> numbers = new Hashtable<String, int[]>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Recuperer la liste des position 
		ArrayList<Point> listePositions = ListePos();
		for (Point i : listCordonnees) {
		      System.out.println(i.coord1 + "  "+i.coord2);
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
					if((j.getCoord1()-10<coord1)&&(coord1<j.getCoord1()+10)) {
						if((j.getCoord2()-10<coord2)&&(coord2<j.getCoord2()+10)){
							listCordonneesExiste.add(new Point(j.getId(), j.coord1 ,j.coord2));
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
