package Ghreborn.util;

public class Map {

	public static int getNewXCoord(int rotation, int j, int k, int l, int i1) {
		rotation &= 3;
		if (rotation == 0)
			return k;
		if (rotation == 1)
			return l;
		if (rotation == 2)
			return 7 - k - (i1 - 1);
		else
			return 7 - l - (j - 1);
	}
	
	public static int getNewYCoord(int j, int k, int rotation, int i1, int j1) {
		rotation &= 3;
		if (rotation == 0)
			return j;
		if (rotation == 1)
			return 7 - j1 - (i1 - 1);
		if (rotation == 2)
			return 7 - j - (k - 1);
		else
			return j1;
	}
	
}