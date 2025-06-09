package com.vasworks.cofo.struts.agent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vasworks.cofo.struts.AgentAction;

public class FileDownloadAction extends AgentAction {
	public static final Log LOG = LogFactory.getLog(FileDownloadAction.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 4138071251332140102L;	
	
	private String contentDisposition;
	
	private InputStream inputStream;
	
	private String contentType;
	
	private String filePath;

	@Override
	public String execute() {
		LOG.debug("execute()");
		
		File file = new File(filePath);
		
		StringBuilder b = new StringBuilder("inline;filename=");		
		b.append("\"");
		b.append(file.getName());
		b.append("\"");
		
		contentDisposition = b.toString();
		
		byte[] rawFileData = getFileFromPath();
		
		if(rawFileData != null) {
			inputStream = new ByteArrayInputStream(rawFileData);
		}
		
		return SUCCESS;
	}

	public byte[] getFileFromPath() {
		LOG.debug("getFileFromPath()");
		byte[] fileData = null;
		try {
			if(filePath != null) {
				File file = new File(filePath);
				inputStream = new FileInputStream(file);
				
				contentType = "application/vnd.ms-excel";
				
				fileData = new byte[(int) file.length()];
				
				inputStream.read(fileData);
				inputStream.close();
			}
			
			if(fileData != null) {
				LOG.debug("fileData.length: " + fileData.length);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileData;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}
	
	public String getContentType() {
		return contentType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
