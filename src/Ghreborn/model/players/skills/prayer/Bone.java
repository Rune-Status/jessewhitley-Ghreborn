package Ghreborn.model.players.skills.prayer;

/**
 * A bone is an item that is used for training the prayer skill. A bone can 
 * be buried by clicking the item once, or the bone can be used on an alter
 * to gain additional experience. 
 * 
 * <p>
 * Each bone has an item id associated as well as a base amount of experience
 * gained when operating that bone.
 * </p>
 * 
 * @author Jason MacKeigan
 * @date Mar 10, 2015, 2015, 3:24:22 AM
 */
public enum Bone {
	REGULAR(526, 4.5),
	BIG(532, 15),
	BABY_DRAG(534, 30),
	DRAG(536, 72),
	LDRAG(11943, 92),
	DAG(6729, 125),
	BAT(530, 4.5),
	WOLF(2859, 4.5),
	MONKEY(3179, 5), 
	JOGRE(3125, 15),
	WYVERN(6812, 72),
	RAURG(4832, 96),
	OURG(4834,140),
	SUPERIOR(22124, 150),
	WYRM(22780, 30);
	
	/**
	 * The item identification value for the bone
	 */
	private final int itemId;
	
	/**
	 * The experience gained from burying a bone
	 */
	private final double experience;
	
	/**
	 * Creates a new {@code Bone} object that will be used in training prayer
	 * @param itemId		the item id of the bone
	 * @param experience	the experience gained
	 */
	private Bone(int itemId, double experience) {
		this.itemId = itemId;
		this.experience = experience;
	}
	
	/**
	 * The item identification value that represents the bone
	 * @return	the item
	 */
	public int getItemId() {
		return itemId;
	}
	
	/**
	 * The base experience gained from operating the bone
	 * @return	the experience gained in the {@code Skill.PRAYER} skill
	 */
	public double getExperience() {
		return experience;
	}

}