package Ghreborn.model;

import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;

public abstract class Entity {


	public int index = -1;
	public static int index1 = -1;
	public int absX, absY, heightLevel;
	public int mapRegionX, mapRegionY;
	public int currentX, currentY;

	public boolean updateRequired; // might have to be 'true' for players
	public boolean animUpdateRequired;
	public boolean hitUpdateRequired;
	public boolean hitUpdateRequired2;
	public boolean gfxUpdateRequired = false;
	public boolean faceUpdateRequired;
	public boolean forcedChatUpdateRequired;
	public boolean facePositionUpdateRequired;
	public boolean isDead;
	/**
	 * The entity is an npc
	 */
	private boolean npc = true;

	public int hitDiff;
	public int hitDiff2;
	public int animId;
	public int animDelay, projectileId, endGfx;
	public int gfxVar1;
	public int gfxVar2;
	public int face;
	public String forcedText;
	public int faceX;
	public int faceY;

	public final void animation(int anim, int delay) {
		animId = anim;
		animDelay = 0;
		updateRequired = true;
		animUpdateRequired = true;
	}

	public final void animation(int anim) {
		animation(anim, 0);
	}

	public final void stopAnimation() {
		animation(65535);
	}

	public final void primaryHit(int hit) {
		hitDiff = hit;
		updateRequired = true;
		hitUpdateRequired = true;
	}

	public final void secondaryHit(int hit) {
		hitDiff2 = hit;
		updateRequired = true;
		hitUpdateRequired2 = true;
	}

	public final void gfx0(int gfx) {
		gfxVar1 = gfx;
		gfxVar2 = 65536;
		gfxUpdateRequired = true;
		updateRequired = true;
	}
	public void gfx(int gfx, int height) {
		gfxVar1 = gfx;
		gfxVar2 = 65536 * height;
		gfxUpdateRequired = true;
		updateRequired = true;
	}
	public void sendHighGraphic(int graphicsId) {
		this.gfxVar1 = graphicsId;
		this.gfxVar2 = 100 << 16 + 0;
		gfxUpdateRequired = true;
		updateRequired = true;
	}
	public void sendHighGraphic(int graphicsId, int graphicsDelay) {
		this.gfxVar1 = graphicsId;
		this.gfxVar2 = 100 << 16 + graphicsDelay;
		gfxUpdateRequired = true;
		updateRequired = true;
	}
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isDead() {
		return isDead;
	}
	public final void gfx100(int gfx) {
		gfxVar1 = gfx;
		gfxVar2 = 6553600;
		gfxUpdateRequired = true;
		updateRequired = true;
	}

	public final void face(Entity e) {
		face = e == null ? 65535 : e instanceof Client ? e.index + 32768 : e.index;
		faceUpdateRequired = true;
		updateRequired = true;
	}

	public final void forceChat(String t) {
		forcedText = t;
		updateRequired = true;
		forcedChatUpdateRequired = true;
	}

	public final void face(int x, int y) {
		faceX = 2 * x + 1;
		faceY = 2 * y + 1;
		updateRequired = true;
		facePositionUpdateRequired = true;
	}
	
	public int getX(){
		return absX;
	}
	public int getY(){
		return absY;
		
	}
	public int getHeight(){
		return heightLevel; 
	}
	
	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getRegionX() {
		return (absX >> 6);
	}

	public int getRegionY() {
		return (absY >> 6);
	}
	public int getLocalX() {
		return getX() - 8 * getMapRegionX();
	}

	public int getLocalY() {
		return getY() - 8 * getMapRegionY();
	}
	public int getRegionID() {
		return ((getLocalX() << 8) + getLocalY());
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public int getRegionId() {
	    int regionX = absX >> 3;
	    int regionY = absY >> 3;
	    int regionId = (regionX / 8 << 8) + regionY / 8;
	    return regionId;
	}
	/**
	 * Gets an entities position
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * The position of this entity
	 */
	private Position position;
	
	/**
	 * Sets a players position to a new coordinate
	 * @param position
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	public final void onReset() {
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		updateRequired = false;
		animUpdateRequired = false;
		gfxUpdateRequired = false;
		faceUpdateRequired = false;
		forcedChatUpdateRequired = false;
		facePositionUpdateRequired = false;
		reset();
	}

	public abstract void reset();

	public int getIndex() {
		// TODO Auto-generated method stub
		return index;
	}

	public boolean isNpc() {
		// TODO Auto-generated method stub
		return npc;
	}

	/*public boolean hasFinished() {
		// TODO Auto-generated method stub
		return false;
	}*/
}
