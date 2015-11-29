package com.server.api.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import csci4311.chat.PacketReader;
import csci4311.chat.UserCommand;
import csci4311.chat.UserSession;

public class ApiService {

	JSONObject json = new JSONObject();
	JSONArray jsonArray = new JSONArray();
	File file;

	public ApiService() {

	}

	@SuppressWarnings("unchecked")
	public String getLoggedInUsers(){
		
//		List<String> currentUsers = PacketReader.currentLoginUser();
//		List<String> currentUsers = PacketReader.loggedInUserList;


		json.put("users",jsonArray);
		return json.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public String getAllGroup() {

		try {
			file = new File("Group.txt");
			json.put("Group", returnJsonArray(file));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return json.toJSONString();
	}

	@SuppressWarnings("unchecked")
	public String getUserByGroup(long groupId) {

		file = new File(groupId + ".txt");
		if (file.exists()) {
			json.put("users", returnJsonArray(file));
		} else {
			// System.out.println("Else "+file.getName());
		}
		return json.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public String getGroupMessage(long groupId) {

		file = new File(groupId + "_groupMessage.txt");
		if (file.exists()) {
			json.put("messages", returnJsonArray(file));
		} else {
//			 System.out.println("Else "+file.getName());
		}
		return json.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public String getUserMessage(String user) {

		File file = new File(user + "_personalMessage.txt");
		if (file.exists()) {
			json.put("messages", returnJsonArray(file));
		} else {
//			 System.out.println("Else "+file.getName());
		}
		return json.toJSONString();
	}
	
	public String createUser(String user,long groupId){
		
    	String fileName = groupId+".txt";
    	File file = new File(fileName);
    	UserCommand.joinUser(file, user,String.valueOf(groupId));
    	return getUserByGroup(groupId);
	}

	@SuppressWarnings("unchecked")
	public String removeUser(long groupId, String userName) {

		File file = new File(groupId + ".txt");
		if (file.exists()) {
		
			UserCommand.leaveGroup(userName, String.valueOf(groupId), null, false);
			try {
				Thread.sleep(2000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			json.put("users", returnJsonArray(file));
		}
		return json.toJSONString();
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public JSONArray returnJsonArray(File file) {

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getName())));
			String line = null;
			// Checking null group
			if (br.readLine() != null) {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getName())));
				while ((line = br.readLine()) != null) {
					jsonArray.add(line);
				}
				br.close();
			}
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return jsonArray;
	}

}
