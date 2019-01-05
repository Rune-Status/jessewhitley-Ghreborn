package Ghreborn.model.players.skills.hunter;

import java.util.HashMap;
import java.util.Random;

import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.util.Misc;

public class Implings {

	public static final Random random = new Random();


	/**
	 * 
	 * Imp rewards
	 * @author Emre
	 */
	public enum ImpRewards {
		BABY(11238, new int[][] {{5291, 1}, {5292, 1}, {5293, 1}, {5294, 1}, {5295, 1}, {5296, 1}, {5297, 1}, {5298, 1}, {5299, 1}, {5300, 1}, {5301, 1}, {5302, 1}, {5303, 1}, {5304, 1}}, new int[][] {{5304, 10}}), //Level 1 Hunter
		YOUNG(11240, new int[][] {{1778, 1}, {222, 1}, {236, 1}, {1526, 1}, {224, 1}, {9737, 1}, {232, 1}, {226, 1}, {240, 1}, {244, 1}, {6050, 1},}, new int[][] {{228, 10}}), //Level 22 Hunter
		GOURMENT(11242, new int[][] {{7947, 5}, {386, 3}, {3145, 1}, {11937, 1}, {13441, 1}}, new int[][] {{11937, 10}}), //Level 28 Hunter
		EARTH(11244, new int[][] {{1512, 5}, {1522, 4}, {1520, 3}, {1518, 3}, {1516, 2}, {1514, 2}}, new int[][] {{1514, 25}}), //Level 36 Hunter
		ESSENCE(11246, new int[][] {{560, 5}, {565, 5}, {566, 5}, {9075, 5}, {562, 5}, {563, 5}, {561, 4}}, new int[][] {{1437, 50}}), //Level 42 Hunter
		ECLECTIC(11248, new int[][] {{448, 4}, {454, 5}, {450, 2}, {452, 1}}, new int[][] {{452, 15}}), //Level 50 Hunter
		NATURE(11250, new int[][] {{200, 1}, {202, 1}, {204, 1}, {206, 1}, {208, 1}, {3050, 1}, {210, 1}, {212, 1}, {212, 1}, {214, 1}, {218, 1}, {216, 1}, {2486, 1}, {218, 1}, {220, 1}}, new int[][] {{6688, 5}}), //Level 58 Hunter
		MAGPIE(11252, new int[][] {{1746, 1}, {2506, 1}, {2508, 1}, {2510, 1}}, new int[][] {{2510, 15}} ), //Level 65 Hunter
		NINJA(11254, new int[][] {{1894, 2}, {6815, 1}, {1806, 1}, {1614, 1}, {1994, 1}}, new int[][] {{995, 35_000}}), //Level 74 Hunter
		DRAGON(11256, new int[][] {{2352, 5}, {2358, 3}, {2354, 5}, {2360, 4}, {2362, 3}, {2364, 2}}, new int[][] {{2364, 15}} ); //Level 83 Hunter

		public static HashMap<Integer, ImpRewards> impReward = new HashMap<>();

		static {
			for(ImpRewards t : ImpRewards.values()) {
				impReward.put(t.itemId, t);
			}
		}

		private int itemId;
		private int[][] rewards;
		private int[][] rareRewards;

		ImpRewards(int itemId, int[][] rewards, int[][] rareRewards) {
			this.itemId = itemId;
			this.rewards = rewards;
			this.rareRewards = rareRewards;
		}
		public int getItemId() {
			return itemId;
		}
		public int[][] getRewards() {
			return rewards;
		}
		public int[][] getRareRewards() {
			return rareRewards;
		}
		public static void getReward(Client c, int itemId) {
			if(c.getItems().freeSlots() < 2) {
				c.sendMessage("You need atleast 2 free slot");
				return;
			}

			ImpRewards t = ImpRewards.impReward.get(itemId);
			c.getItems().deleteItem(t.getItemId(), c.getItems().getItemSlot(t.getItemId()), 1);
			int r = random.nextInt(t.getRewards().length);
			int rare = random.nextInt(t.getRareRewards().length);
			int chance = Misc.random(150);
			if(chance == 1) {
				c.sendMessage("Congratulations you've received a <col=0000FF>RARE <col=000000>reward: <col=0000FF>" + c.getItems().getItemName(t.getRareRewards()[rare][0]) + ": <col=000000>x<col=0000FF>" + t.getRareRewards()[rare][1] + "");
				c.getItems().addItem(t.getRareRewards()[rare][0], t.getRareRewards()[rare][1]);
			}
			if(t.getRewards()[r].length == 3) {
				int amount = t.getRewards()[r][1] + random.nextInt(t.getRewards()[r][2] - t.getRewards()[r][1]);
				c.getItems().addItem(t.getRewards()[r][0], amount);
			} else {
				c.getItems().addItem(t.getRewards()[r][0], t.getRewards()[r][1]);
			}
			if(Misc.random(15) == 0) {
				c.sendMessage("The impling jar breaks but you do get a reward");
			} else {
				c.getItems().addItem(11260, 1);
				c.sendMessage("You open the impling jar and get a reward");
			}
		}
	}

	/**
	 * 
	 * Catching imps, spawning, despawning
	 * @author Emre
	 *
	 */
	public enum Imps {
		BABY_IMPLING(1645, 1, 50, 11238),
		YOUNG_IMPLING(1646, 22, 75, 11240),
		GOURMET_IMPLING(1647, 28, 101, 11242),
		EARTH_IMPLING(1648, 36, 154, 11244),
		ESSENCE_IMPLING(1649, 42, 170, 11246),
		ECLECTIC_IMPLING(1650, 50, 180, 11248),
		NATURE_IMPLING(1651, 58, 190, 11250),
		MAGPIE_IMPLING(1652, 65, 200, 11252),
		NINJA_IMPLING(1653, 74, 245, 11254),
		DRAGON_IMPLING(1654, 83, 310, 11256),
		LUCKY(7302, 89, 355, 19732);
		
		public static HashMap<Integer, Imps> implings = new HashMap<>();

		static {
			for(Imps t : Imps.values()) {
				implings.put(t.npc, t);
			}
		}

		private int npc;
		private int levelRequired;
		private int experience;
		private int itemId;

		Imps(final int npc, final int levelRequired, final int experience, final int itemId) {
			this.npc = npc;
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.itemId = itemId;
		}
		public int getImpId() {
			return npc;
		}
		public int getLevelRequired() {
			return levelRequired;
		}
		private String getName() {
			return Misc.optimizeText(toString().toLowerCase().replaceAll("_", " "));
		}
		public int getXp() {
			return experience;
		}
		public int getJar() {
			return itemId;
		}
		public static Imps forId(int id) {
			return implings.get(id);
		}
		
		public static int getRandom(int[] array) {
		    int rnd = new Random().nextInt(array.length);
		    return array[rnd];
		}
		public static void catchImp(Client c, int npcType, final int npcidx) {
			
			if(c.playerSkilling[c.playerHunter]) {
			return;
			}
			Imps t = Imps.implings.get(npcType);
			int chance = Misc.random(50);
			int[] items = new int[]{13323, 13324, 13325, 13326};
			int chance1 = Misc.random(6000);
			int chance2 = Misc.random(3500);
			
			if (System.currentTimeMillis() - c.hunting < 1500) {
				return;
			}
			if (c.catchingImp) {
				return;
			}
			if(!c.getItems().playerHasItem(10010) &&! c.getItems().playerHasItem(11259) && c.playerEquipment[c.playerWeapon] != 10010 && c.playerEquipment[c.playerWeapon] != 11259 ) {
				c.sendMessage("You need a butterfly net");
				return;
			}
			if(c.playerLevel[c.playerHunter] < t.getLevelRequired()) {
				c.sendMessage("You need a hunter of " + t.getLevelRequired() + " To catch this imp");
				return;
			}

			if(!c.getItems().playerHasItem(11260)) {
				c.sendMessage("You need a impling jar");
				return;
			}
			c.playerSkilling[c.playerHunter] = true;
			if(Misc.random(10) >= ((c.playerLevel[c.playerHunter] - 10) / 10) + 1) {
				c.animation(6605);
				c.sendMessage("You fail catching the " + t.getName() + "!");
				c.hunting = System.currentTimeMillis();
				Server.npcHandler.npcs[npcidx].forceChat(Impling());
				NPCHandler.npcs[npcidx].animation(6616);
			} else {
				if (chance1 == 1500){
					if (npcType == 1645 || npcType == 1646 || npcType == 1647
					 || npcType == 1648	|| npcType == 1649){
						c.sendMessage("<col=DD5C3E>You receive a skilling pet. It has been added to your bank. Congratulations!");
						c.getItems().addItemToBank(getRandom(items), 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						//c2.sendMessage("<col=006600>" + c.playerName + " received a skilling pet: 1 x Random Chincompa.");
								}
							}
						}
					}
					if (chance2 == 3500){
					if (npcType == 1650 || npcType == 1651 || npcType == 1652
							 || npcType == 1653	|| npcType == 1654){
								c.sendMessage("<col=DD5C3E>You receive a skilling pet. It has been added to your bank. Congratulations!");
								c.getItems().addItemToBank(getRandom(items), 1);
							for (int j = 0; j < PlayerHandler.players.length; j++) {
								if (PlayerHandler.players[j] != null) {
								Player c2 = PlayerHandler.players[j];
								//c2.sendMessage("<col=006600>" + c.playerName + " received a skilling pet: 1 x Random Chincompa.");
								}
							}
						}
					}
				c.animation(6605);
				c.getItems().deleteItem(11260, c.getItems().getItemSlot(11260), 1);
				c.getItems().addItem(t.getJar(), 1);
				c.getPA().addSkillXP(t.getXp() * Config.HUNTER_EXPERIENCE, c.playerHunter);
				c.sendMessage("You catch the Imp");
				impDeath(c, npcType, npcidx);
			}
			c.playerSkilling[c.playerHunter] = false;

		}
		public static String Impling() {
			int quote = Misc.random(6);
			switch (quote) {
			case 0:
				return "LOL";
			case 1:
				return "Lmao u cant get me.";
			case 2:
				return "ha ha :P";
			case 3:
				return "Im sorry but im still free";
			case 4:
				return "o no a hunter that cant catch me";
			case 5:
				return "eww that dirty net";
			case 6:
				return "hehe :)";
			}
			return "Hehe";
		}
		public static void respawnImp(Client c, final int id, final int x, final int y, final int z) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				
				int i = 0;
				@Override
				public void execute(CycleEventContainer container) {
					if(i++ == 1) {
						Server.npcHandler.newNPC(id, x, y, z, 1, 0, 0, 0, 0);
						container.stop();
					}
				}

				@Override
				public void stop() {
				}
			}, 100);
		}
		public static void impDeath(Client c, int npcType, int idx) {
			Imps t = Imps.implings.get(npcType);
			NPC n = NPCHandler.npcs[idx];
			if(n != null && n.npcType == t.getImpId()) {
					int x = n.absX;
					int y = n.absY;
					int z = n.heightLevel;
					n.absX = 0;
					n.absY = 0;
					n.isDead = true;
					NPCHandler.npcs[idx] = null;
					respawnImp(c, npcType, x, y, z);
			}
		}
	}
}