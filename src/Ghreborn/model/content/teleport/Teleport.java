package Ghreborn.model.content.teleport;

/**
 * @author Chris
 * @date Aug 24, 2015 10:04:01 PM
 *
 */
public class Teleport {
	
	/**
	 * The {@Link Position} the teleport will end
	 */
	private final Position location;

	/**
	 * The type of teleport this is
	 */
	private final TeleportType type;

	/**
	 * Constructs a new teleport
	 * 
	 * @param location
	 *            The end {@link Position} of the teleport
	 * @param type
	 *            The {@link TeleportType} this is
	 */
	public Teleport(Position location, TeleportType type) {
		this.location = location;
		this.type = type;
	}

	/**
	 * Gets the {@link Position} the teleport will end
	 * 
	 * @return The {@link Position} this teleport will end
	 */
	public Position getLocation() {
		return location;
	}

	/**
	 * Gets the {@link TeleportType} this teleport is
	 * 
	 * @return The {@link TeleportType} this teleport is
	 */
	public TeleportType getType() {
		return type;
	}

	public enum TeleportType {

		/**
		 * A normal spell teleport type
		 */
		NORMAL(3, 2, 714, 715, (65535 + 111), -1),

		/**
		 * The ancient spell teleport type
		 */
		ANCIENT(4, 0, 1979, -1, 392, -1),

		/**
		 * The lunar spell teleport type
		 */
		LUNAR(6, 0, -1, -1, -1, 65535),

		/**
		 * The teleport tablet teleport type
		 */
		TABLET(1, 0, 4731, 65535, 678, 65535),
		
		/**
		 * The lever teleport type.
		 */
		LEVER(4, 0, 714, 715, 65535, -1),
		
		/**
		 * Represents the obelisk <code>Teleport</code> type.
		 */
		OBELISK(4, 0, 1816, 715, (65535 + 111), -1);
		//OBELISK(4, 0, 714, 715, (65535 + 111), -1);
		//OBELISK(11, 0, 1816, 715, (65535 + 111), -1);

		/**
		 * The delay before the teleport ends
		 */
		private final int startDelay, endDelay;

		/**
		 * The start and end animation of the teleport
		 */
		private final int startAnim, endAnim;

		/**
		 * The start and end graphic of the teleport
		 */
		private final int startGfx, endGfx;

		private TeleportType(int startDelay, int endDelay, int startAnim, int endAnim, int startGfx, int endGfx) {
			this.startDelay = startDelay;
			this.endDelay = endDelay;
			this.startAnim = startAnim;
			this.endAnim = endAnim;
			this.startGfx = startGfx;
			this.endGfx = endGfx;
		}

		/**
		 * The delay before the teleport ends
		 * 
		 * @return The delay before the teleport ends
		 */
		public int getStartDelay() {
			return startDelay;
		}

		/**
		 * The delay before the player can walk after the teleport
		 * 
		 * @return The delay before the player can walk after the teleport
		 */
		public int getEndDelay() {
			return endDelay;
		}

		/**
		 * Gets the start animation for the teleport
		 * 
		 * @return The animation to display at the start of the teleport
		 */
		public int getStartAnimation() {
			return startAnim;
		}

		/**
		 * Gets the end animation for the teleport
		 * 
		 * @return The animation to display at the end of the teleport
		 */
		public int getEndAnimation() {
			return endAnim;
		}

		/**
		 * Gets the start graphic for the teleport
		 * 
		 * @return The graphic to display at the beginning of the teleport
		 */
		public int getStartGraphic() {
			return startGfx;
		}

		/**
		 * Gets the end graphic for the teleport
		 * 
		 * @return The graphic to display at the end of the teleport
		 */
		public int getEndGraphic() {
			return endGfx;
		}

	}

}

