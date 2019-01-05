package Ghreborn.model.players.skills.construction;


import com.google.gson.annotations.Expose;

public class RoomObject {

	@Expose private int id, x, y, type, rotation;

	@Expose private String name = "";

	public RoomObject(int id, int x, int y, int rotation) {
		this(id, x, y, rotation, 10, "");
	}
	
	public RoomObject(int id, int x, int y, int rotation, int type) {
		this(id, x, y, rotation, type, "");
	}

	public RoomObject(int id, int x, int y, int rotation, String name) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.type = 10;
		this.name = name;
	}
	
	public RoomObject(int id, int x, int y, int rotation, int type, String name) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.type = type;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}