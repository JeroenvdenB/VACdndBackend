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
}
