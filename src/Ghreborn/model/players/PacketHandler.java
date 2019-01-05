package Ghreborn.model.players;

import Ghreborn.Config;
import Ghreborn.model.players.packets.AttackPlayer;
import Ghreborn.model.players.packets.Bank10;
import Ghreborn.model.players.packets.Bank5;
import Ghreborn.model.players.packets.BankAll;
import Ghreborn.model.players.packets.BankAllButOne;
import Ghreborn.model.players.packets.BankModifiableX;
import Ghreborn.model.players.packets.BankX1;
import Ghreborn.model.players.packets.BankX2;
import Ghreborn.model.players.packets.ChallengePlayer;
import Ghreborn.model.players.packets.ChangeAppearance;
import Ghreborn.model.players.packets.ChangeRegions;
import Ghreborn.model.players.packets.Chat;
import Ghreborn.model.players.packets.ClanChat;
import Ghreborn.model.players.packets.ClickItem;
import Ghreborn.model.players.packets.ClickNPC;
import Ghreborn.model.players.packets.ClickObject;
import Ghreborn.model.players.packets.ClickingButtons;
import Ghreborn.model.players.packets.ClickingInGame;
import Ghreborn.model.players.packets.ClickingStuff;
import Ghreborn.model.players.packets.Commands;
import Ghreborn.model.players.packets.Dialogue;
import Ghreborn.model.players.packets.DropItem;
import Ghreborn.model.players.packets.FollowPlayer;
import Ghreborn.model.players.packets.IdleLogout;
import Ghreborn.model.players.packets.InputField;
import Ghreborn.model.players.packets.ItemClick2;
import Ghreborn.model.players.packets.ItemClick2OnGroundItem;
import Ghreborn.model.players.packets.ItemClick3;
import Ghreborn.model.players.packets.ItemOnGroundItem;
import Ghreborn.model.players.packets.ItemOnItem;
import Ghreborn.model.players.packets.ItemOnNpc;
import Ghreborn.model.players.packets.ItemOnObject;
import Ghreborn.model.players.packets.MagicOnFloorItems;
import Ghreborn.model.players.packets.MagicOnItems;
import Ghreborn.model.players.packets.MoveItems;
import Ghreborn.model.players.packets.OperateItem;
import Ghreborn.model.players.packets.PickupItem;
import Ghreborn.model.players.packets.PrivateMessaging;
import Ghreborn.model.players.packets.RemoveItem;
import Ghreborn.model.players.packets.Report;
import Ghreborn.model.players.packets.SelectItemOnInterface;
import Ghreborn.model.players.packets.SilentPacket;
import Ghreborn.model.players.packets.Trade;
import Ghreborn.model.players.packets.Walking;
import Ghreborn.model.players.packets.WearItem;
import Ghreborn.model.players.packets.action.InterfaceAction;
import Ghreborn.model.players.packets.action.JoinChat;
import Ghreborn.model.players.packets.action.ReceiveString;
import Ghreborn.net.Packet;

public class PacketHandler {

	private static PacketType packetId[] = new PacketType[256];

	static {

		SilentPacket u = new SilentPacket();
		packetId[3] = u;
		packetId[202] = u;
		packetId[77] = u;
		packetId[86] = u;
		packetId[78] = u;
		packetId[36] = u;
		packetId[226] = u;
		packetId[246] = u;
		packetId[148] = u;
		packetId[183] = u;
		packetId[230] = u;
		packetId[136] = u;
		packetId[189] = u;
		packetId[152] = u;
		packetId[200] = u;
		packetId[85] = u;
		packetId[165] = u;
		packetId[238] = u;
		packetId[150] = u;
		packetId[178] = u;
		packetId[40] = new Dialogue();
		ClickObject co = new ClickObject();
		packetId[232] = new OperateItem();
		packetId[132] = co;
		packetId[252] = co;
		packetId[70] = co;
		packetId[234] = co;
		packetId[228] = co;
		packetId[57] = new ItemOnNpc();
		ClickNPC cn = new ClickNPC();
		packetId[72] = cn;
		packetId[131] = cn;
		packetId[155] = cn;
		packetId[17] = cn;
		packetId[21] = cn;
		packetId[18] = cn;
		packetId[223] = u;
		packetId[218] = new Report();
		packetId[16] = new ItemClick2();
		packetId[75] = new ItemClick3();
		packetId[122] = new ClickItem();
		packetId[124] = new SelectItemOnInterface();
		packetId[253] = new ItemClick2OnGroundItem();
		packetId[241] = new ClickingInGame();
		packetId[4] = new Chat();
		packetId[236] = new PickupItem();
		packetId[87] = new DropItem();
		packetId[185] = new ClickingButtons();
		packetId[130] = new ClickingStuff();
		packetId[103] = new Commands();
		packetId[214] = new MoveItems();
		packetId[237] = new MagicOnItems();
		packetId[181] = new MagicOnFloorItems();
		packetId[202] = new IdleLogout();
		packetId[142] = new InputField();
		AttackPlayer ap = new AttackPlayer();
		packetId[73] = ap;
		packetId[249] = ap;
		packetId[128] = new ChallengePlayer();
		packetId[139] = new Trade();
		packetId[39] = new FollowPlayer();
		packetId[41] = new WearItem();
		packetId[145] = new RemoveItem();
		packetId[117] = new Bank5();
		packetId[43] = new Bank10();
		packetId[129] = new BankAll();
		packetId[101] = new ChangeAppearance();
		PrivateMessaging pm = new PrivateMessaging();
		packetId[188] = pm;
		packetId[126] = pm;
		packetId[215] = pm;
		packetId[74] = pm;
		packetId[95] = pm;
		packetId[133] = pm;
		packetId[135] = new BankX1();
		packetId[208] = new BankX2();
		packetId[140] = new BankAllButOne();
		packetId[141] = new BankModifiableX();
		Walking w = new Walking();
		packetId[98] = w;
		packetId[164] = w;
		packetId[248] = w;
		packetId[53] = new ItemOnItem();
		packetId[192] = new ItemOnObject();
		packetId[25] = new ItemOnGroundItem();
		ChangeRegions cr = new ChangeRegions();
		packetId[121] = cr;
		packetId[210] = cr;
		packetId[60] = new JoinChat();
		packetId[127] = new ReceiveString();
		packetId[213] = new InterfaceAction();
	}

	public static void processPacket(Client c, Packet packet) {
        PacketType p = packetId[packet.getOpcode()];
        if(p != null && packet.getOpcode() > 0 && packet.getOpcode() < 257) {
            if (Config.sendServerPackets && c.getRights().isDeveloper() && Config.SERVER_DEBUG) {
                c.sendMessage("PacketType: " + packet.getOpcode() + ". PacketSize: " + packet.getLength() + ".");
            }
            try {
                p.processPacket(c, packet);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            c.disconnected = true;
            System.out.println(c.playerName + " is sending invalid PacketType: " + packet.getOpcode() + ". PacketSize: " + packet.getLength());
        }
    }

}
