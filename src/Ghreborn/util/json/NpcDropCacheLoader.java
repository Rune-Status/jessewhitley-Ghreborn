package Ghreborn.util.json;

import java.util.Objects;


import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Ghreborn.model.npcs.drops.NpcDrop;
import Ghreborn.model.npcs.drops.NpcDropCache;
import Ghreborn.model.npcs.drops.NpcDropManager;
import Ghreborn.util.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all cached {@link NpcDrop}s.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcDropCacheLoader extends JsonLoader {

    /**
     * Creates a new {@link NpcDropCacheLoader}.
     */
    public NpcDropCacheLoader() {
        super("./Data/json/npc_drops_cache.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        NpcDropCache table = Objects.requireNonNull(builder.fromJson(reader.get("table"), NpcDropCache.class));
        NpcDrop[] items = Objects.requireNonNull(builder.fromJson(reader.get("items"), NpcDrop[].class));
        NpcDropManager.COMMON.put(table, items);
    }
}