package Ghreborn.model.npcs.drop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Ghreborn.Server;
import Ghreborn.core.PlayerHandler;
import Ghreborn.model.Entity;
import Ghreborn.model.Location;
import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.GroundItem;
import Ghreborn.model.items.Item;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.logger.PlayerLogger;

/**
 * The npc drop system handles dropping items for npcs
 * 
 * @author Optimum
 */
public class NpcDropSystem 
{
	
	/**
	 * The directory of the npcdropsystem
	 */
	private static final String DIRECTORY = "./data/json/";
	
	/**
	 * The file to save to
	 */
	private static final String FILE = DIRECTORY + "npcdrops.json";
	
	Client c;
    /**
     * The file to save to
     */
    private static final String RARE_FILE = DIRECTORY + "rare_drop_table.json";
	
    /**
     * Gson object used saving and loading data
     */
    private static final Gson GSON = new Gson();
	
	/**
	 * NpcDropSystem instance object
	 */
	private static NpcDropSystem instance = new NpcDropSystem();
	
	/**
	 * Getter for the NpcDropSystem instance
	 * @return
	 */
	public static NpcDropSystem get()
	{
		return instance;
	}

	public RareDropTable rareDropTable = new RareDropTable(null, null);
	
	/**
	 * Testing method to put loot directly in the bank
	 * @param player - the player testing
	 * @param amount - the amount ot kill
	 * @param id - the id killing
	 */
	public void testBankDrops(Client player, int amount, int id)
	{
		for(NpcDropData drops : npcDropData)
		{
			for(int npcs : drops.getNpcList())
			{
				if(npcs == id)
				{
					for(int i = 0; i < amount; i++)
					{
						List<GameItem> items = drops.generateDropList();
						for(GameItem item : items)
						{
							//player.getItems().addItemToBank1(items, amount);
						}
					}
					
				}
			}
		}
		
	}
	
	public void saveDrops()
	{
		try 
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(FILE));
			out.write(toJson());
			out.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void loadRareDrops()
	{
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(RARE_FILE));
			String data = "";
			data = in.readLine();
			fromJsonRare(data);
			in.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public void loadDrops()
	{
		try 
		{
			BufferedReader in = new BufferedReader(new FileReader(FILE));
			String data = "";
			data = in.readLine();
			fromJson(data);
			in.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * loads the npc drop data
	 */
	public void fromJson(String json)
	{
		Type listType = new TypeToken<ArrayList<NpcDropData>>() { }.getType();
		npcDropData = GSON.fromJson(json, listType);
	}
	
	public void fromJsonRare(String json)
	{
		rareDropTable = GSON.fromJson(json, RareDropTable.class);
	}
	
	/**
	 * Private Constructor
	 */
	private NpcDropSystem() {}
	
	/**
	 * Formats the data to json
	 * @return
	 */
	public String toJson()
	{
		return GSON.toJson(npcDropData);
	}
	
	/**
	 * The list of all npc drops
	 */
	private List<NpcDropData> npcDropData = new ArrayList<>();

	
	/**
	 * drops items for a player from an npc
	 * 
	 * @param entity - the player
	 * @param npc - the npc
	 */
	public void drop(Entity entity, NPC npc)
	{
		/*if(entity.isNpc()) { return; }*/
		
		if(npcDropData == null)
		{
			return;
		}
		
		for(NpcDropData drops : npcDropData)
		{
			for(int npcs : drops.getNpcList())
			{
				if(npcs == npc.index)
				{
					List<GameItem> items = drops.generateDropList();
					
					Position location = null;
					
					for(GameItem item : items)
					{
						if (!entity.isNpc()) 
						{
/*							if (item.getDefinition().getGeneralPrice() >= 1_000_000) 
							{
								System.out.println("Iem: " + item.getDefinition().getName() + " price: " + item.getDefinition().getGeneralPrice());
								//PlayerLogger.DROP_LOGGER.log(entity.getPlayer().getUsername(), String.format("%s has recieved %s %s from %s.", Utility.formatPlayerName(entity.getPlayer().getUsername()), item.getAmount(), item.getDefinition().getName(), Utility.formatPlayerName(npc.getDefinition().getName())));
								//AchievementHandler.activateAchievement(entity.getPlayer(), AchievementList.OBTAIN_10_RARE_DROPS, 1);
								//World.sendGlobalMessage("<col=1F8C26>" + entity.getPlayer().getUsername() + " recieved a drop: " + Utility.format(item.getAmount()) + " x " + item.getDefinition().getName() + ".");
							} 
							else 
							{
								//c.getPA().messageall("<col=1F8C26>" + c.getUsername() + " recieved a drop: " + Utility.format(item.getAmount()) + " x " + item.getDefinition().getName() + ".", npc.getLocation());					
							}*/
						}
						if(npc.index == 2042 || npc.index == 2043 || npc.index == 2044)
						{
							location = entity.getPosition();
						}
						else { location = npc.getPosition(); }
						Server.itemHandler.createGroundItem(c, item.getId(), npc.absX, npc.absY,
								c.heightLevel, item.getAmount(), c.index);
						//GroundItem.add(item, location, (entity == null) 
								//|| (entity.isNpc()) ? null : World.getPlayers()[entity.getIndex()],
										//PlayerHandler.getPlayers()[entity.getIndex()].ironPlayer() ? World.getPlayers()[entity.getIndex()] : null);
					}
				}
			}
		}
	}

	/**
	 * Gets the {@linkplain npcDropData}
	 * @return - the {@linkplain npcDropData}
	 */
	public List<NpcDropData> getNpcDropData() 
	{
		return npcDropData;
	}

}//Optimums code
