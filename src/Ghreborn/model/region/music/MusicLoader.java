package Ghreborn.model.region.music;

import java.io.File;
import java.io.FileReader;
import java.util.logging.Logger;

import com.google.gson.*;

import Ghreborn.clip.Region;

/**
 * A class which handles the loading of music data.
 * 
 * @author Michael P
 *
 */
public class MusicLoader {

	/**
	 * An array of loaded music.
	 */
	private static Music[] music;
	
	/**
	 * The {@link Logger}.
	 */
	private static final Logger logger = Logger.getLogger(MusicLoader.class.getName());
	
	/**
	 * Loads the music data from a .JSON file.
	 * 
	 * @throws Exception
	 *            If any exception happens.
	 */
	public static void load() throws Exception {
		logger.info("Loading music...");
		
		JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(new FileReader(new File("./data/json/music.json")));
        music = new Music[array.size()];
		int count = 0;
		
		for (int i = 0; i < array.size(); i++) {
			JsonObject reader = (JsonObject) array.get(i);

			int region = -1;
			if (reader.has("region"))
				region = reader.get("region").getAsInt();
			String name = reader.get("name").getAsString();
			int song = reader.get("song").getAsInt();
			int frame = reader.get("frame").getAsInt();
			int button = reader.get("button").getAsInt();
			
			music[i] = new Music(region, name, song, frame, button);
			count++;
		}
		
		logger.info("Loaded "+ count +" music!");	
	}
	
	/**
	 * Gets the music array.
	 * @return The array of loaded music.
	 */
	public static Music[] getMusic() {
		return music;
	}
	
	/**
	 * Gets the music for the region id.
	 * 
	 * @param region
	 *            The region id.
	 * @return The music track for the region.
	 */
	public static Music forRegion(int region) {
		for (Music m : music) {
			if (m == null)
				continue;
			if (m.region == region)
				return m;
		}
		return null;
	}
	/**
	 * Gets the music for the frame.
	 * 
	 * @param button
	 *            The button of the track.
	 * @return The music track for the frame.
	 */
	public static Music forFrame(int button) {
		for (Music m : music) {
			if (m == null)
				continue;
			if (m.button == button)
				return m;
		}
		return null;
	}
	
	public static Music forSong(int song) {
		for (Music m : music) {
			if (m == null)
				continue;
			if (m.song == song)
				return m;
		}
		return null;
	}
	
	/**
	 * Represents a single music track.
	 * 
	 * @author Michael P
	 *
	 */
	public static class Music {
		
		public Music(int region, String name, int song, int frame, int button) {
			this.region = region;
			this.name = name;
			this.song = song;
			this.frame = frame;
			this.button = button;
		}
		
		private final int region;
		
		private final String name;
		
		private final int song;
		
		private final int frame;
		
		private final int button;
		
		public String getName() {
			return name;
		}
		
		public int getSong() {
			return song;
		}
		
		public int getFrame() {
			return frame;
		}

		public int getButton() {
			return button;
		}
		
	}
}