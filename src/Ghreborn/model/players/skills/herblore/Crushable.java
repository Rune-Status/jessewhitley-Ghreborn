package Ghreborn.model.players.skills.herblore;


import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import Ghreborn.model.items.ItemDefinition;
import Ghreborn.model.players.Client;


/**
 * Items which can be crushed with a Pestle and Mortar
 * 
 * @author Emiel
 *
 */
public enum Crushable {

	UNICORN_HORN(237, 235), 
	CHOCOLATE_BAR(1973, 1975),
	KEBBIT_TEETH(10109, 10111),
	GORAK_CLAW(9016, 9018),
	DESERT_GOAT_HORN(9735, 9738),
	MUD_RUNE(4698, 9594),
	ASH(592, 8865),
	SEAWEED1(401, 6683),
	SEAWEED2(403, 6683),
	BATBONES(530, 2391),
	CHARCOAL(973, 704),
	BLUE_DRAGON_SCALE(243, 241);

	private int original;
	private int result;

	public static final int PESTLE = 233;

	Crushable(int original, int result) {
		this.original = original;
		this.result = result;
	}

	private static final Set<Crushable> VALUES = Collections.unmodifiableSet(EnumSet.allOf(Crushable.class));

	public static Optional<Integer> getResult(int original) {
		return VALUES.stream().filter(c -> c.original == original).map(c -> c.result).findAny();
	}

	public static boolean crushIngredient(Client c, int item1, int item2) {
		if (item1 != PESTLE && item2 != PESTLE) {
			return false;
		}
		int ingredient;
		try {
			ingredient = getOther(PESTLE, item1, item2);
		} catch (IllegalArgumentException e) {
			return false;
		}
		Optional<Integer> result = getResult(ingredient);
		if (result.isPresent()) {
			c.animation(364);
			c.getItems().deleteItem(ingredient, 1);
			c.getItems().addItem(result.get(), 1);
			c.sendMessage(
					"You grind down the " + ItemDefinition.forId(ingredient).getName().toLowerCase() + " to " + ItemDefinition.forId(result.get()).getName().toLowerCase() + ".");
			return true;
		}
		return false;
	}

	/**
	 * Find the integer which does not match the notThis integer.
	 * 
	 * @param notThis The integer which is not to be returned.
	 * @param i1 Integer which may or may not equal notThis.
	 * @param i2 Integer which may or may not equal notThis.
	 * @return The integer which does
	 * @throws IllegalArgumentException Thrown in case neither of the 2 integers matches the notThis integer.
	 */
	private static int getOther(int notThis, int i1, int i2) throws IllegalArgumentException {
		if (notThis == i1) {
			return i2;
		} else if (notThis == i2) {
			return i1;
		} else {
			throw new IllegalArgumentException();
		}
	}

}

