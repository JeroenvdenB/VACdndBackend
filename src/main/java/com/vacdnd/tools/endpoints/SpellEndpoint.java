package com.vacdnd.tools.endpoints;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vacdnd.tools.domain.Spell;
import com.vacdnd.tools.util.SpellFormatter;

@RestController
public class SpellEndpoint {
	
	// Set ftp credentials here with @Value, pass to SpellUploader method.
	// @Value can't be used inside of the Spell object.
	// Would be nice if I could autowire everything together properly but alas, I don't know how
	@Value("${ftp.user}")
	public String ftpUser;
	
	@Value("${ftp.pass}")
	public String ftpPass;
	
	@Value("${ftp.server}")
	public String ftpServer;
	
	@Value("${ftp.port}")
	public String ftpPort;
		
	@PostMapping("addSpell")
	public void addSpell(@RequestBody Spell spell) {
		String formattedSpell = SpellFormatter.formatSpell(spell);
		spell.SpellUploader(formattedSpell, ftpServer, ftpPort, ftpUser, ftpPass);
	}
	
	@PostMapping("pastedSpell")
	public void pastedSpell(@RequestBody String receivedSpell) {
		Spell pastedSpell = SpellFormatter.parsePastedText(receivedSpell);		
		String formattedSpell = SpellFormatter.formatSpell(pastedSpell);
		pastedSpell.SpellUploader(formattedSpell, ftpServer, ftpPort, ftpUser, ftpPass);
	}

}
