package tempeteMentale;

import java.io.Serializable;

public class Point implements Serializable{
	float coord1; 
	float coord2;
	
	public Point(float coord1, float coord2) {
		super();
		this.coord1 = coord1;
		this.coord2 = coord2;
	}
	
	public float getCoord1() {
		return coord1;
	}
	public void setCoord1(float coord1) {
		this.coord1 = coord1;
	}
	public float getCoord2() {
		return coord2;
	}
	public void setCoord2(float coord2) {
		this.coord2 = coord2;
	}
}
