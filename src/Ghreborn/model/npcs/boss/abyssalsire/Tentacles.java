package Ghreborn.model.npcs.boss.abyssalsire;

public class Tentacles {
	
	private enum Tentacle_Data {
		Tentacle_One(5912, 100, 2984, 4844, "Attacking"),
		Tentacle_Two(5912, 100, 2967, 4844, "Attacking"),
		Tentacle_Three(5912, 100, 2970, 4835, "Attacking"),
		Tentacle_Four(5912, 100, 2982, 4835, "Attacking");
		
		private int npcType, health, CoordinateX, CoordinateY;
		String currentState;
		
		private Tentacle_Data(int npcType, int health, int CoordinateX, int CoordinateY, String currentState) {
			this.npcType = npcType;
			this.health = health;	
			this.CoordinateX = CoordinateX;
			this.CoordinateY = CoordinateY;
			this.currentState = "Attacking";
		}
		
		/**
		 * Gets the NPC id
		 * @return
		 */
		
		private int getNpc() {
			return npcType;
		}
		
		/**
		 * Assigns the npc's health amount upon spawning.
		 * @return
		 */
		
		private int getHealth() {
			return health;
		}
		
		/**
		 * Gets the Coordinate X of the location the NPC should be at.
		 * @return
		 */
		
		private int getCoordinateX() {
			return CoordinateX;
		}
		
		/**
		 * Gets the Coordinate Y of the location the NPC should be at.
		 * @return
		 */
		
		private int getCoordinateY() {
			return CoordinateY;
		}
		
		/**
		 * Checks the current state of the tentacle (Sleeping, Stunned ect)
		 * @return
		 */
		
		private String getCurrentState() {
			return currentState;
		}
		
		/**
		 * Sets the state of the Abyssal Tentacle
		 * @param i
		 * @return
		 */
		
		public String SET_STATE(int i) {
			switch(i) {
			case 1:
				return "Attacking";
			case 2:
				return "Stunned";
			case 3:
				return "Sleeping";
			}
			return currentState;
		}
	}
	
	public void initialize() {
		
	}
}

