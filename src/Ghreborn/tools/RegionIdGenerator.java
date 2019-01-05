package Ghreborn.tools;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
/**
 * 
 * @author Tyler
 *
 */
public class RegionIdGenerator {
	public static void main(String[] args) throws IOException {
		Scanner input = new Scanner(System.in);
		BufferedWriter writer = null;
		File file = new File("regionIds.txt");
		writer = new BufferedWriter(new FileWriter(file));
		System.out.println("Please enter the x and y coordinate.");
		while(input.hasNextLine()) {
			if(!input.hasNextInt()) {
				break;
			}
			int x = input.nextInt();
			int y = input.nextInt();
			int rx = x>>6;
			int ry = y>>6;
			int chunkX = x >> 3;
			int chunkY = y >> 3;
			int regionId = rx * 256 + ry;
			System.out.println("RegionId: "+regionId+ " from coords: "+x+ ": "+y+" : "+chunkX+" : "+chunkY);
			writer.append("RegionId: "+regionId+ " from coords: "+x+ ": "+y+" : "+chunkX+" : "+chunkY);
			writer.newLine();
			writer.flush();
		}
		writer.close();
	}
}