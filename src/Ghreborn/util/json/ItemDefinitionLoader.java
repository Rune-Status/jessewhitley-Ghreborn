package Ghreborn.util.json;

import java.util.Objects;

import Ghreborn.model.items.ItemDefinition;
import Ghreborn.util.JsonLoader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;


/**
 * The {@link JsonLoader} implementation that loads all item definitions.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemDefinitionLoader extends JsonLoader {

	/**
	 * Creates a new {@link ItemDefinitionLoader}.
	 */
	public ItemDefinitionLoader() {
		super("./Data/json/item_definitions.json");
	}

	@Override
	public void load(JsonObject reader, Gson builder) {
		int index = reader.get("id").getAsInt();
		String name = Objects.requireNonNull(reader.get("name").getAsString());
		if (name.equalsIgnoreCase("null")) {
			return;
		}
		String description = Objects.requireNonNull(reader.get("examine").getAsString());
		int equipmentSlot = reader.get("equipment-slot").getAsInt();
		boolean noteable = reader.get("noteable").getAsBoolean();
		boolean noted = reader.get("noted").getAsBoolean();
		boolean stackable = reader.get("stackable").getAsBoolean();
		int specialPrice = reader.get("special-price").getAsInt();
		int generalPrice = reader.get("general-price").getAsInt();
		int highAlchValue = reader.get("high-alch").getAsInt();
		int lowAlchValue = reader.get("low-alch").getAsInt();
		double weight = reader.get("weight").getAsDouble();
		int[] bonus = builder.fromJson(reader.get("bonus").getAsJsonArray(), int[].class);
		boolean twoHanded = reader.get("two-handed").getAsBoolean();
		boolean platebody = reader.get("platebody").getAsBoolean();
		boolean fullHelm = reader.get("full-helm").getAsBoolean();
		boolean tradeable = reader.get("tradable").getAsBoolean();
		ItemDefinition.DEFINITIONS[index] = new ItemDefinition(index, name, description, equipmentSlot, noteable, noted,
				stackable, specialPrice, generalPrice, lowAlchValue, highAlchValue, weight, bonus, twoHanded, fullHelm,
				platebody, tradeable);
	}

}