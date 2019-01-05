package Ghreborn.model.objects;

import Ghreborn.Server;
import Ghreborn.definitions.ObjectDef;
import Ghreborn.model.players.Client;
import Ghreborn.world.objects.GlobalObject;

public class SpadeInGround {
	
	public static void pullSpadeFromGround(Client client, int x, int y) {
		if (client.getItems().freeSlots() <= 0) {
			client.sendMessage(
					"Not enough space in your inventory.");
			return;
		}
		client.animation(832);
		client.getItems().addItem(952, 1);
		Server.globalObjects.add(new GlobalObject(10626, x, y, client.heightLevel, 2, 10, 100, 9662));
	}
}
