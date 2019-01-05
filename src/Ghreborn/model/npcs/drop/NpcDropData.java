package Ghreborn.model.npcs.drop;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Ghreborn.model.items.GameItem;
import Ghreborn.model.items.Item;
/**
 * The npcs drop data
 * @author Optimum
 */
public class NpcDropData 
{
	/**
	 * Random object to generate a random amount
	 */
	private static final Random random = new SecureRandom();
	
	/**
	 * The npc's id
	 */
	private List<Integer> npcList;
	
	/**
	 * Checks if the monster is applicable for the rare drop table
	 */
	private boolean rareDropTableAccess;
	
	/**
	 * The chance of the rare drop table dropping
	 */
	private float rareDropTableChance;
	
	
	/**
	 * Holds the drop chances for each dropType,
	 * this will be using the normalDrops list
	 */
	private HashMap<DropType, Float> dropChances;
	
	/**
     * A bunch of normal drops
     */
    private List<NpcDrop> drops;


	/**
	 * NpcDropData constructor
	 */
	public NpcDropData(List<Integer> npcList, boolean rareDropTableAccess,
            float rareDropTableChance, HashMap<DropType, Float> dropChances, List<NpcDrop> drops) 
	{
		this.npcList = npcList;
		this.rareDropTableAccess = rareDropTableAccess;
		this.rareDropTableChance = rareDropTableChance;
		this.drops = drops;
		this.dropChances = dropChances;
		
	}
	
	
	/**
	 * Gets a random item for a drop type
	 * @param dropType - the drop type
	 * @return the new item
	 */
	private GameItem getRandomFor(DropType dropType)
	{
		GameItem item = null;
		
		while(item == null)
		{
			NpcDrop selected = drops.get(random.nextInt(drops.size()));
			
			if(selected.getDropType() == dropType)
			{
				item = selected.getRandomAmount();
			}
		}
		
		return item;
	}
	
	/**
	 * Generates a list of items to drop for the npc
	 * @return - the list of items
	 */
	public List<GameItem> generateDropList()
	{
		List<GameItem> items = new ArrayList<>();
		GameItem itemToDrop = null;

		for(NpcDrop always : drops) //Always drops
		{
			if(always.getDropType() == DropType.ALWAYS)
				items.add(always.getRandomAmount());
		}
		
        if(rareDropTableAccess)
        {
            if(PercentageRoll.roll(rareDropTableChance))
            {
            	itemToDrop = NpcDropSystem.get().rareDropTable.generateDropList();
            }
        }
		
		for(NpcDrop specials : drops) //Special Table drops
		{
			if(specials.getDropType() == DropType.SPECIAL)
			{
				if(specials.roll())
				{
					items.add(specials.getRandomAmount());
					break;	
				}
			}
			
		}
		
		if (itemToDrop == null)
        {
            if (contains(DropType.VERY_RARE))
            {
                if (PercentageRoll.roll(getChanceFor(DropType.VERY_RARE)))
                    itemToDrop = getRandomFor(DropType.VERY_RARE);
            }
        }
        if (itemToDrop == null)
        {
            if (contains(DropType.RARE))
            {
                if (PercentageRoll.roll(getChanceFor(DropType.RARE)))
                    itemToDrop = getRandomFor(DropType.RARE);
            }
        }
        if (itemToDrop == null)
        {
            if (contains(DropType.UNCOMMON))
            {
                if (PercentageRoll.roll(getChanceFor(DropType.UNCOMMON)))
                    itemToDrop = getRandomFor(DropType.UNCOMMON);
            }
        }
        if (itemToDrop == null)
        {
            if (contains(DropType.COMMON))
            {
                if (PercentageRoll.roll(getChanceFor(DropType.COMMON)))
                    itemToDrop = getRandomFor(DropType.COMMON);
            }
        }
		
		
		if(itemToDrop != null) items.add(itemToDrop);
		
		return items;
	}
	
	private float getChanceFor(DropType d)
	{
		
		for(Map.Entry<DropType, Float> entry : dropChances.entrySet())
		{
			if(entry.getKey() == d)
				return entry.getValue();
		}
		return 0;
	}

	private boolean contains(DropType key)
	{
		for(NpcDrop d : drops)
		{
			if(d.getDropType() == key)
				return true;
		}
		return false;
	}


	/**
	 * @return the npcList
	 */
	public List<Integer> getNpcList() 
	{
		return npcList;
	}

	/**
	 * @param npcList the npcList to set
	 */
	public void setNpcList(List<Integer> npcList) 
	{
		this.npcList = npcList;
	}

	/**
	 * @return the rareDropTableAccess
	 */
	public boolean isRareDropTableAccess() 
	{
		return rareDropTableAccess;
	}

	/**
	 * @param rareDropTableAccess the rareDropTableAccess to set
	 */
	public void setRareDropTableAccess(boolean rareDropTableAccess) 
	{
		this.rareDropTableAccess = rareDropTableAccess;
	}

	/**
	 * @return the rareDropTableChance
	 */
	public float getRareDropTableChance() 
	{
		return rareDropTableChance;
	}

	/**
	 * @param rareDropTableChance the rareDropTableChance to set
	 */
	public void setRareDropTableChance(float rareDropTableChance) 
	{
		this.rareDropTableChance = rareDropTableChance;
	}

	/**
	 * @return the dropChances
	 */
	public HashMap<DropType, Float> getDropChances() 
	{
		return dropChances;
	}

	/**
	 * @param dropChances the dropChances to set
	 */
	public void setDropChances(HashMap<DropType, Float> dropChances) 
	{
		this.dropChances = dropChances;
	}

	/**
	 * @return the normalDrops
	 */
	public List<NpcDrop> getDrops() 
	{
		return drops;
	}

	/**
	 * @param normalDrops the normalDrops to set
	 */
	public void setNormalDrops(List<NpcDrop> drops) 
	{
		this.drops = drops;
	}

	
}//Optimums code