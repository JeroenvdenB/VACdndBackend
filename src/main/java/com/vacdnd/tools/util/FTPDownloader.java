package com.vacdnd.tools.util;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.io.*;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * A program to download files from the remote FTP server to the
 * local system using Apache Commons Net API. Based on a code 
 * example from www.codejava.net.
 * 
 * The FTPDownloader class has his own account on the ftp server side.
 * Therefore, every class instance is constructed with credentials from
 * application.properties. Using the constructor allows for 
 * overloading, should the method be used with other credentials.
 * 
 * For the love of God, remember that the remote origin starts with "/". Thank you.
 * 
 * @author jvand
 */

public class FTPDownloader {
	
	private String server = "ftp.vacdnd.com";
	private int port = 21;
	private String user = "u611812703.VACdndRoot";
	private String pass = "VACdndRoot1";
		
	public FTPDownloader(){
		/*
		Properties properties = new Properties();
		try {
			InputStream propertiesFile = new FileInputStream("src/main/resources/application.properties");
			properties.load(propertiesFile);
			this.server = properties.getProperty("ftp.server");
			this.port = Integer.parseInt(properties.getProperty("ftp.port"));
			this.user = properties.getProperty("ftp.user");
			this.pass = properties.getProperty("ftp.pass");
			propertiesFile.close();
		} catch (IOException e){
			e.printStackTrace();
		}*/
	}
	
	public void download(String remoteOrigin, File destinationFile) {
		
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(server, port);
			ftpClient.login(user,  pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(destinationFile));
						
			boolean done = ftpClient.retrieveFile(remoteOrigin, outputStream);
			outputStream.close();
			
			if (done) {
				System.out.println("File was downloaded successfully");
			} else {
				System.out.println("Something went wrong in the retrieveFile method.");
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
