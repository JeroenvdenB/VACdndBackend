package com.vacdnd.tools;

import com.vacdnd.tools.util.FTPUploader;
import com.vacdnd.tools.util.SpellListInitializer;
import com.vacdnd.tools.util.FTPDownloader;
import com.vacdnd.tools.domain.Spell;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);		
		
	
		// Testing file upload
//		File inputFile = new File("D:/testfile.txt");
//		String remoteFileName = "testfile.txt";
//		String remoteDestination = "wiki/data/pages";
//		FTPUploader fu = new FTPUploader();
//		fu.upload(inputFile, remoteDestination, remoteFileName);
		
		//Testing file download
//		String destinationFile = "D:/spells/artificer.txt";
//		File downloadFile = new File(destinationFile);
//		String remoteOrigin = "/public_html/wiki/data/pages/spells/artificer.txt";
//		FTPDownloader fd = new FTPDownloader();
//		fd.download(remoteOrigin, downloadFile);
		
		//Testing spell list update
//		Spell testSpell = new Spell();
//		String[] dndClasses = {"Artificer", "Bard"};
//		testSpell.components = "V, S, M (random stuff)";
//		testSpell.name = "Alia Bbb";
//		try {
//			testSpell.SpellListUpdate(dndClasses, 0);
//		} catch (IOException e){
//			e.printStackTrace();
//		}
//		
//		SpellListInitializer ini = new SpellListInitializer();
//		ini.helpme("All", 9);
	}
	
	
}