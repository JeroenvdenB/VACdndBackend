package com.vacdnd.tools.endpoints;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import com.vacdnd.tools.domain.Spell;
import com.vacdnd.tools.util.SpellFormatter;
import com.vacdnd.tools.util.FTPUploader;
import com.vacdnd.tools.util.FTPDownloader;

@RestController
public class SpellEndpoint {
	
	@PostMapping("addSpell")
	public void addSpell(@RequestBody Spell spell) {
		
		//TODO send formatted spell to spell-uploader class.
		//	This class should:
		//  * make a .txt out of the formatted spell
		//  * use FTPUploader to upload the .txt
		//  * delete the .txt again
		//  * use FTPDownloader to fetch any relevant spell list
		//  * update said spell list
		//  * use FTPUploader to re-upload the updated spell list
		//  * remove any lingering files from the list-update
		
		String formattedSpell = SpellFormatter.formatSpell(spell);
		spell.SpellUploader(formattedSpell);
		
	}
	
}
