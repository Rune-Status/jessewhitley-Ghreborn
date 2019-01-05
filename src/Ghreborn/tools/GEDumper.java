package Ghreborn.tools;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import Ghreborn.Config;
import Ghreborn.definitions.ItemCacheDefinition;
import Ghreborn.model.items.ItemDefinition;

/**
 * 
 * @author http://www.rune-server.org/members/sponjebobu/
 * 
 * 
 *         This class is used to dump grand exchange prices from RuneScape 2007
 *         Wiki
 */

public class GEDumper {

	public static void main(String[] parameters) {

		for (int index = 0; index < 22731; index++) {

			/**
			 * Fetches the item definition class
			 */

			ItemCacheDefinition def = ItemCacheDefinition.forID(index);

			/**
			 * If item definition is null we continue;
			 */
			if (def == null) {
				continue;
			}

			/**
			 * Represents the websites "lines" / "code lines"
			 */
			String line = "";

			/**
			 * Declare a String variable for the item name Replace any <space>
			 * with '_'
			 */
			String itemName = def.getName().replace(" ", "_");

			try {

				/**    public static boolean checkObject(int object, int x, int y, int h) {
        return getObject(object, x, y, h) != null;
    }

	public static GameObjectDef getObject(int object, int x, int y, int h) {
		final GameObject p = ObjectHandler.getInstance().getObject(object, x, y, h);
		if (p != null) {
			return p.getDef();
		}
		final CacheObject g = ObjectLoader.object(object, x, y, h);
		if (g != null) {
			return g.getDef();
		}
		return null;
	}

				 * URL getter
				 */
				URL url = new URL("https://oldschool.runescape.wiki/w/"
						+ itemName);

				/**
				 * Application now connects to
				 * http://2007.runescape.wikia.com/wiki/itemName
				 */
				url.openConnection();

				/**
				 * Scanner reads through the websites code
				 */
				Scanner scanner = new Scanner(new InputStreamReader(
						url.openStream()));

				/**
				 * BufferedWriter write the data we need to a .txt file
				 */
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"prices.txt", true));

				while (scanner.hasNextLine()) {

					/**
					 * Everytime the while loop loops String line fetches a new
					 * line of the website
					 */
					line = scanner.nextLine();

					if (line.contains("</th><td> <span id=\"GEPrice\"><span class=\"GEItem\"><span>")) {
						line = line
								.replace(
										"</th><td> <span id=\"GEPrice\"><span class=\"GEItem\"><span>",
										"");
						line = line.replace("</span></span> coins</span>", "");
						line = line.replace(" (<a href=\"/wiki/Exchange:"
								+ itemName + "\" title=\"Exchange:" + itemName
								+ "\">info</a>)", "");
						line = line.replace(
								" (<a href=\"/wiki/Exchange:" + itemName
										+ "\" title=\"Exchange:"
										+ def.getName() + "\">info</a>)", "");
						line = line.replace(",", "").replace(" ", "");

						if (Integer.valueOf(line) == null) {
							continue;
						}

						int price = Integer.valueOf(line);

						writer.write(index + ":" + price);

						System.out.println(index + ":" + price);

						writer.newLine();
					}

				}
				scanner.close();
				writer.close();

			} catch (IOException e) {

			}
		}

	}
}