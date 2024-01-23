package com.vacdnd.tools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
	
	public FTPUploader(String server, String port, String user, String pass) {
		this.server = server;
		this.port = Integer.parseInt(port);
		this.user = user;
		this.pass = pass;
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
			System.out.println("Error: in FTPUploader -> upload method" + e.getMessage());
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
