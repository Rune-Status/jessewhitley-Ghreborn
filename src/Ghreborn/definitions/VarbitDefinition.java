package Ghreborn.definitions;

import java.util.HashMap;
import java.util.Map;

import Ghreborn.clip.ByteStreamExt;
import Ghreborn.model.players.Client;

public class VarbitDefinition {
	
	
    /**
     * The config definitions mapping.
     */
    private static final Map<Integer, VarbitDefinition> MAPPING = new HashMap<>();

    /**
     * The bit size flags.
     */
    private static final int[] BITS = new int[32];

	private static ByteStreamExt dataBuffer;
	public static void LoadConfig() {
		dataBuffer = new ByteStreamExt(getBuffer("varbit.dat"));
		final int len = dataBuffer.readUnsignedShort();

		if (cache == null) {
			cache = new VarbitDefinition[len];
		}

		System.out.println(String.format("Loaded: %d varbits", len));

		for (int i = 0; i < len; i++) {
			if (cache[i] == null) {
				cache[i] = new VarbitDefinition();
			}

			cache[i].decode(dataBuffer);

			//if (cache[i].aBoolean651) {
			//	Varp.varps[cache[i].configId].aBoolean713 = true;
			//}
		}

		if (dataBuffer.currentOffset != dataBuffer.buffer.length) {
			System.out.println("varbit mismatch! " + dataBuffer.currentOffset + " " + dataBuffer.buffer.length);
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

    /**
     * Initializes the bit flags.
     */
    static {
	int flag = 2;
	for (int i = 0; i < 32; i++) {
	    BITS[i] = flag - 1;
	    flag += flag;
	}
    }
	private void decode(ByteStreamExt buffer) {
		int opcode = buffer.readUnsignedByte();

		if (opcode == 0) {
			return;
		} else if (opcode == 1) {
			configId = buffer.readUnsignedShort();
			lsb = buffer.readUnsignedByte();
			msb = buffer.readUnsignedByte();
		} else {
			System.out.println(opcode);
		}
	}

    /**
     * Gets the current config value for this file.
     * @param player The player.
     * @return The config value.
     */
    public int getValue(Client player) {
	int size = BITS[lsb - msb];
	return size >> msb;
    }

    /**
     * Gets the mapping.
     * @return The mapping.
     */
    public static Map<Integer, VarbitDefinition> getMapping() {
	return MAPPING;
    }

    /**
     * Gets the id.
     * @return The id.
     */
    public int getId() {
	return id;
    }

    /**
     * Gets the configId.
     * @return The configId.
     */
    public int getConfigId() {
	return configId;
    }

    /**
     * Gets the bitShift.
     * @return The bitShift.
     */
    public int getBitShift() {
	return lsb;
    }

    /**
     * Gets the bitSize.
     * @return The bitSize.
     */
    public int getBitSize() {
	return msb;
    }

    @Override
    public String toString() {
	return "ConfigFileDefinition [id=" + id + ", configId=" + configId + ", bitShift=" + msb + ", bitSize=" + lsb + "]";
    }
	public static VarbitDefinition cache[];
	public int configId;
	public int lsb;
	public int msb;
    /**
     * The file id.
     */
    private  int id;
	private boolean aBoolean651;
}
