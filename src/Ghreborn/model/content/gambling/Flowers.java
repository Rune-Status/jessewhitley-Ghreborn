package Ghreborn.model.content.gambling;

import java.security.SecureRandom;

import Ghreborn.Server;
import Ghreborn.clip.Region;
import Ghreborn.model.players.Client;
import Ghreborn.world.objects.GlobalObject;

public class Flowers {

	private static final boolean DISABLED = false;

	private Client player;

	public Flowers(Client player) {
		SECURE_RANDOM = new SecureRandom();
		this.player = player;
	}

	private int flower_to_plant;

	private final int[] position = new int[3];

	private final SecureRandom SECURE_RANDOM;

	private static final int REGULAR_FLOWERS[] = { 2980, 2981, 2982, 2983, 2984, 2985, 2986 };

	private static final int RARE_FLOWERS[] = { 2980, 2981, 2982, 2983, 2984, 2985, 2986, 2987, 2988 };

	private int randomFlower() {
		return (SECURE_RANDOM.nextInt(100) >= 30 ? REGULAR_FLOWERS[(int) (Math.random() * REGULAR_FLOWERS.length)]
				: RARE_FLOWERS[(int) (Math.random() * RARE_FLOWERS.length)]);
	}

	public void plantMithrilSeed(int itemId) {
		switch (itemId) {
		case 299:
			if (System.currentTimeMillis() - player.plant < 1000 || DISABLED) {
				return;
			}
			if (!player.getItems().playerHasItem(299)) {
				return;
			}

			if (Region.getClipping(player.absX, player.absY, player.heightLevel) != 0 || Server.getGlobalObjects().anyExists(player.absX, player.absY, player.heightLevel)) {
				player.sendMessage("You can't plant a flower here.");
				return;
			}
			position[0] = player.absX;
			position[1] = player.absY;
			position[2] = player.heightLevel;

			if (Region.getClipping(player.getX(), player.getY(), player.heightLevel, -1, 0)) {
				flower_to_plant = randomFlower();
				System.out.println("ID: "+flower_to_plant);
				Server.getGlobalObjects().add(new GlobalObject(flower_to_plant, position[0], position[1], position[2], 0, 10, 15, -1));
			}
			if (Region.getClipping(player.getX() - 1, player.getY(), player.heightLevel, -1, 0)) 
				player.getPA().walkTo5(-1, 0);
			else if (Region.getClipping(player.getX() + 1, player.getY(), player.heightLevel, 1, 0)) 
				player.getPA().walkTo5(1, 0);
			else if (Region.getClipping(player.getX(), player.getY() - 1, player.heightLevel, 0, -1))
				player.getPA().walkTo5(0, -1);
			else if (Region.getClipping(player.getX(), player.getY() + 1, player.heightLevel, 0, 1))
				player.getPA().walkTo5(0, 1);

			player.face(position[0] + 1, position[1]);
			player.sendMessage("You have planted a flower.");
			player.getItems().deleteItem(299, player.getItems().getItemSlot(299), 1);
			player.plant = System.currentTimeMillis();
			break;
		}
	}
}

