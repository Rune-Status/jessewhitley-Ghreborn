package Ghreborn.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.Range;
import org.jboss.netty.buffer.ChannelBuffer;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import Ghreborn.model.content.teleport.Position;
import Ghreborn.model.items.GameItem;
import Ghreborn.model.players.Client;
import Ghreborn.model.players.Player;
import Ghreborn.model.players.skills.slayer.Task;

public class Misc {
	
	public static String getRS2String(final ChannelBuffer buf) {
		final StringBuilder bldr = new StringBuilder();
		byte b;
		while (buf.readable() && (b = buf.readByte()) != 10)
			bldr.append((char) b);
		return bldr.toString();
	}
	public static String getFilteredInput(String input) {
		if (input.contains("\r")) {
			input = input.replaceAll("\r", "");
		}
		
		return input;
	}
	public static String formatPlayerName(String str) {
		str = ucFirst(str);
		str.replace("_", " ");
		return str;
	}
	
	public static String longToPlayerName(long l) {
        int i = 0;
        char ac[] = new char[12];

        while (l != 0L) {
            long l1 = l;

            l /= 37L;
            ac[11 - i++] = xlateTable[(int) (l1 - l * 37L)];
        }
        return new String(ac, 12 - i, i);
    }
	
	public static final char playerNameXlateTable[] = {
		'_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
		'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
		't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
		'3', '4', '5', '6', '7', '8', '9', '[', ']', '/', '-', ' '
	};
	
	public static String longToPlayerName2(long l) {
      int i = 0;
      char ac[] = new char[99];
      while(l != 0L) {
         long l1 = l;
         l /= 37L;
         ac[11 - i++] = playerNameXlateTable[(int)(l1 - l * 37L)];
      }
      return new String(ac, 12 - i, i);
	}
	
	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}
	
	public static String ucFirst(String str) {
		str = str.toLowerCase();
		if(str.length() > 1) {
			str = str.substring(0,1).toUpperCase() + str.substring(1);
		} else {
			return str.toUpperCase();
		}
		return str;
	}
	
	public static void print_debug(String str)
	{
		System.out.print(str);				
	}
	public static void println_debug(String str)
	{
		System.out.println(str);
	}
	public static void print(String str)
	{
		System.out.print(str);
	}
	public static int indexOfPartialString(List<String> list, String s) {
		if (s == null || list == null) {
			return -1;
		}
		for (int i = 0; i < list.size(); i++) {
			String element = list.get(i);
			if (element.startsWith(s) || element.equalsIgnoreCase(s)) {
				return i;
			}
		}
		return -1;
	}
	
	public static int lastIndexOfPartialString(List<String> list, String s) {
		if (s == null || list == null) {
			return -1;
		}
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			String element = list.get(i);
			if (element.startsWith(s) || element.equalsIgnoreCase(s)) {
				index = i;
			}
		}
		return index;
	}
	
	public static void println(String str)
	{
		System.out.println(str);
	}

	public static String Hex(byte data[])
	{
		return Hex(data, 0, data.length);
	}
	public static String Hex(byte data[], int offset, int len)
	{
		String temp = "";
		for(int cntr = 0; cntr < len; cntr++) {
			int num = data[offset+cntr] & 0xFF;
			String myStr;
			if(num < 16) myStr = "0";
			else myStr = "";
			temp += myStr + Integer.toHexString(num) + " ";
		}
		return temp.toUpperCase().trim();
	}

	public static int hexToInt(byte data[], int offset, int len)
	{
		int temp = 0;
                int i = 1000;
		for(int cntr = 0; cntr < len; cntr++) {
			int num = (data[offset+cntr] & 0xFF) * i;
			temp += (int)num;
                        if (i > 1)
			  i=i/1000;
		}
		return temp;
	}
	
	public static String basicEncrypt(String s) {
		String toReturn = "";
		for (int j = 0; j < s.length(); j++) {
			toReturn += (int)s.charAt(j);
		}
		//System.out.println("Encrypt: " + toReturn);
		return toReturn;	
	}

	public static int random2(int range) { 
		return (int)((java.lang.Math.random() * range) + 1);
	}

	public static int random(int range) {
		return (int)(java.lang.Math.random() * (range+1));
	}
	public static int random(Range<Integer> range) {
		int minimum = range.getMinimum();
		return minimum + random(range.getMaximum() - minimum);
	}

	public static long playerNameToInt64(String s)
	{
		long l = 0L;
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			l *= 37L;
			if(c >= 'A' && c <= 'Z') l += (1 + c) - 65;
			else if(c >= 'a' && c <= 'z') l += (1 + c) - 97;
			else if(c >= '0' && c <= '9') l += (27 + c) - 48;
		}
		while(l % 37L == 0L && l != 0L) l /= 37L;
		return l;
	}


    private static char decodeBuf[] = new char[4096];
	public static String textUnpack(byte packedData[], int size)
	{
		int idx = 0, highNibble = -1;
		for(int i = 0; i < size*2; i++) {
			int val = packedData[i/2] >> (4-4*(i%2)) & 0xf;
			if(highNibble == -1) {
				if(val < 13) decodeBuf[idx++] = xlateTable[val];
				else highNibble = val;
			}
			else {
				decodeBuf[idx++] = xlateTable[((highNibble<<4) + val) - 195];
				highNibble = -1;
			}
		}


		return new String(decodeBuf, 0, idx);
    }

	public static String optimizeText(String text)
	{
		char buf[] = text.toCharArray();
		boolean endMarker = true;	
		for(int i = 0; i < buf.length; i++) {
            char c = buf[i];
            if(endMarker && c >= 'a' && c <= 'z') {
				buf[i] -= 0x20;	
				endMarker = false;
			}
			if(c == '.' || c == '!' || c == '?') endMarker = true;
		}
		return new String(buf, 0, buf.length);
	}
	/**
	 * Used to determine if the value of the input is a non-negative value. This does permit the value zero as valid input as zero is neither positive nor negative.
	 * 
	 * @param input the input we're trying to determine is a non-negative or not
	 * @return {@code} true if the value is greater than negative one, otherwise {@code false}.
	 */
	public static boolean isNonNegative(int input) {
		return input > -1;
	}

	public static void textPack(byte packedData[], java.lang.String text)
	{
		if(text.length() > 80) text = text.substring(0, 80);
		text = text.toLowerCase();

		int carryOverNibble = -1;
		int ofs = 0;
		for(int idx = 0; idx < text.length(); idx++) {
			char c = text.charAt(idx);
			int tableIdx = 0;
			for(int i = 0; i < xlateTable.length; i++) {
				if(c == xlateTable[i]) {
					tableIdx = i;
					break;
				}
			}
			if(tableIdx > 12) tableIdx += 195;
			if(carryOverNibble == -1) {
				if(tableIdx < 13) carryOverNibble = tableIdx;
				else packedData[ofs++] = (byte)(tableIdx);
			}
			else if(tableIdx < 13) {
				packedData[ofs++] = (byte)((carryOverNibble << 4) + tableIdx);
				carryOverNibble = -1;
			}
			else {
				packedData[ofs++] = (byte)((carryOverNibble << 4) + (tableIdx >> 4));
				carryOverNibble = tableIdx & 0xf;
			}
		}

		if(carryOverNibble != -1) packedData[ofs++] = (byte)(carryOverNibble << 4);
	}

	public static char xlateTable[] = { '_', 'a', 'b', 'c', 'd', 'e',
		'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
		's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E',
		'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
		'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','0', '1', '2', '3', '4',
		'5', '6', '7', '8', '9' };



	
	public static int direction(int srcX, int srcY, int x, int y) {
		double dx = (double) x - srcX, dy = (double) y - srcY;
		double angle = Math.atan(dy / dx);
		angle = Math.toDegrees(angle);
		if (Double.isNaN(angle))
			return -1;
		if (Math.signum(dx) < 0)
			angle += 180.0;
		return (int) ((((90 - angle) / 22.5) + 16) % 16);
	}


	public static byte directionDeltaX[] = new byte[]{ 0, 1, 1, 1, 0,-1,-1,-1 };
	public static byte directionDeltaY[] = new byte[]{ 1, 1, 0,-1,-1,-1, 0, 1 };	
	public static byte xlateDirectionToClient[] = new byte[]{ 1, 2, 4, 7, 6, 5, 3, 0 };

	public static int getCurrentHP(int i, int i1, int i2) {
		double x = (double) i / (double) i1;
		return (int) Math.round(x * i2);
	}
	public static byte[] getBuffer(File f) throws Exception
	{
		if(!f.exists())
			return null;
		byte[] buffer = new byte[(int) f.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		dis.readFully(buffer);
		dis.close();
		byte[] gzipInputBuffer = new byte[999999];
		int bufferlength = 0;
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));
		do {
			if(bufferlength == gzipInputBuffer.length)
			{
				System.out.println("Error inflating data.\nGZIP buffer overflow.");
				break;
			}
			int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
			if(readByte == -1)
				break;
			bufferlength += readByte;
		} while(true);
		byte[] inflated = new byte[bufferlength];
		System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
		buffer = inflated;
		if(buffer.length < 10)
			return null;
		return buffer;
	}
	public static String loadFile(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	public static String capitalize(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)),
						s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1),
							Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s;
	}
	public static String toFormattedHMS(long time) {
		return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time), 
				TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
				TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}
	
	public static Random random = new Random();
	public static int random(final int min, final int max) {
		return min + (max == min ? 0 : random.nextInt(max - min));
	}

	public static String toFormattedMS(long time) {
		return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(time),
				TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
	}
	public static double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	public static int random3(int range) {
		return (int) (java.lang.Math.random() * (range));
	}
	public static GameItem getRandomItem(GameItem[] itemArray) {
		return itemArray[random(itemArray.length - 1)];
	}
	public static int stringToInt(String value) throws NumberFormatException {
		value = value.toLowerCase();
		value = value.replaceAll("k", "000");
		value = value.replaceAll("m", "000000");
		value = value.replaceAll("b", "000000000");
		BigInteger bi = new BigInteger(value);
		if (bi.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
			return Integer.MAX_VALUE;
		} else if (bi.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0) {
			return Integer.MIN_VALUE;
		} else {
			return bi.intValue();
		}
	}
	public static String formatText(String username) {
		// TODO Auto-generated method stub
		return null;
	}
	public static int distanceBetween(Player c1, Player c2) {
		int x = (int) Math.pow(c1.getX() - c2.getX(), 2.0D);
		int y = (int) Math.pow(c1.getY() - c2.getY(), 2.0D);
		return (int) Math.floor(Math.sqrt(x + y));
	}
	private static char validChars[] = { ' ', 'e', 't', 'a', 'o', 'i', 'h',
			'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p',
			'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')',
			'-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%',
			'"', '[', ']', '>', '<', '^', '`', '~', '_', '/' };
		
		public static String decodeMessage(byte[] message, int size) {
			StringBuilder sb = new StringBuilder();
			boolean capitalizeNext = true;
			if (size > message.length) {
				size = message.length;
			}
			for (int i = 0; i < size; i++) {
				char c = validChars[message[i] & 0xff];
				if (capitalizeNext && c >= 'a' && c <= 'z') {
				    sb.append(Character.toUpperCase(c));
				    capitalizeNext = false;
				} else {
				    sb.append(c);				
				}
				if (c == '.' || c == '!' || c == '?') {
					capitalizeNext = true;
				}
				
			}
			return sb.toString();
		}
	public static int combatDifference(Player player, Player player2) {
		if (player.combatLevel > player2.combatLevel)
			return player.combatLevel - player2.combatLevel;
		else if (player.combatLevel < player2.combatLevel)
			return player2.combatLevel - player.combatLevel;
		return 0;
	}
	public static int distanceToPoint(int x1, int y1, int x2, int y2) {
		int x = (int) Math.pow(x1 - x2, 2.0D);
		int y = (int) Math.pow(y1 - y2, 2.0D);
		return (int) Math.floor(Math.sqrt(x + y));
	}
	/**
	 * Returns the delta coordinates. Note that the returned Position is not an
	 * actual position, instead it's values represent the delta values between
	 * the two arguments.
	 * 
	 * @param a
	 *            the first position.
	 * @param b
	 *            the second position.
	 * @return the delta coordinates contained within a position.
	 */
	public static Position delta(Position a, Position b) {
		return new Position(b.getX() - a.getX(), b.getY() - a.getY());
	}
	public static <T> List<T> jsonArrayToList(Path path, Class<T[]> clazz) {
		try {
			T[] collection = new Gson().fromJson(Files.newBufferedReader(path), clazz);
			return new ArrayList<T>(Arrays.asList(collection));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static <T> T randomSearch(T[] elements, int inclusive, int exclusiveLength) {
		Preconditions.checkArgument(exclusiveLength <= elements.length, "The length specified is greater than the length of the array.");
		return elements[RandomUtils.nextInt(inclusive, exclusiveLength)];
	}
	public static String[] nullToEmpty(int length) {
		String[] output = new String[length];
		Arrays.fill(output, 0, length, "");
		return output;
	}
	public static String insertCommas(String str) {
		if (str.length() < 4) {
			return str;
		}
		return insertCommas(str.substring(0, str.length() - 3)) + ","
				+ str.substring(str.length() - 3, str.length());
	}
	public static String getValueRepresentation(long amount) {
		StringBuilder bldr = new StringBuilder();
		if (amount < 1_000) {
			bldr.append(amount);
		} else if (amount >= 1_000 && amount < 1_000_000) {
			bldr.append("@cya@" + Long.toString(amount / 1_000) + "K <col=ffffff>("
					+ insertCommas(Long.toString(amount)) + ")");
		} else if (amount >= 1_000_000) {
			bldr.append("<col=3CB71E>" + Long.toString(amount / 1_000_000)
					+ "M <col=ffffff>(" + insertCommas(Long.toString(amount)) + ")");
		}
		return bldr.toString();
	}
	public static int randomSearch(int[] elements, int inclusive, int exclusiveLength) {
		Preconditions.checkArgument(exclusiveLength <= elements.length, "The length specified is greater than the length of the array.");
		return elements[RandomUtils.nextInt(inclusive, exclusiveLength)];
	}
	public static GameItem getRandomItem(List<GameItem> itemArray) {
		return itemArray.get(random(itemArray.size() - 1));
	}
	public static String longToReportPlayerName(long l) {
		int i = 0;
		final char ac[] = new char[12];
		while (l != 0L) {
			final long l1 = l;
			l /= 37L;
			ac[11 - i++] = Misc.playerNameXlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}
	public static int randomMinusOne(int range) {
		int number = (int) (Math.random() * range);
		return number < 0 ? 0 : number;
	}
	public static double preciseRandom(Range<Double> range) {
		Preconditions.checkArgument(range.getMinimum() <= range.getMaximum(), "The maximum range cannot be less than the minimum range.");
		return range.getMinimum() + (new Random().nextDouble() * (range.getMaximum() - range.getMinimum()));
	}
	public static String getValueWithoutRepresentation(long amount) {
		StringBuilder bldr = new StringBuilder();
		if (amount < 1_000) {
			bldr.append(amount);
		} else if (amount >= 1_000 && amount < 1_000_000) {
			bldr.append(Long.toString(amount / 1_000) + "K");
		} else if (amount >= 1_000_000) {
			bldr.append(Long.toString(amount / 1_000_000) + "M");
		}
		return bldr.toString();
	}






}
