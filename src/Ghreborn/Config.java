package Ghreborn;


public class Config {

	public static final boolean ALLOWPINS = true;	// are you allowing pins
	public static final boolean SERVER_DEBUG = false;//needs to be false for Real world to work
	 public static boolean sendServerPackets = false;
	public static final int[] DESTROYABLES 	= 	{10548, 10551, 18349, 18351, 18353, 18355, 18357, 18359}; // Destroyable
	public static String SERVER_NAME = "Ghreborn";
	public static final String WELCOME_MESSAGE = "Welcome to "+SERVER_NAME;
	public static final String FORUMS = "";
	public static int MAX_NPCS = Server.npcHandler.maxNPCs;
	public static final boolean PLACEHOLDER_ECONOMY = false;
	
	public static final int CLIENT_VERSION = 3;
	public static final boolean SOUND = false;
	public static final int TOKENS_ID = 13204;
	
	public static int MESSAGE_DELAY = 6000;
	public static final int ITEM_LIMIT = 30000; // item id limit, different clients have more items like silab which goes past 15000
	public static final int MAXITEM_AMOUNT = Integer.MAX_VALUE;
	public static final int BANK_SIZE = 352;
	public static final int MAX_PLAYERS = 1024;

	public static int REGION_SIZE = 0;
	public static int REGION_AMOUNT = 70;
	public static final boolean ADMIN_CAN_PVP = false; // can admins be attacked or attack other players?
	public static int REGION_DECREASE = 6;
	public static int REGION_NORMALREGION = 32;
	public static boolean LOCK_EXPERIENCE = false;
	public static boolean MINI_GAMES = true;
	public static String LOGOUT_MESSAGE = "Click here to logout!";
	public static String DEATH_MESSAGE = "Oh dear you are dead!";
	public static boolean DOUBLE_EXP = true;
	public static final int CONNECTION_DELAY = 100; // how long one ip can keep connecting
	public static final int IPS_ALLOWED = 1; // how many ips are allowed

	public static final boolean WORLD_LIST_FIX = false; // change to true if you want to stop that world--8 thing, but it can cause the screen to freeze on silabsoft client

	public static final int[] ITEM_SELLABLE 		=	{23936, 15340, 15341, 15342, 15343, 25342, 25343, 25344, 25345, 12745,15004,3842,3844,3840,8844,8845,8846,8847,8848,8849,8850,10551,6570,7462,7461,7460,7459,7458,7457,7456,7455,7454,8839,8840,8842,11663,11664,11665,10499,
		9748,9754,9751,9769,9757,9760,9763,9802,9808,9784,9799,9805,9781,9796,9793,9775,9772,9778,9787,9811,9766,
		9749,9755,9752,9770,9758,9761,9764,9803,9809,9785,9800,9806,9782,9797,9794,9776,9773,9779,9788,9812,9767,
		9747,9753,9750,9768,9756,9759,9762,9801,9807,9783,9798,9804,9780,9795,9792,9774,9771,9777,9786,9810,9765,995}; // what items can't be sold in any store
	public static final int[] ITEM_TRADEABLE 		= 	{23936, 15340, 15341, 15342, 15343, 25342, 25343, 25344, 25345, 12745,15004,8850,10551,8839,8840,8842,11663,11664,11665,3842,3844,3840,8844,8845,8846,8847,8848,8849,8850,10551,6570,7462,7461,7460,7459,7458,7457,7456,7455,7454,8839,8840,8842,11663,11664,11665,10499,
		9748,9754,9751,9769,9757,9760,9763,9802,9808,9784,9799,9805,9781,9796,9793,9775,9772,9778,9787,9811,9766,
		9749,9755,9752,9770,9758,9761,9764,9803,9809,9785,9800,9806,9782,9797,9794,9776,9773,9779,9788,9812,9767,
		9747,9753,9750,9768,9756,9759,9762,9801,9807,9783,9798,9804,9780,9795,9792,9774,9771,9777,9786,9810,9765}; // what items can't be traded or staked
	public static final int[] UNDROPPABLE_ITEMS 	= 	{23936,15340, 15341, 15342, 15343, 25342, 25343, 25344, 25345}; // what items can't be dropped

	public static final int[] FUN_WEAPONS	=	{2460,2461,2462,2463,2464,2465,2466,2467,2468,2469,2470,2471,2471,2473,2474,2475,2476,2477}; // fun weapons for dueling
	public static final int[] LUCKY_ITEMS = {15340, 15341, 15342, 15343, 25342, 25343, 25344, 25345};
	public static boolean ADMIN_CAN_TRADE = false; //can admins trade?
	public static  boolean ADMIN_CAN_SELL_ITEMS = false; // can admins sell items?
	public static  boolean ADMIN_DROP_ITEMS = false; // can admin drop items?

	public static final int START_LOCATION_X = 1640;
	public static final int START_LOCATION_Y = 2858;
	public static final int RESPAWN_X = 1640;
	public static final int RESPAWN_Y = 2858;
	public static final int DUELING_RESPAWN_X = 3362; // when dead in duel area spawn here
	public static final int DUELING_RESPAWN_Y = 3263;
	public static final int RANDOM_DUELING_RESPAWN = 5; // random coords

	public static final int NO_TELEPORT_WILD_LEVEL = 20; // level you can't tele on and above
	public static final int SKULL_TIMER = 1200; // how long does the skull last? seconds x 2
	public static final int TELEBLOCK_DELAY = 20000; // how long does teleblock last for.
	public static final boolean SINGLE_AND_MULTI_ZONES = true; // multi and single zones?
	public static final boolean COMBAT_LEVEL_DIFFERENCE = true; // wildy levels and combat level differences matters
	public static final boolean WEEKEND_DOUBLE_EXP = false;
	public static final boolean FARMING_ENABLED = true;
	
	public static final boolean itemRequirements = true; // attack, def, str, range or magic levels required to wield weapons or wear items?

	public static final int MELEE_EXP_RATE = 150; // damage * exp rate
	public static final int RANGE_EXP_RATE = 150;
	public static final int MAGIC_EXP_RATE = 300;
	public static final int Ironman_exp_rate = 5;
	public static  double SERVER_EXP_BONUS = 1;
	public static boolean doubleEXPWeekend = false;
	public static boolean superiorSlayerActivated = true;
	public static int GLOBAL_MESSAGE = 0;
	/**
	 * Maximum amount of packets processed per cycle per player
	 */
	public static final int MAX_PROCESS_PACKETS = 10;
	
	public static final int INCREASE_SPECIAL_AMOUNT = 17500; // how fast your special bar refills
	public static final boolean PRAYER_POINTS_REQUIRED = true; // you need prayer points to use prayer
	public static final boolean PRAYER_LEVEL_REQUIRED = true; // need prayer level to use different prayers
	public static final boolean MAGIC_LEVEL_REQUIRED = true; // need magic level to cast spell
	public static final int GOD_SPELL_CHARGE = 300000; // how long does god spell charge last?
	public static final boolean RUNES_REQUIRED = true; // magic rune required?
	public static final boolean CORRECT_ARROWS = true; // correct arrows for bows?
	public static final boolean CRYSTAL_BOW_DEGRADES = true; // magic rune required?

	public static final int SAVE_TIMER = 120; // save every 1 minute
	public static final int NPC_RANDOM_WALK_DISTANCE = 5; // 5x5 square, NPCs
	// would be able to
	// walk 25 squares
	// around.
	public static final int NPC_FOLLOW_DISTANCE = 10; // how far can the npc follow you from it's spawn point, 													
	public static final int[] UNDEAD_NPCS = {90,91,92,93,94,103,104,73,74,75,76,77}; // undead npcs

	public static final int EDGEVILLE_X = 3087;
	public static final int EDGEVILLE_Y = 3498;
	public static final String EDGEVILLE = "";
	public static final int AL_KHARID_X = 3293;
	public static final int AL_KHARID_Y = 3174;
	public static final String AL_KHARID = "";
	public static final int KARAMJA_X = 3087;
	public static final int KARAMJA_Y = 3500;
	public static final String KARAMJA = "";
	public static final int MAGEBANK_X = 2538;
	public static final int MAGEBANK_Y = 4716;
	public static final String MAGEBANK = "";

	/**
	 * Teleport Spells
	 **/
	// modern
	public static final int VARROCK_X = 3213;
	public static final int VARROCK_Y = 3429;
	public static final String VARROCK = "";
	public static final int LUMBY_X = 3222;
	public static final int LUMBY_Y = 3218;
	public static final String LUMBY = "";
	public static final int FALADOR_X = 2964;
	public static final int FALADOR_Y = 3378;
	public static final String FALADOR = "";
	public static final int CAMELOT_X = 2757;
	public static final int CAMELOT_Y = 3477;
	public static final String CAMELOT = "";
	public static final int ARDOUGNE_X = 2662;
	public static final int ARDOUGNE_Y = 3305;
	public static final String ARDOUGNE = "";
	public static final int WATCHTOWER_X = 3087;
	public static final int WATCHTOWER_Y = 3500;
	public static final String WATCHTOWER = "";
	public static final int TROLLHEIM_X = 3243;
	public static final int TROLLHEIM_Y = 3513;
	public static final String TROLLHEIM = "";

	// ancient

	public static final int PADDEWWA_X = 3098;
	public static final int PADDEWWA_Y = 9884;

	public static final int SENNTISTEN_X = 3322;
	public static final int SENNTISTEN_Y = 3336;

	public static final int KHARYRLL_X = 3492;
	public static final int KHARYRLL_Y = 3471;

	public static final int LASSAR_X = 3006;
	public static final int LASSAR_Y = 3471;

	public static final int DAREEYAK_X = 3161;
	public static final int DAREEYAK_Y = 3671;

	public static final int CARRALLANGAR_X = 3156;
	public static final int CARRALLANGAR_Y = 3666;

	public static final int ANNAKARL_X = 3288;
	public static final int ANNAKARL_Y = 3886;

	public static final int GHORROCK_X = 2977;
	public static final int GHORROCK_Y = 3873;
/**
 * timeout time
 */
	public static final int TIMEOUT = 20;
	/**
	 * cycle time
	 */
	public static final int CYCLE_TIME = 600;
	/**
	 * buffer size
	 */
	public static final int BUFFER_SIZE = 4092;

	/**
	 * Slayer Variables
	 */
	public static final int[][] SLAYER_TASKS = {{1,87,90,4,5}, //low tasks
		{6,7,8,9,10}, //med tasks
		{11,12,13,14,15}, //high tasks
		{1,1,15,20,25}, //low reqs
		{30,35,40,45,50}, //med reqs
		{60,75,80,85,90}}; //high reqs

	public static final int ATTACK = 0;
	public static final int DEFENCE = 1;
	public static final int STRENGTH = 2;
	public static final int HITPOINTS = 3;
	public static final int RANGED = 4;
	public static final int PRAYER = 5;
	public static final int MAGIC = 6;
	public static final int COOKING = 7;
	public static final int WOODCUTTING = 16;
	public static final int FLETCHING = 9;
	public static final int FISHING = 10;
	public static final int FIREMAKING = 11;
	public static final int CRAFTING = 12;
	public static final int SMITHING = 13;
	public static final int MINING = 14;
	public static final int HERBLORE = 15;
	public static final int AGILITY = 16;
	public static final int THIEVING = 17;
	public static final int SLAYER = 18;
	public static final int FARMING = 19;
	public static final int RUNECRAFTING = 20;
	/**
	 * Skill Experience Multipliers
	 */	
	public static final int WOODCUTTING_EXPERIENCE = 20;
	public static final int MINING_EXPERIENCE = 20;
	public static final int SMITHING_EXPERIENCE = 20;
	public static final int FARMING_EXPERIENCE = 40;
	public static final int FIREMAKING_EXPERIENCE = 20;
	public static final int HERBLORE_EXPERIENCE = 20;
	public static final int FISHING_EXPERIENCE = 20;
	public static final int AGILITY_EXPERIENCE = 40;
	public static final int PRAYER_EXPERIENCE = 20;
	public static final int RUNECRAFTING_EXPERIENCE = 20;
	public static final int CRAFTING_EXPERIENCE = 20;
	public static final int THIEVING_EXPERIENCE = 20;
	public static final int SLAYER_EXPERIENCE = 5;
	public static final int COOKING_EXPERIENCE = 20;
	public static final int FLETCHING_EXPERIENCE = 20;
	public static final int HUNTER_EXPERIENCE = 20;
	public static final int CONSTRUCTION_EXPERIENCE = 200;
	public static final boolean ADMIN_ATTACKABLE = false;
	public static final boolean BOUNTY_HUNTER_ACTIVE = false;
	public static final int[] DROP_AND_DELETE_ON_DEATH = {

			6822, 6824, 6826, 6828, 6830, 6832, 6834, 6836, 6838, 6840, 6842, 6844, 6846, 6848, 6850, 10507, 10025 };
	public static final int[] ITEMS_KEPT_ON_DEATH = {

			12785, 12954, 15573, 8135, 11864, 11865, 12432, 12389, 12373, 12379, 12369, 12367, 12365, 12363, 7449, 611,
			8840, 8839, 8842, 11664, 15098, 12650, 12649, 12651, 12652, 15567, 12644, 12645, 12643, 15568, 12653, 12655,
			15571, 11663, 11665, 6570, 8845, 8846, 8847, 8848, 8849, 8850, 10551, 10548, 7462, 7461, 7460, 7459, 7458,
			7457, 7456, 7455, 7582, 15572, 12855, 12856,

			12806, 12807, 962, 11770, 11771, 11772, 11773, 551, 13140, 13107, 13115, 13120, 13124, 13132, 13103, 13136,
			13128, 13111, 13144, 12637, 12638, 12639, 6665, 6666, 12813, 12814, 12815, 12810, 12811, 12812, 12845, 9920,
			12887, 12888, 12889, 12890, 12891, 12892, 12893, 12894, 12895, 12896, 11919, 12956, 12957, 12958, 12959,
			2990, 2991, 2992, 2993, 2994, 2995, 12432, 12434, 2651, 8950, 8928, 12412, 12432, 12434, 2639, 2641, 2643,
			12321, 12323, 12325, 11280, 394, 430, 12335, 12414, 2653, 12436, 12434, 10396, 12337, 12327, 12325, 12323,
			2645, 2643, 11850, 11851, 11852, 11853, 11854, 11855, 11856, 11857, 11858, 11859, 11860, 11861, 13579,
			13580, 13581, 13582, 13583, 13584, 13585, 13586, 13587, 13588, 13589, 13590, 13591, 13592, 13593, 13594,
			13595, 13596, 13597, 13598, 13599, 13600, 13601, 13602, 13603, 13604, 13605, 13606, 13607, 13608, 13609,
			13610, 13611, 13612, 13613, 13614, 13615, 13616, 13617, 13618, 13619, 13620, 13621, 13622, 13623, 13624,
			2641 };
	public static final String[] UNDEAD = { "armoured zombie", "ankou", "banshee", "crawling hand", "dried zombie",
			"ghost", "ghostly warrior", "ghast", "mummy", "mighty banshee", "revenant imp", "revenant goblin",
			"revenant icefiend", "revenant pyrefiend", "revenant hobgoblin", "revenant vampyre", "revenant werewolf",
			"revenant cyclops", "revenant darkbeast", "revenant demon", "revenant ork", "revenant hellhound",
			"revenant knight", "revenant dragon", "shade", "skeleton", "skeleton brute", "skeleton thug",
			"skeleton warlord", "summoned zombie", "skorge", "tortured soul", "undead chicken", "undead cow",
			"undead one", "undead troll", "zombie", "zombie rat", "zogre" };
	/**
	 * Enable File Server (JagGrab)
	 */
		public static final boolean JagGrab_Enabled = true;
	public static final long[] UNTRADEABLE_ITEMS = {};
}
