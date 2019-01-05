package Ghreborn.model.multiplayer_session;

import Ghreborn.model.players.Client;

/**
 * @author Jason MacKeigan
 * @date Oct 19, 2014, 8:03:05 PM
 */
public abstract class Multiplayer {
	/**
	 * The last time, in milliseconds, that you accepted a trade.
	 */
	protected long lastAccept;
	
	/**
	 * Player associated with trading operations
	 */
	protected Client player;
	
	/**
	 * Constructs a new class for managing trade operations
	 * @param player
	 */
	public Multiplayer(Client player) {
		this.player = player;
	}
	
	public abstract boolean requestable(Client request);
	
	public abstract void request(Client requested);

	/**
	 * The last time in millisecods that you traded
	 * @return the last time in milliseconds
	 */
	public long getLastAccept() {
		return lastAccept;
	}

	/**
	 * Records the last time you accepted a trade
	 * @param lastAccept the last time in milliseconds
	 */
	public void setLastAccept(long lastAccept) {
		this.lastAccept = lastAccept;
	}
}
