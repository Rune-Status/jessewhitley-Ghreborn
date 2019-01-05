package Ghreborn.model.content.teleport;

import Ghreborn.Config;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.teleport.Teleport.TeleportType;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.players.Client;

/**
 * Executes a new teleport.
 * @date Aug 24, 2015 10:03:43 PM
 *
 */
public class TeleportExecutor {
	
	/**
	 * Starts a teleport process for the player with just a location using the
	 * players current magic book to determine the type
	 * 
	 * @param player
	 *            The {@link Client} trying to teleport
	 * @param location
	 *            The {@link Position} the player is teleporting too
	 */
	public static void teleport(Client player, Position location) {
		player.canWalk = false;
		if (player.zulrah.getInstancedZulrah() != null) {
			InstancedAreaManager.getSingleton().disposeOf(player.zulrah.getInstancedZulrah());
			player.getZulrahEvent().DISPOSE_EVENT();
			player.ZULRAH_CLICKS = 0;
		}
		if (player.getKraken().getInstancedKraken() != null) {
			InstancedAreaManager.getSingleton().disposeOf(player.getKraken().getInstancedKraken());
			player.getKraken().stop();
			player.KRAKEN_CLICKS = 0;
		}
		if (player.getArmadyl().getInstancedArmadyl() != null) {
			InstancedAreaManager.getSingleton().disposeOf(player.getArmadyl().getInstancedArmadyl());
			player.getArmadyl().stop();
			player.ARMADYL_INSTANCE = false;
			player.ARMADYL_CLICKS = 0;
			player.ARMADYL_MINION = 0;
		}
		if (player.getKalphite().getInstancedKalphite() != null) {
			InstancedAreaManager.getSingleton().disposeOf(player.getKalphite().getInstancedKalphite());
			player.getKalphite().stop();
			player.KALPHITE_CLICKS = 0;
			player.KALPHITE_MINION = 0;
			player.KALPHITE_INSTANCE = false;
		}
		if (player.getBandos().getInstancedBandos() != null) {
			InstancedAreaManager.getSingleton().disposeOf(player.getBandos().getInstancedBandos());
			player.getBandos().stop();
			player.BANDOS_INSTANCE = false;
			player.BANDOS_CLICKS = 0;
			player.BANDOS_MINION = 0;
		}
/*		if (player.getCerberusEvent().getInstancedCerberus() != null) {
			InstancedAreaManager.getSingleton().disposeOf(player.getCerberusEvent().getInstancedCerberus());
			player.getCerberusEvent().stop();
		}*/
		if (player.getSaradomin().getInstancedSaradomin() != null) {
			InstancedAreaManager.getSingleton().disposeOf(player.getSaradomin().getInstancedSaradomin());
			player.getSaradomin().stop();
			player.SARADOMIN_INSTANCE = false;
			player.SARADOMIN_CLICKS = 0;
			player.SARADOMIN_MINION = 0;
		}
		if (player.getZamorak().getInstancedZamorak() != null) {
			InstancedAreaManager.getSingleton().disposeOf(player.getZamorak().getInstancedZamorak());
			player.getZamorak().stop();
			player.ZAMORAK_INSTANCE = false;
			player.ZAMORAK_CLICKS = 0;
			player.ZAMORAK_MINION = 0;
		}
		if (player.getHouse() != null && player.inConstruction()) {
			player.getHouse().leave(player);
		}

		TeleportType type = player.playerMagicBook == 2 ? TeleportType.NORMAL : player.playerMagicBook == 1 ? TeleportType.ANCIENT : TeleportType.NORMAL;
		teleport(player, new Teleport(location, type), true);
	}
	
	/**
	 * Starts a teleport process for the player
	 * 
	 * @param player
	 *            The {@link Client} attempting to teleport
	 * @param teleport
	 *            The {@link Teleport} type to the location
	 */
	public static void teleport(Client player, Teleport teleport) {
		teleport(player, teleport, true);
	}

	/**
	 * Starts a teleport process for the player
	 * 
	 * @param player
	 *            The {@link Client} attempting to teleport
	 * @param teleport
	 *            The {@link Teleport} to the location
	 * @param requirements
	 *            Auth requirements before attempting to teleport
	 */
	public static void teleport(Client player, Teleport teleport, boolean requirements) {
		if (player.teleTimer > 0) {
			return;
		}
		

		/**
		 * Auth if we need to perform requirements on our teleport, if so
		 * validate our teleport, this allows us to perform an unchecked
		 * teleport for teleports such as the lever in the wilderness
		 */
		if (requirements && !canTeleport(player, teleport) && teleport.getType() != TeleportType.LEVER) {
			return;
		}

		/**
		 * The start animation of our teleport
		 */
		final int startAnim = teleport.getType().getStartAnimation();

		/**
		 * The end animation of our teleport
		 */
		final int endAnim = teleport.getType().getEndAnimation();

		/**
		 * The start graphic of our teleport
		 */
		int startGraphic = teleport.getType().getStartGraphic();

		/**
		 * The end graphic of our teleport
		 */
		final int endGraphic = teleport.getType().getEndGraphic();

		/**
		 * The initial delay of our teleport, the time it takes to start the
		 * animation till your able to walk again
		 */
		final int initialDelay = teleport.getType().getStartDelay() + teleport.getType().getEndDelay();

		/**
		 * Auth if we need to play our start animation
		 */
		if (startAnim != -1) {
			player.animation(startAnim);
		}

		/**
		 * Auth if we need to play our start graphic
		 */
		if (startGraphic != -1) {
			if (startGraphic > 65535) {
				startGraphic = (startGraphic - 65535);
				player.gfx100(startGraphic);
			} else {
				player.gfx0(startGraphic);
			}
		}
		
		player.getCombat().resetPlayerAttack();
		player.stopMovement();
		player.getPA().removeAllWindows();
		player.npcIndex = player.playerIndex = 0;
		player.face(null);
		player.teleTimer = initialDelay;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			/**
			 * A modifiable end gfx due to the nature of having to finalize the
			 * included variables so we can differentiate between the height
			 * levels of the graphic
			 */
			int endGfx = endGraphic;

			@Override
			public void execute(CycleEventContainer container) {
				player.teleTimer--;
				if (player.teleTimer == initialDelay - teleport.getType().getStartDelay()) {
			
					/**
					 * Finalize our location by setting our coordinates
					 */
					player.getPA().movePlayer(teleport.getLocation().getX(), teleport.getLocation().getY(), teleport.getLocation().getZ());
					/**
					 * Auth if we need to play our end animation
					 */
					if (endAnim != -1) {
						player.animation(endAnim);
					}

					/**
					 * Auth if we need to play our end graphic
					 */
					if (endGfx != -1) {
						if (endGfx > 65535) {
							// differentiate between height levels
							endGfx = (endGfx - 65535);
							player.gfx100(endGfx);
						} else {
							player.gfx0(endGfx);
						}
					}

				}
				
				if (player.teleTimer == 0) {
					player.teleTimer = -1;
					player.teleporting = false;
					container.stop();
				}
			}
			
		}, 1);
	}

	/**
	 * Determines if the player can teleport under the current circumstances
	 * 
	 * @param player
	 *            The {@link Client} attempting to teleport
	 * @param teleport
	 *            The {@link Teleport} the player is currently doing
	 * @return If the player can teleport
	 */
	public static boolean canTeleport(Client player, Teleport tele) {
		if (System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}
		//if (!player.startTele2) { TODO
			if (player.inWild() && player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL && !player.usingGlory && tele.getType() != Teleport.TeleportType.OBELISK && tele.getType() != Teleport.TeleportType.LEVER) {
				player.sendMessage("You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
				return false;
			} else if (player.usingGlory && player.wildLevel > 30) {
				player.sendMessage("You can't teleport above level 30 in the wilderness.");
				return false;
			}
		//}
			
		if (!player.lastSpear.elapsed(4000)) {
			player.sendMessage("You are stunned and can not teleport!");
			return false;
		}

		if (player.isDead || player.teleporting || player.stopPlayerPacket) {
			return false;
		}
		if (player.isNpc) {
			player.sendMessage("You can't teleport like this!");
			return false;
		}
		if (System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}
		if (player.playerIndex > 0 || player.npcIndex > 0) {
			player.getCombat().resetPlayerAttack();
		}
		player.teleporting = true;
		player.getPA().removeAllWindows();
		player.dialogueAction = -1;
		player.npcIndex = 0;
		player.playerIndex = 0;
		player.face(null);
		return true;
	}
	
	/**
	 * Executes the teleport action when the player pulls a lever.
	 * @param player
	 */
	public static void executeLeverTeleport(Client player, Teleport teleport) {
		if (player.inWild() && player.teleBlockLength > 0) {
			player.sendMessage("You are teleblocked and cannot teleport.");
			return;
		}
			player.animation(2140);
			player.sendMessage("You pull the lever...");
			player.getSkilling().stop();
			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
	
			@Override
			public void execute(CycleEventContainer container) {	
				teleport(player, teleport);
				if (player.teleTimer > 0)
					container.stop();
			}
			}, 1);
	}

	/**
	 * Gets the message sent when attempting to teleport while teleblocked
	 * 
	 * @param player
	 *            The {@link Client} attempting to teleport
	 * @return A converted message of the {@link Client}s teleblock delay
	 */
	@SuppressWarnings("unused")
	private static String getTeleblockMessage(Client player) {
		long remaining = player.teleBlockLength - (System.currentTimeMillis() - player.teleBlockDelay);
		long left = (long) Math.ceil(remaining / 60000.0);
		return "You are teleblocked for the next " + (left == 0 ? 1 : left) + " minutes";
	}


}

