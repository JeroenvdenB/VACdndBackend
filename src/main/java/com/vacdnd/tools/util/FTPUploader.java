package com.vacdnd.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.Spring;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * A program to upload files from the local system to a remote 
 * FTP server using Apache Commons Net API. Based on a code 
 * example from www.codejava.net.
 * 
 * The FPTUploader class has his own account on the ftp server side.
 * Therefore, every class instance is constructed with credentials from
 * application.properties. Using the constructor allows for 
 * overloading, should the method be used with other credentials.
 * 
 * IMPORTANT: dokuwiki demands all lowercase file names!!
 * 
 * @author jvand
 */

public class FTPUploader {
		
	private String server;
	private int port;
	private String user;
	private String pass;
	private String profile;
		
	public FTPUploader(){
		// Retrieve the path, for file structure may differ in deployments
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String appProfilePath = rootPath + "application.properties";
		
		Properties properties = new Properties();
		
		// Retrieve the active profile from application.properties
		try {
			InputStream profileFile = new FileInputStream(appProfilePath);
			properties.load(profileFile);
			this.profile = properties.getProperty("spring.profiles.active");
			profileFile.close();
		} catch (IOException e) {
			System.out.println("An error occured in opening profileFile in FTPUploader");
			e.printStackTrace();
		}
		
		// Construct the path for the active properties file
		//String appPropertiesPath = String.format("application-%.properties", profile); //Unknown error in format method due to period?	
		String appPropertiesPath = rootPath + "application-" + profile + ".properties";
		System.out.println(appPropertiesPath);
		
		try {
			InputStream propertiesFile = new FileInputStream(appPropertiesPath);
			properties.load(propertiesFile);
			this.server = properties.getProperty("ftp.server");
			this.port = Integer.parseInt(properties.getProperty("ftp.port"));
			this.user = properties.getProperty("ftp.user");
			this.pass = properties.getProperty("ftp.pass");
			propertiesFile.close();
		} catch (IOException e){
			System.out.println("An error occured in opening propertiesFile in FTPUploader");
			e.printStackTrace();
		} 
		
//		System.out.println("New FTPUploader object created\n"
//				+ "Properties set as:"
//				+ "\nserver: " + this.server
//				+ "\nuser: " + this.user
//				+ "\npass: " + this.pass);
		
	}
	
	public void upload(File inputFile, String remoteDestination, String remoteFileName) {
		
		FTPClient ftpClient = new FTPClient();
		
		try {
			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			InputStream inputStream = new FileInputStream(inputFile);
			
			ftpClient.changeWorkingDirectory(remoteDestination);
			boolean done = ftpClient.storeFile(remoteFileName, inputStream);
			inputStream.close();
			if (done) {
				System.out.println("File uploaded successfully");
			} else {
				System.out.println("Problem in uploading the file.");
			}
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
