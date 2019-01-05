package Ghreborn.net;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * Represents a single packet.
 * 
 * @author Graham Edgecombe
 * 
 */
public class Packet {

	/**
	 * The type of packet.
	 * 
	 * @author Graham Edgecombe
	 * 
	 */
	public enum Type {

		/**
		 * A fixed size packet where the size never changes.
		 */
		FIXED,

		/**
		 * A variable packet where the size is described by a byte.
		 */
		VARIABLE,

		/**
		 * A variable packet where the size is described by a word.
		 */
		VARIABLE_SHORT;

	}

	/**
	 * The opcode.
	 */
	private final int opcode;

	/**
	 * The type.
	 */
	private final Type type;

	/**
	 * The payload.
	 */
	private final ChannelBuffer payload;

	/**
	 * Creates a packet.
	 * 
	 * @param opcode
	 *            The opcode.
	 * @param type
	 *            The type.
	 * @param payload
	 *            The payload.
	 */
	public Packet(final int opcode, final Type type, final ChannelBuffer payload) {
		this.opcode = opcode;
		this.type = type;
		this.payload = payload;
	}

	/**
	 * Checks if this packet is raw. A raw packet does not have the usual
	 * headers such as opcode or size.
	 * 
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isRaw() {
		return opcode == -1;
	}

	/**
	 * Gets the opcode.
	 * 
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * Gets the type.
	 * 
	 * @return The type.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Gets the payload.
	 * 
	 * @return The payload.
	 */
	public ChannelBuffer getPayload() {
		return payload;
	}

	/**
	 * Gets the length.
	 * 
	 * @return The length.
	 */
	public int getLength() {
		return payload.capacity();
	}

	/**
	 * Reads a single byte.
	 * 
	 * @return A single byte.
	 */
	public byte get() {
		return payload.readByte();
	}

	/**
	 * Reads several bytes.
	 * 
	 * @param b
	 *            The target array.
	 */
	public void get(final byte[] b) {
		payload.readBytes(b);
	}

	/**
	 * Reads a byte.
	 * 
	 * @return A single byte.
	 */
	public byte getByte() {
		return get();
	}

	/**
	 * Reads an unsigned byte.
	 * 
	 * @return An unsigned byte.
	 */
	public int getUnsignedByte() {
		return payload.readByte() & 0xff;
	}

	/**
	 * Reads a short.
	 * 
	 * @return A short.
	 */
	public short getShort() {
		return payload.readShort();
	}

	/**
	 * Reads an unsigned short.
	 * 
	 * @return An unsigned short.
	 */
	public int getUnsignedShort() {
		int value = 0;
		value |= (get() & 0xff) << 8;
		value |= (get() & 0xff);
		return value;
	}

	public int getUnsignedShortA() {
		int value = 0;
		value |= (get() & 0xff) << 8;
		value |= ((get() - 128) & 0xff);
		return value;
	}

	/**
	 * Reads an integer.
	 * 
	 * @return An integer.
	 */
	public int getInt() {
		return payload.readInt();
	}

	/**
	 * Reads a long.
	 * 
	 * @return A long.
	 */
	public long getLong() {
		return payload.readLong();
	}

	/**
	 * Reads a type C byte.
	 * 
	 * @return A type C byte.
	 */
	public byte getByteC() {
		return (byte) (-get());
	}

	/**
	 * Gets a type S byte.
	 * 
	 * @return A type S byte.
	 */
	public byte getByteS() {
		return (byte) (128 - get());
	}

	/**
	 * Reads a little-endian type A short.
	 * 
	 * @return A little-endian type A short.
	 */
	public short getLEShortA() {
		int i = (get() - 128 & 0xFF) | ((get() & 0xFF) << 8);
		if (i > 32767)
			i -= 0x10000;
		return (short) i;
	}

	/**
	 * Reads a little-endian short.
	 * 
	 * @return A little-endian short.
	 */
	public short getLEShort() {
		int i = (get() & 0xFF) | ((get() & 0xFF) << 8);
		if (i > 32767)
			i -= 0x10000;
		return (short) i;
	}

	/**
	 * Reads a V1 integer.
	 * 
	 * @return A V1 integer.
	 */
	public int getInt1() {
		final byte b1 = get();
		final byte b2 = get();
		final byte b3 = get();
		final byte b4 = get();
		return ((b3 << 24) & 0xFF) | ((b4 << 16) & 0xFF) | ((b1 << 8) & 0xFF)
				| (b2 & 0xFF);
	}

	/**
	 * Reads a V2 integer.
	 * 
	 * @return A V2 integer.
	 */
	public int getInt2() {
		final int b1 = get() & 0xFF;
		final int b2 = get() & 0xFF;
		final int b3 = get() & 0xFF;
		final int b4 = get() & 0xFF;
		return ((b2 << 24) & 0xFF) | ((b1 << 16) & 0xFF) | ((b4 << 8) & 0xFF)
				| (b3 & 0xFF);
	}

	/**
	 * Gets a 3-byte integer.
	 * 
	 * @return The 3-byte integer.
	 */
	public int getTriByte() {
		return ((get() << 16) & 0xFF) | ((get() << 8) & 0xFF) | (get() & 0xFF);
	}

	/**
	 * Reads a type A byte.
	 * 
	 * @return A type A byte.
	 */
	public byte getByteA() {
		return (byte) (get() - 128);
	}

	/**
	 * Reads a RuneScape string.
	 * 
	 * @return The string.
	 */
	public String getRS2String() {
		StringBuilder bldr = new StringBuilder();
		byte b;
		while (payload.readable() && (b = payload.readByte()) != 10)
			bldr.append((char) b);
		return bldr.toString();
	}

	/**
	 * Reads a type A short.
	 * 
	 * @return A type A short.
	 */
	public short getShortA() {
		int i = ((get() & 0xFF) << 8) | (get() - 128 & 0xFF);
		if (i > 32767)
			i -= 0x10000;
		return (short) i;
	}

	/**
	 * Reads a series of bytes in reverse.
	 * 
	 * @param is
	 *            The target byte array.
	 * @param offset
	 *            The offset.
	 * @param length
	 *            The length.
	 */
	public void getReverse(final byte[] is, final int offset, final int length) {
		for (int i = (offset + length - 1); i >= offset; i--)
			is[i] = get();
	}

	/**
	 * Reads a series of type A bytes in reverse.
	 * 
	 * @param is
	 *            The target byte array.
	 * @param offset
	 *            The offset.
	 * @param length
	 *            The length.
	 */
	public void getReverseA(final byte[] is, final int offset, final int length) {
		for (int i = (offset + length - 1); i >= offset; i--)
			is[i] = getByteA();
	}

	/**
	 * Reads a series of bytes.
	 * 
	 * @param is
	 *            The target byte array.
	 * @param offset
	 *            The offset.
	 * @param length
	 *            The length.
	 */
	public void get(final byte[] is, final int offset, final int length) {
		for (int i = 0; i < length; i++)
			is[offset + i] = get();
	}

	/**
	 * Gets a smart.
	 * 
	 * @return The smart.
	 */
	public int getSmart() {
		final int peek = payload.getByte(payload.readerIndex());
		if (peek < 128)
			return (get() & 0xFF);
		else
			return (getShort() & 0xFFFF) - 32768;
	}

	/**
	 * Gets a signed smart.
	 * 
	 * @return The signed smart.
	 */
	public int getSignedSmart() {
		final int peek = payload.getByte(payload.readerIndex());
		if (peek < 128)
			return ((get() & 0xFF) - 64);
		else
			return ((getShort() & 0xFFFF) - 49152);
	}


	public void readBytes(byte abyte0[], int i, int j) {
		for (int k = j; k < j + i; k++)
			abyte0[k] = getByte();

	}

	public void readBytes_reverse(byte abyte0[], int i, int j) {
		for (int k = (j + i) - 1; k >= j; k--)
			abyte0[k] = getByte();

	}


	public void readBytes_reverseA(byte abyte0[], int i, int j) {
		for (int k = (j + i) - 1; k >= j; k--)
			abyte0[k] = (byte) (getByte() - 128);
	}
	/*
	public byte buffer[] = null;

	public int currentOffset = 0;

	private int opcode;
	
	public ISAACCipher packetEncryption = null;

	private int packetSize;

	public Packet(int packetType, int packetSize) {
		this.setOpcode(packetType);
		this.setPacketSize(packetSize);
	}


	public int getLength() {
		return packetSize;
	}
    public int getOpcode() {
		return opcode;
	}



	
	public int getInt() {
		currentOffset += 4;
		return ((buffer[currentOffset - 4] & 0xff) << 24)
				+ ((buffer[currentOffset - 3] & 0xff) << 16)
				+ ((buffer[currentOffset - 2] & 0xff) << 8)
				+ (buffer[currentOffset - 1] & 0xff);
	}

	public int getInt1() {
		currentOffset += 4;
		return ((buffer[currentOffset - 2] & 0xff) << 24)
				+ ((buffer[currentOffset - 1] & 0xff) << 16)//b4
				+ ((buffer[currentOffset - 4] & 0xff) << 8)//b1
				+ (buffer[currentOffset - 3] & 0xff);
	}

	public int getInt2() {
		currentOffset += 4;
		return ((buffer[currentOffset - 3] & 0xff) << 24)//b2
				+ ((buffer[currentOffset - 4] & 0xff) << 16)//b1
				+ ((buffer[currentOffset - 1] & 0xff) << 8)//b4
				+ (buffer[currentOffset - 2] & 0xff);//b3
	}

	public long getLong() {
	  try {
	  long l = getInt() & 0xffffffffL;
	  long l1 = getInt() & 0xffffffffL;
	  return (l << 32) + l1;
	  } catch(Throwable t) {
	   return 0L;
	  }
	 }

	public byte getByte() {
		return buffer[currentOffset++];
	}

	public byte getByteC() {
		return (byte) (-buffer[currentOffset++]);
	}

	public int readSignedShortLittleEndianA() {
		currentOffset += 2;
		int i = (buffer[currentOffset - 1] & 0xff) 
				+ ((buffer[currentOffset - 2] - 128 & 0xff) << 8); //
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int getShort() {
		currentOffset += 2;
		int i = ((buffer[currentOffset - 2] & 0xff) << 8)
				+ (buffer[currentOffset - 1] & 0xff);
		if (i > 32767)
			i -= 0x10000;
		return i;
	}

	public int getShortA() {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 2] & 0xff) << 8) + (buffer[currentOffset - 1] - 128 & 0xff);
        if(i > 32767)
            i -= 0x10000;
        return i;
    }

	public int getLEShort() {
	    try {
        currentOffset += 2;
        int i = ((buffer[currentOffset - 1] & 0xff) << 8) + (buffer[currentOffset - 2] & 0xff);
        if(i > 32767)
            i -= 0x10000;
        return i;
		 } catch(Throwable t) {
   return -1;
  }
    }
	public int getLEShortA() {
  try {
  currentOffset += 2;
  int i = ((buffer[currentOffset - 1] & 0xff) << 8)
    + (buffer[currentOffset - 2] - 128 & 0xff);//f
  if (i > 32767)
   i -= 0x10000;
  return i;
  } catch(Throwable t) {
   return -1;
  }
 }

	public int getUnsignedByte() {
		return buffer[currentOffset++] & 0xff;
	}


	public int getByteS() {
		return 128 - buffer[currentOffset++] & 0xff;
	}

	public int getUnsignedShort() {
		currentOffset += 2;
		return ((buffer[currentOffset - 2] & 0xff) << 8)
				+ (buffer[currentOffset - 1] & 0xff);
	}

	public int getUnsignedShortA() {
		currentOffset += 2;
		return ((buffer[currentOffset - 1] & 0xff) << 8)
				+ (buffer[currentOffset - 2] - 128 & 0xff);
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public void setPacketSize(int packetSize) {
		this.packetSize = packetSize;
	}
	

	public java.lang.String getRS2String() {
		int i = currentOffset;
		while (buffer[currentOffset++] != 10);
		return new String(buffer, i, currentOffset - i - 1);
	}

*/

	
	public int hexToInt() {
		return (getUnsignedByte() * 1000) + getUnsignedByte();
	}
	
	public long readQWord2() {
		final long l = getInt() & 0xffffffffL;
		final long l1 = getInt() & 0xffffffffL;
		return (l << 32) + l1;
	}
	
}