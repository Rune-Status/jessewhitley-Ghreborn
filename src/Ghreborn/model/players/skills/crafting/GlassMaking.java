package Ghreborn.model.players.skills.crafting;

import Ghreborn.Config;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.content.fillables.sandtoBucket.sandData;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;

public class GlassMaking {
	public enum glassdata {		
		BUCKET(1783, 1781, 40, 1775);

		
		private int bucketID, soda, xp, glassid;
		
		private glassdata(int bucketID, int soda, int xp, int glassid){
			this.bucketID = bucketID;
			this.soda = soda;
			this.xp = xp;
			this.glassid = glassid;
			
		}
		public int getsodaId(){
			return soda;
		}
		public int getbucketId(){
			return bucketID;
		}
		public int getXp() {
			return xp;
		}
		public int getglassId() {
			return glassid;
		}
	}

		public static void MakeGlass(final Client c, int itemId, int objectID) {
			for(final glassdata g : glassdata.values()) {
			if(c.getItems().playerHasItem(g.getsodaId(), 1)) {

			if(itemId == g.getsodaId()) {
			c.fillingWater = true;
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if(c == null || c.disconnected || c.getSession() == null) {
						stop();
						return;
					}
					if (c.fillingWater) {
						if (!c.getItems().playerHasItem(g.getsodaId(), 1) && !c.getItems().playerHasItem(g.getbucketId(), 1)) {
							c.sendMessage("You have ran out of materials to make molten glass.");
							container.stop();
							return;
						}
			c.animation(899);
			c.getItems().deleteItem(g.getsodaId(), 1);
			c.getItems().deleteItem(g.getbucketId(), 1);
			c.getItems().addItem(g.getglassId(), 1);
			c.getItems().addItem(1925, 1);
			c.getPA().addSkillXP(g.getXp() * Config.CRAFTING_EXPERIENCE, Player.playerCrafting);
			c.getPA().refreshSkill(c.playerCrafting);
			//c.sendMessage("You fill the bucket with sand.");
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
