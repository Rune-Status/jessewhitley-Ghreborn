package Ghreborn.util;

import Ghreborn.model.players.Client;

/**
 * @author Jayden
 */

public class Text {

	public static String readChatboxText(Client c) {
		int count = c.getInStream().readUnsignedByte();
		char[] messageChars = new char[count];
		for (int i = 0; i < count; i++) {
			messageChars[i] = validChatCharacters[c.getInStream().readUnsignedByte()];
		}
		return optimizeText(new String(messageChars, 0, count));
	}

	public static void writeChatboxText(String text, Stream str) {
		if (text.length() > 80) {
			text.substring(0, 80);
		}
		Stream tempStream = new Stream(new byte[100]);
		text = text.toLowerCase();
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			for (int j = 0; j < validChatCharacters.length; j++) {
				if (c != validChatCharacters[j]) {
					continue;
				}
				tempStream.writeByte(j);
				count++;
				break;
			}
		}
		str.writeByte(count);
		str.writeBytes(tempStream.buffer, tempStream.currentOffset, 0);
	}

	public static String longToName(long l) {
		int i = 0;
		char ac[] = new char[12];

		while (l != 0L) {
			long l1 = l;

			l /= 37L;
			ac[11 - i++] = validCharacterNameChars[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	private static String optimizeText(String text) {
		char buf[] = text.toCharArray();
		boolean endMarker = true;
		for (int i = 0; i < buf.length; i++) {
			char c = buf[i];
			if (endMarker && c >= 'a' && c <= 'z') {
				buf[i] -= 0x20;
				endMarker = false;
			}
			if (c == '.' || c == '!' || c == '?') {
				endMarker = true;
			}
		}
		return new String(buf, 0, buf.length);
	}

	public static long nameToLong(String s) {
		long l = 0L;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z') {
				l += (1 + c) - 65;
			} else if (c >= 'a' && c <= 'z') {
				l += (1 + c) - 97;
			} else if (c >= '0' && c <= '9') {
				l += (27 + c) - 48;
			}
		}
		while (l % 37L == 0L && l != 0L) {
			l /= 37L;
		}
		return l;
	}

	private static final char[] validCharacterNameChars = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static final char[] validChatCharacters = { ' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm', 'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=', '\243', '$', '%', '"', '[', ']', '<', '>', '`', '/', '_',
			'^' };

	public static final String[] messageCodes = { "@369@", "@mon@", "<col=DD5C3E>", "<col=3CB71E>", "<col=0000FF>", "<col=ffff00>", "@cya@", "@mag@", "<col=ffffff>", "<col=000000>", "@lre@", "<col=8B0000>", "<col=00008B>", "@or1@", "@or1@", "@or2@",
			"<col=ff3000>", "@gr1", "@gr2", "@gr3", "@str@", "@end@" };
}
