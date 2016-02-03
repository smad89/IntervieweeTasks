package com.interset.DataIntegrationTask;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerData extends HashMap<String, ArrayList<CustomerData>> {

	//initialize your JSON parsed data
	private Long eventID;
	private String user, ipAddr, file, activity, timestamp, timeOffset;
	private String fileName;
	
	
	public Long getEventID() {
		return eventID;
	}
	public void setEventID(Long eventID) {
		
		this.eventID = eventID;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getTimeOffset() {
		return timeOffset;
	}
	public void setTimeOffset(String timeOffset) {
		this.timeOffset = timeOffset;
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
}
