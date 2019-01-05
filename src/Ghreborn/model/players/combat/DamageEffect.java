package Ghreborn.model.players.combat;

import Ghreborn.model.npcs.NPC;
import Ghreborn.model.players.Client;

/**
 * 
 * @author Jason MacKeigan
 * @date Nov 24, 2014, 9:47:29 PM
 */
public interface DamageEffect {

	/**
	 * Executes some effect during the damage step of a player attack
	 * @param attacker	the attacking player in combat
	 * @param defender	the defending player in combat
	 * @param damage	the damage dealt during this step
	 */
	public void execute(Client attacker, Client defender, Damage damage);
	
	/**
	 * Executes some effect during the damage step of a player attack
	 * @param attacker	the attacking player in combat
	 * @param defender	the defending npc in combat
	 * @param damage	the damage dealt during this step
	 */
	public void execute(Client attacker, NPC defender, Damage damage);
	
	/**
	 * Determines if the event is executable by the operator
	 * @param operator	the player executing the effect
	 * @return	true if it can be executed based on some operation, otherwise false 
	 */
	public boolean isExecutable(Client operator);
	
}
