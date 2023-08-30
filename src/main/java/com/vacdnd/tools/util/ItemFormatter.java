package com.vacdnd.tools.util;

import com.vacdnd.tools.domain.Item;

public class ItemFormatter {
	
	public static String formatItem(Item item) {
		String header = String.format("====== %s ======\n\n----\n\n" , item.name);
		String source = String.format("Source: %s\n\n", item.source);
		
		String itemType;
		if (item.typeComment.isBlank()) {
			itemType = String.format("//%s, //", item.itemType);
		} else {
			itemType = String.format("//%s (%s), //", item.itemType, item.typeComment); 
		}
		
		String rarity = String.format("//%s //", item.rarity);
		
		String attunement = "";
		if (item.attunementComment.isBlank() && item.attunement) { //attunement required but no comment
			attunement = "//(requires attunement)//";
		} if (!item.attunementComment.isBlank() && item.attunement) { //attunement required with a comment
			attunement = String.format("//(requires attunement by %s)//", item.attunementComment);
		}
		
		String whiteLine = " \\\\\n\\\\\n";
		String description = item.description;
		
		String formattedItem = header +
				source +
				itemType +
				rarity +
				attunement +
				whiteLine +
				description;
		
		return formattedItem;
	}
}
