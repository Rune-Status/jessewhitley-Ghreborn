package Ghreborn.model.players.skills.construction;

public class Buildable {

	private RoomObject object, replacement;
	
	private int levelRequirement, experience, buildAnimation;
	
	private int[] toolsRequired;
	
	private int[][] itemsRequired;
	
	public Buildable(RoomObject object, RoomObject replacement, int levelRequirement, int experience, int[] toolsRequired, int[][] itemsRequired) {
		this(object, replacement, levelRequirement, experience, toolsRequired, itemsRequired, 898);
	}
	
	public Buildable(RoomObject object, RoomObject replacement, int levelRequirement, int experience, int[] toolsRequired, int[][] itemsRequired, int buildAnimation) {
		this.object = object;
		this.replacement = replacement;
		this.levelRequirement = levelRequirement;
		this.experience = experience;
		this.toolsRequired = toolsRequired;
		this.itemsRequired = itemsRequired;
		this.buildAnimation = buildAnimation;
	}

	public RoomObject getObject() {
		return object;
	}

	public RoomObject getReplacement() {
		return replacement;
	}

	public int getLevelRequirement() {
		return levelRequirement;
	}

	public int getExperience() {
		return experience;
	}

	public int getBuildAnimation() {
		return buildAnimation;
	}

	public int[] getToolsRequired() {
		return toolsRequired;
	}

	public int[][] getItemsRequired() {
		return itemsRequired;
	}
	
}
