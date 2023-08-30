package com.vacdnd.tools.domain;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import com.vacdnd.tools.util.FTPUploader;
import com.vacdnd.tools.util.FTPDownloader;

public class Item {
	
	public String name;
	public String source;
	public String itemType;
	public String typeComment;
	public String rarity;
	public boolean attunement;
	public String attunementComment;
	public String description;
	
	public void ItemUploader(String formattedItem) {
		/**
		 * A method to upload the item instance to the vacdnd.com dokuwiki file system. 
		 * It accepts a formatted item string, ready for upload, and will infer
		 * the appropriate file names and locations from Item attributes. The method 
		 * does not return anything.
		 * 
		 * @param	formatteditem	Formatted spell string. Will be uploaded literally, as is.
		 * @return	void
		 * 
		 * @author jvand
		 */
		
		//Dokuwiki pages are all lower case without spaces. This makes a file name.
		//Dokuwiki file systems also don't allow for '. Replace with underscore.
		String fileName = this.name.toLowerCase().replaceAll(" ", "_").replaceAll("'",  "_").replaceAll(",", "") + ".txt";
		
		File tempItemFile = new File(fileName);
		
		try {
			FileUtils.writeStringToFile(tempItemFile, formattedItem, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FTPUploader uploader = new FTPUploader();
		uploader.upload(tempItemFile, "wiki/data/pages/item", fileName);
		tempItemFile.delete();
		
		try {
			ItemListUpdate();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void ItemListUpdate() throws IOException {
		
		FTPDownloader downloader = new FTPDownloader();
		
		File tempItemList = new File("tempItemList.txt");
		String targetFileName = this.rarity.toLowerCase().replaceAll(" ", "_").replaceAll("'", "_").replaceAll(",", "");
		String remoteOrigin = String.format("/public_html/wiki/data/pages/itemlist/%s.txt", targetFileName);
		downloader.download(remoteOrigin, tempItemList);
		
		String fileContent = FileUtils.readFileToString(tempItemList, Charset.forName("UTF-8"));
	
		// '^' is used in headers only. The last occurrence is the end of the headers. 
		// add 2, to include '^' and '\n' after it.
		String headers = fileContent.substring(0,fileContent.lastIndexOf('^')+2);
		String table = fileContent.substring(fileContent.lastIndexOf('^')+2);
		
		String attunementTableNotation;
		if (this.attunement) {
			attunementTableNotation = "Attuned";
		} else {
			attunementTableNotation = "-";
		}
		
		String newItemLine = String.format("|[[item:%s]] |%s |%s |", this.name, this.itemType, attunementTableNotation);
		
		//Check if the item is already on the list. indexOf returns -1 if the string is not present.
		int positionCheck = table.indexOf(String.format("|[[item:%s]]", this.name));
		if (positionCheck == -1) { //Item is not yet on the list: add and sort
			System.out.println("Spell is new to the list. Adding and sorting...");
			table = table.concat(newItemLine);
			String[] tableLines = table.split("\n");
			Arrays.sort(tableLines);
			table = "";
			for (int i=0; i<tableLines.length; i++) {
				table = table.concat(tableLines[i] + "\n");
			}		
		} else { //Item is on the list: identify position and replace line
			System.out.println("Spell is already on the list. Updating line...");
			String[] tableLines = table.split("\n");
			REPLACE: for (int i=0; i<tableLines.length; i++) {
				if (tableLines[i].contains(String.format("|[[spell:%s]]", this.name))) {
					tableLines[i] = newItemLine;
					break REPLACE;
				}
			}
			table = "";
			for (int i=0; i<tableLines.length; i++) {
				table = table.concat(tableLines[i] + "\n");
			}	
		}
		
		fileContent = headers + table;
		
		FileUtils.writeStringToFile(tempItemList, fileContent, Charset.forName("UTF-8"));
		
		FTPUploader uploader = new FTPUploader();
		String fileDestination ="/public_html/wiki/data/pages/itemlist";
		uploader.upload(tempItemList, fileDestination, String.format("%s.txt", targetFileName));
		
		tempItemList.delete();
	
	}
}
