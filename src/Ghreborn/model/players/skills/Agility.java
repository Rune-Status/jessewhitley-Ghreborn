package Ghreborn.model.players.skills;

import Ghreborn.model.players.Client;

/**
 * Agility.java
 * 
 * @author Sanity
 * 
 **/

public class Agility {

	private Client c;

	public Agility(Client c) {
		this.setC(c);
	}

	public void handleGnomeCourse(int object, int objectX, int objectY) {
		/*
		 * if (object == 2286 && objectY > c.getY()) { //net
		 * c.animation(844); c.getPA().movePlayer(c.getX(), c.getY() + 2,
		 * 0); gnomeCourse[4] = true; c.getPA().addSkillXP(EXP[5] *
		 * Config.AGILITY_EXPERIENCE, c.playerAgility); } else if (object == 154
		 * || object == 4058) { //tube c.animation(844);
		 * c.getPA().walkTo(0,7); gnomeCourse[5] = true; if (isDoneGnome())
		 * giveReward(1); c.getPA().addSkillXP(EXP[6] *
		 * Config.AGILITY_EXPERIENCE, c.playerAgility); } else if (object ==
		 * 2295) { c.playerSE = 0x328;//walk c.playerSEW = 762;//walk
		 * c.isRunning = false; if (objectX > c.getX()) c.getPA().walkTo(1,0);
		 * else if (objectX < c.getX()) c.getPA().walkTo(-1,0);
		 * c.getPA().walkTo(0,-7); gnomeCourse[0] = true;
		 * c.getPA().addSkillXP(EXP[0] * Config.AGILITY_EXPERIENCE,
		 * c.playerAgility); } else if (object == 2285 && c.heightLevel == 0) {
		 * c.animation(828); c.getPA().movePlayer(c.getX(), c.getY()-2, 1);
		 * gnomeCourse[1] = true; c.getPA().addSkillXP(EXP[1] *
		 * Config.AGILITY_EXPERIENCE, c.playerAgility); } else if (object ==
		 * 2313 && c.heightLevel == 1) { c.animation(828);
		 * c.getPA().movePlayer(c.getX(), c.getY()-2, 2); gnomeCourse[2] = true;
		 * c.getPA().addSkillXP(EXP[2] * Config.AGILITY_EXPERIENCE,
		 * c.playerAgility); } else if (object == 2312) { c.getPA().walkTo(6,0);
		 * c.getPA().addSkillXP(EXP[3] * Config.AGILITY_EXPERIENCE,
		 * c.playerAgility); } else if (object == 2314) {
		 * c.getPA().movePlayer(c.getX(), c.getY(), 0); gnomeCourse[3] = true;
		 * c.getPA().addSkillXP(EXP[4] * Config.AGILITY_EXPERIENCE,
		 * c.playerAgility); }
		 */
	}

	public Client getC() {
		return c;
	}

	public void setC(Client c) {
		this.c = c;
	}
}