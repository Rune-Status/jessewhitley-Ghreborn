package Ghreborn.model.content.donatorboxs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.ItemAssistant;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Rights;
import Ghreborn.util.Misc;

public class DonatorBox extends CycleEvent {

	public static final int DONATOR_BOX = 16542;
	
	/**
	 * A map containing a List of {@link GameItem}'s that contain items relevant to their rarity.
	 */
	private static Map<Rarity, List<GameItem>> items = new HashMap<>();

	/**
	 * Stores an array of items into each map with the corresponding rarity to the list
	 */
	static {
		items.put(Rarity.COMMON, //
			Arrays.asList(
				new GameItem(23212), 
				new GameItem(23213),
				new GameItem(23214),
				new GameItem(23215), 
				new GameItem(23216), 
				new GameItem(23217),
				new GameItem(23218), 
				new GameItem(23219), 
				new GameItem(23220),
				new GameItem(23221),
				new GameItem(23222),
				new GameItem(23223), 
				new GameItem(23224),
				new GameItem(23225), 
				new GameItem(23226),
				new GameItem(23227), 
				new GameItem(23228), 
				new GameItem(23229),
				new GameItem(23230), 
				new GameItem(23231), 
				new GameItem(23232), 
				new GameItem(23233), 
				new GameItem(23234), 
				new GameItem(23235), 
				new GameItem(23236), 
				new GameItem(23237), 
				new GameItem(23238), 
				new GameItem(23239), 
				new GameItem(23240),
				new GameItem(23241), 
				new GameItem(23242), 
				new GameItem(23243), 
				new GameItem(23244), 
				new GameItem(23245), 
				new GameItem(23246), 
				new GameItem(23247), 
				new GameItem(23248),
				new GameItem(23249), 
				new GameItem(23250), 
				new GameItem(23251), 
				new GameItem(23252), 
				new GameItem(23253),
				new GameItem(23254), 
				new GameItem(23255),
				new GameItem(23256),
				new GameItem(23257),
				new GameItem(23258), 
				new GameItem(23259), 
				new GameItem(23260), 
				new GameItem(23261), 
				new GameItem(23262), 
				new GameItem(23263), 
				new GameItem(23264), 
				new GameItem(23265), 
				new GameItem(23266), 
				new GameItem(23267), 
				new GameItem(23268), 
				new GameItem(23269), 
				new GameItem(23270), 
				new GameItem(23271), 
				new GameItem(23272), 
				new GameItem(23273), 
				new GameItem(23274), 
				new GameItem(23275), 
				new GameItem(23276), 
				new GameItem(23277), 
				new GameItem(23278),
				new GameItem(23279),
				new GameItem(23280),
				new GameItem(23281),
				new GameItem(23282),
				new GameItem(23283),
				new GameItem(23284),
				new GameItem(23285),
				new GameItem(23286),
				new GameItem(23287),
				new GameItem(23288),
				new GameItem(23289),
				new GameItem(23290),
				new GameItem(23291),
				new GameItem(23292), 
				new GameItem(23293),
				new GameItem(23294), 
				new GameItem(23295), 
				new GameItem(23296),
				new GameItem(23297),
				new GameItem(23298),
				new GameItem(23299), 
				new GameItem(23300), 
				new GameItem(23301), 
				new GameItem(23302), 
				new GameItem(23303), 
				new GameItem(23304), 
				new GameItem(23305), 
				new GameItem(23306), 
				new GameItem(23307),
				new GameItem(23308), 
				new GameItem(23309), 
				new GameItem(23310),
				new GameItem(23311),
				new GameItem(23312),
				new GameItem(23313),
				new GameItem(23314),
				new GameItem(23315),
				new GameItem(23316),
				new GameItem(23317),
				new GameItem(23318),
				new GameItem(23319),
				new GameItem(23320),
				new GameItem(23321),
				new GameItem(23322),
				new GameItem(23323),
				new GameItem(23324),
				new GameItem(23325),
				new GameItem(23326), 
				new GameItem(23327),
				new GameItem(23328),
				new GameItem(23329),
				new GameItem(23330),
				new GameItem(23331),
				new GameItem(23332),
				new GameItem(23334),
				new GameItem(23335),
				new GameItem(23336))
		);
		
	items.put(Rarity.UNCOMMON,
			Arrays.asList(
					new GameItem(23348),
					new GameItem(23349),
					new GameItem(23350),
					new GameItem(23351),
					new GameItem(23352),
					new GameItem(23353),
					new GameItem(23354),
					new GameItem(23355),
					new GameItem(23356),
					new GameItem(23357),
					new GameItem(23358))
	);
		  
		items.put(Rarity.RARE,
				Arrays.asList(
						new GameItem(23362), 
						new GameItem(23363),
						new GameItem(23364),
						new GameItem(23365),
						new GameItem(23366), 
						new GameItem(23367), 
						new GameItem(23368), 
						new GameItem(23369), 
						new GameItem(23370), 
						new GameItem(23371), 
						new GameItem(23372), 
						new GameItem(23373), 
						new	GameItem(23374), 
						new GameItem(23375),
						new GameItem(23376),
						new GameItem(23377), 
						new GameItem(23378)));
	}

	/**
	 * The player object that will be triggering this event
	 */
	private Client player;

	/**
	 * Constructs a new myster box to handle item receiving for this player and this player alone
	 * 
	 * @param player the player
	 */
	public DonatorBox(Client player) {
		this.player = player;
	}

	/**
	 * Opens a mystery box if possible, and ultimately triggers and event, if possible.
	 * 
	 * @param player the player triggering the evnet
	 */
	public void open() {
		if (System.currentTimeMillis() - player.lastMysteryBox < 150 * 4) {
			return;
		}
		if (player.getItems().freeSlots() < 3) {
			player.sendMessage("You need atleast 3 free slots to open a Donator Box.");
			return;
		}
		if (!player.getItems().playerHasItem(DONATOR_BOX)) {
			player.sendMessage("You need a Normal Donator Box for this");
			return;
		}
		player.getItems().deleteItem(DONATOR_BOX, 1);
		player.lastMysteryBox = System.currentTimeMillis();
		CycleEventHandler.getSingleton().stopEvents(this);
		CycleEventHandler.getSingleton().addEvent(this, this, 2);
	}

	/**
	 * Executes the event for receiving the mystery box
	 */
	@Override
	public void execute(CycleEventContainer container) {
		if (player.disconnected || Objects.isNull(player)) {
			container.stop();
			return;
		}
		int random = Misc.random(50);
		List<GameItem> itemList = random < 15 ? items.get(Rarity.COMMON) : random >= 15 && random <= 38 ? items.get(Rarity.UNCOMMON) : items.get(Rarity.RARE);
		GameItem item = Misc.getRandomItem(itemList);
		GameItem itemDouble = Misc.getRandomItem(itemList);
		GameItem itemDouble2 = Misc.getRandomItem(itemList);
		
		player.setRights(Rights.DONATOR);
			//player.getItems().addItem(995, coins);
		player.DonatorPoints += 5000;
			player.getItems().addItem(item.getId(), item.getAmount());
			player.getItems().addItem(itemDouble.getId(), itemDouble.getAmount());
			player.getItems().addItem(itemDouble2.getId(), itemDouble2.getAmount());
			player.sendMessage("You receive <col=255>" + item.getAmount() + " x " + ItemAssistant.getItemName(item.getId()) + "</col>.");
			player.sendMessage("You receive <col=255>" + itemDouble.getAmount() + " x " + ItemAssistant.getItemName(itemDouble.getId()) + "</col>.");
			player.sendMessage("You receive <col=255>" + itemDouble2.getAmount() + " x " + ItemAssistant.getItemName(itemDouble2.getId()) + "</col>.");
			if(player.getRights().isPlayer()){
			player.logout();
			}
		container.stop();
	}
	/**
	 * Represents the rarity of a certain list of items
	 */
	enum Rarity {
		UNCOMMON, COMMON, RARE
	}
}
