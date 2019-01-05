package Ghreborn.model.npcs.boss.abyssalsire;

import Ghreborn.model.content.InstancedArea;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.players.Client;

public class AbyssalSire extends InstancedArea {

	private NPC abyssalSire;

	public AbyssalSire(Client player) {
		super(player, 0, AbyssalSireConstants.BOUNDARY);
	}

	@Override
	public void onStart() {
		abyssalSire = spawnNpc(AbyssalSireConstants.SLEEPING_NPC_ID, AbyssalSireConstants.INITIAL_LOCATION, 0, 400, 1, 1, 1, false, false);
		getPlayer().getPA().movePlayer(getPlayer().getX(), getPlayer().getY(), getInstanceHeight());
		setState(AbyssalSireStates.SLEEPING);
	}

	@Override
	public void onProcess() {
		AbyssalSireStates state = getState();

		switch (state) {
		case SLEEPING:
			System.out.println(abyssalSire.HP + " " + abyssalSire.maximumHealth);
			if (abyssalSire.HP < abyssalSire.maximumHealth * 0.9) {
				abyssalSire.requestTransform(AbyssalSireConstants.MELEE_NPC_ID);
				abyssalSire.animation(AbyssalSireConstants.RISING_ANIMATION);
				abyssalSire.underAttack = true;
				abyssalSire.killerId = getPlayer().getId();
				setState(AbyssalSireStates.ATTACKING);
			}
			break;
		case ATTACKING:
			break;
		case CHARGING:
			break;
		case CHARGING_PORTAL:
			break;
		}
	}

	@Override
	public void onDestruct() {

	}

}
