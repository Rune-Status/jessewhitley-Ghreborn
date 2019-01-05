package Ghreborn.model.region.music;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import Ghreborn.clip.Region;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.Rights;
import Ghreborn.model.region.music.MusicLoader.Music;

/**
 * Manages in-game music.
 * 
 * @author Michael P
 *
 */
public class MusicManager {
	
	/**
	 * NOTES:
	 * 
	 * MAN; The MAN/Manual feature is set when a player selects a certain music to be played.
	 * While the MAN feature is on, music does not play by region. Once the song is done
	 * playing, nothing else will play unless clicked on. This feature can't be set
	 * while the AUTO feature is on; however it CAN be set while the LOOP feature is set.
	 * If you click a song to be played, it will switch from AUTO to MAN.
	 * 
	 * LOOP; The LOOP feature replays a song once it's over. This feature can be set while the
	 * MAN or AUTO feature is on.
	 * 
	 * AUTO; The AUTO feature plays music by it's region/location. This feature can be set while
	 * the LOOP feature is on; however can't be set while the MAN feature is set. Once the
	 * song is done playing, no other song is played until called.
	 * 
	 * Messages:
	 * 
	 * LOOP; Music looping is now enabled/disabled.
	 */
	
	/**
	 * Constructs a new music manager.
	 * 
	 * @param player
	 *            The player to construct the manager for.
	 */
	public MusicManager(Client player) {
		this.player = player;
	}
	
	/**
	 * The player to manage the music for.
	 */
	private final Client player;
	
	/**
	 * The auto-play flag.
	 */
	private boolean auto = true;
	
	/**
	 * The manual-play flag.
	 */
	private boolean man;
	
	/**
	 * The looping flag.
	 */
	private boolean loop;
	
	/**
	 * A list of unlocked music.
	 */
	public List<Integer> unlockedMusics = new LinkedList<Integer>();
	
	/**
	 * Loads the default settings.
	 */
	public void load() {
		// Add the default unlocked tracks.
		unlockedMusics.add(62);
		unlockedMusics.add(466);
		unlockedMusics.add(0);
		unlockedMusics.add(400);
		unlockedMusics.add(547);
		unlockedMusics.add(321);
		
		// Set the AUTO config.
		player.getPA().sendConfig(18, 1);
		player.getPA().sendFrame126("AUTO", 23595);
		
		// Update track list.
		updateTrackList();
	}
	
	/**
	 * Updates the music by region.
	 * 
	 * @param region
	 *            The region id.
	 */
	public void updateRegionMusic(int region) {
		Music music = MusicLoader.forRegion(region);
		if (!auto)
			return;
		if (music == null) {
			music = MusicLoader.forSong(2);
			player.getPA().playMusic(music.getSong());
			player.getPA().sendFrame126(music.getName(), 23595);
			if(player.getRights().isOwner()){
			player.sendMessage("Music isn't added into this region yet! Region = "+ player.getRegionId());
			}
			return;
		}
		player.getPA().playMusic(music.getSong());
		player.getPA().sendFrame126(music.getName(), 23595);
		if (!unlockedMusics.contains(music.getSong())) {
			unlockedMusics.add(music.getSong());
			player.sendMessage("<col=ff0000>You have unlocked a new music track: "+music.getName());
			player.getPA().sendInterfaceTextColor(0 << 10 | 255 << 5 | 0, music.getFrame());
		}
	}
	
	/**
	 * Updates the music track list.
	 */
	public void updateTrackList() {
		for (Music music : MusicLoader.getMusic()) {
			if (music == null)
				continue;
		
	
			if (!unlockedMusics.contains(music.getSong()))
				player.getPA().sendInterfaceTextColor(255 << 10 | 0 << 5 | 0, music.getFrame());
			else
				player.getPA().sendInterfaceTextColor(0 << 10 | 255 << 5 | 0, music.getFrame());
		}
	}
	
	/**
	 * Handles music buttons. This could be clicking a song to be played, or setting the
	 * AUTO, MAN, or LOOP feature.
	 * 
	 * @param button
	 *            The button clicked.
	 */
	public void handleMusicButtons(int button) {
		switch (button) {
			case 24125: // AUTO feature.
				if (!auto) {
					this.auto = true;
					this.man = false;
					player.getPA().sendConfig(18, 1);
					player.getPA().sendFrame126("AUTO", 23595);
				}
				break;
			case 24126: // MAN feature.
				if (!man) {
					this.auto = false;
					this.man = true;
					player.getPA().sendConfig(18, 0);
					player.getPA().sendFrame126("MANUAL", 23595);
				}
				break;
			case 38197: // LOOP feature.
				this.loop = !loop;
				player.getPA().sendConfig(19, loop ? 1 : 0);
				player.sendMessage("Music looping is now "+ (loop ? "enabled" : "disabled") +".");
				break;
		}
		
		Music music = MusicLoader.forFrame(button);
		if (music == null)
			return;
		if (!unlockedMusics.contains(music.getSong())) {
			player.sendMessage("You have not unlocked this piece of music yet!");
			return;
		}
		player.getPA().playMusic(music.getSong());
		player.getPA().sendFrame126(music.getName(), 23595);
		if (auto) {
			this.auto = false;
			this.man = true;
			player.getPA().sendConfig(18, 0);
		}
	}
	
	/**
	 * Gets the list of unlocked musics.
	 * @return The list of unlocked music.
	 */
	public List<Integer> getUnlockedMusic() {
		return unlockedMusics;
	}
	
}