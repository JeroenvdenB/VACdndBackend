package com.vacdnd.tools.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
	
	private String server;
	private int port;
	private String user;
	private String pass;
	
	public FTPDownloader(String server, String port, String user, String pass) {
		this.server = server;
		this.port = Integer.parseInt(port);
		this.user = user;
		this.pass = pass;
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
				System.out.println("Something went wrong in the download method of FTPDownloader.");
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
