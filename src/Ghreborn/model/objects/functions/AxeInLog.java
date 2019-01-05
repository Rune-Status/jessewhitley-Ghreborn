package Ghreborn.model.objects.functions;

import Ghreborn.Server;
import Ghreborn.model.players.Client;
import Ghreborn.world.objects.GlobalObject;

public class AxeInLog {

	public static void pullAxeFromLog(Client client, int x, int y) {
		if (client.getItems().freeSlots() <= 0) {
			client.sendMessage(
					"Not enough space in your inventory.");
			return;
		}
		client.animation(832);
		client.getItems().addItem(1351, 1);
		client.sendMessage(
				"You take the axe from the log.");
		Server.globalObjects.add(new GlobalObject(5582, x, y, client.heightLevel, 2, 10, 100, 5581));
	}
}
