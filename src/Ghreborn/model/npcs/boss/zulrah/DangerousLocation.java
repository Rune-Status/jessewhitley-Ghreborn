package Ghreborn.model.npcs.boss.zulrah;

import java.awt.Point;

public enum DangerousLocation {
	SOUTH_WEST(new Point(2262, 3069), new Point(2265, 3068)),
	SOUTH_EAST(new Point(2268, 3068), new Point(2271, 3069)),
	EAST(new Point(2272, 3071), new Point(2272, 3074)),
	WEST(new Point(2262, 3071), new Point(2262, 3074)),
	NEW_LOC(new Point(2263, 3076), new Point(2273, 3076));
	
	private Point[] points;
	
	private DangerousLocation(Point... points) {
		this.points = points;
	}
	
	public Point[] getPoints() {
		return points;
	}
	
}