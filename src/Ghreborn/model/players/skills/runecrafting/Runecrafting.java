package Ghreborn.model.players.skills.runecrafting;

import java.util.stream.IntStream;

import Ghreborn.Config;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.skills.SkillHandler;
import Ghreborn.util.Misc;

public class Runecrafting extends SkillHandler {
	
	private enum RUNECRAFTING_DATA {
		AIR(14897, 556, 5, new int[] { 1, 11, 22, 33, 44, 55, 66, 77, 88, 99 }, 25000, 20667), 
		MIND(14898, 558, 5.5, new int[] { 2, 14, 28, 42, 56, 70, 84, 98 }, 24500, 20669), 
		WATER(14899, 555, 6, new int[] { 5, 19, 38, 57, 76, 88, 99 }, 24000, 20671), 
		MIST(-1, -1, 8, new int[] { 6 }, 15, -1), 
		EARTH(14900, 557, 6.5, new int[] { 9, 26, 52, 78, 88, 96 }, 23500, 20673), 
		DUST(-1, -1, 8.3, new int[] { 10 }, 15, -1), 
		MUD(-1, -1, 9.3, new int[] { 13 }, 15, -1), 
		FIRE(14901, 554, 7, new int[] { 14, 35, 70, 85, 95 }, 23000, 20665), 
		SMOKE(-1, -1, 8.5, new int[] { 15 }, 15, -1), 
		STEAM(-1, -1, 9.3, new int[] { 19 }, 15, -1), 
		BODY(14902, 559, 7.5, new int[] { 20, 46, 75, 92 }, 22500, 20675), 
		LAVA(-1, -1, 10, new int[] { 23 }, 15, -1), 
		COSMIC(14903, 564, 8, new int[] { 27, 59, 78 }, 22000, 20677), 
		CHAOS(14906, 562, 8.5, new int[] { 35, 74, 86 }, 21500, 20679), 
		ASTRAL(14911, 9075, 8.7, new int[] { 40, 82 }, 21000, 20689), 
		NATURE(14905, 561, 9, new int[] { 44, 91, 95 }, 20000, 20681), 
		LAW(14904, 563, 9.5, new int[] { 54, 79, 93 }, 19500, 20683), 
		DEATH(25035, 560, 10, new int[] { 65, 84, 96 }, 19000, 20685);
		//BLOOD(25380, 565, 10.5, new int[] { 77, 89, 94 }, 18800, 20691), 
		//SOUL(25377, 566, 12, new int[] { 90, 95, 99 }, 18000, 20687);

		private int objectId, runeId, petChance, petId;
		private double experience;
		private int[] multiplier;

		public int getObjectId() {
			return objectId;
		}

		public int getRuneId() {
			return runeId;
		}

		public int getPetChance() {
			return petChance;
		}

		public int getPetId() {
			return petId;
		}
		
		public double getExperience() {
			return experience;
		}
		
		public int getLevelRequirement() {
			return multiplier[0];
		}

		RUNECRAFTING_DATA(int objectId, int runeId, double experience, int[] multiplier, int petChance, int petId) {
			this.objectId = objectId;
			this.runeId = runeId;
			this.experience = experience;
			this.multiplier = multiplier;
			this.petChance = petChance;
			this.petId = petId;
		}
	};
	
	public static void execute(Client player, int objectId) {
		for (RUNECRAFTING_DATA data : RUNECRAFTING_DATA.values()) {
			
			String name = data.name().toLowerCase().replaceAll("_", " ");
			if (data.getObjectId() == objectId) {
				if (!hasRequiredLevel(player, 20, data.getLevelRequirement(), "runecrafting", "craft these runes")) {
					return;
				}
				if (!player.getItems().playerHasItem(1436) && !player.getItems().playerHasItem(7936)) {
					player.sendMessage("You need some essence to craft runes!");
					return;
				}
				int multiplier = 1;
				for (int multiply = 0; multiply < data.multiplier.length; multiply++) {
					if (player.playerLevel[20] >= data.multiplier[multiply]) {
						multiplier = multiply + 1;
					}
				}
				int count = player.getItems().getItemAmount(7936) + player.getItems().getItemAmount(1436);
				int essence = player.getItems().getItemAmount(7936) + player.getItems().getItemAmount(1436);
				int multiply = essence *= multiplier;
				player.getItems().deleteItem2(7936, essence);
				player.getItems().deleteItem2(1436, essence);
				player.gfx100(186);
				player.animation(791);
				double percentOfXp = data.getExperience() * count * 2.5;
				player.getPA().addSkillXP(Config.RUNECRAFTING_EXPERIENCE + (player.getItems().isWearingItem(20008) ? percentOfXp : 0), 20);
				player.getItems().addItem(data.getRuneId(), multiply);
				player.sendMessage("You bind the temple's power into " + essence + " " + name + " runes.");
				player.getPA().requestUpdates();
				
					boolean hasGuardian = IntStream.range(20665, 20691).anyMatch(id -> player.getItems().getItemCount(id) > 0);
					boolean hasGuardianItem = IntStream.range(20665, 20691).anyMatch(id -> player.getItems().playerHasItem(id));
					if (hasGuardianItem) {
						IntStream.range(20665, 20691).forEach(id -> player.getItems().deleteItem(id, 1));
						player.getItems().addItem(data.getPetId(), 1);
					}
					if (!hasGuardian) {
					if (Misc.random(data.getPetChance()) == 18) {
						PlayerHandler.executeGlobalMessage("[<col=CC0000>News</col>] @cr20@ <col=255>" + player.playerName + "</col> successfully crafted a <col=CC0000>Rift guardian</col> pet!");
						player.getItems().addItemUnderAnyCircumstance(data.getPetId(), 1);
					}
				}
			}
		}
	}
}
