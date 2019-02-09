package Ghreborn.definitions;

import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import Ghreborn.Config;
import Ghreborn.definitions.items.ItemDefinition_Sub1;
import Ghreborn.definitions.items.ItemDefinition_Sub2;
import Ghreborn.definitions.items.ItemDefinition_Sub3;
import Ghreborn.util.Stream;

/**
 * A class that loads & manages NPC configurations. 
 * 
 * <p>An <code>NPCDefinition</code> is a component of the NPC configuration file
 * that defines several aspects of the NPC (such as models, model colors, animations, 
 * name, description, and combat level). We use these definitions both client and server-
 * sided in order to display information on the client and pull data when necessary
 * server-sided (i.e. - for combat formulas). </p>  
 * @author Craze/Warren
 * @date Sep 20, 2015 5:13:47 PM

 */
public class ItemCacheDefinition {
	
	/**
	 * Represents the total whole number integer of Items.
	 */
	public static int ITEM_TOTAL = 30000;
	
	/**
	 * Returns a {@link ITEMCacheDefinition} for the specified ID.
	 * @param i	the id of the Item to get the definition for
	 * @return	the definition
	 */
    public static final ItemCacheDefinition forID(int i) {
        for(int j = 0; j < 10; j++){
            if(cache[j].id == i){
                return cache[j];
            }
        }
        cacheIndex = (cacheIndex + 1) % 10;
        ItemCacheDefinition ItemDef = cache[cacheIndex] = new ItemCacheDefinition();
       		npcData.currentOffset = streamIndices[i];
        ItemDef.id = i;
	if(i < totalItems)
        	ItemDef.decode(npcData);
	ItemDef = ItemDefinition_Sub1.itemDef(i, ItemDef);
	ItemDef = ItemDefinition_Sub2.itemDef(i, ItemDef);
	ItemDef = ItemDefinition_Sub3.itemDef(i, ItemDef);
    if(ItemDef.certTemplateID != -1) {
    	ItemDef.toNote();
     }
	return ItemDef;
    }
	public static void dumpItemDefs() {
		final int[] wikiBonuses = new int[18];
		int bonus = 0;
		int amount = 0;
		int value = 0;
		int slot = -1;
		// Testing Variables just so i know format is correct
		String fullmask = "false";
		// boolean stackable1 = false;
		String stackable = "false";
		// boolean noteable1 = false;
		String noteable = "true";
		// boolean tradeable1 = false;
		String tradeable = "true";
		// boolean wearable1 = false;
		String wearable = "true";
		String showBeard = "true";
		String members = "true";
		boolean twoHanded = false;
		System.out.println("Starting to dump item definitions...");
		for (int i = 0; i < totalItems; i++) {
			ItemCacheDefinition item = ItemCacheDefinition.forID(i);
			try {
				try {
					try {
						final URL url = new URL("http://oldschool.runescape.wiki/w/" + item.name.replaceAll(" ", "_"));
						URLConnection con = url.openConnection();
						BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
						String line;
						writer = new BufferedWriter(new FileWriter("itemDefs.json", true));
						while ((line = in.readLine()) != null) {
							try {
								if (line.contains("<td style=\"text-align: center; width: 35px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "")
											.replace("\"\"", "")
											.replace("<td style=\"text-align: center; width: 35px;\">", "");
									wikiBonuses[bonus] = Integer.parseInt(line);
									bonus++;
								} else if (line.contains("<td style=\"text-align: center; width: 30px;\">")) {
									line = line.replace("</td>", "").replace("%", "").replace("?", "").replace("%", "")
											.replace("<td style=\"text-align: center; width: 30px;\">", "");
									wikiBonuses[bonus] = Integer.parseInt(line);
									bonus++;
								}
								if (line.contains("<div id=\"GEPCalcResult\" style=\"display:inline;\">")) {
									line = line.replace("</div>", "").replace("%", "").replace("?", "").replace("%", "")
											.replace("<div id=\"GEPCalcResult\" style=\"display:inline;\">", "");
									value = Integer.parseInt(line);
								}

							} catch (NumberFormatException e) {
								e.printStackTrace();
							}
							in.close();
							// fw.write("ItemID: "+itemDefinition.id+" - "+itemDefinition.name);
							// fw.write(System.getProperty("line.separator"));
							// writer.write("[\n");
							writer.write("  {\n\t\"id\": " + item.id + ",\n\t\"name\": \"" + item.name
									+ "\",\n\t\"desc\": \"" + item.name + "\",\n\t\"value\": "
									+ value + ",\n\t\"dropValue\": " + value + ",\n\t\"bonus\": [\n\t  "
									+ wikiBonuses[0] + ",\n\t  " + wikiBonuses[1] + ",\n\t  " + wikiBonuses[2]
									+ ",\n\t  " + wikiBonuses[3] + ",\n\t  " + wikiBonuses[4] + ",\n\t  "
									+ wikiBonuses[5] + ",\n\t  " + wikiBonuses[6] + ",\n\t  " + wikiBonuses[7]
									+ ",\n\t  " + wikiBonuses[8] + ",\n\t  " + wikiBonuses[9] + ",\n\t  "
									+ wikiBonuses[10] + ",\n\t  " + wikiBonuses[13] + ",\n\t],\n\t\"slot\": " + slot
									+ ",\n\t\"fullmask\": " + fullmask + ",\n\t\"stackable\": " + stackable
									+ ",\n\t\"noteable\": " + noteable + ",\n\t\"tradeable\": " + tradeable
									+ ",\n\t\"wearable\": " + wearable + ",\n\t\"showBeard\": " + showBeard
									+ ",\n\t\"members\": " + members + ",\n\t\"twoHanded\": " + twoHanded
									+ ",\n\t\"requirements\": [\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t  0,\n\t]\n  },\n");
							/*
							 * writer.write("item = " + i + "	" + item.name.replace(" ", "_") + "	" +
							 * item.description.replace(" ", "_") + "	" + item.value + "	" + item.value +
							 * "	" + item.value + "	" + wikiBonuses[0] + "	" + wikiBonuses[1] + "	" +
							 * wikiBonuses[2] + "	" + wikiBonuses[3] + "	" + wikiBonuses[4] + "	" +
							 * wikiBonuses[5] + "	" + wikiBonuses[6] + "	" + wikiBonuses[7] + "	" +
							 * wikiBonuses[8] + "	" + wikiBonuses[9] + "	" + wikiBonuses[10] + "	" +
							 * wikiBonuses[13]);
							 */
							amount++;
							wikiBonuses[0] = wikiBonuses[1] = wikiBonuses[2] = wikiBonuses[3] = wikiBonuses[4] = wikiBonuses[5] = wikiBonuses[6] = wikiBonuses[7] = wikiBonuses[8] = wikiBonuses[9] = wikiBonuses[10] = wikiBonuses[11] = wikiBonuses[13] = 0;
							writer.newLine();
							writer.close();
						}
					} catch (NullPointerException e) {
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Done dumping " + amount + " item definitions!");
		}
	}
	/**
	 * Unpacks the Item configurations.
	 */
    public static final void unpackConfig() {
    	try {
        npcData = new Stream(FileUtils.readFileToByteArray(new File("./Data/cache/obj.dat")));
        Stream indexStream = new Stream(FileUtils.readFileToByteArray(new File("./Data/cache/obj.idx")));
        totalItems = indexStream.readUnsignedWord();
        streamIndices = new int[totalItems + 30000];
        int i = 2;
        for(int j = 0; j < totalItems; j++)
        {
            streamIndices[j] = i;
            i += indexStream.readUnsignedWord();
        }

        cache = new ItemCacheDefinition[10];
        for(int k = 0; k < 10; k++) {
            cache[k] = new ItemCacheDefinition();
        }
        //dumpItemDefs();
        System.out.println("Successfully loaded: " + totalItems + " Item Cache definitions.");
    	} catch (Exception e) {
    		System.err.println("An error has occurred whilst loading Item definitions!");
    		e.printStackTrace();
    	}
    }
    
    private void decode(Stream buffer) {
        while(true){
            int opcode = buffer.readUnsignedByte();

            if (opcode == 0) {
                return;
            } else if (opcode == 1) {
                modelId = buffer.readUnsignedWord();
            } else if (opcode == 2) {
                name = buffer.readString();
            } else if (opcode == 4) {
                spriteScale = buffer.readUnsignedWord();
            } else if (opcode == 5) {
                spritePitch = buffer.readUnsignedWord();
            } else if (opcode == 6) {
                spriteCameraRoll = buffer.readUnsignedWord();
            } else if (opcode == 7) {
                spriteTranslateX = buffer.readUnsignedWord();
                if (spriteTranslateX > 32767) {
                    spriteTranslateX -= 0x10000;
                }
            } else if (opcode == 8) {
                spriteTranslateY = buffer.readUnsignedWord();
                if (spriteTranslateY > 32767) {
                    spriteTranslateY -= 0x10000;
                }
            } else if (opcode == 11) {
                stackable = true;
            } else if (opcode == 12) {
                value = buffer.readInt();
            } else if (opcode == 16) {
                membersObject = true;
            } else if (opcode == 23) {
                primaryMaleModel = buffer.readUnsignedWord();
                maleTranslation = buffer.readSignedByte();
            } else if (opcode == 24) {
                secondaryMaleModel = buffer.readUnsignedWord();
            } else if (opcode == 25) {
                primaryFemaleModel = buffer.readUnsignedWord();
                femaleTranslation = buffer.readSignedByte();
            } else if (opcode == 26) {
                secondaryFemaleModel = buffer.readUnsignedWord();
            } else if (opcode >= 30 && opcode < 35) {
                if (groundActions == null) {
                    groundActions = new String[5];
                }
                groundActions[opcode - 30] = buffer.readString();
                if (groundActions[opcode - 30].equalsIgnoreCase("Hidden")) {
                    groundActions[opcode - 30] = null;
                }
            } else if (opcode >= 35 && opcode < 40) {
                if (itemActions == null) {
                    itemActions = new String[5];
                }

                itemActions[opcode - 35] = buffer.readString();
            } else if (opcode == 40) {
                int len = buffer.readUnsignedByte();
                originalModelColors = new int[len];
                modifiedModelColors = new int[len];
                for (int i = 0; i < len; i++) {
                    originalModelColors[i] = buffer.readUnsignedWord();
                    modifiedModelColors[i] = buffer.readUnsignedWord();
                }
            } else if(opcode == 41) {
                int var3 = buffer.readUnsignedByte();
                this.textureToReplace = new short[var3];
                this.textToReplaceWith = new short[var3];

                for(int var4 = 0; var4 < var3; ++var4) {
                   this.textureToReplace[var4] = (short)buffer.readUnsignedWord();
                   this.textToReplaceWith[var4] = (short)buffer.readUnsignedWord();
                }
            } else if (opcode == 42) {
                shiftClickIndex = buffer.readUnsignedByte();
            } else if (opcode == 65) {
                searchable = true;
            }  else if (opcode == 78) {
                tertiaryMaleEquipmentModel = buffer.readUnsignedWord();
            } else if (opcode == 79) {
                tertiaryFemaleEquipmentModel = buffer.readUnsignedWord();
            } else if (opcode == 90) {
                primaryMaleHeadPiece = buffer.readUnsignedWord();
            } else if (opcode == 91) {
                primaryFemaleHeadPiece = buffer.readUnsignedWord();
            } else if (opcode == 92) {
                secondaryMaleHeadPiece = buffer.readUnsignedWord();
            } else if (opcode == 93) {
                secondaryFemaleHeadPiece = buffer.readUnsignedWord();
            } else if (opcode == 95) {
                spriteCameraYaw = buffer.readUnsignedWord();
            } else if (opcode == 97) {
                certID = buffer.readUnsignedWord();
            } else if (opcode == 98) {
                certTemplateID = buffer.readUnsignedWord();
            } else if (opcode >= 100 && opcode < 110) {
                if (stackIds == null) {
                    stackIds = new int[10];
                    stackAmounts = new int[10];
                }
                stackIds[opcode - 100] = buffer.readUnsignedWord();
                stackAmounts[opcode - 100] = buffer.readUnsignedWord();
            } else if (opcode == 110) {
                groundScaleX = buffer.readUnsignedWord();
            } else if (opcode == 111) {
                groundScaleY = buffer.readUnsignedWord();
            } else if (opcode == 112) {
                groundScaleZ = buffer.readUnsignedWord();
            } else if (opcode == 113) {
                ambience = buffer.readSignedByte();
            } else if (opcode == 114) {
                diffusion = buffer.readSignedByte();
            } else if (opcode == 115) {
                team = buffer.readUnsignedByte();
            } else if (opcode == 139) {
                unnotedId = buffer.readUnsignedWord(); // un-noted id
            } else if (opcode == 140) {
                notedId = buffer.readUnsignedWord(); // noted id
            } else if (opcode == 148) {
                buffer.readUnsignedWord(); // placeholder id
            } else if (opcode == 149) {
                buffer.readUnsignedWord(); // placeholder template
            }
        }
    }
    /**
     * this handles the noting of thems so the server knows its that noted item.
     */
    public void toNote() {
        ItemCacheDefinition noted = forID(this.certTemplateID);
        this.modelId = noted.modelId;
        this.spriteScale = noted.spriteScale;
        this.spritePitch = noted.spritePitch;
        this.spriteCameraRoll = noted.spriteCameraRoll;
        this.spriteCameraYaw = noted.spriteCameraYaw;
        this.spriteTranslateX = noted.spriteTranslateX;
        this.spriteTranslateY = noted.spriteTranslateY;
        this.modifiedModelColors = noted.modifiedModelColors;
        this.originalModelColors = noted.originalModelColors;
        ItemCacheDefinition unnoted = forID(this.certID);
        
        if (unnoted == null || unnoted.name == null) {
            return;
        }
        this.name = unnoted.name;
        this.membersObject = unnoted.membersObject;
        this.value = unnoted.value;
        String s = "a";
        char c = unnoted.name.charAt(0);
  		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
  			s = "an";
  		}
        this.description = ("Swap this note at any bank for " + s + " " + unnoted.name + ".");
        this.stackable = true;
     }
    /**
     * Reads opcode values from the {@link Stream}.
     * @param str	the stream
     */

    ItemCacheDefinition()
    {
        modelId = 0;
        name = "null";
        description = "";
        modifiedModelColors = null;
        originalModelColors = null;
        this.textToReplaceWith = null;
        this.textureToReplace = null;
        spriteScale = 2000;
        spritePitch = 0;
        spriteCameraRoll = 0;
        spriteCameraYaw = 0;
        spriteTranslateX = 0;
        spriteTranslateY = 0;
        stackable = false;
        value = 1;
        membersObject = false;
        groundActions = new String[]{null, null, "Take", null, null};;
        itemActions = new String[]{null, null, null, null, "Drop"};
        primaryMaleModel = -1;
        secondaryMaleModel = -1;
        maleTranslation = 0;
        primaryFemaleModel = -1;
        secondaryFemaleModel = -1;
        femaleTranslation = 0;
        tertiaryMaleEquipmentModel = -1;
        tertiaryFemaleEquipmentModel = -1;
        primaryMaleHeadPiece = -1;
        secondaryMaleHeadPiece = -1;
        primaryFemaleHeadPiece = -1;
        secondaryFemaleHeadPiece = -1;
        stackIds = null;
        stackAmounts = null;
        certID = -1;
        certTemplateID = -1;
        groundScaleX = 128;
        groundScaleY = 128;
        groundScaleZ = 128;
        ambience = 0;
        diffusion = 0;
        team = 0;
    }
    
    /**
     * Represents an array of {@link ITEMCacheDefinition}s.
     */
    private static ItemCacheDefinition[] definitions = new ItemCacheDefinition[ITEM_TOTAL];

	/**
	 * Gets all definitions in the form of an array.
	 * @return	definitions	the {@link ITEMCacheDefinition} in array form
	 */
	public static ItemCacheDefinition[] getDefinitions() {
		return definitions;
	}
	
	public boolean isUntradable() {
		return untradable;
	}

    public boolean isUntradable(int id, boolean stackable, boolean noteable) {
    	if (!stackable && !noteable) {
    		return true;
    	}
        return Arrays.binarySearch(Config.UNTRADEABLE_ITEMS, id) > -1;
    }

    
    /**
     * Gets the items noted id
     * @return
     */
    public int isNoted() {
    	return notedId;
    }
    
    /**
     * Gets the item's value.
     * @return
     */
    public int getvalue() {
    	return value;
    }
    
    public boolean isStackable(){
    	return stackable;
    }
    /**
     * Gets the size of the NPC.
     * @return	size	the size of the NPC
     */
    public int getUnnotedId() {
    	return unnotedId;
    }
    
    /**
     * Gets the name of the NPC.
     * @return	name	the name of the NPC.
     */
    public String getName() {
    	return name;
    }
    
    /**
     * Gets the examine string for the NPC.
     * @return	examine	the examine string
     */
    public String getExamine() {
    	return description;
    }
    
    /**
     * Gets the Actions of the Item
     * @return     */
    public String[] getitemActions() {
    	return itemActions;
    }
    public static void Models(int i, int j, int k) {
	      ItemCacheDefinition class8 = cache[cacheIndex];
	      class8.modelId = i;
	      class8.primaryMaleModel = j;
	      class8.primaryFemaleModel = k;
	   }

	   public static void NewColor(int i, int j, int k) {
	      ItemCacheDefinition class8 = cache[cacheIndex];
	      class8.modifiedModelColors[k] = i;
	      class8.originalModelColors[k] = j;
	   }

	   public static void NEO(String s, String s1, String s2) {
	      ItemCacheDefinition class8 = cache[cacheIndex];
	      class8.itemActions = new String[5];
	      class8.itemActions[1] = s2;
	      class8.itemActions[4] = "Drop";
	      class8.name = s;
	      class8.description = s1;
	   }

	   public static void Zoom(int i, int j, int k, int l, int i1, boolean flag) {
	      ItemCacheDefinition class8 = cache[cacheIndex];
	      class8.spriteScale = i;
	      class8.spritePitch = l;
	      class8.spriteCameraRoll = i1;
	      class8.spriteTranslateX = k;
	      class8.spriteTranslateY = j;
	      class8.stackable = flag;
	   }

	   public static void Jukkycolors(int i, int j, int k) {
	      ItemCacheDefinition class8 = cache[cacheIndex];
	      class8.modifiedModelColors[k] = i;
	      class8.originalModelColors[k] = j;
	   }

	   public static void Jukkyzoom(int i, int j, int k, int l, int i1, int j1, int k1, int l1, boolean flag) {
	      ItemCacheDefinition class8 = cache[cacheIndex];
	      class8.spriteScale = i;
	      class8.spritePitch = j;
	      class8.spriteCameraRoll = k;
	      class8.spriteCameraYaw = l;
	      class8.spriteTranslateX = i1;
	      class8.spriteTranslateY = j1;
	      class8.stackable = flag;
	      class8.primaryMaleHeadPiece = k1;
	      class8.primaryFemaleHeadPiece = l1;
	   }

	   public static void Jukkyname(String s, String s1) {
	      ItemCacheDefinition class8 = cache[cacheIndex];
	      class8.itemActions = new String[5];
	      class8.itemActions[1] = "Wear";
	      class8.itemActions[4] = "Drop";
	      class8.name = s;
	      class8.description = s1;
	   }

	   public static void JukkyModels(int male, int malearms, int female, int femalearms, int dropmdl) {
	      ItemCacheDefinition class8 = cache[cacheIndex];
	      class8.primaryMaleModel = male;
	      class8.secondaryMaleModel = malearms;
	      class8.primaryFemaleModel = female;
	      class8.secondaryFemaleModel = femalearms;
	      class8.modelId = dropmdl;
	   }
	/**
	 * Untradable
	 */
	private boolean untradable;
    private static int cacheIndex;
    private static Stream npcData;
    public static int totalItems;
    public String name;
    public String actions[];
    public byte boundDim;
    private static int streamIndices[];
    public long type;
    private byte femaleTranslation;
    private byte maleTranslation;
    public int id;// anInt157
    public int team;
    public int value;// anInt155
    public int certID;
    public int modelId;// dropModel
    public int primaryMaleHeadPiece;
    public int primaryFemaleModel;// femWieldModel
    public int spriteCameraYaw;// modelPositionUp
    public int secondaryFemaleModel;// femArmModel
    public int primaryFemaleHeadPiece;
    public int secondaryMaleModel;// maleArmModel
    public int primaryMaleModel;// maleWieldModel
    public int spriteScale;
    public int spriteTranslateX;
    public int spriteTranslateY;//
    public int certTemplateID;
    public int unnotedId = -1;
    public int notedId = -1;
    public int spriteCameraRoll;// modelRotateRight
    public int spritePitch;// modelRotateUp
    public int[] stackIds;// modelStack
    public int[] stackAmounts;// itemAmount
    public int[] modifiedModelColors;// newModelColor
    public int[] originalModelColors;
    public short[] modifiedTexture;
    public short[] originalTexture;
    private int ambience;
    private int tertiaryFemaleEquipmentModel;
    private int secondaryMaleHeadPiece;
    private int diffusion;
    private int tertiaryMaleEquipmentModel;
    private int groundScaleZ;
    private int groundScaleY;
    private int groundScaleX;
    //@Export("textureToReplace")
    public short[] textureToReplace;
   // @ObfuscatedName("v")
   // @Export("textToReplaceWith")
    public short[] textToReplaceWith;
    private int secondaryFemaleHeadPiece;
    private int shiftClickIndex = -2;
    private boolean stockMarket;
    public boolean searchable;
    private static int[] offsets;
    public boolean stackable;// itemStackable
    public boolean membersObject;// aBoolean161
	private static BufferedWriter writer;
    public static boolean isMembers = true;
    public String description;// itemExamine
    public String itemActions[];// itemMenuOption
    public String groundActions[];
    private static ItemCacheDefinition cache[];
}