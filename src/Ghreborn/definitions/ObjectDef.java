package Ghreborn.definitions;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import Ghreborn.clip.ByteStreamExt;
import Ghreborn.clip.MemoryArchive;

public final class ObjectDef
{

    public static ObjectDef getObjectDef(int id){
		if (id > totalObjects667 || id > streamIndices667.length - 1) {
			id = 0;
		}
        for(int j = 0; j < 20; j++)
            if(cache[j].id == id)
                return cache[j];

        cacheIndex = (cacheIndex + 1) % 20;
        ObjectDef class46 = cache[cacheIndex];
        dataBuffer667.currentOffset = streamIndices667[id];
        class46.id = id;
        class46.setDefaults();
		try {
		class46.readValues(dataBuffer667);
		} catch (Exception e) {
			e.printStackTrace();
		}
 	   switch (id) {
 	   case 16891:
 		  class46.name = "Ckey/Rare Key Chest";
 		 class46.actions = new String[] {"Search", null, null, null, null};
 		   break;
 	   case 25382:
 		  class46.name = "Portal";
 		 class46.interactive = true;
 		class46.actions = new String[] { "Teleport", null, null, null, null };
 		   break;
 	   }
		if(id == 6552) {
			class46.name = "Ancient Altar";
		}
        return class46;
    }
    private void setDefaults()
    {
		modelArray = null;
		objectModelType = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		tileSizeX = 1;
		tileSizeY = 1;
		unwalkable = true;
		impenetrable = true;
		interactive = false;
		aBoolean762 = false;
		aBoolean769 = false;
		aBoolean764 = false;
		anInt781 = -1;
		anInt775 = 16;
		aByte737 = 0;
		aByte742 = 0;
		actions = null;
		anInt746 = -1;
		anInt758 = -1;
		aBoolean751 = false;
		aBoolean779 = true;
		anInt748 = 128;
		anInt772 = 128;
		anInt740 = 128;
		anInt768 = 0;
		anInt738 = 0;
		anInt745 = 0;
		anInt783 = 0;
		aBoolean736 = false;
		aBoolean766 = false;
		anInt760 = -1;
		anInt774 = -1;
		anInt749 = -1;
		childrenIDs = null;
    }
	private static ByteStreamExt dataBuffer667;
	
	public static int totalObjects667;
	
    public static void loadConfig()
    {
		dataBuffer667 = new ByteStreamExt(getBuffer("loc.dat"));
		ByteStreamExt idxBuffer667 = new ByteStreamExt(getBuffer("loc.idx"));
		
		totalObjects667 = idxBuffer667.readUnsignedShort();
		
		streamIndices667 = new int[totalObjects667];
		
		int i = 2;
		for (int j = 0; j < totalObjects667; j++) {
			streamIndices667[j] = i;
			i += idxBuffer667.readUnsignedShort();
		}
		
		cache = new ObjectDef[20];
		for (int k = 0; k < 20; k++) {
			cache[k] = new ObjectDef();
		}
    }

	public static byte[] getBuffer(String s)
	{
		try {
			java.io.File f = new java.io.File("./Data/cache/" + s);
			if(!f.exists())
				return null;
			byte[] buffer = new byte[(int) f.length()];
			java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			return buffer;
		} catch(Exception e) {
		}
		return null;
	}

	private void readValues(ByteStreamExt buffer) {
		while(true) {
				int opcode = buffer.readUnsignedByte();
				if (opcode == 0)
					break;
				if (opcode == 1) {
					int k = buffer.readUnsignedByte();
					if (k > 0)
						if (modelArray == null || lowMem) {
							objectModelType = new int[k];
							modelArray = new int[k];
							for (int k1 = 0; k1 < k; k1++) {
								modelArray[k1] = buffer.readUnsignedShort();
								objectModelType[k1] = buffer.readUnsignedByte();
							}
						} else {
							buffer.currentOffset += k * 3;
						}
				} else if (opcode == 2)
					name = buffer.readString();
				else if (opcode == 5) {
					int l = buffer.readUnsignedByte();
					if (l > 0)
						if (modelArray == null || lowMem) {
							objectModelType = null;
							modelArray = new int[l];
							for (int l1 = 0; l1 < l; l1++)
								modelArray[l1] = buffer.readUnsignedShort();
						} else {
							buffer.currentOffset += l * 2;
						}
				} else if (opcode == 14)
					tileSizeX = buffer.readUnsignedByte();
				else if (opcode == 15)
					tileSizeY = buffer.readUnsignedByte();
				else if (opcode == 17)
					unwalkable = false;
				else if (opcode == 18)
					impenetrable = false;
				else if (opcode == 19) {
					int i = buffer.readUnsignedByte();
					if (i == 1)
						interactive = true;
				} else if (opcode == 21) {
					//conformable = true;
				} else if (opcode == 22) {
					//blackModel = false;//
				} else if (opcode == 23)
					aBoolean764 = true;
				else if (opcode == 24) {
					buffer.readUnsignedShort();
				} else if (opcode == 28)
					buffer.readUnsignedByte();
				else if (opcode == 29)
					aByte737 = buffer.readByte();
				else if (opcode == 39)
					aByte742 = buffer.readByte() * 25;
				else if (opcode >= 30 && opcode < 39) {
					if (actions == null)
						actions = new String[10];
					actions[opcode - 30] = buffer.readString();
					if (actions[opcode - 30].equalsIgnoreCase("hidden")
							|| actions[opcode - 30].equalsIgnoreCase("null"))
						actions[opcode - 30] = null;
				} else if (opcode == 40) {
					int i1 = buffer.readUnsignedByte();
					for (int i2 = 0; i2 < i1; i2++) {
						buffer.readUnsignedShort();
						buffer.readUnsignedShort();
					}
				} else if (opcode == 41) {
					int len = buffer.readUnsignedByte();
					for (int i1 = 0; i1 < len; i1++) {
						buffer.readUnsignedShort();
					 buffer.readUnsignedShort();
					}
				} else if (opcode == 62)
					aBoolean751 = true;
				else if (opcode == 64)
					aBoolean779 = false;
				else if (opcode == 65)
					anInt748 = buffer.readUnsignedShort();
				else if (opcode == 66)
					anInt772 = buffer.readUnsignedShort();
				else if (opcode == 67)
					anInt740 = buffer.readUnsignedShort();
				else if (opcode == 68)
					buffer.readUnsignedShort();
				else if (opcode == 69)
					anInt768 = buffer.readUnsignedByte();
				else if (opcode == 70)
					anInt738 = buffer.readShort();
				else if (opcode == 71)
					anInt745 = buffer.readShort();
				else if (opcode == 72)
					anInt783 = buffer.readShort();
				else if (opcode == 73)
					aBoolean736 = true;
				else if (opcode == 74)
					aBoolean766 = true;
					else if (opcode == 75)
					anInt760 = buffer.readUnsignedByte();
					else if (opcode == 78) {
				buffer.readUnsignedShort(); // ambient sound id
				buffer.readUnsignedByte();
					} else if (opcode == 79){
				buffer.readUnsignedShort();
				buffer.readUnsignedShort();
				buffer.readUnsignedByte();
				int len = buffer.readUnsignedByte();

				for (int i1 = 0; i1 < len; i1++) {
					buffer.readUnsignedShort();
				}
					} else if (opcode == 81) 
				buffer.readUnsignedByte();
			 else if (opcode == 82) 
				buffer.readUnsignedShort();
			 else if (opcode == 77 || opcode == 92) {
				anInt774 = buffer.readUnsignedShort();
			if (anInt774 == 65535)
				anInt774 = -1;
				anInt749 = buffer.readUnsignedShort();
			if (anInt749 == 65535)
				anInt749 = -1;
			int value = -1;

			if (opcode == 92) {
                value = buffer.readUnsignedShort();

                if (value == 0xFFFF) {
                    value = -1;
                }
            }

			int j1 = buffer.readUnsignedByte();
			childrenIDs = new int[j1 + 2];
			for (int j2 = 0; j2 <= j1; j2++) {
				childrenIDs[j2] = buffer.readUnsignedShort();
				if (childrenIDs[j2] == 65535)
					childrenIDs[j2] = -1;
			}
			childrenIDs[j1 + 1] = value;
			
			if (!interactive) {
			interactive = (modelArray != null && (objectModelType == null || objectModelType[0] == 10) || actions != null);
		}
		if (aBoolean766) {
			unwalkable = false;
			impenetrable = false;
		}
		if (anInt760 == -1)
			anInt760 = unwalkable ? 1 : 0;
			 }
		}
    }


    public ObjectDef()
    {
        id = -1;
    }

    public boolean hasActions()
    {
		return interactive;
    }
	
    public String getName(){
    	return name;
    }
	public boolean hasName()
	{
		return name != null && name.length() > 1;
	}

    public boolean solid()
    {
		return aBoolean779;
    }
	
	public int xLength() {
		return tileSizeX;
	}

	public int yLength() {
		return tileSizeY;
	}
	
	public boolean aBoolean767()
	{
		return unwalkable;
	}

	public boolean aBoolean736;
	public byte aByte737;
	public int anInt738;
	public String name;
	public int anInt740;
	public int aByte742;
	public int tileSizeX;
	public int anInt745;
	public int anInt746;
	public int[] originalModelColors;
	public int anInt748;
	public int anInt749;
	public boolean aBoolean751;
	public static boolean lowMem;
	public int id;
	public static int[] streamIndices525;
	public static int[] streamIndices667;
	public boolean impenetrable;
	public int anInt758;
	public int childrenIDs[];
	public int anInt760;
	public int tileSizeY;
	public boolean aBoolean762;
	public boolean aBoolean764;
	public boolean aBoolean766;
	public boolean unwalkable;
	public int anInt768;
	public boolean aBoolean769;
	public static int cacheIndex;
	public int anInt772;
	public int[] modelArray;
	public int anInt774;
	public int anInt775;
	public int[] objectModelType;
	public byte description[];
	public boolean interactive;
	public boolean aBoolean779;
	public int anInt781;
	public static ObjectDef[] cache;
	public int anInt783;
	public int[] modifiedModelColors;
	public String actions[];
    private boolean unknown;
	public static MemoryArchive archive;

    public boolean unknown() {
        return unknown;
    }


}
