package com.vacdnd.tools.util;

import java.io.*;

public class SpellListInitializer {
	
	String[] classes = new String[] {"Artificer", "Bard", "Cleric", "Druid", "Paladin", "Ranger", "Sorcerer", "Warlock", "Wizard"};
	
	public void printList() {
		for (String type: classes) {
			System.out.println(type);
		}
	}
	
	public void helpme(String type, int maxLevel) {
		//Start with the header - identical in ALL files we're about to create
		String header = "====== "+ type + " Spell List ====== \n----\n\n";
		String fullTable = header;
		String spellTableHeader = "\n\n^Spell Name ^School ^Casting Time ^Range ^Duration ^Components ^\n";
		
		//Generate general purpose table for class, sized to the max number of levels
		for (int i = 0; i <= maxLevel; i++) {
			if (i==0) {
				String tablePiece = String.format("^[[spells:%s:lvl%d|%s]] ", type, i, "Cantrip");
				fullTable += tablePiece;				
			} else {
				String tablePiece = String.format("^[[spells:%s:lvl%d|Lvl %d]] ", type, i, i);
				fullTable += tablePiece;	
			}
		}
		fullTable = fullTable + "^" + spellTableHeader;
		
		//Based off general purpose table, we make an adaptation per level.
		//Save each level to its own file. Files will be FTP'd manually to the wiki.
		for (int i = 0; i<=maxLevel; i++) {
			String target;
			String replacement;
			
			if (i==0) {
				target = String.format("^[[spells:%s:lvl%d|%s]] ", type, i, "Cantrip");	
				replacement = String.format("^//[[spells:%s:lvl%d|%s]]// ", type, i, "Cantrip");
			} else {
				target = String.format("^[[spells:%s:lvl%d|Lvl %d]] ", type, i, i);	
				replacement = String.format("^//[[spells:%s:lvl%d|Lvl %d]]// ", type, i, i);
			}
			
			String levelTable = fullTable.replace(target, replacement);
			
			try {
				File levelFile = new File("lvl"+i+".txt");
				if (levelFile.createNewFile()) {
					System.out.println("File created: " + levelFile.getName());
				} else {
					System.out.println("File already exists.");
				}
				
				FileWriter myWriter = new FileWriter(levelFile.getName());
				myWriter.write(levelTable);
				myWriter.close();
				
				System.out.println("Written successfully.");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
