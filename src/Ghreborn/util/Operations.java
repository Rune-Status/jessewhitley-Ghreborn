package Ghreborn.util;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import Ghreborn.model.items.Item;

public class Operations {
	public static DateFormat dateFormat;
	private static Date date;

	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance) && (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public static double max(double... values) {
		int index = 0;
		double val = -1;

		while (index < values.length) {
			if (val == -1) {
				val = values[index];
			}
			for (int i = 0; i < values.length; i++) {
				if (index == i) {
					continue;
				}
				if (val < values[i]) {
					index++;
					val = -1;
					break;
				}
			}
			if (val > -1) {
				return values[index];
			}
		}
		return -1; // unlikely
	}

	public int getSeconds(long time) {
		int seconds = (int) (time / 1000);
		if (seconds > 60) {
			seconds = seconds % 60;
		}
		return seconds;
	}

	public int getMinutes(long time) {
		int minutes = (int) (time / (60 * 1000));
		if (minutes >= 60) {
			minutes = minutes % 60;
		}
		return minutes;
	}

	public int getHours(long time) {
		int hours = (int) (time / (60 * 60 * 1000));
		if (hours >= 24) {
			hours = hours % 24;
		}
		return hours;
	}

	public int getDays(long time) {
		return (int) (time / (24 * 60 * 60 * 1000));
	}

	public static String removeMsgCodes(String str) {
		for (String s : Text.messageCodes) {
			str = str.replace(s, "");
		}
		return str;
	}

	public static void fillEmptySlots(Item[] items) {
		for (int i = 0; i < items.length; i++) {
			for (int j = 0; j < items.length - i; j++) {
				if (j - 1 == -1) {
					continue;
				}
				if (items[j].getId() > 0 && items[j - 1].getId() == 0) {
					Item temp = items[j];
					items[j] = items[j - 1];
					items[j - 1] = temp;
				}
			}
		}
	}

	public static void bubbleSortMulti(int[][] MultiIn, int compIdx) {
		int[][] temp = new int[MultiIn.length][MultiIn[0].length];
		boolean finished = false;
		while (!finished) {
			finished = true;
			for (int i = 0; i < MultiIn.length - 1; i++) {
				if (MultiIn[i][compIdx] > MultiIn[i + 1][compIdx]) {
					for (int j = 0; j < MultiIn[i].length; j++) {
						temp[i][j] = MultiIn[i][j];
						MultiIn[i][j] = MultiIn[i + 1][j];
						MultiIn[i + 1][j] = temp[i][j];
					}
					finished = false;
				}
			}
		}
	}

	public static void bubbleSort(int[] array) {
		for (int counter = 0; counter < array.length - 1; counter++) {
			for (int index = 0; index < array.length - 1 - counter; index++) {
				if (array[index] > array[index + 1]) {
					int temp = array[index];
					array[index] = array[index + 1];
					array[index + 1] = temp;
				}
			}
		}
	}

	public static String capitalize(String s) {
		return (s.length() > 0) ? Character.toUpperCase(s.charAt(0)) + s.substring(1) : s;
	}

	public static final char playerNameXlateTable[] = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0',
			'1', '2', '3', '4', '5', '6', '7', '8', '9', '[', ']', '/', '-', ' ' };

	public static String longToName(long l) {
		int i = 0;
		char ac[] = new char[99];
		while (l != 0L) {
			long l1 = l;
			l /= 37L;
			ac[11 - i++] = playerNameXlateTable[(int) (l1 - l * 37L)];
		}
		return new String(ac, 12 - i, i);
	}

	public static String format(int num) {
		return NumberFormat.getInstance().format(num);
	}

	public static int hexToInt(byte data[], int offset, int len) {
		int temp = 0;
		int i = 1000;
		for (int cntr = 0; cntr < len; cntr++) {
			int num = (data[offset + cntr] & 0xFF) * i;
			temp += num;
			if (i > 1) {
				i = i / 1000;
			}
		}
		return temp;
	}

	public static String basicEncrypt(String s) {
		String toReturn = "";
		for (int j = 0; j < s.length(); j++) {
			toReturn += (int) s.charAt(j);
		}
		return toReturn;
	}

	public static int random(int range) {
		return (int) (Math.random() * (range + 1));
	}

	public static int random(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public static int direction(int srcX, int srcY, int x, int y) {
		double dx = (double) x - srcX, dy = (double) y - srcY;
		double angle = Math.atan(dy / dx);
		angle = Math.toDegrees(angle);
		if (Double.isNaN(angle)) {
			return -1;
		}
		if (Math.signum(dx) < 0) {
			angle += 180.0;
		}
		return (int) ((((90 - angle) / 22.5) + 16) % 16);
	}

	public static String getDateTime() {
		if (date == null || dateFormat == null) {
			dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
			dateFormat.setTimeZone(TimeZone.getTimeZone("utc"));
			date = new Date();
		}
		date.setTime(System.currentTimeMillis());
		return dateFormat.format(date);
	}

	public static byte directionDeltaX[] = new byte[] { 0, 1, 1, 1, 0, -1, -1, -1 };

	public static byte directionDeltaY[] = new byte[] { 1, 1, 0, -1, -1, -1, 0, 1 };

	public static byte xlateDirectionToClient[] = new byte[] { 1, 2, 4, 7, 6, 5, 3, 0 };
}
