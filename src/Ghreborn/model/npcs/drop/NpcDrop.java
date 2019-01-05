package Ghreborn.model.npcs.drop;

import java.security.SecureRandom;
import java.util.Random;

import Ghreborn.model.items.GameItem;


/**
 * An Npc Drop
 * @author Optimum
 */
public class NpcDrop 
{
	/**
	 * Random object to generate a random amount
	 */
	protected static final Random random = new SecureRandom();
	
	/**
	 * The item bein dropped
	 */
	protected int item;
	
	public int getItem()
	{
		return item;
	}
	
	/**
	 * The minimum that can be dropped at once
	 */
	protected int minimumAmount;
	
	/**
	 * The maximum that can be dropped at once
	 */
	protected int maximumAmount;
	
	/**
	 * The drop type
	 */
	protected DropType dropType;

	/**
	 * @param item the item to set
	 */
	public void setItem(int item) 
	{
		this.item = item;
	}

	/**
	 * @return the minimumAmount
	 */
	public int getMinimumAmount() 
	{
		return minimumAmount;
	}

	/**
	 * @param minimumAmount the minimumAmount to set
	 */
	public void setMinimumAmount(int minimumAmount) 
	{
		this.minimumAmount = minimumAmount;
	}
	
    /**
     * The chance of falling
     */
    private float chance;

	/**
	 * NpcDrop Constructor
	 * @param item - the item
	 * @param minimumAmount - the minimumAmount
	 * @param maximumAmount - the maximumAmount
	 * @param dropType - the drop type
	 */
	public NpcDrop(int item, int minimumAmount, int maximumAmount, DropType dropType, float chance) 
	{
		this.item = item;
		this.minimumAmount = minimumAmount;
		this.maximumAmount = maximumAmount;
		this.dropType = dropType;
		this.chance = chance;
	}
	
	/**
     * Attempts a npc special drop
     * @param items
     */
    public boolean roll()
    {
        if (PercentageRoll.roll(chance)) return true;
        return false;
    }

    /**
     * @return the chance
     */
    public float getChance()
    {
        return chance;
    }

    /**
     * @param chance the chance to set
     */
    public void setChance(float chance)
    {
        this.chance = chance;
    }

	/**
	 * @return the maximumAmount
	 */
	public int getMaximumAmount() 
	{
		return maximumAmount;
	}

	/**
	 * Gets an item and sets it's amount to a random
	 * within the max and min
	 * @return - the new item object
	 */
	public GameItem getRandomAmount()
	{
		GameItem item = new GameItem(this.item);
		
		if(maximumAmount < minimumAmount ||
				maximumAmount == minimumAmount) 
		{
			item.setAmount(minimumAmount);
			return item;
		}
		
		int delta = random.nextInt(maximumAmount - minimumAmount);
		if(delta < 0) delta = 0;
		
		item.setAmount(minimumAmount + delta);
		return item;
	}
	
	/**
	 * @param maximumAmount the maximumAmount to set
	 */
	public void setMaximumAmount(int maximumAmount) 
	{
		this.maximumAmount = maximumAmount;
	}

	/**
	 * @return the dropType
	 */
	public DropType getDropType() 
	{
		return dropType;
	}

	/**
	 * @param dropType the dropType to set
	 */
	public void setDropType(DropType dropType) 
	{
		this.dropType = dropType;
	}

}//Optimums code

