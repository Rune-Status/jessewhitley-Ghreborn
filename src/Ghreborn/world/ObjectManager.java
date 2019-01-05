package Ghreborn.world;

import java.util.ArrayList;

import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.objects.DoubleGates;
import Ghreborn.model.objects.Object;
import Ghreborn.model.objects.Objects;
import Ghreborn.util.Misc;
import Ghreborn.world.objects.GlobalObject;
import Ghreborn.model.players.Client;

/**
 * @author Sanity
 */

public class ObjectManager {

	public ArrayList<Object> objects = new ArrayList<Object>();
	private ArrayList<Object> toRemove = new ArrayList<Object>();

	public void process() {
		for (Object o : objects) {
			if (o.tick > 0)
				o.tick--;
			else {
				updateObject(o);
				toRemove.add(o);
			}
		}
		for (Object o : toRemove) {
			if (isObelisk(o.newId)) {
				int index = getObeliskIndex(o.newId);
				if (activated[index]) {
					activated[index] = false;
					teleportObelisk(index);
				}
			}
			objects.remove(o);
		}
		toRemove.clear();
	}

	public void removeObject(int x, int y) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				c.getPA().object(-1, x, y, 0, 10);
			}
		}
	}

	public void updateObject(Object o) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				c.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);
			}
		}
	}

	public void placeObject(Object o) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.distanceToPoint(o.objectX, o.objectY) <= 60)
					c.getPA().object(o.objectId, o.objectX, o.objectY, o.face,
							o.type);
			}
		}
	}

	public Object getObject(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height)
				return o;
		}
		return null;
	}

	public void loadObjects(Client c) {
		if (c == null)
			return;
		for (Object o : objects) {
			if (loadForPlayer(o, c))
				c.getPA().object(o.objectId, o.objectX, o.objectY, o.face,
						o.type);
		}
		loadCustomSpawns(c);
	}

	public void loadCustomSpawns(Client client) {
		client.getPA().checkObjectSpawn(-1, 3233, 3207, -1, 10);
		client.getPA().checkObjectSpawn(-1, 3234, 3207, -1, 10);
		
		client.getPA().checkObjectSpawn(6943, 2459, 3178, 0, 10);
		client.getPA().checkObjectSpawn(6943, 2460, 3178, 0, 10);
		client.getPA().checkObjectSpawn(6943, 2461, 3178, 0, 10);
		client.getPA().checkObjectSpawn(6943, 2462, 3178, 0, 10);
		//skillz
		client.getPA().checkObjectSpawn(6943, 3257, 3290, 1, 10);//bank
		client.getPA().checkObjectSpawn(6943, 3257, 3291, 1, 10);//bank
		client.getPA().checkObjectSpawn(6943, 3257, 3288, 1, 10);//bank
		client.getPA().checkObjectSpawn(6943, 3257, 3287, 1, 10);//bank
		client.getPA().checkObjectSpawn(2097, 3252, 3291, 0, 10);//anvil
		client.getPA().checkObjectSpawn(2097, 3252, 3289, 0, 10);//anvil
		client.getPA().checkObjectSpawn(26181, 3256, 3292, 0, 10);//range
		client.getPA().checkObjectSpawn(26181, 3254, 3292, 0, 10);//range
		//train
		client.getPA().checkObjectSpawn(6943, 1770, 5498, 0, 10);//bank
		client.getPA().checkObjectSpawn(6943, 1771, 5498, 0, 10);//bank
		client.getPA().checkObjectSpawn(6943, 1772, 5498, 0, 10);//bank
		client.getPA().checkObjectSpawn(6943, 1773, 5498, 0, 10);//bank
		client.getPA().checkObjectSpawn(6943, 1774, 5498, 0, 10);//bank
		client.getPA().checkObjectSpawn(6943, 1775, 5498, 0, 10);//bank
		client.getPA().checkObjectSpawn(6943, 1776, 5498, 0, 10);//bank
		client.getPA().checkObjectSpawn(6943, 1777, 5498, 0, 10);//bank
		client.getPA().checkObjectSpawn(6943, 1772, 5494, 0, 10);//bank
		client.getPA().checkObjectSpawn(11663, 1777, 5486, 0, 10);//range
		client.getPA().checkObjectSpawn(2380, 3256, 3285, -1, 10);//range
		client.getPA().checkObjectSpawn(61, 1770, 5484, 0, 10);//range
		client.getPA().checkObjectSpawn(4113, 3206, 3222, -3, 10);//chest
		client.getPA().checkObjectSpawn(404, 3234, 3209, -3, 10);//block
		client.getPA().checkObjectSpawn(404, 3235, 3209, -3, 10);//block
		client.getPA().checkObjectSpawn(404, 3236, 3209, -3, 10);//block
		client.getPA().checkObjectSpawn(404, 3237, 3209, -3, 10);//block
		client.getPA().checkObjectSpawn(5259, 3237, 3201, -2, 10);//Ghosty Portal
//hang
		client.getPA().checkObjectSpawn(6943, 2385, 3485, 1, 10);//bank
		client.getPA().checkObjectSpawn(6943, 2385, 3486, 1, 10);//bank
		client.getPA().checkObjectSpawn(6943, 2385, 3487, 1, 10);//bank
		client.getPA().checkObjectSpawn(6943, 2385, 3488, 1, 10);//bank
		client.getPA().checkObjectSpawn(6943, 2385, 3489, 1, 10);//bank
		client.getPA().checkObjectSpawn(6943, 2385, 3490, 1, 10);//bank
		client.getPA().checkObjectSpawn(6943, 2385, 3491, 1, 10);//bank
		client.getPA().checkObjectSpawn(24957, 2389, 3489, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3490, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3491, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3492, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3493, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3494, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3495, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3481, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3482, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3483, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3484, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3485, 1, 10);
		client.getPA().checkObjectSpawn(24957, 2389, 3486, 1, 10);
		//theve
		client.getPA().checkObjectSpawn(4876, 2969, 3384, 0, 10);
		client.getPA().checkObjectSpawn(4878, 2969, 3381, 0, 10);
		client.getPA().checkObjectSpawn(4877, 2961, 3383, 0, 10);
	    client.getPA().checkObjectSpawn(11731, 2961, 3380, 1, 10);
		client.getPA().checkObjectSpawn(11729, 2965, 3376, 0, 10);
		//rcing	
		
		client.getPA().checkObjectSpawn(6943, 1633, 4504, 0, 10);//bank
		if (client.heightLevel == 0) {
			client.getPA().checkObjectSpawn(2492, 2911, 3614, 1, 10);
		} else {
			client.getPA().checkObjectSpawn(-1, 2911, 3614, 1, 10);
		}
	}

	public final int IN_USE_ID = 14825;

	public boolean isObelisk(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return true;
		}
		return false;
	}

	public int[] obeliskIds = { 14829, 14830, 14827, 14828, 14826, 14831 };
	public int[][] obeliskCoords = { { 3154, 3618 }, { 3225, 3665 },
			{ 3033, 3730 }, { 3104, 3792 }, { 2978, 3864 }, { 3305, 3914 } };
	public boolean[] activated = { false, false, false, false, false, false };

	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				addObject(new Object(14825, obeliskCoords[index][0],
						obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4,
						obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0],
						obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4,
						obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId, 16));
			}
		}
	}

	public int getObeliskIndex(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return j;
		}
		return -1;
	}

	public void teleportObelisk(int port) {
		int random = Misc.random(5);
		while (random == port) {
			random = Misc.random(5);
		}
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				int xOffset = c.absX - obeliskCoords[port][0];
				int yOffset = c.absY - obeliskCoords[port][1];
				if (c.goodDistance(c.getX(), c.getY(),
						obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2,
						1)) {
					c.getPA().startTeleport2(
							obeliskCoords[random][0] + xOffset,
							obeliskCoords[random][1] + yOffset, 0);
				}
			}
		}
	}

	public boolean loadForPlayer(Object o, Client c) {
		if (o == null || c == null)
			return false;
		return c.distanceToPoint(o.objectX, o.objectY) <= 60
				&& c.heightLevel == o.height;
	}

	public void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}
	}

	public static void singleGateTicks(final Client player, final int objectId, final int objectX, final int objectY, final int x1, final int y1, final int objectH, final int face, int ticks) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (DoubleGates.gateAmount == 0) {
					container.stop();
					return;
				}
				Server.globalObjects.add(new GlobalObject(-1, x1, y1, objectH, face, 0, 0));
				Server.globalObjects.add(new GlobalObject(objectId, objectX, objectY, objectH, face, 0, 0));
				container.stop();
			}

			@Override
			public void stop() {
				if (DoubleGates.gateAmount == 1) {
					DoubleGates.gateAmount = 0;
				}
			}
		}, ticks);
	}
	
	public static void doubleGateTicks(final Client player, final int objectId, final int objectX, final int objectY, final int x1, final int y1, final int x2, final int y2, final int objectH, final int face, int ticks) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (DoubleGates.gateAmount == 0) {
					container.stop();
					return;
				}
				Server.globalObjects.add(new GlobalObject(-1, x1, y1, objectH, face, 0, 0));
				Server.globalObjects.add(new GlobalObject(-1, x2, y2, objectH, face, 0, 0));
				Server.globalObjects.add(new GlobalObject(objectId, objectX, objectY, objectH, face, 0, 0));
				container.stop();
			}

			@Override
			public void stop() {
				if (DoubleGates.gateAmount == 2) {
					DoubleGates.gateAmount = 1;
				} else if (DoubleGates.gateAmount == 1) {
					DoubleGates.gateAmount = 0;
				}
			}
		}, ticks);
	}


}