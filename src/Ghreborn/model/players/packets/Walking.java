package Ghreborn.model.players.packets;


import Ghreborn.Server;
import Ghreborn.clip.Region;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.multiplayer_session.MultiplayerSessionType;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.model.players.skills.SkillHandler;
import Ghreborn.net.Packet;
/**
 * Walking packet
 **/
public class Walking implements PacketType {

	@Override
	public void processPacket(Client c, Packet packet) {
		int packetSize = packet.getLength();
		c.setWalkInteractionTask(null);
		if (!c.canUsePackets) {
			return;
		}
		if (c.getPlayerAction().canWalk(false)) {
			return;
		}
		if (c.isIdle) {
			if (c.debugMessage)
				c.sendMessage("You are no longer in idle mode.");
			c.isIdle = false;
		}
		if (c.getSkilling().isSkilling()) {
			c.getSkilling().stop();
		}
		if (c.teleporting == false) {
			c.canWalk = true;
		}
		if (c.alreadyFishing) {
			c.alreadyFishing = false;
		}
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		if (c.getInferno() != null && c.getInferno().cutsceneWalkBlock)
			return;
		c.getPA().resetVariables();
		SkillHandler.isSkilling[12] = false;
		/*
		 * if (c.teleporting == true) { c.animation(65535); c.teleporting =
		 * false; c.isRunning = false; c.gfx0(-1); c.animation(-1); }
		 */ // TODO
		c.fillingWater = false;
		c.walkingToItem = false;
		//c.isWc = false;
		c.clickNpcType = 0;
		c.clickObjectType = 0;
		if (c.isBanking)
			c.isBanking = false;
		if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			c.sendMessage("You must decline the trade to start walking.");
			return;
		}
		if (packet.getOpcode() == 248 || packet.getOpcode() == 164) {
			c.clickObjectType = 0;
			c.clickNpcType = 0;
			c.face(null);
			c.npcIndex = 0;
			c.playerIndex = 0;
			if (c.followId > 0 || c.followId2 > 0)
				c.getPA().resetFollow();
		}
		if (c.getGnomeAgility().PERFORMING_ACTION) {
			return;
		}
		c.getPA().removeAllWindows();
		c.DonatorPod = false; 
		if (c.stopPlayerSkill) {
			SkillHandler.resetPlayerSkillVariables(c);
			c.stopPlayerSkill = false;
		}
		if (c.canWalk == false) {
			return;
		}
		if (c.freezeTimer > 0) {
			if (PlayerHandler.players[c.playerIndex] != null && c.goodDistance(c.getX(), c.getY(),
					PlayerHandler.players[c.playerIndex].getX(), PlayerHandler.players[c.playerIndex].getY(), 1)
					&& packet.getOpcode() != 98) {
				c.playerIndex = 0;
			} else {
				c.sendMessage("A magical force stops you from moving.");
				if (packet.getOpcode() != 98) {
					c.playerIndex = 0;
				}

			}
			return;
		}

		if (!c.lastSpear.elapsed(4000)) {
			c.sendMessage("You have been stunned.");
			c.playerIndex = 0;
			return;
		}
		if (packet.getOpcode() == 98) {
			c.mageAllowed = true;
		}
		if (!c.lastSpear.elapsed(4000)) {
			c.sendMessage("You have been stunned.");
			c.playerIndex = 0;
			return;
		}
		if (c.respawnTimer > 3) {
			return;
		}
		if (c.inTrade) {
			return;
		}
		if (packet.getOpcode() == 248) {
			packetSize -= 14;
		}
		c.newWalkCmdSteps = (packetSize - 5) / 2;
		if (++c.newWalkCmdSteps > c.walkingQueueSize) {
			c.newWalkCmdSteps = 0;
			return;
		}
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int absX = packet.getLEShortA();
		int firstStepX = absX - c.getMapRegionX() * 8;
		for (int i = 1; i < c.newWalkCmdSteps; i++) {
			c.getNewWalkCmdX()[i] = packet.getByte();
			c.getNewWalkCmdY()[i] = packet.getByte();
		}
		int absY = packet.getLEShort();
		int firstStepY = absY - c.getMapRegionY() * 8;
		c.setNewWalkCmdIsRunning((packet.getByteC() == 1));

		int lastX = 0, lastY = 0;
		for (int i1 = 0; i1 < c.newWalkCmdSteps; i1++) {
			lastX = absX + c.getNewWalkCmdX()[i1];
			lastY = absY + c.getNewWalkCmdY()[i1];
			c.getNewWalkCmdX()[i1] += firstStepX;
			c.getNewWalkCmdY()[i1] += firstStepY;
		}

		c.setWalkingDestination(new Position(lastX, lastY, c.heightLevel));
			//c.getMusic().updateRegionMusic(c.getRegionId());
		
		}

	

}
