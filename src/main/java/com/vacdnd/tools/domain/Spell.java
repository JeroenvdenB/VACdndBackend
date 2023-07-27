package com.vacdnd.tools.domain;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import com.vacdnd.tools.util.FTPUploader;
import com.vacdnd.tools.util.FTPDownloader;

public class Spell {
	
	public String name;
	public String school;
	public int level;
	public String castingTime;
	public String range;
	public String components;
	public String duration;
	public String description;
	public String higherLevels;
	public String[] lists;
	public String source;
	public boolean ritual;
	
	public void SpellUploader(String formattedSpell) {
		/**
		 * A method to upload the spell instance to the vacdnd.com dokuwiki file system. 
		 * It accepts a formatted spell string, ready for upload, and will infer
		 * the appropriate file names and locations from Spell attributes. The method 
		 * does not return anything.
		 * 
		 * @param	formattedSpell	Formatted spell string. Will be uploaded literally, as is.
		 * @return	void
		 * 
		 * @author jvand
		 */
				
		//Dokuwiki pages are all lower case without spaces. This makes a file name.
		String fileName = this.name.toLowerCase().replaceAll(" ", "_") + ".txt";
				
		File tempSpellFile = new File(fileName); //Don't forget to delete this again
				
		try {
			FileUtils.writeStringToFile(tempSpellFile, formattedSpell, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		FTPUploader uploader = new FTPUploader();
		uploader.upload(tempSpellFile, "wiki/data/pages/spell", fileName);
		tempSpellFile.delete();
		
		try {
			SpellListUpdate(this.lists, this.level);
		} catch (IOException e) {
			System.out.println("Problem in SpellListUpdate, reading file to string.");
		}
	}
	
	public void SpellListUpdate(String[] dndClasses, int level) throws IOException {
		/**
		 * A method to update new spells to existing spell lists in the dokuwiki file structure.
		 * All file names and locations are highly dependent on this particular application.
		 * 
		 * @param 	dndClasses	A String[] of all classes that the new spell belongs to.
		 * @param	level		int representing the spell level. This determines the sublist.
		 * @return	void
		 * 
		 * @author jvand
		 */
		
		FTPDownloader downloader = new FTPDownloader();
		
		String[] classes = new String[dndClasses.length+1];
		for (int i = 0; i<dndClasses.length; i++) {
			classes[i] = dndClasses[i];
		}
		classes[classes.length-1] = "All";
		
		for (String dndClass: classes) {
			File tempClassList = new File("tempClassList.txt");
			String remoteOrigin = String.format("/public_html/wiki/data/pages/spells/%s/lvl%d.txt", dndClass.toLowerCase(), level);
			downloader.download(remoteOrigin, tempClassList);
			
			String fileContent = FileUtils.readFileToString(tempClassList, Charset.forName("UTF-8"));
			
			// '^' is used in headers only. The last occurrence is the end of the headers. 
			// add 2, to include '^' and '\n' after it.
			String headers = fileContent.substring(0,fileContent.lastIndexOf('^')+2);
			String table = fileContent.substring(fileContent.lastIndexOf('^')+2);
			
			//Components should be shortened for the overview table (materials only show as M).
			String shortComponents = this.components;
			if (shortComponents.contains("(")) { //contains M (materials). Parenthesis need to be removed.
				shortComponents = shortComponents.substring(0,this.components.lastIndexOf('('));
			} else { //ends in "V, S, " or similar. Cut off at comma.
				shortComponents = shortComponents.substring(0,this.components.lastIndexOf(','));
			}
			
			//Custom casting times are too long for the table to display properly
			//Check if the entry is too long and replace it by 'custom'
			//Check for a ritual too while we're at it
			String castingTime = this.castingTime.length() < 16 ? this.castingTime : 
				this.castingTime.substring(0, this.castingTime.indexOf(','));
			castingTime = this.ritual ? castingTime + " (R)" : castingTime;
			
			String newSpellLine = String.format("|[[spell:%s]] |//%s// |%s |%s |%s |%s |" , this.name, this.school,
					castingTime, this.range, this.duration, shortComponents);
			
			
			table = table.concat(newSpellLine);
			String[] tableLines = table.split("\n");
			Arrays.sort(tableLines);
			table = "";
			for (int i=0; i<tableLines.length; i++) {
				table = table.concat(tableLines[i] + "\n");
			}		
			fileContent = headers + table;
			
			FileUtils.writeStringToFile(tempClassList, fileContent, Charset.forName("UTF-8"));
			
			FTPUploader uploader = new FTPUploader();
			String fileDestination = String.format("/public_html/wiki/data/pages/spells/%s/", dndClass.toLowerCase());
			uploader.upload(tempClassList, fileDestination, String.format("lvl%d.txt", level));
			
			tempClassList.delete();
		}
		
		
	}
	
	
}
