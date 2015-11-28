package csci4311.chat;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nazar on 11/10/15.
 */
public class PacketReader extends Thread {

	public Socket socket;
	private boolean isRunning;
	private Boolean isSenderDeclared;
	List<Socket> sockets;
	int length;
	// public static List<String> loggedInUserList;
	// private static String packetStr;

	public PacketReader(List<Socket> sockets) {

		this.sockets = sockets;
		// if (loggedInUserList == null) {
		// loggedInUserList = new LinkedList<>();
		// }
	}

	@Override
	public void run() {

		InputStreamReader isr;
		BufferedReader br;
		isRunning = true;
		String packetStr;
		String sender = null;

		if (!sockets.contains(socket)) {
			sockets.add(socket);
		}

		try {

			InputStream is = socket.getInputStream();

			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			while (!socket.isClosed() && isRunning) {

				try {

					packetStr = br.readLine();
					// final String sender = null;

					if (packetStr != null) {

						new UserSession(packetStr);
						System.out.println(UserSession.getIntValue());
						// List<String> user =
						// currentLoginUser();
						// for (String s : user) {
						// System.out.println("User :
						// "+UserSession.getLoggedInUserList());
						// }

//						 System.out.println(packetStr + " from : " + socket);

						if (packetStr != null) {

							String[] firstUserMessage = packetStr.split("=");
							String finalSender;
							if (firstUserMessage.length == 2) {
								sender = firstUserMessage[1];
								isSenderDeclared = true;
//								System.out.println("sender=" + sender);
							}
							if (isSenderDeclared) {
								finalSender = sender;
								String[] clientRequest = packetStr.split(" ");
								String baseCommand = clientRequest[0];
								response(baseCommand, clientRequest, packetStr, finalSender);
							}
						}
					}

				} catch (Exception ex) {
					// ex.printStackTrace();
				}

			}

		} catch (Exception ex) {
			// ex.printStackTrace();
		}
	}

	// public String currentLoginUser() {
	//
	// String[] firstUserMessage = packetStr.split("=");
	//
	// if (firstUserMessage.length == 2) {
	// System.out.println("firstUserMessage[1]="+firstUserMessage[1]);
	// return firstUserMessage[1];
	// }
	// else{
	// return null;
	// }
	// }

	public void response(String baseCommand, String[] packet, String packetString, String sender) {

		String group = null;
		BufferedReader br;
		File file = null;
		FileWriter fWriter;
		OutputStream ostream;
		PrintWriter pwrite;

		try {

			switch (baseCommand) {

			case "join":

				if (packet[1] != null) {

					group = packet[1];
					file = new File(group + ".txt");
					int counter = 0;
					if (file.exists()) {

						try {

							br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getName())));

							if (br.readLine() != null) {

								// System.out.println(ReplyCode.OK.getReplyType());
								br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getName())));
								while ((br.readLine()) != null) {
									counter++;
								}

								br.close();
							} else {
								// System.out.println(ReplyCode.NORESULT.getReplyType());
								// //User is not a member of the group
							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}

						if (packet[2] != null) {

							String user = packet[2];
							boolean isUserPresent = false;
							Scanner scanner = null;

							try {
								scanner = new Scanner(file);
							} catch (FileNotFoundException e) {
								e.printStackTrace(); // To change body of catch
														// statement use File |
														// Settings | File
														// Templates.
							}
							while (scanner.hasNextLine()) {
								/**
								 * User Already Member
								 */
								if (user.equals(scanner.nextLine().trim())) {
									// same user found
									// System.out.println(ReplyCode.NORESULT.getReplyType());
									isUserPresent = true;
									break;
								} else {

								}

							}

							if (isUserPresent == false) {

								// System.out.println(ReplyCode.OK.getReplyType());
								fWriter = null;
								try {
									// Writing into file
									fWriter = new FileWriter(file.getName(), true);
									BufferedWriter out = new BufferedWriter(fWriter);
									scanner = new Scanner(file);
									if (scanner.hasNext()) {
										out.write("\n");
									}
									out.write(user);
									out.flush();
									out.close();

									// Sending back confirmation message to
									// client over socket
									ostream = socket.getOutputStream();
									pwrite = new PrintWriter(ostream, true);
									pwrite.println("joined #" + group + " with " + counter + " current members");
									pwrite.flush();

								} catch (IOException e) {
									// e.printStackTrace();
								}

							}

						}
					}
				}
				break;

			case "leave":

				group = packet[1];
				String user = packet[2];
				new UserCommand().leaveGroup(user, group, socket, true);

				break;

			case "groups":

				length = packet.length;
				String groupname;
				BufferedReader individualGroupbr;

				try {

					String groupValue;

					br = new BufferedReader(new FileReader("Group.txt"));

					while ((groupValue = br.readLine()) != null) {

						groupname = groupValue + ".txt";
						individualGroupbr = new BufferedReader(new FileReader(groupname));

						int counter = 0;
						while (individualGroupbr.readLine() != null) {
							counter++;
						}

						ostream = socket.getOutputStream();
						pwrite = new PrintWriter(ostream, true);
						pwrite.println("#" + groupValue + " has " + counter + " members");
						pwrite.flush();
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

				break;

			case "users":

				group = packet[1];
				new UserCommand().usersCommand(group, socket, true);

				break;

			case "history":

				group = packet[1];
				file = new File(group + "_history.txt");
				// System.out.println("History file = "+file.getName());
				new UserCommand().showFileContents(file, true, socket);

				break;

			case "send":

				String groupRegEx = "#\\s*(\\w+)"; //Here two group. Each () is a group

				Pattern p = Pattern.compile(groupRegEx);
				Matcher m = p.matcher(packetString);
				String users;
				List<String> userList = null;
				String originalMessage = null;
				String[] splitedMessage = new String[0];
				boolean isGroupMessage = false;

				while (m.find()) {

					group = m.group(1);
					file = new File(group + ".txt");

					if (file.exists()) {

						isGroupMessage = true;
						br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getName())));
						userList = new LinkedList<>();

						if (br.readLine() != null) {

							br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getName())));
							while ((users = br.readLine()) != null) {

								// System.out.println(users); //Displaying users
								// name in server prompt
								userList.add(users);

							}
							br.close();
						} else {
							System.out.println(ReplyCode.NORESULT.getReplyType()); // User
																					// is
																					// not
																					// a
																					// member
																					// of
																					// the
																					// group
						}

						String messageWithoutGroupName = packetString.replaceAll("(#).*?\\S*", "");
						splitedMessage = messageWithoutGroupName.split(" ", 2);
						originalMessage = splitedMessage[1];

						for (String s : userList) {

							originalMessage = "@" + s + " " + originalMessage;
						}

					}

				}

				if (isGroupMessage) {

					for (Socket socket : sockets) {

						new UserCommand().displayInClientWindow(splitedMessage[0] + " " + originalMessage, socket);
					}

					int lastWordPosition = splitedMessage[1].lastIndexOf(" ");
					String mainMessage = splitedMessage[1].substring(0, lastWordPosition).trim();
					String senderName = splitedMessage[1].substring((lastWordPosition)).trim();
					// System.out.println(packetString + " from " + senderName);
					String historyMessage = "[" + senderName + "] " + mainMessage;
					String groupMessage = "from: " + sender + "  to: #" + group + "  " + mainMessage + "  ";
					File groupFile = new File(group + "_groupMessage.txt");

					saveHistory(historyMessage, group);
					saveHistoryWithSenderReceiverName(groupMessage, groupFile);

				} else if (!isGroupMessage) {
					
					archivePersonalMessage(packetString,sender);
					for (Socket socket : sockets) {
						new UserCommand().displayInClientWindow(packetString, socket);
					}
				}

				break;

			default:

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	public void archivePersonalMessage(String packetString,String sender){
		
		System.out.println("packetString="+packetString);
		String receiverRegEx = "@\\s*(\\w+)";
		String receiverName = null;
		String messageToSave;
		String[] splitedMessage = null ;
		List<String> receiverList = new LinkedList<>();
//		File userFile = null ;
		
		Pattern p = Pattern.compile(receiverRegEx);
		Matcher m = p.matcher(packetString);
		
		/**
		 * Extracting userName from message.
		 * Populating List.
		 */
		while(m.find()){
			
			receiverName = m.group(1);
			receiverList.add(receiverName);
			String messageWithoutUserName = packetString.replaceAll("(@).*?\\S*", "");
			splitedMessage = messageWithoutUserName.split(" ", 2);
			String originalMessage = splitedMessage[1];

		}
		
		int lastWordPosition = splitedMessage[1].lastIndexOf(" ");
		String mainMessage = splitedMessage[1].substring(0, lastWordPosition).trim();
		String senderName = splitedMessage[1].substring((lastWordPosition)).trim();
		messageToSave = "from: " + sender + "  to: @" + receiverName + "  " + mainMessage + "  ";


		/**
		 * Creating and Saving contents for each user
		 */
		for(String receiver : receiverList){
			
			File userFile = new File(receiver + "_personalMessage.txt");
			if(!userFile.exists()){
				
				userFile = new File(receiver + "_personalMessage.txt");;
				saveHistoryWithSenderReceiverName(messageToSave,userFile);

			}
			else{
				saveHistoryWithSenderReceiverName(messageToSave,userFile);
			}
		}
	}

	public void saveHistory(String message, String groupName) {

		try {
			// Writing into file
			File file = new File(groupName + "_history.txt");
			FileWriter fWriter = new FileWriter(file.getName(), true);
			BufferedWriter out = new BufferedWriter(fWriter);
			Scanner scanner = new Scanner(file);
			if (scanner.hasNext()) {
				out.write("\n");
			}
			out.write(message);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveHistoryWithSenderReceiverName(String groupMessage, File file) {

		try {
//			System.out.println("groupMessage=" + groupMessage);
			// Writing into file
			FileWriter fWriter = new FileWriter(file.getName(), true);
			BufferedWriter out = new BufferedWriter(fWriter);
			Scanner scanner = new Scanner(file);
			if (scanner.hasNext()) {
				out.write("\n");
			}
			out.write(groupMessage);
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
