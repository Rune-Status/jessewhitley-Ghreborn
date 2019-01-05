package Ghreborn.model.players.packets;
 
import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.PacketType;
import Ghreborn.net.Packet;
 
public class FollowPlayer implements PacketType {
       
        @Override
        public void processPacket(Client c, Packet packet) {
    		int followPlayer = packet.getLEShort();
    		if (!c.canUsePackets) {
    			return;
    		}
        		if (PlayerHandler.players[followPlayer] != null) {
        			c.playerIndex = 0;
        			c.npcIndex = 0;
        			c.mageFollow = false;
        			c.usingBow = false;
        			c.usingRangeWeapon = false;
        			c.followDistance = 1;
        			c.followId = followPlayer;
        		}   
        }
}
 