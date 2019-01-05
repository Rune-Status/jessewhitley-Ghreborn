package Ghreborn.util.json;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Ghreborn.model.players.Requirement;
import Ghreborn.util.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all equipment level
 * requirements.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class EquipmentRequirementLoader extends JsonLoader {

    /**
     * Creates a new {@link EquipmentRequirementLoader}.
     */
    public EquipmentRequirementLoader() {
        super("./Data/json/equipment_requirements.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        Requirement[] requirements = Objects.requireNonNull(builder.fromJson(reader.get("requirements"), Requirement[].class));
        Preconditions.checkState(requirements.length > 0);

        if (Requirement.REQUIREMENTS.containsKey(id))
            throw new IllegalStateException("Duplicate key values [" + id + "] for equipment requirements.");
        Requirement.REQUIREMENTS.put(id, requirements);
    }
}
