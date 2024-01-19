package com.vacdnd.tools.endpoints;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vacdnd.tools.domain.Spell;
import com.vacdnd.tools.util.SpellFormatter;

@RestController
public class SpellEndpoint {

	@PostMapping("addSpell")
	public void addSpell(@RequestBody Spell spell) {
		String formattedSpell = SpellFormatter.formatSpell(spell);
		spell.SpellUploader(formattedSpell);
	}
	
	@PostMapping("pastedSpell")
	public void pastedSpell(@RequestBody String receivedSpell) {

		Spell spell = new Spell();
		String[] head = receivedSpell.substring(0, receivedSpell.indexOf("Casting Time")).split("\n");
		spell.name = head[0].trim();
		spell.source = head[1].substring(head[1].indexOf("Source:")+7).trim();
		
		// Cantrips state "school [space] cantrip"
		// Leveled spells state "[number]rd-level [space] school"
		String[] schoolAndLevel = head[3].split(" ");
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
		System.out.println(body.length);
		String description = "";
		for (int i=4 ; i < body.length; i++) {
			System.out.println(body[i]);
			if (body[i].contains("At Higher Levels.")) {
				spell.higherLevels = body[i].substring(body[i].indexOf(".")+1).trim();
			} else if(body[i].contains("Spell Lists.")) {
				String spellLists = body[i].substring(body[i].indexOf(".")+1).trim();
				if (spellLists.contains("(")) {
					spellLists = spellLists.replaceAll("\\(([^\\)]+)\\)", ""); //Cut out anything between () 
				}
				spell.lists = spellLists.split(", ");
			} else if(body[i].length() > 1){ //empty lines should be skipped
				description = description.concat(body[i]).concat("\n\n");
			}			
		}
		spell.description = description.trim();
		
		if(spell.higherLevels == null) {
			spell.higherLevels = "N/A";
		}
		
		// Check if all properties were set correctly
		System.out.println(spell.name);
		System.out.println(spell.school);
		System.out.println(spell.level);
		System.out.println(spell.castingTime);
		System.out.println(spell.range);
		System.out.println(spell.components);
		System.out.println(spell.duration);
		System.out.println(spell.description);
		System.out.println(spell.higherLevels);
		for (String line:spell.lists) {
			System.out.println(line);
		}
		System.out.println(spell.source);
		System.out.println(spell.ritual);
		
		String formattedSpell = SpellFormatter.formatSpell(spell);
		spell.SpellUploader(formattedSpell);
	}

}
