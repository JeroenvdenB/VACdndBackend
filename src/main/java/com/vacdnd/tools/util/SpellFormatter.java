package com.vacdnd.tools.util;

import com.vacdnd.tools.domain.Spell;

public class SpellFormatter {
	public static String formatSpell(Spell spell) {
		String header = String.format("====== %s ======\n\n----\n\n" , spell.name);
		String source = String.format("Source: %s \\\\ \n", spell.source);
		
		String school = "";
		String ritual = "";
		if (spell.ritual) {
			ritual = " (ritual)";
		}
		switch (spell.level) {
			case 0: 
				school = String.format("//%s cantrip%s//\\\\ \n", spell.school, ritual);
				break;
			case 1: 
				school = String.format("//1st-level %s%s//\\\\ \n", spell.school,ritual);
				break;
			case 2: 
				school = String.format("//2nd-level %s%s//\\\\ \n", spell.school, ritual);
				break;
			case 3: 
				school = String.format("//3rd-level %s%s//\\\\ \n", spell.school, ritual);
				break;
			default:
				school = String.format("//%dth-level %s%s//\\\\ \n", spell.level, spell.school, ritual);
		}
		String emptyLine = "\\\\\n";
		
		String castingTime = String.format("**Casting Time:** %s\\\\\n", spell.castingTime);
		String range = String.format("**Range:** %s\\\\\n", spell.range);
		
		//This corrects for a messy component string (possibly ending in ", ")
		String comp = spell.components;
		if (comp.endsWith(", ")) {
			comp = comp.substring(0, comp.length()-2);
		}
		String components = String.format("**Components:** %s\\\\\n", comp);
		
		String duration = String.format("**Duration:** %s\\\\\n", spell.duration);
		String description = spell.description.concat("\\\\\n");
		String higherLevels = String.format("//**At Higher Levels.**// %s\\\\\n", spell.higherLevels);
		
		String lists = "//**Spell Lists.**// ";
		for (String dndClass: spell.lists) {
			lists += String.format("[[spells:%s:lvl0|%s]], ", dndClass, dndClass);
		}
		lists = lists.substring(0,lists.length()-2);
		
		String formattedSpell = header + 
				source + 
				school + 
				emptyLine +
				castingTime + 
				range +
				components +
				duration +
				emptyLine +
				description +
				emptyLine +
				higherLevels +
				emptyLine +
				lists;
		
		return formattedSpell;
	}
	
	public static Spell parsePastedText (String receivedSpell) {
		Spell spell = new Spell();
		String[] head = receivedSpell.substring(0, receivedSpell.indexOf("Casting Time")).split("\n");
		spell.name = head[0].trim();
		
		// Debugging option to show all the lines in head
//		for (int i=0; i <head.length; i++) {
//			System.out.println("line " + i + ": " + head[i]);
//		}
		
		// The location of the source can vary how the browser copy-pastes. That's why I search for it.
		int sourceLineNumber = 0; // So the next search-loop can continue where this one stopped.
		SEARCHSOURCE: for (int i = 0; i < head.length; i++) {
			if (head[i].contains("Source:")) {
				spell.source = head[i].substring(head[i].indexOf("Source:")+7).trim();
				sourceLineNumber = i;
				break SEARCHSOURCE;
			}		
		}
		
		// Location of the school and level can vary too.
		int schoolLineIndex = 0;
		SEARCHSCHOOL: for (int i = sourceLineNumber; i < head.length; i++) {
			if(head[i].toLowerCase().contains("cantrip") || head[i].toLowerCase().contains("level")) { // That's the line I'm looking for
				schoolLineIndex = i;
				break SEARCHSCHOOL;
			}
		}
		
			
		// Cantrips state "school [space] cantrip"
		// Leveled spells state "[number]rd-level [space] school"
		String [] schoolAndLevel = head[schoolLineIndex].split(" ");	
		if (schoolAndLevel[1].equals("cantrip")) {
			spell.school = schoolAndLevel[0].toLowerCase();
			spell.level = 0;	
		} else {
			spell.school = schoolAndLevel[1].toLowerCase().trim();
			spell.level = schoolAndLevel[0].charAt(0)-48; //number 1 is character 49
		}
		
		// If a third element exists in schoolAndLevel, it will be the "(ritual)" tag
		try {
			if (schoolAndLevel[2].equals("(ritual)")) {
				spell.ritual = true;
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			// Nothing happens if the error is thrown. Just move on.
		}
		
		String[] body = receivedSpell.substring(receivedSpell.indexOf("Casting Time")).split("\n");
		spell.castingTime = body[0].substring(body[0].indexOf(":")+1).trim();
		spell.range = body[1].substring(body[1].indexOf(":")+1).trim();
		spell.components = body[2].substring(body[2].indexOf(":")+1).trim();
		spell.duration = body[3].substring(body[3].indexOf(":")+1).trim();
		
		// Reminder: spell.description might need to be changed to get proper paragraphs. Try without
		// modification first.
		String description = "";
		for (int i=4 ; i < body.length; i++) {
			if (body[i].contains("At Higher Levels.")) {
				spell.higherLevels = body[i].substring(body[i].indexOf(".")+1).trim();
			} else if(body[i].contains("Spell Lists.")) {
				String spellLists = body[i].substring(body[i].indexOf(".")+1).trim();
				if (spellLists.contains("(")) {
					spellLists = spellLists.replaceAll("\\((.*?)\\)", ""); // Regex: select anything between (), par included, non greedy
				}
				spellLists = spellLists.replaceAll("\\s*", ""); // Regex: remove any spaces
				spell.lists = spellLists.split(",");
			} else if(body[i].length() > 1){ // empty lines or spaces should be skipped
				description = description.concat(body[i]).concat("\n\n"); // ensures correct spacing between paragraphs
			}			
		}
		spell.description = description.trim();
		
		if(spell.higherLevels == null) {
			spell.higherLevels = "N/A";
		}
		
		// Check if all properties were set correctly
//		System.out.println(spell.name);
//		System.out.println(spell.school);
//		System.out.println(spell.level);
//		System.out.println(spell.castingTime);
//		System.out.println(spell.range);
//		System.out.println(spell.components);
//		System.out.println(spell.duration);
//		System.out.println(spell.description);
//		System.out.println(spell.higherLevels);
//		for (String line:spell.lists) {
//			System.out.println(line);
//		}
//		System.out.println(spell.source);
//		System.out.println(spell.ritual);
		
		return spell;
	}
}
