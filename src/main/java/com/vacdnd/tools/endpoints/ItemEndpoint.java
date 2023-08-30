package com.vacdnd.tools.endpoints;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vacdnd.tools.domain.Item;
import com.vacdnd.tools.util.ItemFormatter;

@RestController
public class ItemEndpoint {
	
	@PostMapping("addItem")
	public void addItem(@RequestBody Item item) {
		String formattedItem = ItemFormatter.formatItem(item);
		item.ItemUploader(formattedItem);
	}
}
