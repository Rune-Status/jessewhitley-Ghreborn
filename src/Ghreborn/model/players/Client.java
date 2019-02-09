package Ghreborn.model.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import Ghreborn.Config;
import Ghreborn.Server;
import Ghreborn.clip.Region;
import Ghreborn.model.Location;
import Ghreborn.model.content.DailyGearBox;
import Ghreborn.model.content.DailySkillBox;
import Ghreborn.model.content.InstancedArea;
import Ghreborn.model.content.Lootbag;
import Ghreborn.model.content.MoneyBag;
import Ghreborn.model.content.MysteryBox;
import Ghreborn.model.content.QuickPrayer;
import Ghreborn.model.content.barrows.Barrows;
import Ghreborn.model.content.barrows.TunnelEvent;
import Ghreborn.model.content.clan.Clan;
import Ghreborn.model.content.dailytasks.DailyTasks;
import Ghreborn.model.content.dialogue.Dialogue;
import Ghreborn.model.content.donatorboxs.DonatorBox;
import Ghreborn.model.content.donatorboxs.ExtremeDonatorBox;
import Ghreborn.model.content.donatorboxs.SuperDonatorBox;
import Ghreborn.model.content.gambling.Flowers;
import Ghreborn.model.content.randomevents.RandomEvent;
import Ghreborn.model.content.randomevents.InterfaceClicking.impl.InterfaceClickHandler;
import Ghreborn.model.content.teleport.TeleportExecutor;
import Ghreborn.model.content.trails.TreasureTrails;
import Ghreborn.model.items.EquipmentSet;
import Ghreborn.model.items.Item;
import Ghreborn.model.items.ItemAssistant;
import Ghreborn.model.items.bank.Bank;
import Ghreborn.model.items.bank.BankPin;
import Ghreborn.model.minigames.bounty_hunter.BountyHunter;
import Ghreborn.model.minigames.fight_cave.FightCave;
import Ghreborn.model.minigames.inferno.Inferno;
import Ghreborn.model.minigames.inferno.Tzkalzuk;
import Ghreborn.model.minigames.raids.Raids;
import Ghreborn.model.minigames.rfd.DisposeTypes;
import Ghreborn.model.minigames.rogues_den.Wallsafe;
import Ghreborn.model.minigames.warriors_guild.WarriorsGuild;
import Ghreborn.model.minigames.warriors_guild.WarriorsGuildBasement;
import Ghreborn.model.multiplayer_session.trade.Trade;
import Ghreborn.model.shops.ShopAssistant;
import Ghreborn.model.sounds.Sound;
import Ghreborn.net.HostList;
import Ghreborn.net.Packet;
import Ghreborn.net.Packet.Type;
import Ghreborn.util.Misc;
import Ghreborn.util.Stream;
import Ghreborn.model.npcs.NPC;
import Ghreborn.model.npcs.NPCDeathTracker;
import Ghreborn.model.npcs.NPCHandler;
import Ghreborn.model.npcs.PetHandler;
import Ghreborn.model.npcs.PetHandler.Pets;
import Ghreborn.model.npcs.boss.Alchemical_Hydra.Alchemical_Hydra;
import Ghreborn.model.npcs.boss.Armadyl.Armadyl;
import Ghreborn.model.npcs.boss.Bandos.Bandos;
import Ghreborn.model.npcs.boss.Cerberus.Cerberus;
import Ghreborn.model.npcs.boss.Kalphite.Kalphite;
import Ghreborn.model.npcs.boss.Kraken.Kraken;
import Ghreborn.model.npcs.boss.Saradomin.Saradomin;
import Ghreborn.model.npcs.boss.Zamorak.Zamorak;
import Ghreborn.model.npcs.boss.instances.InstancedAreaManager;
import Ghreborn.model.npcs.boss.skotizo.Skotizo;
import Ghreborn.model.npcs.boss.vorkath.Vorkath;
import Ghreborn.model.npcs.boss.zulrah.Zulrah;
import Ghreborn.model.npcs.boss.zulrah.ZulrahLostItems;
import Ghreborn.model.players.combat.CombatAssistant;
import Ghreborn.model.players.combat.Damage;
import Ghreborn.model.players.combat.DamageQueueEvent;
import Ghreborn.model.players.combat.Hitmark;
import Ghreborn.model.players.skills.construction.House;
import Ghreborn.model.players.skills.construction.Room;
import Ghreborn.model.players.skills.cooking.Cooking;
import Ghreborn.model.players.skills.farming.Allotments;
import Ghreborn.model.players.skills.farming.Bushes;
import Ghreborn.model.players.skills.firemake.Firemaking;
import Ghreborn.model.players.skills.fletching.Fletching;
import Ghreborn.model.players.skills.herblore.Herblore;
import Ghreborn.model.players.skills.hunter.Hunter;
import Ghreborn.model.players.skills.mining.Mining;
import Ghreborn.model.players.skills.prayer.Prayer;
import Ghreborn.model.players.skills.runecrafting.Runecrafting;
import Ghreborn.model.players.skills.slayer.Slayer;
import Ghreborn.model.players.skills.thieving.Thieving;
import Ghreborn.model.players.skills.woodcutting.Woodcutting;
import Ghreborn.model.region.music.MusicManager;
import Ghreborn.core.PlayerHandler;
import Ghreborn.event.CycleEvent;
import Ghreborn.event.CycleEventContainer;
import Ghreborn.event.CycleEventHandler;
import Ghreborn.event.EventManager;
import Ghreborn.event.event.Event2;
import Ghreborn.event.Event;
import Ghreborn.event.EventContainer;
import Ghreborn.model.players.skills.*;
import Ghreborn.model.players.skills.Fishing.Fishing;
import Ghreborn.model.players.skills.agility.impl.GnomeAgility;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.*;
import java.io.*;
						

public class Client extends Player {

	private Channel session;
	public Channel getSession() {
		return session;
	}
	
    public int emotes = 0;
	public int totalLevel;
	public int xpTotal;
	public byte buffer[] = null;
	private int skillTask;
    private int destroyItem;
	private PlayerKill playerKills;
	private Cerberus cerberus = null;
	public int woodcuttingTree;
	private InterfaceClickHandler randomInterfaceClick = new InterfaceClickHandler(this);
	private final QuickPrayer quick = new QuickPrayer();
	private Tzkalzuk tzkalzuk = null;
	public InstancedArea instancedArea;
	private ZulrahLostItems lostItemsZulrah;
	private int clickItemId = -1;
	public NPC spawnedNpc;
	public Rights rights = Rights.PLAYER;
	public Stream inStream = null, outStream = null;
	public Zulrah zulrah = new Zulrah(this);
	private Smelting smelting = new Smelting(this);
	private Bandos bandos = new Bandos(this);
	private Vorkath vorkath = new Vorkath(this);
	private TeleportExecutor teleports = new TeleportExecutor();
	private MoneyBag moneybag = new MoneyBag(this);
	private DailyGearBox dailyGearBox = new DailyGearBox(this);
	private DailySkillBox dailySkillBox = new DailySkillBox(this);
	private MysteryBox mysteryBox = new MysteryBox(this);
	private TreasureTrails trails = new TreasureTrails(this);
	private DonatorBox donatorBox = new DonatorBox(this);
	private WarriorsGuild warriorsGuild = new WarriorsGuild(this);
	private WarriorsGuildBasement warriorsGuildbasement = new WarriorsGuildBasement(this);
	private SuperDonatorBox superdonatorBox = new SuperDonatorBox(this);
	private ExtremeDonatorBox extremedonatorBox = new ExtremeDonatorBox(this);
	private NPCDeathTracker npcDeathTracker = new NPCDeathTracker(this);
	private Armadyl armadyl = new Armadyl(this);
	private Alchemical_Hydra hydra = new Alchemical_Hydra(this);
	private Zamorak zamorak = new Zamorak(this);
	private SkillInterfaces skillinterface = new SkillInterfaces(this);
	private List<Byte> poisonDamageHistory = new ArrayList<>(4);
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private Trade trade = new Trade(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private Friends friend = new Friends(this);
	private Raids raid = new Raids(this);
	private Kalphite kalphite = new Kalphite(this);
	private BountyHunter bountyHunter = new BountyHunter(this);
	private Ignores ignores = new Ignores(this);
	private Kraken kraken = new Kraken(this);
	private Lootbag lootbag = new Lootbag(this);
	private Wallsafe wallsafe = new Wallsafe(this);
	//private TextHandler textHandler = new TextHandler(this);
	//private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Queue<Packet> queuedPackets = new LinkedList<Packet>();
	private Potions potions = new Potions(this);
	private MusicManager music = new MusicManager (this);
	private Barrows barrows = new Barrows(this);
	private Food food = new Food(this);
	private Dialogue dialogue = null;
	public PlayerCannon playerCannon;
	public DamageQueueEvent getDamageQueue() {
		return damageQueue;
	}
private DamageQueueEvent damageQueue = new DamageQueueEvent(this);

	/**
	 */
	Slayer slayer = new Slayer(this);
	private Runecrafting runecrafting = new Runecrafting();
	//private Woodcutting woodcutting = new Woodcutting();
	private Mining mine = new Mining(this);
	private Agility agility = new Agility(this);
	private Cooking cooking = new Cooking();
	private Crafting crafting = new Crafting(this);
	private Smithing smith = new Smithing(this);
	private Prayer prayer = new Prayer(this);
	private Fletching fletching = new Fletching(this);
	private SmithingInterface smithInt = new SmithingInterface(this);
	private Thieving thieving = new Thieving(this);
	private Firemaking firemaking = new Firemaking(this);
	private Herblore herblore = new Herblore(this);
	private FightCave fightcave = null;
	private int toxicBlowpipeCharge;
	//public Smelting.Bars bar = null;
	private int toxicBlowpipeAmmo;
	private int toxicBlowpipeAmmoAmount;
	private int serpentineHelmCharge;
	private int toxicStaffOfDeadCharge;
	private int tridentCharge;
	private long lastVenomHit;
	public boolean hasBeenPoisoned;
	public boolean hasBeenVenomed;
	private long lastVenomCure;
	private long venomImmunity;
	private long lastPoisonHit;
	private long lastPoisonCure;
	private long poisonImmunity;
	byte poisonDamage;
	private byte venomDamage;
	private boolean trading;
	private Skotizo skotizo = null;
	private int toxicTridentCharge;
	public int staffOfDeadCharge;
	public Allotments allotment = new Allotments(this);
	public Bushes bushes = new Bushes(this);
	private Logs logs = new Logs(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Saradomin saradomin = new Saradomin(this);
	public int lowMemoryVersion = 0;
	public int timeOutCounter = 0;
	public int returnCode = 2;
	public Future<?> currentTask2;
	public String lastClanChat = "";
	private String revertOption = "";
	private CycleEvent skilling2;
	
	private GnomeAgility gnomeAgility = new GnomeAgility();

	public GnomeAgility getGnomeAgility() {
		return gnomeAgility;
	}
	public MoneyBag getMoneyBag() {
		return moneybag;
	}
	public SkillInterfaces getSkillInterface() {
		return skillinterface;
	}
	public ZulrahLostItems getZulrahLostItems() {
		if (lostItemsZulrah == null) {
			lostItemsZulrah = new ZulrahLostItems(this);
		}
		return lostItemsZulrah;
	}
	public void setTrading(boolean trading) {
		this.trading = trading;
	}
	 public Vorkath getVorkath() {
	        return vorkath;
	    }
	 public Smelting getSmelting() {
		 return smelting;
	 }

	    public void setVorkath(Vorkath vorkath) {
	        this.vorkath = vorkath;
	    }
	public Skotizo createSkotizoInstance() {
		Boundary boundary = Boundary.SKOTIZO_BOSSROOM;

		int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

		skotizo = new Skotizo(this, boundary, height);

		return skotizo;
	}
	
	public Skotizo getSkotizo() {
		return skotizo;
	}
	public Logs getLogs() {
		return logs;
	}

	public Raids getRaids() {
		// TODO Auto-generated method stub
		return raid;
	}
	/**
	 * The amount of damage received when hit by toxic
	 * 
	 * @return the toxic damage
	 */
	public byte getPoisonDamage() {
		return poisonDamage;
	}
	/**
	 * Sets the current amount of damage received when hit by toxic
	 * 
	 * @param toxicDamage
	 *            the new amount of damage received
	 */
	public void setPoisonDamage(byte toxicDamage) {
		this.poisonDamage = toxicDamage;
	}
	public boolean isTrading() {
		return this.trading;
	}
	public Skilling getSkilling() {
		return skilling;
	}
	private Skilling skilling = new Skilling(this);
	public Client(Channel s, int _playerId) {
		super(_playerId);
		this.session = s;
		outStream = new Stream(new byte[Config.BUFFER_SIZE]);
		outStream.currentOffset = 0;
		inStream = new Stream(new byte[Config.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[Config.BUFFER_SIZE];
	}
	private long nameAsLong;
	public Bushes getBushes() {
		return bushes;
	}
	public long getNameAsLong() {
		return nameAsLong;
	}

	public void setNameAsLong(long hash) {
		this.nameAsLong = hash;
	}

	public Saradomin getSaradomin() {
		return saradomin;
	}

	public int getMaximumHealth() {
		int base = getLevelForXP(playerXP[3]);
		if (EquipmentSet.GUTHAN.isWearingBarrows(this) && getItems().isWearingItem(12853)) {
			base += 10;
		}
		return base;
	}
	private Bank bank;

	public Bank getBank() {
		if (bank == null)
			bank = new Bank(this);
		return bank;
	}

	private BankPin pin;

	public BankPin getBankPin() {
		if (pin == null)
			pin = new BankPin(this);
		return pin;
	}
	public Kraken getKraken() {
		return kraken;
	}
	public int getTask() {
		skillTask++;
		if (skillTask > Integer.MAX_VALUE - 2) {
			skillTask = 0;
		}
		return skillTask;
	}
	public void setSkilling(CycleEvent event) {
		this.skilling2 = event;
	}

	public CycleEvent getSkilling2() {
		return skilling2;
	}

	public boolean checkTask(int task) {
		return task == skillTask;
	}

	public void start(Dialogue dialogue) {
		this.dialogue = dialogue;
		if (dialogue != null) {
			getPA().closeAllWindows();
			dialogue.setNext(0);
			dialogue.setPlayer(this);
			dialogue.execute();
		} /*
			 * else if (getAttributes().get("pauserandom") != null) {
			 * getAttributes().remove("pauserandom"); }
			 */
	}
	public Allotments getAllotment() {
		return allotment;
	}
	public boolean validClient(int id) {
		if (id < 0 || id > Config.MAX_PLAYERS) {
			return false;
		}
		return validClient(getClient(id));
	}
	public boolean validClient(String name) {
		return validClient(getClient(name));
	}
	public boolean validClient(Client client) {
		return (client != null && !client.disconnected);
	}
	public boolean validNpc(int index) {
		if (index < 0 || index >= Config.MAX_NPCS) {
			return false;
		}
		NPC n = getNpc(index);
		if (n != null) {
			return true;
		}
		return false;
	}
	public NPC getNpc(int index) {
		return ((NPC) Server.npcHandler.npcs[index]);
	}
	public void yell(String s) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (validClient(i)) {
				getClient(i).sendMessage(s);
			}
		}
	}
	public void flushOutStream() {
		if (!session.isConnected() || disconnected || outStream.currentOffset == 0)
			return;

		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		Packet packet = new Packet(-1, Type.FIXED, ChannelBuffers.wrappedBuffer(temp));
		session.write(packet);
		outStream.currentOffset = 0;           
	}

	public void sendClan(String name, String message, String clan, int rights) {
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(rights);
		outStream.endFrameVarSize();
	}
	public static final int PACKET_SIZES[] = { 
			0, 0, 0, 1, -1, 0, 0, 0, 4, 0, // 0
		0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
		0, 2, 0, 6, -1, 12, 0, 0, 0, 0, // 20
		0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
		2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
		0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
		8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
		6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
		0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
		0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
		0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100
		0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
		1, 0, 6, 0, 16, 0, -1, -1, 2, 6, // 120
		0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
		6, 10, -1, 0, 0, 6, 0, 0, 0, 0, // 140
		0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
		0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
		0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
		0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
		2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
		4, 0, 0, 4, 7, 8, 0, 0, 10, 0, // 210
		0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
		1, 0, 4, 0, 6, 0, 6, 8, 1, 0, // 230
		0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240
		0, 0, 6, 6, 0, 0, 0 // 250
	};
	public boolean antifireEventRunning;
	public void destruct() {
		Hunter.abandon(this, null, true);
		if(session == null) 
			return;
		if (zulrah.getInstancedZulrah() != null) {
			InstancedAreaManager.getSingleton().disposeOf(zulrah.getInstancedZulrah());
		}
		if (kraken.getInstancedKraken() != null) {
			InstancedAreaManager.getSingleton().disposeOf(kraken.getInstancedKraken());
		}
		if (cerberus != null) {
			InstancedAreaManager.getSingleton().disposeOf(cerberus);
		}
		if (vorkath.getVorkathInstance() != null) { //if logging out from vorkath or while in vorkath instance
            getPA().movePlayer(2272, 4052, 0);
            vorkath.disposeInstance();
        }
		if (tzkalzuk != null) {
			InstancedAreaManager.getSingleton().disposeOf(tzkalzuk);
		}
		if (kalphite.getInstancedKalphite() != null) {
			InstancedAreaManager.getSingleton().disposeOf(kalphite.getInstancedKalphite());
		}
		if (bandos.getInstancedBandos() != null) {
			InstancedAreaManager.getSingleton().disposeOf(bandos.getInstancedBandos());
		}
		if (zamorak.getInstancedZamorak() != null) {
			InstancedAreaManager.getSingleton().disposeOf(zamorak.getInstancedZamorak());
		}
		if (saradomin.getInstancedSaradomin() != null) {
			InstancedAreaManager.getSingleton().disposeOf(saradomin.getInstancedSaradomin());
		}
		if (armadyl.getInstancedArmadyl() != null) {
			InstancedAreaManager.getSingleton().disposeOf(armadyl.getInstancedArmadyl());
		}
		if (hydra.getInstancedAlchemicalHydra() != null) {
			InstancedAreaManager.getSingleton().disposeOf(hydra.getInstancedAlchemicalHydra());
		}
		if (skotizo != null) {
			InstancedAreaManager.getSingleton().disposeOf(skotizo);
		}
		if (getHouse() != null) {
			getHouse().save();
			//getPA().movePlayer(2953, 3224, 0);
		}
		if (getPA().viewingOtherBank) {
			getPA().resetOtherBank();
		}
		if(getRights().isIronmans() && !getRights().isPlayer() && !getRights().isAdministrator() && !getRights().isOwner() && !getRights().isCoOwner() && !playerName.equalsIgnoreCase("raven")) {
		boolean debugMessage = false; 
		com.everythingrs.hiscores.Hiscores.update("1yas9sbywkw3j71agw4iiicnmi251x4tx8auv1vcz8c2d0io1orma598wc2jvhvgxtmhu4k7qfr",  "Ironman Mode", this.playerName, this.getRights().getValue(), this.playerXP, debugMessage);
		}    
		if(getRights().isHardcoreIronman() && !getRights().isPlayer() && !getRights().isAdministrator() && !getRights().isOwner() && !getRights().isCoOwner() && !playerName.equalsIgnoreCase("raven")) {
		boolean debugMessage = false; 
		com.everythingrs.hiscores.Hiscores.update("1yas9sbywkw3j71agw4iiicnmi251x4tx8auv1vcz8c2d0io1orma598wc2jvhvgxtmhu4k7qfr",  "Hardcore Ironman Mode", this.playerName, this.getRights().getValue(), this.playerXP, debugMessage);
		}    
		if(!getRights().isAdministrator() &&!getRights().isIronman() && !getRights().isOwner() && !getRights().isCoOwner() && !playerName.equalsIgnoreCase("raven")) {
		boolean debugMessage = false; 
		com.everythingrs.hiscores.Hiscores.update("1yas9sbywkw3j71agw4iiicnmi251x4tx8auv1vcz8c2d0io1orma598wc2jvhvgxtmhu4k7qfr",  "Normal Mode", this.playerName, this.getRights().getValue(), this.playerXP, debugMessage);
		}      
		Server.panel.removeEntity(playerName);
		if (underAttackBy > 0 || underAttackBy2 > 0)
			return;
		if (disconnected == true) {
			saveCharacter = true;
		}

		PlayerSave.saveGame(this);//dat is voor normale logout ja,maar voor unexpected logout meotn we bij destruct zijn denk ik
		Server.getMultiplayerSessionListener().removeOldRequests(this);
		if (clan != null) {
			clan.removeMember(this);
		}
		getFriends().notifyFriendsOfUpdate();
		PlayerHandler.playerCount--;
		Misc.println("[Logged out]: "+playerName+"");
		Server.getEventHandler().stop(this);
		CycleEventHandler.getSingleton().stopEvents(this);
		HostList.getHostList().remove(session);
		disconnected = true;
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;
		super.destruct();
	}

	public void sendMessage(String s) {
			if (getOutStream() != null) {
				outStream.createFrameVarSize(253);
				outStream.writeString(s);
				outStream.endFrameVarSize();
		}
	}
	public void clearPlayersInterface() {
	      for (int i = 8147; i < 8348; i++) {
	            getPA().sendFrame126("",i);
	      }
	};
	public void setSidebarInterface(int menuId, int form) {
			if (getOutStream() != null) {
				outStream.createFrame(71);
				outStream.writeWord(form);
				outStream.writeByteA(menuId);
		}
	}
	private Flowers flower = new Flowers(this);
	public Flowers getFlowers() {
		return flower;
	}

public MusicManager getMusic(){
	return music;
}
	public void initialize() {
		try {
			if (displayName.equalsIgnoreCase("notset")) {
				displayName = playerName;
				}
		Server.panel.addEntity(playerName);
		clearPlayersInterface();
		getPA().loadQuests();
			outStream.createFrame(249);
			outStream.writeByteA(1); // 1 for members, zero for free
			outStream.writeWordBigEndianA(index);
			//farming stuff
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (j == index)
					continue;
				if (PlayerHandler.players[j] != null) {
					if (PlayerHandler.players[j].playerName
							.equalsIgnoreCase(playerName))
						disconnected = true;
				}
			}
			//getPA().updatefarming();
			if(hasNpc == true) {
				if (summonId > 0) {
					Pets pet = PetHandler.forItem(summonId);
					if (pet != null) {
						PetHandler.spawn(this, pet, true, false);
					}
				}
			
			}
			for (int i = 0; i < 23; i++) {
				getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
				getPA().refreshSkill(i);
			}
			for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows
				prayerActive[p] = false;
				getPA().sendFrame36(PRAYER_GLOW[p], 0);
			}
			getPA().handleWeaponStyle();
			getPA().handleLoginText();
			accountFlagged = getPA().checkForFlags();
			// getPA().sendFrame36(43, fightMode-1);
			getPA().sendFrame36(108, 0);// resets autocast button
			getPA().sendFrame36(172, 1);
			getPlayerAction().setAction(false);
			getPlayerAction().canWalk(true);
			getPA().sendFrame126(runEnergy+"%", 149);
			getPA().sendFrame107(); // reset screen
			getPA().sendFrame36(170, mouseButton ? 1 : 0);
			getPA().sendFrame36(427, acceptAid ? 0 : 1);
			getPA().sendFrame36(400, 4);
			getPA().sendFrame36(171, chatEffects ? 0 : 1);
			getPA().sendFrame36(173, isRunning2 ? 0 : 1);
			getPA().sendFrame36(287, splitChat ? 1 : 0);
			getPA().setChatOptions(0, 0, 0); // reset private messaging options
			setSidebarInterface(1, 27263);
			setSidebarInterface(2, 638);
			setSidebarInterface(3, 3213);
			setSidebarInterface(4, 1644);
			setSidebarInterface(5, 5608);
			if (playerMagicBook == 0) {
				setSidebarInterface(6, 1151); // modern
			} else {
				if (playerMagicBook == 2) {
					setSidebarInterface(6, 29999); // lunar
				} else {
					setSidebarInterface(6, 12855); // ancient
				}
			}
			correctCoordinates();
			setSidebarInterface(7, 18128);
			setSidebarInterface(8, 5065);
			setSidebarInterface(9, 5715);
			setSidebarInterface(10, 2449);
			// setSidebarInterface(11, 4445); // wrench tab
			setSidebarInterface(11, 904); // wrench tab
			setSidebarInterface(12, 147); // run tab
			setSidebarInterface(13, 23418);
			setSidebarInterface(0, 2423);
			getMusic().load();
			setHouse(House.load(this));
			//Server.clanManager.joinOnLogin(this);
			if (playerName.equalsIgnoreCase("sgsrocks")){
				setRights(Rights.OWNER);
			        }
			if (playerName.equalsIgnoreCase("twistndshout")){
				setRights(Rights.Co_OWNER);
			        }
			if (playerName.equalsIgnoreCase("spadow")){
				setRights(Rights.DEVELOPER);
			        }
			if (playerName.equalsIgnoreCase("") || playerName.equalsIgnoreCase("") || playerName.equalsIgnoreCase("") || playerName.equalsIgnoreCase("")){
				setRights(Rights.MODERATOR);
			        }
			if (playerName.equalsIgnoreCase("") || playerName.equalsIgnoreCase("lp316")){
				setRights(Rights.ADMINISTRATOR);
			        }
				  
				  if (playerName.equalsIgnoreCase("") || playerName.equalsIgnoreCase("super") || playerName.equalsIgnoreCase("Twistndshout")){//Co Owners
					  setRights(Rights.Co_OWNER);
				  }
	            sendMessage("Welcome to Godzhell Reborn");
	            sendMessage("Staff applications are now open! do ::forums");
	            sendMessage("Do ::commands to see player commands");
	            sendMessage("Do ::discord to see newest updates and talk to players.");
	            sendMessage("Remember to do Vote every 12 hours for great rewards! and");
	            sendMessage("to bring more players to the server!");
	            if (Config.doubleEXPWeekend == true) {
					sendMessage("Enjoy Double EXP Weekend!");
				}
	           // if(!HasXmasItems()) {
	            //sendMessage("I see you havn't done the xmas event, Talk to Hans at home.", 255);
	          //  }
/*	            if(!getItems().playerHasItem(23936) && !getItems().bankContains(23936) && !getItems().isWearingItem(23936)) {
	            	sendMessage("Heres an bag of gold.");
	            getItems().addItem(23936, 1);
		} else {
	            	sendMessage("You allready have an bag of gold. :)");
	            	
	            }*/
	            if(getRights().isDonator()) {
	            	DonatorPod = true;
	            }
	            if(DonatorPod == true) {
	            	 //stillgfx(369, absY, absX);
	            	 gfx0(369);
	            }
	           // getPA().newWelc();

	            //sendMessage(
	              //      "There are currently " + PlayerHandler.getPlayerCount()
	                //    + " players On Godzhell Reborn");
			getPA().showOption(4, 0, "Trade With", 3);
			getPA().showOption(5, 0, "Follow", 4);
			getItems().resetItems(3214);
			getItems().sendWeapon(playerEquipment[playerWeapon],
					getItems().getItemName(playerEquipment[playerWeapon]));
			getItems().resetBonus();
			getItems().getBonus();
			getItems().writeBonus();
			getItems().setEquipment(playerEquipment[playerHat], 1, playerHat);
			getItems().setEquipment(playerEquipment[playerCape], 1, playerCape);
			getItems().setEquipment(playerEquipment[playerAmulet], 1,
					playerAmulet);
			getItems().setEquipment(playerEquipment[playerArrows],
					playerEquipmentN[playerArrows], playerArrows);
			getItems().setEquipment(playerEquipment[playerChest], 1,
					playerChest);
			getItems().setEquipment(playerEquipment[playerShield], 1,
					playerShield);
			getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
			getItems().setEquipment(playerEquipment[playerHands], 1,
					playerHands);
			getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
			getItems().setEquipment(playerEquipment[playerRing], 1, playerRing);
			getItems().setEquipment(playerEquipment[playerWeapon],
					playerEquipmentN[playerWeapon], playerWeapon);
			getCombat().getPlayerAnimIndex(
					getItems().getItemName(playerEquipment[playerWeapon])
					.toLowerCase());
			/* Login Friend List */
			outStream.createFrame(221);
			outStream.writeByte(2);

			outStream.createFrame(206);
			outStream.writeByte(0);
			outStream.writeByte(getPrivateChat());
			outStream.writeByte(0);
			getFriends().sendList();
			getIgnores().sendList();

			//getPA().logIntoPM();
			getItems().addSpecialBar(playerEquipment[playerWeapon]);
			saveTimer = Config.SAVE_TIMER;
			saveCharacter = true;
			Misc.println("[REGISTERED]: " + playerName + "");
			totalLevel = getPA().totalLevel();
			xpTotal = getPA().xpTotal();
			//getPA().sendFrame126("Combat Level: " + combatLevel + "", 3983);
			getPA().sendFrame126("Level: " + totalLevel + "", 3984);
			handler.updatePlayer(this, outStream);
			handler.updateNPC(this, outStream);
			flushOutStream();
			globalMessage();
			getPA().resetFollow();
			getPA().clearClanChat();
			getPA().resetFollow();
			getPA().setClanData();
			//Server.clanManager.getHelpClan().addMember(this);
/*			if(HasEasterItems)
				getPA().addEaster();*/
			if (addStarter){
				getPA().addStarter();
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Player c2 = (Player) PlayerHandler.players[j];
					c2.sendMessage("[<img=10><col=255>New Player</col>] " + Misc.ucFirst(playerName)
							+ " </col>has logged in! Welcome!");
				}
			}
			}
			if (autoRet == 1)
				getPA().sendFrame36(172, 1);
			else
				getPA().sendFrame36(172, 0);
			DailyTasks.complete(this);
			//DailyTasks.assignTask(this);
			RandomEvent.startRandomEvent(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean isSusceptibleToPoison() {
		return System.currentTimeMillis() - this.lastPoisonCure > this.poisonImmunity;
	}
	/**
	 * The time in milliseconds that the player healed themselves of poison
	 * 
	 * @return the last time the player cured themself of poison
	 */
	public long getLastPoisonCure() {
		return lastPoisonCure;
	}

	/**
	 * Sets the time in milliseconds that the player cured themself of poison
	 * 
	 * @param lastPoisonCure
	 *            the last time the player cured themselves
	 */
	public void setLastPoisonCure(long lastPoisonCure) {
		this.lastPoisonCure = lastPoisonCure;
	}

	public void update() {
			handler.updatePlayer(this, outStream);
			handler.updateNPC(this, outStream);
			flushOutStream();
	}
	public void logout() {
		if (this.clan != null) {
			this.clan.removeMember(this);
		}
		if (getPA().viewingOtherBank) {
			getPA().resetOtherBank();
		}
		if (!isIdle && underAttackBy2 > 0) {
			sendMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (underAttackBy > 0) {
			sendMessage("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		if (logoutDelay.elapsed(10000)) {
			Hunter.abandon(this, null, true);
			outStream.createFrame(109);
			if (skotizo != null)
				skotizo.end(DisposeTypes.INCOMPLETE);
			if (getHouse() != null) {
				getHouse().save();
				//getPA().movePlayer(2953, 3224, 0);
			}
			Server.getEventHandler().stop(this);
			CycleEventHandler.getSingleton().stopEvents(this);
			properLogout = true;
			disconnected = true;
			//CycleEventHandler.getSingleton().stopEvents(this);
		}
	}
	public void stillgfx(int id, int Y, int X)
	{
	for (Player p : Server.playerHandler.players)
	{
	if(p != null)
	{
	Client person = (Client)p;
	if((person.playerName != null || person.playerName != "null"))
	{
	if(person.distanceToPoint(X, Y) <= 60)
	{
	person.getPA().stillgfx2(id, Y, X);
	}
	}
	}
	}
	}
	public void globalMessage() {
		EventManager.getSingleton().addEvent(new Event() {
			public void execute(EventContainer c) {
				if(Config.GLOBAL_MESSAGE == 0) {
					sendMessage("[Information] Make sure you are registered on the forums!", 255);
					Config.GLOBAL_MESSAGE = 1;
				} else if(Config.GLOBAL_MESSAGE == 1) {
					sendMessage("[Information] Never play from any other website but www.Ghreborn.com",255);
					Config.GLOBAL_MESSAGE = 2;
				} else if(Config.GLOBAL_MESSAGE == 2) {
					sendMessage("[Information] Staff members will never ask you for your password!",255);
					Config.GLOBAL_MESSAGE = 3;
				} else if(Config.GLOBAL_MESSAGE == 3) {
					sendMessage("[Information] Be sure to report any bugs on the forums.",255);
					Config.GLOBAL_MESSAGE = 4;
				} else if(Config.GLOBAL_MESSAGE == 4) {
					sendMessage("[Information] Donating to the server grants additional perks!",255);
					Config.GLOBAL_MESSAGE = 5;
				} else if(Config.GLOBAL_MESSAGE == 5) {
					sendMessage("[Information] Thank-You for playing Ghreborn!",255);
					Config.GLOBAL_MESSAGE = 6;
				} else if(Config.GLOBAL_MESSAGE == 6) {
					sendMessage("[Information] Remember to Vote Every 12 Hours. :)",255);
					Config.GLOBAL_MESSAGE = 0;
				}
	        }
	    }, 600000); // Timer that runs on milliseconds
	};
	public int packetSize = 0, packetType = -1;
	public int donatorPoints = 0;

	public void process() {
		getItems().update();
		getAgilityHandler().agilityProcess(this);
		//startWinterSpawnTimer();
		if (RANGE_ATTACK == 1) {
			MAGIC_ATTACK = 0;
			MELEE_ATTACK = 0;
			RANGE_ATTACK = 0;
			RANDOM = 0;
			RANDOM_MELEE = 0;
		}
		if (getInstancedArea() != null) {
			getInstancedArea().process();
		}
		if(Boundary.isIn(this, Boundary.KARUULM_SLAYER_DUNGEON)) {
			if(playerEquipment[playerFeet] == 23037/*getItems().playerHasEquipped(23037)*/) {
			} else {
				appendDamage(4, Hitmark.HIT);
			}
		}
		if (vorkath != null) {
            if (vorkath.getVorkathInstance() != null) {
                if (vorkath.getNpc() != null) {
                    if (distanceToPoint(vorkath.getNpc().getX(), vorkath.getNpc().getY()) >= 40) {
                        vorkath.disposeInstance();
                        sendMessage("Vorkath seems to have despawned..");
                    }
                }
            }
        }

		
		secondsPlayed += 1; // done in ticks
		if (secondsPlayed >= 100) {
			minutesPlayed += 1;
			getPA().sendFrame126("<col=FF7F00>Days:</col> <col=ffffff>" + daysPlayed + "</col><col=FF7F00> Hrs:</col> <col=ffffff>" + hoursPlayed
					+ "</col><col=FF7F00> Mins:</col> <col=ffffff>" + minutesPlayed + "</col>", 29162);
			secondsPlayed = 0;
		}
		if(Boundary.isIn(this, Boundary.STAFF_ZONE)){
			if(!getRights().isStaff()){
				getPA().movePlayer(3254, 3287, 0);
			}
		}
		if(Boundary.isIn(this, Boundary.BANKS)){
			if (playerLevel[3] < getMaximumHealth()) {
			playerLevel[3] += 10;
			}
			if (playerLevel[3] > getMaximumHealth()) {
				playerLevel[3] = getMaximumHealth();
			}
				getPA().refreshSkill(3);
			
		}
		if (getPoisonDamage() > 0
				&& ((System.currentTimeMillis() - getLastPoisonHit() > TimeUnit.MINUTES.toMillis(1))
						|| System.currentTimeMillis() - getLastPoisonHit() > 12000)) {
			if (!hasBeenPoisoned && playerEquipment[playerHat] != 12931) {
				sendMessage("You have been poisoned!");
			}
			hasBeenPoisoned = true;
			appendPoisonDamage();
			setLastPoisonHit(System.currentTimeMillis());
		}
		if (minutesPlayed == 60) {
			hoursPlayed += 1;
			minutesPlayed = 0;
		}
		if (hoursPlayed == 24) {
			daysPlayed += 1;
			hoursPlayed = 0;
		}
		if(prestigeLevel >=1) {
			prestigePoints += prestigeLevel*1000;
		}

/*		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer container) {
			if(c.getRights().isContributor()) {
				DonatorPoints += 1000;
				c.getPA().sendFrame126("@or1@Donator Points: <col=ffffff>"+c.DonatorPoints,
						29167);
				c.sendMessage("Because Your Normal Donator heres 1000 Donator Points for Being Active.");
			}
			if(c.getRights().isSuperDonater()) {
				DonatorPoints += 2000;
				c.getPA().sendFrame126("@or1@Donator Points: <col=ffffff>"+c.DonatorPoints,
						29167);
				c.sendMessage("Because Your Super Donator heres 2000 Donator Points for Being Active.");
			}
			if(c.getRights().isExtremeDonator()) {
				DonatorPoints += 5000;
				c.getPA().sendFrame126("@or1@Donator Points: <col=ffffff>"+c.DonatorPoints,
						29167);
				c.sendMessage("Because Your Extreme Donator heres 5000 Donator Points for Being Active.");
			}
			if(c.getRights().isVIP()) {
				DonatorPoints += 15000;
				c.getPA().sendFrame126("@or1@Donator Points: <col=ffffff>"+c.DonatorPoints,
						29167);
				c.sendMessage("Because Your Legendary Donator heres 15000 Donator Points for Being Active.");
			}

		}
		@Override
		public void stop() {
		}
	}, 3600);*/
			
		//c.getPA().sendFrame126("<col=FF7F00>Players:</col> <col=3CB71E>"+ Server.playerHandler.getPlayersOnline()+"", 29163);
		if(isDead && respawnTimer == -6) {
			getPA().applyDead();
		}
		if (respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if (respawnTimer == 12) {
			respawnTimer--;
			animation(0x900);
		}
		if (Boundary.isIn(this, Zulrah.BOUNDARY) && getZulrahEvent().isInToxicLocation()) {
			appendDamage(1 + Misc.random(3), Hitmark.VENOM);
		}
		if (getVenomDamage() > 0 && System.currentTimeMillis() - getLastVenomHit() > 15_0000) {
			if (!hasBeenVenomed && playerEquipment[playerHat] != 12931) {
				sendMessage("You have been infected by venom!");
			}
			hasBeenVenomed = true;
			appendVenomDamage();
			setLastVenomHit(System.currentTimeMillis());
		}
		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (hitDelay > 0) {
			hitDelay--;
		}
		getAgilityHandler().agilityProcess(this);
		if (specDelay.elapsed(Config.INCREASE_SPECIAL_AMOUNT)) {
			specDelay.reset();
			if (specAmount < 10) {
				specAmount += 1;
				if (specAmount > 10)
					specAmount = 10;
				getItems().addSpecialBar(playerEquipment[playerWeapon]);
			}
		}
		if (runEnergy < 100) {
			if (System.currentTimeMillis() > getPA().getAgilityRunRestore() + lastRunRecovery) {
				runEnergy++;
				lastRunRecovery = System.currentTimeMillis();
				getPA().sendFrame126(runEnergy+"%", 149);
			}
		}
		getCombat().handlePrayerDrain();
		if (System.currentTimeMillis() - singleCombatDelay > 5000) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 5000) {
			underAttackBy2 = 0;
		}
		if (inWild() && Boundary.isIn(this, Boundary.SAFEPK)) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			if (Config.SINGLE_AND_MULTI_ZONES) {
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			} else {
				getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			}
			getPA().showOption(3, 0, "Attack", 1);
			if (Config.BOUNTY_HUNTER_ACTIVE && !inClanWars()) {
				getPA().walkableInterface(28000);
				getPA().sendFrame171(1, 28070);
				getPA().sendFrame171(0, 196);
			} else {
				getPA().walkableInterface(197);
			}
	} else if (inWild() && !inClanWars() && !Boundary.isIn(this, Boundary.SAFEPK)) {
		int modY = absY > 6400 ? absY - 6400 : absY;
		wildLevel = (((modY - 3520) / 8) + 1);
		if (Config.SINGLE_AND_MULTI_ZONES) {
			getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
		} else {
			getPA().multiWay(-1);
			getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
		}
		getPA().showOption(3, 0, "Attack", 1);
		if (Config.BOUNTY_HUNTER_ACTIVE && !inClanWars()) {
			getPA().walkableInterface(28000);
			getPA().sendFrame171(1, 28070);
			getPA().sendFrame171(0, 196);
		} else {
			getPA().walkableInterface(197);
		}

		// } else if (Boundary.isIn(this, Boundary.SKELETAL_MYSTICS)) {
		// getPA().walkableInterface(42300);
	} else if (inClanWars() && inWild()) {
		getPA().showOption(3, 0, "Attack", 1);
		getPA().walkableInterface(197);
		getPA().sendFrame126("@yel@3-126", 199);
		wildLevel = 126;
	} else if (Boundary.isIn(this, Boundary.SCORPIA_LAIR)) {
		getPA().sendFrame126("@yel@Level: 54", 199);
		// getPA().walkableInterface(197);
		wildLevel = 54;
	} else if (getItems().isWearingItem(10501, 3) && !inWild()) {
		getPA().showOption(3, 0, "Throw-At", 1);
	} else if (inEdgeville()) {
		if (Config.BOUNTY_HUNTER_ACTIVE) {
			if (bountyHunter.hasTarget()) {
				getPA().walkableInterface(28000);
				getPA().sendFrame171(0, 28070);
				getPA().sendFrame171(1, 196);
				bountyHunter.updateOutsideTimerUI();
			} else {
				getPA().walkableInterface(-1);
			}
		} else {
			getPA().sendFrame99(0);
			getPA().walkableInterface(-1);
			getPA().showOption(3, 0, "Null", 1);
		}
		getPA().showOption(3, 0, "null", 1);
/*	} else if (Boundary.isIn(this, PestControl.LOBBY_BOUNDARY)) {
		getPA().walkableInterface(21119);
		PestControl.drawInterface(this, "lobby");
	} else if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
		getPA().walkableInterface(21100);
		PestControl.drawInterface(this, "game");*/
	} else if ((inDuelArena() || Boundary.isIn(this, Boundary.DUEL_ARENA))) {
		getPA().walkableInterface(201);
		if (Boundary.isIn(this, Boundary.DUEL_ARENA)) {
			getPA().showOption(3, 0, "Attack", 1);
		} else {
			getPA().showOption(3, 0, "Challenge", 1);
		}
		wildLevel = 126;
	} else if (barrows.inBarrows()) {
		barrows.drawInterface();
		getPA().walkableInterface(27500);
	} else if (inGodwars()) {
		//godwars.drawInterface();
		//getPA().walkableInterface(16210);
	} else if (inCwGame || inPits) {
		getPA().showOption(3, 0, "Attack", 1);
	} else if (getPA().inPitsWait()) {
		getPA().showOption(3, 0, "Null", 1);
	} else if (Boundary.isIn(this, Boundary.SKOTIZO_BOSSROOM)) {
		getPA().walkableInterface(29230);
	} else {
		getPA().walkableInterface(-1);
		getPA().showOption(3, 0, "Null", 1);
	}
	if (Boundary.isIn(this, Barrows.TUNNEL)) {
		if (!Server.getEventHandler().isRunning(this, "barrows_tunnel")) {
			Server.getEventHandler().submit(new TunnelEvent("barrows_tunnel", this, 1));
		}
		getPA().sendFrame99(2);
	} else {
		if (Server.getEventHandler().isRunning(this, "barrows_tunnel")) {
			Server.getEventHandler().stop(this, "barrows_tunnel");
		}
		getPA().sendFrame99(0);
	}
	if (Boundary.isIn(this, Boundary.PURO_PURO)) {
		getPA().sendFrame99(2);
	}
	if (!inWild()) {
		wildLevel = 0;
	}

		if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}

		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}
		if (!inMulti() && inWild())
			getPA().sendFrame70(30, 0, 196);
		else if (inMulti() && inWild())
			getPA().sendFrame70(0, 0, 196);
		if (this.skullTimer > 0) {
			--skullTimer;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}
		}
		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (PlayerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY, PlayerHandler.players[frozenBy].absX,
						PlayerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}
		if (attackTimer > 0) {
			attackTimer--;
		}
		if (followId > 0) {
			getPA().followPlayer();
		} else if (followId2 > 0) {
			getPA().followNpc();
		}

		if (hitDelay == 1) {
			if (oldNpcIndex > 0) {
				getCombat().delayedHit(this, oldNpcIndex);
				
			}
			if(oldPlayerIndex > 0){
			getCombat().delayedHit(this, oldPlayerIndex);	
			}
		}
		if (attackTimer <= 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		}
	}

	public void setCurrentTask(Future<?> task) {
		currentTask2 = task;
	}
	/**
	 * The time in milliseconds that the player healed themselves of venom
	 * 
	 * @return the last time the player cured themself of poison
	 */
	public long getLastVenomCure() {
		return lastVenomCure;
	}
	/**
	 * The duration of time in milliseconds the player is immune to venom for
	 * 
	 * @return the duration of time the player is immune to poison for
	 */
	public long getVenomImmunity() {
		return venomImmunity;
	}

	/**
	 * Modifies the current duration of venom immunity
	 * 
	 * @param duration
	 *            the new duration
	 */
	public void setVenomImmunity(long duration) {
		this.venomImmunity = duration;
	}
	public void appendPoisonDamage() {
		if (hasBeenVenomed) {
			hasBeenPoisoned = false;
			getPA().requestUpdates();
			return;
		}
		if (poisonDamage <= 0) {
			sendMessage("The poison has subsided.");
			hasBeenPoisoned = false;
			getPA().requestUpdates();
	Client client = this;
		if (poisonDamageHistory.size() >= 4) {
			poisonDamageHistory.clear();
			poisonDamage--;
		}
		poisonDamageHistory.add(poisonDamage);
		appendDamage(poisonDamage, Hitmark.POISON);
		client.getPA().requestUpdates();
	}
	}
	/**
	 * Sets the time in milliseconds that the player cured themself of poison
	 * 
	 * @param lastVenomCure
	 *            the last time the player cured themselves
	 */
	public void setLastVenomCure(long lastVenomCure) {
		this.lastVenomCure = lastVenomCure;
	}
	public Future<?> getCurrentTask() {
		return currentTask2;
	}

	public synchronized Stream getInStream() {
		return inStream;
	}

	public synchronized int getPacketType() {
		return packetType;
	}

	public synchronized int getPacketSize() {
		return packetSize;
	}

	public synchronized Stream getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public Dialogue getDialogue() {
		return dialogue;
	}
	public ShopAssistant getShops() {
		return shopAssistant;
	}


	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}
	public Potions getPotions() {
		return potions;
	}

	public Food getFood() {
		return food;
	}

	/**
	 * Skill Constructors
	 */
	public Slayer getSlayer() {
		if (slayer == null) {
			slayer = new Slayer(this);
		}
		return slayer;
	}

	public Runecrafting getRunecrafting() {
		return runecrafting;
	}
	public MysteryBox getMysteryBox() {
		return mysteryBox;
	}
	public Wallsafe getWallSafe() {
		return wallsafe;
	}

	public Mining getMining() {
		return mine;
	}

	public Cooking getCooking() {
		return cooking;
	}

	public Agility getAgility() {
		return agility;
	}


	public Crafting getCrafting() {
		return crafting;
	}

	public Smithing getSmithing() {
		return smith;
	}


	public Thieving getThieving() {
		return thieving;
	}

	public Herblore getHerblore() {
		return herblore;
	}

	public Firemaking getFiremaking() {
		return firemaking;
	}

	public SmithingInterface getSmithingInt() {
		return smithInt;
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public Fletching getFletching() {
		return fletching;
	}

	/**
	 * End of Skill Constructors
	 */

	public void queueMessage(Packet arg1) {
		synchronized(queuedPackets) {
			queuedPackets.add(arg1);
		}
	}

	public void correctCoordinates() {
	
		final Boundary fc = Boundary.FIGHT_CAVE;
		int x = teleportToX;
		int y = teleportToY;
		if (x > fc.getMinimumX() && x < fc.getMaximumX() && y > fc.getMinimumY() && y < fc.getMaximumY()) {

			getPA().movePlayer(absX, absY, heightLevel * 4);
			sendMessage("Wave " + (this.waveId + 1) + " will start in approximately 5-10 seconds. ");
			getFightCave().spawn();
		}

	}

	public void addNPC(int npcType, int maxHit, int attack, int defence) {
	//x	y	height	walk	maxhit	attack	defence	desc
		//Server.npcHandler.spawnNpc(c, npcType, absX, absY, heightLevel, 1, 120, 7, 70, 70, false, false);
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(".//Data/Spawn.txt", true));
		    try {
				out.newLine();
				out.write("spawn = "+npcType+"	"+absX+"	"+absY+"	"+heightLevel+"	1	"+maxHit+"	"+attack+"	"+defence+"	"+Ghreborn.definitions.NPCCacheDefinition.forID(npcType).getName()+".");
		    } finally {
				out.close();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public House getHouse() {
		return house;
	}
	public Zulrah getZulrahEvent() {
		return zulrah;
	}
	
	public void setDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}
	/**
	 * Outputs a send packet which is built from the data
	 * params provided towards a connected user client channel.
	 * @param id The identification number of the sound.
	 * @param volume The volume amount of the sound (1-100)
	 * @param delay The delay (0 = immediately 30 = 1/2cycle 60=full cycle) before
	 * the sound plays.
	 */
	public void sendSound(int id, int type, int delay, int volume)  {
		try {
			outStream.createFrameVarSize(174);
			outStream.writeWord(id);
			outStream.writeByte(type);
			outStream.writeWord(delay);
			outStream.writeWord(volume);
			updateRequired = true;
			appearanceUpdateRequired = true;
			outStream.endFrameVarSize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Outputs a send packet which is built from the data
	 * params provided towards a connected user client channel.
	 * @param id The identification number of the sound.
	 * @param volume The volume amount of the sound (1-100)
	 */
	public void sendSound(int id, int volume) {
		sendSound(id, 000, 000, 10);
	}

	/**
	 * Outputs a send packet which is built from the data
	 * params provided towards a connected user client channel.
	 * @param id The identification number of the sound.
	 */
	public void sendSound(int id) {
		sendSound(id, 100);//pretty sure it's 100 just double check
		// otherwise it will be 1
	}

	public int getPrivateChat() {
		return privateChat;
	}

	public Friends getFriends() {
		return friend;
	}
	public Ignores getIgnores() {
		return ignores;
	}

	public void setPrivateChat(int option) {
		this.privateChat = option;
	}
private Sound sound = new Sound(this);
    public Sound getSound() {
    return sound;
    }
	public Kalphite getKalphite() {
		return kalphite;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}
	public void setHouse(House house) {
		this.house = house;
	}
		public boolean getHouse;
		public boolean inHouse;
		private House house;
		public boolean isMorphed = false;
		public long hunting;
		public boolean catchingImp;
		public boolean usingMelee;
		public int krakenTent;
		public String getUsername() {
			// TODO Auto-generated method stub
			return playerName;
		}
		public void sendMessage(String s, int color) {
			if (getOutStream() != null) {
				s = "<col=" + color + ">" + s + "</col>";
				outStream.createFrameVarSize(253);
				outStream.writeString(s);
				outStream.endFrameVarSize();
			}

		}
		private HashMap<String, ArrayList<Damage>> damageReceived = new HashMap<>();
		public boolean teleporting;
		public long teleDelay;
		private int dragonfireShieldCharge;
		private long lastDragonfireShieldAttack;
		private boolean dragonfireShieldActive;
		private int[] runeEssencePouch = new int[3], pureEssencePouch = new int[3];
		public void addDamageReceived(String player, int damage) {
			if (damage <= 0) {
				return;
			}
			Damage combatDamage = new Damage(damage);
			if (damageReceived.containsKey(player)) {
				damageReceived.get(player).add(new Damage(damage));
			} else {
				damageReceived.put(player, new ArrayList<>(Arrays.asList(combatDamage)));
			}
		}
		public Lootbag getLoot() {
			return lootbag;
		}
		public void resetDamageReceived() {
			damageReceived.clear();
		}
		public FightCave getFightCave() {
			if (fightcave == null)
				fightcave = new FightCave(this);
			return fightcave;
		}


		/**
		 * Retrieves the bounty hunter instance for this client object. We use lazy
		 * initialization because we store values from the player save file in the
		 * bountyHunter object upon login. Without lazy initialization the value
		 * would be overwritten.
		 * 
		 * @return the bounty hunter object
		 */
		public BountyHunter getBH() {
			if (Objects.isNull(bountyHunter)) {
				bountyHunter = new BountyHunter(this);
			}
			return bountyHunter;
		}

		public PlayerKill getPlayerKills() {
			if (Objects.isNull(playerKills)) {
				playerKills = new PlayerKill();
			}
			return playerKills;
		}
		/**
		 * Returns the single instance of the {@link NPCDeathTracker} class for this
		 * player.
		 * 
		 * @return the tracker clas
		 */
		public NPCDeathTracker getNpcDeathTracker() {
			return npcDeathTracker;
		}

		public Bandos getBandos() {
			return bandos;
		}
		public Armadyl getArmadyl() {
			return armadyl;
		}
		public Alchemical_Hydra getAlchemicalHydra() {
			return hydra;
		}
		public void setInstancedArea(InstancedArea instancedArea) {
			this.instancedArea = instancedArea;
			// instancedArea.onStart();
		}

		public InstancedArea getInstancedArea() {
			return instancedArea;
		}

		public Zamorak getZamorak() {
			return zamorak;
		}

		public void setDragonfireShieldCharge(int charge) {
			this.dragonfireShieldCharge = charge;
		}

		public int getDragonfireShieldCharge() {
			return dragonfireShieldCharge;
		}

		public void setLastDragonfireShieldAttack(long lastAttack) {
			this.lastDragonfireShieldAttack = lastAttack;
		}

		public long getLastDragonfireShieldAttack() {
			return lastDragonfireShieldAttack;
		}

		public boolean isDragonfireShieldActive() {
			return dragonfireShieldActive;
		}
		public int getToxicBlowpipeCharge() {
			return toxicBlowpipeCharge;
		}

		public void setToxicBlowpipeCharge(int charge) {
			this.toxicBlowpipeCharge = charge;
		}

		public int getToxicBlowpipeAmmo() {
			return toxicBlowpipeAmmo;
		}

		public int getToxicBlowpipeAmmoAmount() {
			return toxicBlowpipeAmmoAmount;
		}

		public void setToxicBlowpipeAmmoAmount(int amount) {
			this.toxicBlowpipeAmmoAmount = amount;
		}

		public void setToxicBlowpipeAmmo(int ammo) {
			this.toxicBlowpipeAmmo = ammo;
		}

		public int getSerpentineHelmCharge() {
			return this.serpentineHelmCharge;
		}
		public String getLastClanChat() {
			return lastClanChat;
		}

		public void setLastClanChat(String founder) {
			lastClanChat = founder;
		}

		public int getToxicStaffOfDeadCharge() {
			return this.toxicStaffOfDeadCharge;
		}

		public void setSerpentineHelmCharge(int charge) {
			this.serpentineHelmCharge = charge;
		}

		public void setToxicStaffOfDeadCharge(int charge) {
			this.toxicStaffOfDeadCharge = charge;
		}

		public int getTridentCharge() {
			return tridentCharge;
		}

		public void setTridentCharge(int tridentCharge) {
			this.tridentCharge = tridentCharge;
		}

		public int getToxicTridentCharge() {
			return toxicTridentCharge;
		}

		public void setToxicTridentCharge(int toxicTridentCharge) {
			this.toxicTridentCharge = toxicTridentCharge;
		}

		public void setDragonfireShieldActive(boolean dragonfireShieldActive) {
			this.dragonfireShieldActive = dragonfireShieldActive;
		}
		public Trade getTrade() {
			return trade;
		}
		/**
		 * Sets the current amount of damage received when hit by venom
		 * 
		 * @param venomDamage
		 *            the new amount of damage received
		 */
		public void setVenomDamage(byte venomDamage) {
			this.venomDamage = venomDamage;
		}

		/**
		 * The time in milliseconds of the last venom damage the player received
		 * 
		 * @return the time in milliseconds of the last venom damage hit
		 */
		public long getLastVenomHit() {
			return lastVenomHit;
		}

		/**
		 * Sets the last time, in milliseconds, the venom damaged the player
		 * 
		 * @param toxic
		 *            the time in milliseconds
		 */
		public void setLastVenomHit(long toxic) {
			lastVenomHit = toxic;
		}
		/**
		 * The amount of damage received when hit by venom
		 * 
		 * @return the venom damage
		 */
		public byte getVenomDamage() {
			return venomDamage;
		}

		/**
		 * Determines if the player is susceptible to venom by comparing the
		 * duration of their immunity to the time of the last cure.
		 * 
		 * @return true of they can be infected by venom.
		 */
		public boolean isSusceptibleToVenom() {
			return System.currentTimeMillis() - lastVenomCure > venomImmunity && !getItems().isWearingItem(12931);
		}
		public void appendVenomDamage() {
			if (getItems().isWearingItem(12931)) {
				sendMessage("Your sepertine helm protects you from the venom.");
				getPA().requestUpdates();
				return;
			}
			if (venomDamage <= 0) {
				sendMessage("Your venom infection has cleared up!");
				hasBeenVenomed = false;
				getPA().requestUpdates();
				return;
			}
			venomDamage += 2;
			if (venomDamage >= 20) {
				venomDamage = 20;
			}
			appendDamage(venomDamage, Hitmark.VENOM);
			getPA().requestUpdates();
		}
		public int getRuneEssencePouch(int index) {
			return runeEssencePouch[index];
		}

		public void setRuneEssencePouch(int index, int runeEssencePouch) {
			this.runeEssencePouch[index] = runeEssencePouch;
		}
		
		public int getPureEssencePouch(int index) {
			return pureEssencePouch[index];
		}

		public void setPureEssencePouch(int index, int pureEssencePouch) {
			this.pureEssencePouch[index] = pureEssencePouch;
		}
		public Inferno inferno = new Inferno(this, Boundary.INFERNO, 0);
		public String statedInterface = "";
		private NPC randomEventNpc;
		
		
		public Inferno getInfernoMinigame() {
			return inferno;
		}
		
		public Inferno createInfernoInstance() {
			Boundary boundary = Boundary.INFERNO;

			int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

			inferno = new Inferno(this, boundary, height);

			return inferno;
		}
		public Cerberus createCerberusInstance() {
			Boundary boundary = Boundary.BOSS_ROOM_WEST;

			int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

			cerberus = new Cerberus(this, boundary, height);

			return cerberus;
		}

		public Tzkalzuk createTzkalzukInstance() {
			Boundary boundary = Boundary.INFERNO;

			int height = InstancedAreaManager.getSingleton().getNextOpenHeightCust(boundary, 4);

			tzkalzuk = new Tzkalzuk(this, boundary, height);

			return tzkalzuk;
		}
		public Tzkalzuk getInferno() {
			return tzkalzuk;
		}
		public DailyGearBox getDailyGearBox() {
			return dailyGearBox;
		}
		public DailySkillBox getDailySkillBox() {
			return dailySkillBox;
		}
		public DonatorBox getDonatorBox() {
			return donatorBox;
		}
		public SuperDonatorBox getSuperDonatorBox() {
			return superdonatorBox;
		}
		public ExtremeDonatorBox getExtremeDonatorBox() {
			return extremedonatorBox;
		}
		public Coordinate getCoordinate() {
			return new Coordinate(absX, absY, heightLevel);
		}
		/**
		 * The single {@link WarriorsGuild} instance for this player
		 * 
		 * @return warriors guild
		 */
		public WarriorsGuild getWarriorsGuild() {
			return warriorsGuild;
		}
		/**
		 * The time in milliseconds of the last toxic damage the player received
		 * 
		 * @return the time in milliseconds of the last toxic damage hit
		 */
		public long getLastPoisonHit() {
			return lastPoisonHit;
		}

		/**
		 * Sets the last time, in milliseconds, the toxic damaged the player
		 * 
		 * @param toxic
		 *            the time in milliseconds
		 */
		public void setLastPoisonHit(long toxic) {
			lastPoisonHit = toxic;
		}
		/**
		 * The single {@link WarriorsGuildBasement} instance for this player
		 * 
		 * @return warriors guild basement
		 */
		public WarriorsGuildBasement getWarriorsGuildBasement() {
			return warriorsGuildbasement;
		}

		public QuickPrayer getQuick() {
			return quick;
		}

		public void setStatedInterface(String statedInterface) {
			this.statedInterface = statedInterface;
		}

		/**
		 * @param randomEventNpc the randomEventNpc to set
		 */
		public void setRandomEventNpc(NPC randomEventNpc) {
			this.randomEventNpc = randomEventNpc;
		}
		
		/**
		 * @return the randomEventNpc
		 */
		public NPC getRandomEventNpc() {
			return randomEventNpc;
		}
		public void setClickItem(int itemId) {
			this.clickItemId = itemId;
		}

		public int getClickItem() {
			return clickItemId;
		}
		public void setNpcClickIndex(int npcClickIndex) {
			this.npcType = npcClickIndex;
		}

		public int getNpcClickIndex() {
			return npcType;
		}
		/**
		 * @param itemId the destroyItem to set
		 */
		public void setDestroyItem(int itemId) {
			this.destroyItem = itemId;
		}
		public void setClickX(int clickX) {
			this.clickX = clickX;
		}

		public int getClickX() {
			return clickX;
		}

		public void setClickY(int clickY) {
			this.clickY = clickY;
		}

		public int getClickY() {
			return clickY;
		}

		public void setClickId(int clickId) {
			this.clickId = clickId;
		}

		public int getClickId() {
			return clickId;
		}
		/**
		 * @param clickZ the clickZ to set
		 */
		public void setClickZ(int clickZ) {
			this.clickZ = clickZ;
		}

		/**
		 * @return the clickZ
		 */
		public int getClickZ() {
			return clickZ;
		}
		public Cerberus getCerberus() {
			return cerberus;
		}

		/**
		 * @return the destroyItem
		 */
		public int getDestroyItem() {
			return destroyItem;
		}
public TeleportExecutor getTeleports(){
	return teleports;
}
public Barrows getBarrows() {
	return barrows;
}
public TreasureTrails getTrails() {
	return trails;
}

public InterfaceClickHandler getRandomInterfaceClick() {
	return randomInterfaceClick;
}
public int distanceToPoint(int pointX, int pointY, int pointZ) {
	return (int) Math.sqrt(
			Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2) + Math.pow(Math.abs(heightLevel) - pointZ, 2));
}
	/**
	 * 0 North 1 East 2 South 3 West
	 */
	public void setForceMovement(int xOffset, int yOffset, int speedOne, int speedTwo, String directionSet,
			int animation) {
		if (isForceMovementActive() || forceMovement) {
			return;
		}
		stopMovement();
		xOffsetWalk = xOffset - absX;
		yOffsetWalk = yOffset - absY;
		playerStandIndex = animation;
		playerRunIndex = animation;
		playerWalkIndex = animation;
		forceMovementActive = true;
		getPA().requestUpdates();
		setAppearanceUpdateRequired(true);
		Server.getEventHandler().submit(new Event2<Client>("force_movement", this, 2) {

			@Override
			public void execute() {
				if (attachment == null || attachment.disconnected) {
					super.stop();
					return;
				}
				attachment.updateRequired = true;
				attachment.forceMovement = true;
				attachment.x1 = currentX;
				attachment.y1 = currentY;
				attachment.x2 = currentX + xOffsetWalk;
				attachment.y2 = currentY + yOffsetWalk;
				attachment.speed1 = speedOne;
				attachment.speed2 = speedTwo;
				attachment.direction = directionSet == "NORTH" ? 0
						: directionSet == "EAST" ? 1 : directionSet == "SOUTH" ? 2 : directionSet == "WEST" ? 3 : 0;
				super.stop();
			}
		});
		Server.getEventHandler().submit(new Event2<Client>("force_movement", this, Math.abs(xOffsetWalk) + Math.abs(yOffsetWalk)) {

					@Override
					public void execute() {
						if (attachment == null || attachment.disconnected) {
							super.stop();
							return;
						}
						forceMovementActive = false;
						attachment.getPA().movePlayer(xOffset, yOffset, attachment.heightLevel);
						if (attachment.playerEquipment[attachment.playerWeapon] == -1) {
							attachment.playerStandIndex = 0x328;
							attachment.playerTurnIndex = 0x337;
							attachment.playerWalkIndex = 0x333;
							attachment.playerTurn180Index = 0x334;
							attachment.playerTurn90CWIndex = 0x335;
							attachment.playerTurn90CCWIndex = 0x336;
							attachment.playerRunIndex = 0x338;
						} else {
							attachment.getCombat().getPlayerAnimIndex(Item
									.getItemName(attachment.playerEquipment[attachment.playerWeapon]).toLowerCase());
						}
						forceMovement = false;
						super.stop();
					}
				});
	}



}
