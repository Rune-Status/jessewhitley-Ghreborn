package Ghreborn.model.content.dialogue;

import Ghreborn.model.players.Client;

public abstract class Dialogue {

	protected int next = 0;
	protected int option;
	protected Client player;

	public boolean clickButton(int id) {
		return false;
	}

	public void end() {
		//player.getPA().closeAllWindows();
		next = -1;
	}

	public abstract void execute();

	public int getNext() {
		return next;
	}

	public int getOption() {
		return option;
	}

	public Client getPlayer() {
		return player;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public void setOption(int option) {
		this.option = option;
	}

	public void setPlayer(Client player) {
		this.player = player;
	}

}
