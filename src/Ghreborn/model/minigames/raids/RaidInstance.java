package Ghreborn.model.minigames.raids;

import Ghreborn.model.minigames.rfd.DisposeTypes;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.boss.instances.SingleInstancedArea;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;

public class RaidInstance extends SingleInstancedArea {

	public RaidInstance(Client player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		end(DisposeTypes.INCOMPLETE);
	}
	
	public final void end(DisposeTypes dispose) {
		if (player == null) {
			return;
		}
		
		NPCHandler.kill(7604, height);
		NPCHandler.kill(7605, height);
		
		if (dispose == DisposeTypes.COMPLETE) {
			
		} else if (dispose == DisposeTypes.INCOMPLETE) {
			
		}
	}
	
	public int getHeight() {
		return height;
	}

}
