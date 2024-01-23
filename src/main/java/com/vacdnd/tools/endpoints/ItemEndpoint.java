package com.vacdnd.tools.endpoints;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vacdnd.tools.domain.Item;
import com.vacdnd.tools.util.ItemFormatter;

@RestController
public class ItemEndpoint {
	@Value("${ftp.user}")
	public String ftpUser;
	
	@Value("${ftp.pass}")
	public String ftpPass;
	
	@Value("${ftp.server}")
	public String ftpServer;
	
	@Value("${ftp.port}")
	public String ftpPort;
	
	@PostMapping("addItem")
	public void addItem(@RequestBody Item item) {
		String formattedItem = ItemFormatter.formatItem(item);
		item.ItemUploader(formattedItem, ftpServer, ftpPort, ftpUser, ftpPass);
	}
}
