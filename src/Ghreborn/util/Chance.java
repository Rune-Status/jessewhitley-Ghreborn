package Ghreborn.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The enumerated type whose elements represent the chance rates.
 *
 * @author lare96 <http://github.com/lare96>
 */
public enum Chance {
    ALWAYS(0, 1, 1) {
        @Override
        public boolean successful() {
            return true;
        }
    },
    COMMON(1, 2, 25), UNCOMMON(2, 51, 100), VERY_UNCOMMON(3, 77, 125), RARE(4, 60, 511), VERY_RARE(5, 60, 791), EXTREMELY_RARE(6, 45, 1000), LEGNEDLY_RARE(7, 45, 5000);
	/**
	 * The tier level of "rareness".
	 */
	private final int tier;

	/**
	 * The numerator of this chance.
	 */
	private final int numerator;

	/**
	 * The denominator of this chance.
	 */
	private final int denominator;

	/**
	 * Creates a new {@link Chance}.
	 *
	 * @param tier
	 *            the tier level of "rareness".
	 * @param numerator
	 *            the numerator of this chance.
	 * @param denominator
	 *            the denominator of this chance.
	 */
	private Chance(int tier, int numerator, int denominator) {
		this.tier = tier;
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/**
	 * Determines if this chance will be successful or not.
	 *
	 * @param random
	 *            the random number generator used to determine this.
	 * @return {@code true} if the drop was successful, {@code false} otherwise.
	 */
	public boolean successful() {
		return (ThreadLocalRandom.current().nextInt(numerator, denominator + 1)) % numerator == 0;
	}

	/**
	 * Debugs this chance {@code results} times (a minimum of 10 is
	 * recommended).
	 * 
	 * @param results
	 *            the amount of times to debug this chance.
	 */
	public void debug(int results) {
		int chance = 0;
		for (int amount = 0; amount < results; amount++) {
			do {
				chance++;
			} while (!successful());
			System.out.println("[" + this + "] Item dropped, took " + chance + " tries!");
			chance = 0;
		}
	}
	/**
	 * Debugs this chance {@code results} times (a minimum of 10 is
	 * recommended).
	 * 
	 * @param results
	 *            the amount of times to debug this chance.
	 */
	public void debugAverage(int results) {
		List<Integer> chances = new LinkedList<>();
		int chance = 0;
		for (int amount = 0; amount < results; amount++) {
			do {
				chance++;
			} while (!successful());
			chances.add(chance);
			chance = 0;
		}
		int size = chances.size();
		int average = 0;
		for (int $it : chances) {
			average += $it;
		}
		average /= size;
		System.out.println("[" + this + "] Took an average amount of " + average + " tries!");
	}

	/**
	 * Gets the tier level of "rareness".
	 * 
	 * @return the tier level.
	 */
	public final int getTier() {
		return tier;
	}

	/**
	 * Gets the numerator of this chance.
	 *
	 * @return the numerator.
	 */
	public final int getNumerator() {
		return numerator;
	}

	/**
	 * Gets the denominator of this chance.
	 *
	 * @return the denominator.
	 */
	public final int getDenominator() {
		return denominator;
	}
}