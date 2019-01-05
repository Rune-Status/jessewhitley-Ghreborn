package Ghreborn.model.objects.functions;

import Ghreborn.definitions.ObjectDef;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.skills.SkillHandler;
import Ghreborn.world.GameObject;

public class Ladders {


	public static void climbLadder(final Client player, int x, int y, int z) {
		//player.setStopPacket(true);
		player.animation(828/*method == "up" ? 828 : 827*/);
		player.getPA().closeAllWindows();
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
					player.getPA().movePlayer(x, y, z);
				player.animation(65535);
				container.stop();
			}
			@Override
			public void stop() {
				//player.setStopPacket(false);
			}
		}, 2);
	}

	/*	public static void checkClimbLadder(Client player, String method) {
		GameObject def = GameObject.getObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ());
		if (def == null) {
			return;
		}
		int z = player.getPosition().getZ();
		int height = method.equalsIgnoreCase("up") ? z + 1 : (z - 1 < 1 ? 0 : z - 1);
		climbLadder(player, player.getPosition().getX(), player.getPosition().getY(), height);
	}
	public static void checkClimbStaircase(Player player, int xLength, int yLength, String method) {
		GameObjectDef def = SkillHandler.getObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ());
		if (def == null) {
			return;
		}
		int face = def.getFace();
		int z = player.getPosition().getZ();
		int height = method.equalsIgnoreCase("up") ? z + 1 : (z - 1 < 1 ? 0 : z - 1);
		int x = face == 1 ? (method.equalsIgnoreCase("up") ? xLength : -xLength) : (face == 3 ? (method.equalsIgnoreCase("up") ? -xLength : xLength) : 0);
		int y = face == 0 ? (method.equalsIgnoreCase("up") ? yLength : -yLength) : (face == 2 ? (method.equalsIgnoreCase("up") ? -yLength : yLength) : 0);
		player.teleport(new Position(player.getPosition().getX() + x, player.getPosition().getY() + y, height));
	}

	public static void checkClimbStaircaseBackwards(Player player, int xLength, int yLength, String method) {
		GameObjectDef def = SkillHandler.getObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ());
		if (def == null) {
			return;
		}
		int face = def.getFace();
		int z = player.getPosition().getZ();
		int height = method.equalsIgnoreCase("up") ? z + 1 : (z - 1 < 1 ? 0 : z - 1);
		int x = face == 1 ? (method.equalsIgnoreCase("up") ? -xLength : xLength) : (face == 3 ? (method.equalsIgnoreCase("up") ? xLength : -xLength) : 0);
		int y = face == 0 ? (method.equalsIgnoreCase("up") ? -yLength : yLength) : (face == 2 ? (method.equalsIgnoreCase("up") ? yLength : -yLength) : 0);
		player.teleport(new Position(player.getPosition().getX() + x, player.getPosition().getY() + y, height));
	}

	public static void checkClimbTallStaircase(Player player, String method) {
		GameObjectDef def = SkillHandler.getObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ());
		if (def == null) {
			return;
		}
		int face = def.getFace();
		int z = player.getPosition().getZ();
		int height = method.equalsIgnoreCase("up") ? z + 1 : (z - 1 < 1 ? 0 : z - 1);
		int x = (face == 0 ? 1 : face == 1 ? 1 : face == 2 ? -1 : -1) * (method.equalsIgnoreCase("up") ? 1 : -1);
		int y = (face == 0 ? 1 : face == 1 ? -1 : face == 2 ? -1 : 1) * (method.equalsIgnoreCase("up") ? 1 : -1);
		if (def.getId() == 1738 || def.getId() == 1739 || def.getId() == 1740) {
			x = y = 0;
		}
		player.teleport(new Position(player.getPosition().getX() + x, player.getPosition().getY() + y, height));
	}

	public static void checkClimbStaircaseDungeon(Player player, int xLength, int yLength, String method) {
		GameObjectDef def = SkillHandler.getObject(player.getClickId(), player.getClickX(), player.getClickY(), player.getPosition().getZ());
		if (def == null) {
			return;
		}
		int face = def.getFace();
		int x = face == 1 ? (method.equalsIgnoreCase("up") ? xLength : -xLength) : (face == 3 ? (method.equalsIgnoreCase("up") ? -xLength : xLength) : 0);
		int y = face == 0 ? (method.equalsIgnoreCase("up") ? yLength : -yLength) : (face == 2 ? (method.equalsIgnoreCase("up") ? -yLength : yLength) : 0);
		player.teleport(new Position(player.getPosition().getX() + x, player.getPosition().getY() + y + (method.equalsIgnoreCase("up") ? -6400 : 6400), 0));
	}
*/
}
