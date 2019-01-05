package Ghreborn.model.players.packets;

import Ghreborn.Config;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.items.ItemAssistant;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;

/**
 * Attack Player
 **/
public class AttackPlayer implements PacketType {

	public static final int ATTACK_PLAYER = 73, MAGE_PLAYER = 249;

	@Override
	public void processPacket(Client client, Packet packet) {
		client.playerIndex = 0;
		client.npcIndex = 0;
		if (client.isMorphed) {
			return;
			}
		switch (packet.getOpcode()) {

		/**
		 * Attack player
		 **/
		case ATTACK_PLAYER:
			client.playerIndex = packet.getLEShort();

			if (!client.canUsePackets) {
				return;
			}

			if (client.playerIndex >= PlayerHandler.players.length || client.playerIndex < 0) {
				return;
			}
			if (PlayerHandler.players[client.playerIndex] == null) {
				break;
			}
			if (client.respawnTimer > 0) {
				break;
			}
			if (client == (PlayerHandler.players[client.playerIndex])) {
				client.sendMessage("You cannot attack yourself, " + client.playerName + ".");
				return;
			}
			if (client.getPA().viewingOtherBank) {
				client.getPA().resetOtherBank();
			}
			if (client.autocastId > 0)
				client.autocasting = true;

			if (!client.autocasting && client.spellId > 0) {
				client.spellId = 0;
			}
			client.mageFollow = false;
			client.spellId = 0;
			client.usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = client.playerEquipment[client.playerWeapon] == 9185
					|| client.playerEquipment[client.playerWeapon] == 11785
					|| client.playerEquipment[client.playerWeapon] == 8880
					|| client.playerEquipment[client.playerWeapon] == 19481;
			for (int bowId : client.BOWS) {
				if (client.playerEquipment[client.playerWeapon] == bowId) {
					usingBow = true;
					for (int arrowId : client.ARROWS) {
						if (client.playerEquipment[client.playerArrows] == arrowId) {
							usingArrows = true;
						}
					}
				}
			}
			for (int otherRangeId : client.OTHER_RANGE_WEAPONS) {
				if (client.playerEquipment[client.playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}

			if ((usingBow || client.autocasting) && client.goodDistance(client.getX(), client.getY(),
					PlayerHandler.players[client.playerIndex].getX(), PlayerHandler.players[client.playerIndex].getY(),
					6)) {
				client.usingBow = true;
				client.stopMovement();
			}

			if (usingOtherRangeWeapons && client.goodDistance(client.getX(), client.getY(),
					PlayerHandler.players[client.playerIndex].getX(), PlayerHandler.players[client.playerIndex].getY(),
					6)) {
				client.usingRangeWeapon = true;
				client.stopMovement();
			}
			if (!usingBow)
				client.usingBow = false;
			if (!usingOtherRangeWeapons)
				client.usingRangeWeapon = false;

			if (!usingCross && !usingArrows && usingBow && client.playerEquipment[client.playerWeapon] < 4212
					&& client.playerEquipment[client.playerWeapon] > 4223) {
				client.sendMessage("You have run out of arrows!");
				return;
			}
			if (!client.getCombat().correctBowAndArrows() && Config.CORRECT_ARROWS && usingBow && !client.getCombat().usingCrystalBow()
					&& !client.getItems().isWearingAnyItem(4734, 9185, 11785, 21012, 19481, 19478)) {
				client.sendMessage("You can't use " + ItemAssistant.getItemName(client.playerEquipment[client.playerArrows]).toLowerCase() + "'s with a "
						+ ItemAssistant.getItemName(client.playerEquipment[client.playerWeapon]).toLowerCase() + ".");
				client.stopMovement();
				client.getCombat().resetPlayerAttack();
				return;
			}
			if (client.playerEquipment[client.playerWeapon] == 9185 && !client.getCombat().properBolts()
					|| client.playerEquipment[client.playerWeapon] == 11785 && !client.getCombat().properBolts()
					|| client.playerEquipment[client.playerWeapon] == 8880 && !client.getCombat().properBolts()) {
				client.sendMessage("You must use bolts with a crossbow.");
				client.stopMovement();
				client.getCombat().resetPlayerAttack();
				return;
			}
			if (client.playerEquipment[client.playerWeapon] == 19481 && !client.getCombat().usingJavelins()) {
				client.sendMessage("You must use javelins with a ballista.");
				client.stopMovement();
				client.getCombat().resetPlayerAttack();
				return;
			}
			if (client.getCombat().checkReqs()) {
				client.followId = client.playerIndex;
				if (!client.usingMagic && !usingBow && !usingOtherRangeWeapons) {
					client.followDistance = 1;
					client.usingMelee = true;
					client.getPA().followPlayer();
				}
				if (client.attackTimer <= 0) {
					// client.sendMessage("Tried to attack...");
					// client.getCombat().attackPlayer(client.playerIndex);
					// client.attackTimer++;
				}

			}
			break;

		case MAGE_PLAYER:

			if (!client.canUsePackets) {
				return;
			}

			if (!client.mageAllowed) {
				client.mageAllowed = true;
				break;
			}

			client.playerIndex = packet.getShortA();
			if (client.playerIndex >= PlayerHandler.players.length || client.playerIndex < 0) {
				return;
			}
			int castingSpellId = packet.getLEShort();
			client.usingMagic = false;
			if (PlayerHandler.players[client.playerIndex] == null) {
				break;
			}

			if (client.respawnTimer > 0) {
				break;
			}

			for (int i = 0; i < client.MAGIC_SPELLS.length; i++) {
				if (castingSpellId == client.MAGIC_SPELLS[i][0]) {
					client.spellId = i;
					client.usingMagic = true;
					break;
				}
			}

			if (client.autocasting)
				client.autocasting = false;

			if (!client.getCombat().checkReqs()) {
				break;
			}
			if (client == (PlayerHandler.players[client.playerIndex])) {
				client.sendMessage("You cannot attack yourself, " + client.playerName + ".");
				return;
			}

			if (client == (PlayerHandler.players[client.playerIndex])) {
				client.sendMessage("You cannot attack this player");
				return;
			}

			for (int r = 0; r < client.REDUCE_SPELLS.length; r++) { // reducing
				// spells,
				// confuse etc
				if (PlayerHandler.players[client.playerIndex].REDUCE_SPELLS[r] == client.MAGIC_SPELLS[client.spellId][0]) {
					if ((System.currentTimeMillis()
							- PlayerHandler.players[client.playerIndex].reduceSpellDelay[r]) < PlayerHandler.players[client.playerIndex].REDUCE_SPELL_TIME[r]) {
						client.sendMessage("That player is currently immune to this spell.");
						client.usingMagic = false;
						client.stopMovement();
						client.getCombat().resetPlayerAttack();
					}
					break;
				}
			}

			if (System.currentTimeMillis()
					- PlayerHandler.players[client.playerIndex].teleBlockDelay < PlayerHandler.players[client.playerIndex].teleBlockLength
					&& client.MAGIC_SPELLS[client.spellId][0] == 12445) {
				client.sendMessage("That player is already affected by this spell.");
				client.usingMagic = false;
				client.stopMovement();
				client.getCombat().resetPlayerAttack();
			}

			/*
			 * if(!client.getCombat().checkMagicReqs(client.spellId)) {
			 * client.stopMovement(); client.getCombat().resetPlayerAttack();
			 * break; }
			 */

			if (client.usingMagic) {
				if (client.goodDistance(client.getX(), client.getY(), PlayerHandler.players[client.playerIndex].getX(),
						PlayerHandler.players[client.playerIndex].getY(), 7)) {
					client.stopMovement();
				}
				if (client.getCombat().checkReqs()) {
					client.followId = client.playerIndex;
					client.mageFollow = true;
					if (client.attackTimer <= 0) {
						// client.getCombat().attackPlayer(client.playerIndex);
						// client.attackTimer++;
					}
				}

			}
			break;

		}

	}

}
