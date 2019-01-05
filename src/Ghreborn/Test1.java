package Ghreborn;


public class Test1 {

	public static int[] getNewObjectCoords(int rotation, int[] startCoords) {
		int[][] coords = new int[4][];

		coords[0] = new int[] { startCoords[0], startCoords[1] };

		for (int i = 1; i < 4; i++) {
			coords[i] = new int[] { coords[i - 1][1], 7 - coords[i - 1][0] };
		}

		return coords[rotation];
	}

	public static int[] getOriginalObjectCoords(int rotation, int[] startCoords) {
		int[][] coords = new int[4][];

		if (startCoords[0] == 3 && startCoords[1] == 3)
			return new int[] {3, 3};
		
		coords[0] = startCoords;

		for (int i = 1; i < 4; i++) {
			coords[i] = new int[] { coords[i - 1][1], 7 - coords[i - 1][0] };
		}

		switch (rotation) {
		case 0:
			return coords[0];
		case 1:
			return coords[3];
		case 2:
			return coords[2];
		case 3:
			return coords[1];
		}
		
		return coords[0];
	}

}
