package Ghreborn.model.npcs.drop;

import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Percentage roll based system
 * 
 * used for npc drops
 * @author Optimum
 */
public final class PercentageRoll 
{
	
	/**
	 * Secure random to generate a random roll
	 */
	private static final Random RANDOM = new SecureRandom();
	
	/**
	 * Illegal Argument Exception to throw when {@link percentage} 
	 * is set to above 100 or below 0
	 */
	private static final IllegalArgumentException ILLEGAL_ARGUMENT = 
			new IllegalArgumentException("Percentage value must be below or equal to 100 and above 0");
	
	/**
	 * Decimal formatter to format the 
	 * random percentage roll to 6 decimal places
	 */
	private static final DecimalFormat DF = new DecimalFormat("#.########");
	
	
	/**
	 * Attempts to roll with a percentage
	 * 
	 * @param percentage - the percentage
	 * 
	 * @return true if the roll is successful
	 * false if the roll fails
	 * 
	 * @throws IllegalArgumentException 
	 * if the percentage is above 100 or below 0
	 */
	public static boolean roll(float percentage)
	{
		if(percentage > 100 || percentage < 0) throw ILLEGAL_ARGUMENT;
		float roll = Float.parseFloat(DF.format(RANDOM.nextDouble() * 100));
		return percentage >= roll;
	}
	
}//Optimums Code
