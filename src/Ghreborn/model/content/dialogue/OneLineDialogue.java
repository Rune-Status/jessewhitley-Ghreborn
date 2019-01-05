package Ghreborn.model.content.dialogue;

import java.util.HashMap;

import Ghreborn.model.players.Client;

public class OneLineDialogue {

	private static final HashMap<Integer, String> idsForChat = new HashMap<Integer, String>();

	public static void declare() {
		idsForChat.put(462, "Welcome to the Mages' guild!");
		idsForChat.put(553, "Hello there, I've got all kinds of magical supply!");
	}

	public static boolean doOneLineChat(Client player, int id) {
		if (idsForChat.containsKey(id)) {
			DialogueManager.sendNpcChat(player, id, Emotion.HAPPY, idsForChat.get(id));
			return true;
		}
		return false;
	}

}
