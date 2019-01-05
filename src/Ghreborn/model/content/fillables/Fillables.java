package Ghreborn.model.content.fillables;

import Ghreborn.definitions.ObjectDef;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.model.players.Client;

/**
 * Class Fillables
 * Handles Filling water in unfilled item's
 * @author Genesis/Organic
 * 11:00 2:14 7/2/2012
 */

public class Fillables  {
	public enum fillData {		
		VIAL(229, 227),
		BUCKET(1925, 1929),
		JUG(1935, 1937),
		BOWL(1923, 1921),
		CUP(1980, 4458),
		WATERING_CAN(5331, 5340),
		WATERING_CAN2(5333, 5340),
		WATERING_CAN3(5334, 5340),
		WATERING_CAN4(5335, 5340),
		WATERING_CAN5(5336, 5340),
		WATERING_CAN6(5337, 5340),
		WATERING_CAN7(5338, 5340),
		WATERING_CAN8(5339, 5340),
		WATERSKIN(1831, 1823),
		WATERSKIN2(1825, 1823),
		WATERSKIN3(1827, 1823),
		WATERSKIN4(1829, 1823),
		FISHBOWL(6667, 6668);
		
		private int emptyid, filledid;
		
		private fillData(int emptyid, int filledid){
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
for(final fillData g : fillData.values()) {
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
c.animation(832);
c.getItems().deleteItem(g.getEmptyId(), 1);
c.getItems().addItem(g.getFilledId(), 1);
c.sendMessage("You fill "+c.getItems().getItemName(g.getEmptyId())+" from the "+ObjectDef.getObjectDef(objectID).name+".");
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




