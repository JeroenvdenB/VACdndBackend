package com.vacdnd.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
		
	public FTPUploader(){
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
		}
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
