package Ghreborn.model.content;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Ghreborn.Server;
import Ghreborn.definitions.ItemCacheDefinition;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.players.Boundary;
import Ghreborn.model.players.Client;
import Ghreborn.util.Misc;

public class CrystalChest {

	private static final int KEY = 989;
	private static final int DRAGONSTONE = 1631;
	private static final int KEY_HALVE1 = 985;
	private static final int KEY_HALVE2 = 987;
	private static final int ANIMATION = 881;

	private static final Map<Rarity, List<GameItem>> items = new HashMap<>();

	static {
		items.put(Rarity.COMMON, Arrays.asList(
				new GameItem(140, 10), 
				new GameItem(374, 50), 
				new GameItem(380, 100), 
				new GameItem(995, 100000), 
				new GameItem(1127, 1),
				new GameItem(2435, 2),
				new GameItem(1163, 1), 
				new GameItem(1201, 1), 
				new GameItem(1303, 1), 
				new GameItem(1712, 1),
				new GameItem(2677, 1), 
				new GameItem(441, 25), 
				new GameItem(454, 25), 
				new GameItem(1516, 20), 
				new GameItem(1512, 35), 
				new GameItem(208, 15), 
				new GameItem(565, 250), 
				new GameItem(560, 250), 
				new GameItem(71, 25), 
				new GameItem(1632, 5), 
				new GameItem(537, 10), 
				new GameItem(384, 15), 
				new GameItem(4131, 1)));
		
		items.put(Rarity.UNCOMMON, Arrays.asList(
				new GameItem(386, 20), 
				new GameItem(990, 3), 
				new GameItem(995, 500000), 
				new GameItem(1305, 1), 
				new GameItem(1377, 1),
				new GameItem(2368, 1), 
				new GameItem(2801, 1), 
				new GameItem(3027, 10), 
				new GameItem(3145, 15), 
				new GameItem(4587, 1), 
				new GameItem(6688, 10), 
				new GameItem(11840, 1)));
	}

	private static GameItem randomChestRewards(int chance) {
		int random = Misc.random(chance);
		List<GameItem> itemList = random < chance ? items.get(Rarity.COMMON) : items.get(Rarity.UNCOMMON);
		return Misc.getRandomItem(itemList);
	}

	public static void makeKey(Client c) {
		if (c.getItems().playerHasItem(KEY_HALVE1, 1) && c.getItems().playerHasItem(KEY_HALVE2, 1)) {
			c.getItems().deleteItem(KEY_HALVE1, 1);
			c.getItems().deleteItem(KEY_HALVE2, 1);
			c.getItems().addItem(KEY, 1);
		}
	}

	public static void searchChest(Client c) {
 if (c.getItems().playerHasItem(KEY)) {
			c.getItems().deleteItem(KEY, 1);
			c.startAnimation(ANIMATION);
			c.getItems().addItem(DRAGONSTONE, 1);
			GameItem reward = Boundary.isIn(c, Boundary.DONATOR_ZONE) && c.getRights().isDonator() ? randomChestRewards(2) : randomChestRewards(9);
			if (!c.getItems().addItem(reward.getId(), reward.getAmount())) {
				Server.itemHandler.createGroundItem(c, reward.getId(), c.getX(), c.getY(), c.heightLevel, reward.getAmount());
			}
			//Achievements.increase(c, AchievementType.LOOT_CRYSTAL_CHEST, 1);
			c.sendMessage("@blu@You stick your hand in the chest and pull an item out of the chest.");
 } else {
			c.sendMessage("@blu@The chest is locked, it won't budge!");
		}
	 if (c.getItems().playerHasItem(23964)) {
    	int[] Luckys = {23340, 23341, 23342, 23343, 23344, 23345, 23346, 23347, 23348, 23349, 23350, 23351, 23352, 23353, 23354, 23355, 23356, 23357, 23358, 23359, 23360, 23361, 23362, 23363, 23364, 23365, 23366, 23367, 23368, 23369, 23370, 23371, 23372, 23373, 23374, 23375, 23376, 23377, 23378, 23379, 23380, 23381, 23382, 23383, 23384, 23385, 23386, 23387, 23388, 23389, 23390, 23391, 23392, 23393, 23394, 23395, 23396, 23397, 23398, 23399, 23400, 23401, 23402, 23403, 23404, 23405, 23406, 23407, 23408, 23409, 23410, 23411, 23412, 23413, 23414, 23415, 23416, 23417, 23418, 23419, 23420, 23421, 23422, 23423, 23424, 23425, 23426, 23427, 23428, 23429, 23430, 23431, 23432, 23433, 23434, 23435, 23436, 23437, 23438, 23439, 23440, 23441, 23442, 23443, 23444, 23445, 23446, 23447, 23448, 23449, 23450, 23451, 23452, 23453, 23454, 23455, 23456, 23457, 23458, 23459, 23460, 23461, 23462, 23463, 23464, 23465, 23466, 23467, 23468, 23469, 23470, 23471, 23472, 23473, 23474, 23475, 23476, 23477, 23478, 23479, 23480, 23481, 23482, 23483, 23484, 23485, 23486, 23487, 23488, 23489, 23490, 23491, 23492, 23493, 23494, 23495, 23496, 23497, 23498, 23499, 23500, 23501, 23502, 23503, 23504, 23505, 23506, 23507, 23508, 23509, 23510, 23511, 23512, 23513, 23514, 23515, 23516, 23517, 23518, 23519, 23520, 23521, 23522, 23523, 23524, 23525, 23526, 23527, 23528, 23529, 23530, 23531, 23532, 23533, 23534, 23535, 23536, 23537, 23538, 23539, 23540, 23541, 23542, 23543, 23544, 23545, 23546, 23547, 23548, 23549, 23550, 23551, 23552, 23553, 23554, 23555, 23556, 23557, 23558, 23559, 23560, 23561, 23562, 23563, 23564, 23565, 23566, 23567, 23568, 23569, 23570, 23571, 23572, 23573, 23574, 23575, 23576, 23577, 23578, 23579, 23580, 23581, 23582, 23583, 23584, 23585, 23586, 23587, 23588, 23589, 23590, 23591, 23592, 23593, 23594, 23595, 23596, 23597, 23598, 23599, 23600, 23601, 23602, 23603, 23604, 23605, 23606, 23607, 23608, 23609, 23610, 23611, 23612, 23613, 23614, 23615, 23616, 23617, 23618, 23619, 23620, 23621, 23622, 23623, 23624, 23625, 23626, 23627, 23628, 23629, 23630, 23631, 23632, 23633, 23634, 23635, 23636, 23637, 23638, 23639, 23640, 23641, 23642, 23643, 23644, 23645, 23646, 23647, 23648, 23649, 23650, 23651, 23652, 23653, 23654, 23655, 23656, 23657, 23658, 23659, 23660, 23661, 23662, 23663, 23664, 23665, 23666, 23667, 23668, 23669, 23670, 23671, 23672, 23673, 23674, 23675, 23676, 23677, 23678, 23679, 23680, 23681, 23682, 23683, 23684, 23685};
    int randomLucky = Luckys[Misc.random(Luckys.length - 1)];
    if (c.getItems().freeSlots() < 1) {
    	c.sendMessage("You need 1 free slots to use this key on this chest.");
    	return;
    } 
    if(c.getItems().playerHasItem(23964)){
    	   c.getItems().deleteItem2(23964, 1);
    	   c.getItems().addItem(randomLucky, 1);
    	   c.sendMessage("You just got an "+ItemCacheDefinition.forID(randomLucky).getName()+"!");
    	   c.getPA().messageall("<col=CA1726>"+c.playerName+" Has Gotten an "+ItemCacheDefinition.forID(randomLucky).getName()+" from the chest at home!");
 } else {
	 c.sendMessage("@blu@The chest is locked, it won't budge!");
 }
	 }
 }

	enum Rarity {
		UNCOMMON, COMMON, RARE
	}

}
