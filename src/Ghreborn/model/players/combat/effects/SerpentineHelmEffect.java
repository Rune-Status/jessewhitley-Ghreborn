package Ghreborn.model.players.combat.effects;

import Ghreborn.model.npcs.NPC;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.combat.Damage;
import Ghreborn.model.players.combat.DamageEffect;
import Ghreborn.util.Misc;

public class SerpentineHelmEffect implements DamageEffect {

	@Override
	public void execute(Client attacker, Client defender, Damage damage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void execute(Client attacker, NPC defender, Damage damage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isExecutable(Client operator) {
		return (operator.getItems().isWearingItem(12931) || operator.getItems().isWearingItem(13199) || operator.getItems().isWearingItem(13197)) && Misc.random(5) == 1;
	}

}
