package Ghreborn.model.content.dialogue.impl;

import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.content.dialogue.Emotion;
import Ghreborn.model.content.dialogue.OptionDialogue;

public class ConverterDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), NPC_ID, Emotion.LAUGHING, "Hello adventurer, I'm the Convertor!");
			setNext(1);
			break;
		case 1:
			DialogueManager.sendNpcChat(getPlayer(), NPC_ID, Emotion.LAUGHING, "I can buy or sell coins or",
					"convert your items into coins.", "What would you like to do?");
			setNext(2);
			break;
		case 2:
			getPlayer().start(new OptionDialogue("Buy platinum tokens (2.5k each)", p -> {
				buyTokens();
				end();
				getPlayer().getPA().removeAllWindows();
			}, "Sell platinum tokens (2.4k each)", p -> {
				sellTokens();
				end();
				getPlayer().getPA().removeAllWindows();
			}, "Sell items", p -> {
				getPlayer().getShops().openShop(SHOP_ID);
				end();
			}));
			end();
			break;
		}
	}

	private void buyTokens() {
		int coinsAmount = getPlayer().getItems().getItemCount(995);
		coinsAmount = coinsAmount - (coinsAmount % 2_500);
		
		int tokensAmount = coinsAmount / 2_500;
		
		if (tokensAmount <= 0) {
			getPlayer().sendMessage("You don't have enough coins.");
			return;
		}
		
		if (getPlayer().getItems().canAdd(995, tokensAmount)) {
			getPlayer().getItems().addItem(995, tokensAmount);
			getPlayer().getItems().deleteItem2(995, coinsAmount);
			getPlayer().sendMessage("You received " + tokensAmount + " coins!");
		} else {
			getPlayer().sendMessage("Not enough space in your inventory.");
		}
	}

	private void sellTokens() {
		int tokensAmount = getPlayer().getItems().getItemCount(995);
		int coinsAmount = tokensAmount * 2_400;
		
		if (tokensAmount <= 0) {
			getPlayer().sendMessage("You don't have enough coins.");
			return;
		}
		
		if (getPlayer().getItems().canAdd(995, coinsAmount)) {
			getPlayer().getItems().addItem(995, coinsAmount);
			getPlayer().getItems().deleteItem2(995, tokensAmount);
			getPlayer().sendMessage("You received " + tokensAmount + " coins!");
		} else {
			getPlayer().sendMessage("Not enough space in your inventory.");
		}
	}

	public static final int NPC_ID = 6074;
	public static final int SHOP_ID = 120;

}

