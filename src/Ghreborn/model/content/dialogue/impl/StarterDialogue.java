package Ghreborn.model.content.dialogue.impl;

import Ghreborn.Connection;
import Ghreborn.Server;
import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.dialogue.DialogueConstants;
import Ghreborn.model.content.dialogue.DialogueManager;
import Ghreborn.model.items.Item2;
import Ghreborn.model.players.PlayerSave;
import Ghreborn.model.players.Rights;

public class StarterDialogue extends Dialogue {

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendStatement(getPlayer(), "Welcome to Ghreborn we are a custom osrs server.", "We currently have 3 modes, one is Ironman Mode", " and Hardcore Ironman mode and Normal Mode.","What one do you want?");
			setNext(1);
			break;
		case 1:
			DialogueManager.sendOption(getPlayer(), "Ironman Mode (x5)", "Hardcore Ironman Mode (x5)", "Normal mode");
			break;
		} 
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean clickButton(int id) {
		switch(id) {
	case DialogueConstants.OPTIONS_3_1:
		getPlayer().setRights(Rights.IRONMAN);
		getPlayer().getPA().requestUpdates();
		getPlayer().updateRequired = true;
		getPlayer().getItems().addItem(995, 1000);
		getPlayer().getItems().addItem(12810, 1);
		getPlayer().getItems().addItem(12811, 1);
		getPlayer().getItems().addItem(12812, 1);
		getPlayer().getItems().addItem(1351, 1);
		getPlayer().getItems().addItem(1265, 1);
		getPlayer().getItems().addItem(2347, 1);
		getPlayer().getItems().addItem(1277, 1);
		getPlayer().getItems().addItem(1171, 1);
		getPlayer().getItems().addItem(841, 1);
		getPlayer().getItems().addItem(882, 25);
		getPlayer().getPA().showInterface(3559);
		PlayerSave.saveGame(getPlayer());
		end();
		getPlayer().addStarter = true;
		getPlayer().canChangeAppearance = true;
		break;
	case DialogueConstants.OPTIONS_3_2:
		getPlayer().setRights(Rights.HARDCORE_IRONMAN);
		getPlayer().getPA().requestUpdates();
		getPlayer().getItems().addItem(995, 1000);
		getPlayer().getItems().addItem(20792, 1);
		getPlayer().getItems().addItem(20794, 1);
		getPlayer().getItems().addItem(20796, 1);
		getPlayer().getItems().addItem(1351, 1);
		getPlayer().getItems().addItem(1265, 1);
		getPlayer().getItems().addItem(2347, 1);
		getPlayer().getItems().addItem(1277, 1);
		getPlayer().getItems().addItem(1171, 1);
		getPlayer().getItems().addItem(841, 1);
		getPlayer().getItems().addItem(882, 25);
		getPlayer().getPA().showInterface(3559);
		PlayerSave.saveGame(getPlayer());
		end();
		getPlayer().addStarter = true;
		getPlayer().canChangeAppearance = true;
		break;
	case DialogueConstants.OPTIONS_3_3:
		if (!Connection.hasRecieved1stStarter(Server.playerHandler.players[getPlayer().index].connectedFrom)) {
			getPlayer().getItems().addItem(995, 100000);
			getPlayer().getItems().addItem(1731, 1);
			getPlayer().getItems().addItem(554, 200);
			getPlayer().getItems().addItem(555, 200);
			getPlayer().getItems().addItem(556, 200);
			getPlayer().getItems().addItem(558, 600);
			getPlayer().getItems().addItem(1381, 1);
			getPlayer().getItems().addItem(1323, 1);
			getPlayer().getItems().addItem(841, 1);
			getPlayer().getItems().addItem(882, 500);
			getPlayer().getItems().addItem(380, 100);
			getPlayer().getItems().addItem(Item2.randomGH(), 1);
			getPlayer().getPA().showInterface(3559);
			getPlayer().addStarter = true;
			end();
			getPlayer().canChangeAppearance = true;
			PlayerSave.saveGame(getPlayer());
			Connection.addIpToStarterList1(Server.playerHandler.players[getPlayer().index].connectedFrom);
			Connection.addIpToStarter1(Server.playerHandler.players[getPlayer().index].connectedFrom);
			getPlayer().sendMessage("You have recieved 1 out of 2 starter packages on this IP address.");
		} else if (Connection.hasRecieved1stStarter(Server.playerHandler.players[getPlayer().index].connectedFrom) && !Connection.hasRecieved2ndStarter(Server.playerHandler.players[getPlayer().index].connectedFrom)) {
			getPlayer().getItems().addItem(995, 100000);
			getPlayer().getItems().addItem(1731, 1);
			getPlayer().getItems().addItem(554, 200);
			getPlayer().getItems().addItem(555, 200);
			getPlayer().getItems().addItem(556, 200);
			getPlayer().getItems().addItem(558, 600);
			getPlayer().getItems().addItem(1381, 1);
			getPlayer().getItems().addItem(1323, 1);
			getPlayer().getItems().addItem(841, 1);
			getPlayer().getItems().addItem(882, 500);
			getPlayer().getItems().addItem(380, 100);
			getPlayer().getPA().showInterface(3559);
			PlayerSave.saveGame(getPlayer());
			getPlayer().canChangeAppearance = true;
			getPlayer().addStarter = true;
			end();
			getPlayer().sendMessage("You have recieved 2 out of 2 starter packages on this IP address.");
			Connection.addIpToStarterList2(Server.playerHandler.players[getPlayer().index].connectedFrom);
			Connection.addIpToStarter2(Server.playerHandler.players[getPlayer().index].connectedFrom);
		} else if (Connection.hasRecieved1stStarter(Server.playerHandler.players[getPlayer().index].connectedFrom) && Connection.hasRecieved2ndStarter(Server.playerHandler.players[getPlayer().index].connectedFrom)) {
			getPlayer().sendMessage("You have already recieved 2 starters!");
			getPlayer().getPA().showInterface(3559);
			PlayerSave.saveGame(getPlayer());
			end();
			getPlayer().addStarter = true;
			getPlayer().canChangeAppearance = true;
		}
		break;
		}
		
		return false;
	}
}
