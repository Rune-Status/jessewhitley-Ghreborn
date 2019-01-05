package Ghreborn.model.content.fillables;

import Ghreborn.definitions.ObjectDef;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.fillables.Fillables.fillData;
import Ghreborn.model.players.Client;

public class sandtoBucket {
	public enum sandData {		
		BUCKET(1925, 1783);

		
		private int emptyid, filledid;
		
		private sandData(int emptyid, int filledid){
			this.emptyid = emptyid;
			this.filledid = filledid;
			
		}
		public int getEmptyId(){
			return emptyid;
		}
		public int getFilledId(){
			return filledid;
		}


	}
	public static void fillTheItem(final Client c, int itemId, int objectID) {
		for(final sandData g : sandData.values()) {
		if(c.getItems().playerHasItem(g.getEmptyId(), 1)) {

		if(itemId == g.getEmptyId()) {
		c.fillingWater = true;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if(c == null || c.disconnected || c.getSession() == null) {
					stop();
					return;
				}
				if (c.fillingWater) {
					if (!c.getItems().playerHasItem(g.getEmptyId(), 1)) {
						//c.sendMessage("You have run out of molten glass.");
						container.stop();
						return;
					}
		c.animation(895);
		c.getItems().deleteItem(g.getEmptyId(), 1);
		c.getItems().addItem(g.getFilledId(), 1);
		c.sendMessage("You fill the bucket with sand.");
				}else {
					container.stop();
				}
				
				
			}
			@Override
		public void stop() {
			c.stopAnimation();
			c.fillingWater = false;
		}
		}, 3);
		}
		}
		}
		}
		}
