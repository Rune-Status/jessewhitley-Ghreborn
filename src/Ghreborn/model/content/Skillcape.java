package Ghreborn.model.content;

import Ghreborn.model.players.Player;

public class Skillcape {

	public enum emotes {
		ATTACK(new int[] { 9747, 9748 }, 4959, 823), 
		DEFENCE(new int[] { 9753, 9754 }, 4961, 824), 
		STRENGTH(new int[] { 9750, 9751 }, 4981, 828), 
		HITPOINTS(new int[] { 9768, 9769 }, 4971, 833), 
		RANGED(new int[] { 9756, 9757 }, 4973, 832), 
		PRAYER(new int[] { 9759, 9760 }, 4979, 829), 
		MAGIC(new int[] { 9762, 9763 }, 4939, 813), 
		COOKING(new int[] { 9801, 9802 }, 4955, 821), 
		WOODCUTTING(new int[] { 9807, 9808 }, 4957, 822), 
		FLETCHING(new int[] { 9783, 9784 }, 4937, 812), 
		FISHING(new int[] { 9798, 9799 }, 4951, 819), 
		FIREMAKING(new int[] { 9804, 9805 }, 4975, 831), 
		CRAFTING(new int[] { 9780, 9781 }, 4949, 818),
		SMITHING(new int[] { 9795, 9796 }, 4943, 815), 
		MINING(new int[] { 9792, 9793 }, 4941, 814), 
		HERBLORE(new int[] { 9774, 9775 }, 4969, 835), 
		AGILITY(new int[] { 9771, 9772 }, 4977, 830), 
		THIEVING(new int[] { 9777, 9778 }, 4965, 826), 
		SLAYER(new int[] { 9786, 9787 }, 4967, 827), 
		FARMING(new int[] { 9810, 9811 }, 4963, 825), 
		RUNECRAFTING(new int[] { 9765, 9766 }, 4947, 817), 
		HUNTER(new int[] { 9948, 9949 }, 5158, 907), 
		CONSTRUCTION(new int[] { 9789, 9790 }, 4953, 820),
		QUEST(new int[] { 9813, 13068 }, 4945, 816),
		MAX_CAPE(new int[] { 13280, 13329, 13331, 13333, 13335, 13337, 13342 }, 7121, 1286);

		private int gfxId, emoteId;

		public int[] capeId;

		emotes(int[] capeId, int emoteId, int gfxId) {
			this.capeId = capeId;
			this.emoteId = emoteId;
			this.gfxId = gfxId;
		}

		public int[] getCapeId() {
			return capeId;
		}

		public int getEmoteId() {
			return emoteId;
		}

		public int getGraphicId() {
			return gfxId;
		}
	}

	public static void performEmote(Player c, int itemId) {
		for (emotes r : emotes.values()) {
			for (int i : r.getCapeId()) {
				if (i == c.playerEquipment[c.playerCape]) {
					c.gfx0(r.getGraphicId());
					c.animation(r.getEmoteId());
				}
			}
		}
	}
}