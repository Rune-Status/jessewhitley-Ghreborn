package Ghreborn.model;

public class Location {

	private int x, y, z;
	

	public static Location create(int x, int y, int z) {
		return new Location(x, y, z);
	}

	public Location(int x, int y) {
		this(x, y, 0);
	}
	
	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	/**
	 * Checks if this location is within range of another.
	 * 
	 * @param other
	 *            The other location.
	 * @return <code>true</code> if the location is in range, <code>false</code>
	 *         if not.
	 */
	public boolean isWithinDistance(Location other) {
		if (z != other.z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}

	/**
	 * Gets the local x coordinate relative to this region.
	 * 
	 * @return The local x coordinate relative to this region.
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the local y coordinate relative to this region.
	 * 
	 * @return The local y coordinate relative to this region.
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Gets the local x coordinate relative to a specific region.
	 * 
	 * @param l
	 *            The region the coordinate will be relative to.
	 * @return The local x coordinate.
	 */
	public int getLocalX(Location l) {
		return x - 8 * (l.getRegionX() - 6);
	}

	/**
	 * Gets the local y coordinate relative to a specific region.
	 * 
	 * @param l
	 *            The region the coordinate will be relative to.
	 * @return The local y coordinate.
	 */
	public int getLocalY(Location l) {
		return y - 8 * (l.getRegionY() - 6);
	}

	/**
	 * Gets the region x coordinate.
	 * 
	 * @return The region x coordinate.
	 */
	public int getRegionX() {
		return x >> 3;
	}

	/**
	 * Gets the region y coordinate.
	 * 
	 * @return The region y coordinate.
	 */
	public int getRegionY() {
		return y >> 3;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Location)) {
			return false;
		}
		Location loc = (Location) other;
		return loc.x == x && loc.y == y && loc.z == z;
	}

	@Override
	public String toString() {
		return "[" + super.toString() + " - " + x + "," + y + "," + z + "]";
	}

	public boolean bankLocations() {
		if (x > 3090 && x < 3099 && y > 3487 && y < 3500 || x > 3089 && x < 3090 && y > 3492 && y < 3498 || x > 3248 && x < 3258 && y > 3413 && y < 3428 || x > 3179 && x < 3191 && y > 3432 && y < 3448 || x > 2944 && x < 2948 && y > 3365 && y < 3374 || x > 2942 && x < 2948 && y > 3367 && y < 3374 || x > 2944 && x < 2950 && y > 3365 && y < 3370 || x > 3008 && x < 3019 && y > 3352 && y < 3359 || x > 3017 && x < 3022 && y > 3352 && y < 3357 || x > 3203 && x < 3213 && y > 3200 && y < 3237 || x > 3212 && x < 3215 && y > 3200 && y < 3235 || x > 3215 && x < 3220 && y > 3202 && y < 3235 || x > 3220 && x < 3227 && y > 3202 && y < 3229 || x > 3227 && x < 3230 && y > 3208 && y < 3226 || x > 3226 && x < 3228 && y > 3230 && y < 3211 || x > 3227 && x < 3229 && y > 3208 && y < 3226) {
			return true;
		}
		return false;
	}

	public boolean bankLocations(int x, int y) {
		if (x > 3090 && x < 3099 && y > 3487 && y < 3500 || x > 3089 && x < 3090 && y > 3492 && y < 3498 || x > 3248 && x < 3258 && y > 3413 && y < 3428 || x > 3179 && x < 3191 && y > 3432 && y < 3448 || x > 2944 && x < 2948 && y > 3365 && y < 3374 || x > 2942 && x < 2948 && y > 3367 && y < 3374 || x > 2944 && x < 2950 && y > 3365 && y < 3370 || x > 3008 && x < 3019 && y > 3352 && y < 3359 || x > 3017 && x < 3022 && y > 3352 && y < 3357 || x > 3203 && x < 3213 && y > 3200 && y < 3237 || x > 3212 && x < 3215 && y > 3200 && y < 3235 || x > 3215 && x < 3220 && y > 3202 && y < 3235 || x > 3220 && x < 3227 && y > 3202 && y < 3229 || x > 3227 && x < 3230 && y > 3208 && y < 3226 || x > 3226 && x < 3228 && y > 3230 && y < 3211 || x > 3227 && x < 3229 && y > 3208 && y < 3226) {
			return true;
		}
		return false;
	}

	public static boolean hotZone(int x, int y) {
		if (x >= 3205 && x <= 3222 && y >= 3420 && y <= 3438 || // Varrock
				x >= 3231 && x <= 3238 && y >= 3212 && y <= 3225 || // Lumbridge
				x >= 2949 && x <= 2978 && y >= 3367 && y <= 3391 || // Falador
				x >= 2741 && x <= 2774 && y >= 3464 && y <= 3481 || // Camelot
				x >= 2652 && x <= 2672 && y >= 3294 && y <= 3318) { // Ardougne
			return true;
		}
		return false;
	}

	/**
	 * Calculate the distance between a player and a point.
	 * 
	 * @param p
	 *            A point.
	 * @return The square distance.
	 */
	public double getDistance(Location other) {
		int xdiff = this.getX() - other.getX();
		int ydiff = this.getY() - other.getY();
		return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

	/**
	 * Checks if we're in a specific arena based on simple coordinates.
	 * 
	 * @param minX
	 *            The minimum x coordinate.
	 * @param minY
	 *            The minimum y coordinate.
	 * @param minHeight
	 *            the minimum height.
	 * @param maxX
	 *            The maximum x coordinate.
	 * @param maxY
	 *            The maximum y coordinate.
	 * @param maxHeight
	 *            The maximum height.
	 * @return True if we're in the area, false it not.
	 */
	public static boolean isInArea(int x, int y, int z, int minX, int minY, int minHeight, int maxX, int maxY, int maxHeight) {
		if (z != minHeight || z != maxHeight) {
			return false;
		}
		return x >= minX && y >= minY && x <= maxX && y <= maxY;
	}

	/**
	 * Checks if this position is viewable from the other position.
	 * 
	 * @param other
	 *            the other position
	 * @return true if it is viewable, false otherwise
	 */
	/*
	 * public boolean isViewableFrom(Location other) { Location p =
	 * Misc.delta(this, other); return p.x <= 14 && p.x >= -15 && p.y <= 14 &&
	 * p.y >= -15; }
	 */

	/**
	 * Creates a new location based on this location.
	 *  dude wtf :P ? can u rip it and then onceu do ill look at it if any issues? i guess im not good at 317 server why im a client dev :/ k
	 * @param diffX
	 *            X difference.
	 * @param diffY
	 *            Y difference.
	 * @param diffZ
	 *            Z difference.
	 * @return The new location.
	 */
	public Location transform(int diffX, int diffY, int diffZ) {
		return Location.create(x + diffX, y + diffY, z + diffZ);
	}

	public void set(Location location) {
		this.x = location.x;
		this.y = location.y;
		this.z = location.z;
	}

	public Location copy() {
		return Location.create(x, y, z);
	}

    public int getRegionID() {
        return ((getRegionX() << 8) + getRegionY());
    }
}

