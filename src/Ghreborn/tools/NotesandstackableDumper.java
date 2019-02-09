package Ghreborn.tools;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import Ghreborn.definitions.ItemCacheDefinition;

public class NotesandstackableDumper {

	public static void main(String[] args) throws IOException {
	      dumpNotes();
	      dumpNotableList();
	      dumpStackable();
	      dumpStackableList();
	}
	public static void dumpNotes() {
		try {
			FileOutputStream out = new FileOutputStream(new File("notes.dat"));
			for (int j = 0; j < 23060; j++) {
				ItemCacheDefinition item = ItemCacheDefinition.forID(j);
				for (int i = 0; i < 23060; i++)
						out.write(item.certTemplateID != -1 ? 0 : 1);
				System.out.println("Dumped id "+j);
			}
			out.write(-1);
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	public static void dumpStackableList() {
		try {
			File file = new File("stackables.dat");

			if (!file.exists()) {
				file.createNewFile();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (int i = 0; i < 23060; i++) {
					ItemCacheDefinition definition = ItemCacheDefinition.forID(i);
					if (definition != null) {
						writer.write(definition.id + "\t" + definition.stackable);
						writer.newLine();
					} else {
						writer.write(i + "\tfalse");
						writer.newLine();
					}
					System.out.println("Dumped id "+i);
				}
			}

			System.out.println("Finished dumping noted items definitions.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void dumpStackable() {
		try {
			FileOutputStream out = new FileOutputStream(new File("stackable.dat"));
			for (int j = 0; j < 23060; j++) {
				ItemCacheDefinition item = ItemCacheDefinition.forID(j);
				out.write(item.stackable ? 1 : 0);
				System.out.println("Dumped id "+j);
			}
			out.write(-1);
			out.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	public static void dumpNotableList() {
		try {
			File file = new File("note_id.dat");

			if (!file.exists()) {
				file.createNewFile();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (int i = 0; i < 23060; i++) {
					ItemCacheDefinition definition = ItemCacheDefinition.forID(i);
					if (definition != null) {
						if (definition.certTemplateID == -1 && definition.certID != -1) {
							writer.write(definition.id + "\t" + definition.certID);
							writer.newLine();
						}
					} else {
						writer.write(i + "\t-1");
						writer.newLine();
					}
					System.out.println("Dumped id "+i);
				}
			}

			System.out.println("Finished dumping noted items definitions.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
