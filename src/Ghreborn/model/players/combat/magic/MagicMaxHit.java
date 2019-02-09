package Ghreborn.model.players.combat.magic;

import Ghreborn.model.players.Client;

public class MagicMaxHit {

	public static int mageAttack(Client c) {
		int attackLevel = c.playerLevel[6];
		if (c.fullVoidMage()) {
			attackLevel += c.getLevelForXP(c.playerXP[6]) * 0.2;
		}
		if (c.prayerActive[4]) {
			attackLevel *= 1.05;
		} else if (c.prayerActive[12]) {
			attackLevel *= 1.10;
		} else if (c.prayerActive[20]) {
			attackLevel *= 1.15;
		}
		return (int) (attackLevel + (c.playerBonus[3] * 2));
	}

	public static int mageDefefence(Client c) {
		int defenceLevel = c.playerLevel[1]/2 + c.playerLevel[6]/2;
		if (c.prayerActive[0]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
		} else if (c.prayerActive[3]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
		} else if (c.prayerActive[9]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
		} else if (c.prayerActive[18]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
		} else if (c.prayerActive[19]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
		}
		return (int) (defenceLevel + c.playerBonus[8] + (c.playerBonus[8] / 3));
	}

	public static int magiMaxHit(Client c) {
		double damage = MagicData.MAGIC_SPELLS[c.oldSpellId][6];
		double damageMultiplier = 1;

		if (c.playerLevel[c.playerMagic] > c.getLevelForXP(c.playerXP[6]) && c.getLevelForXP(c.playerXP[6]) >= 95) {
			damageMultiplier += .03 * (c.playerLevel[c.playerMagic] - 99);
		}

		switch (c.playerEquipment[c.playerWeapon]) {
			case 4675: 
			case 6914:
				damageMultiplier += .10;
				break;
		}
		switch (MagicData.MAGIC_SPELLS[c.oldSpellId][0]) {
			case 12037:
				damage += c.playerLevel[6]/10;
				break;
		}

		damage *= damageMultiplier;

		//c.sendMessage("Final damage: " + damage + " Damage Multiplier: " + damageMultiplier);
		return (int)damage;
	}
}