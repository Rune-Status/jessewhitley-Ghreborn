package Ghreborn.util.json;


import java.util.Arrays;
import java.util.Objects;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Ghreborn.model.npcs.drops.NpcDrop;
import Ghreborn.model.npcs.drops.NpcDropCache;
import Ghreborn.model.npcs.drops.NpcDropManager;
import Ghreborn.model.npcs.drops.NpcDropTable;
import Ghreborn.util.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all npc drops.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDropTableLoader extends JsonLoader {

    /**
     * Creates a new {@link NpcDropTableLoader}.
     */
    public NpcDropTableLoader() {
        super("./Data/json/npc_drops.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int[] array = builder.fromJson(reader.get("ids"), int[].class);
        NpcDrop[] unique = Objects.requireNonNull(builder.fromJson(reader.get("unique"), NpcDrop[].class));
        NpcDropCache[] common = Objects.requireNonNull(builder.fromJson(reader.get("common"), NpcDropCache[].class));
        if (Arrays.stream(common).anyMatch(Objects::isNull))
            throw new NullPointerException("Invalid common drop table, npc_drops.json");
        Arrays.stream(array).forEach(id -> NpcDropManager.TABLES.put(id, new NpcDropTable(unique, common)));
    }
}