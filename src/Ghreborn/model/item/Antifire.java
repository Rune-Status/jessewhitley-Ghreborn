package Ghreborn.model.item;

import Ghreborn.model.players.Client;


/**
 * @author JoesMe
 * 
 */

/*
 * Copyright (c) 2013  -  JoesMe
 *
 * This software is provided as is with no warranty, or guarantee of revision.
 *
 * The GNU General Public License applies to this free, distributed software,
 * (for more info please see <http://www.gnu.org/licenses/>).
 */

public class Antifire {
	
	public static int protection(final Client c) {
		int bonus = 0;
		
		if (c.getAntifireTime() > 0) {
			bonus += 3;
		} 
		if (c.playerEquipment[c.playerShield] == 11283) {
			bonus += 4;
		}	
		if (c.playerEquipment[c.playerShield] == 1540) {
			bonus += 3;
		}
		if (c.prayerActive[17]) {
			bonus += 1;
		}
			
		return bonus * 10;
	}
	

}