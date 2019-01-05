package Ghreborn.model.players.skills.mining;

public class Motherlode {

	@SuppressWarnings("unused")
	private enum Data {
		WALL(new int[][][] { { { 28869, 1, 1 } }

		});

		private int objectType, level;
		private int[][][] oreType;

		Data(int[][][] oreType) {
			this.oreType = oreType;
		}

		public int getObjectId() {
			return objectType;
		}
	}

	public void mine() {
		for (Data i : Data.values()) {

		}
	}
}