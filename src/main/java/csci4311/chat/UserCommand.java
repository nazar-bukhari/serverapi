package csci4311.chat;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by nazar on 11/8/15.
 */
public class UserCommand extends Thread {

    private final String firstKey = "msgp";


    public UserCommand() {

    }

    @Override
    public void run() {

        while (true) {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

            try {

                String userInput = bufferRead.readLine();
                if (userInput != null) {
                    String[] inputSet = userInput.split(" ");

                    if (inputSet[0].equals(firstKey)) {
                        verifySecondKey(inputSet, inputSet[1]);
                    } else {
                        System.out.println(ReplyCode.NORESULT.getReplyType());
                    }
                }

            } catch (IOException e) {
//            e.printStackTrace();
            }
        }
    }

    public void verifySecondKey(String[] inputSet, String secondKey) {

        String user;
        String group;
        String fileName;
        File file;
//        boolean isUserPresent = false;
        int length;

        switch (secondKey) {

            case "join":

                length = inputSet.length;
                if (length == 4) {
//                System.out.println("join command");
                    user = inputSet[2];
                    group = inputSet[3];
                    file = new File(group + ".txt");
                    fileName = file.getName();
                    joinUser(file,user,group); 

                }

                break;

            case "leave":

                length = inputSet.length;
                if (length == 4) {

                    user = inputSet[2];
                    group = inputSet[3];
                    leaveGroup(user, group, null, false);
                }

                break;

            case "groups":

                File groupFile = new File("Group.txt");
                showFileContents(groupFile, false,null);

                break;

            case "users":

                length = inputSet.length;
                if (length == 3) {

                    group = inputSet[2];
                    usersCommand(group, null, false);
                }

                break;

            case "send":

                break;
            case "history":
                break;
            default:
//                System.out.println("Comamnd not found");

        }

    }

    public void showFileContents(File file, boolean clientCommand,Socket socket) {

        if (file.exists()) {

            try {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getName())));
                String line = null;
                //Checking null group
                if (br.readLine() != null) {

                    if (!clientCommand) {
                        System.out.println(ReplyCode.OK.getReplyType());
                    }

                    br = new BufferedReader(new InputStreamReader(new FileInputStream(file.getName())));
                    while ((line = br.readLine()) != null) {
                        if (clientCommand) {
                                displayInClientWindow(line,socket);
                        }  else{
                        System.out.println(line);
                        }
                    }
                    br.close();
                } else {
                    if (!clientCommand) {
                        System.out.println(ReplyCode.NORESULT.getReplyType()); //User is not a member of the group
                    }
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
        } else {
//                    System.out.println(ReplyCode.NORESULT.getReplyType()); //No such group
        }
    }

    public void usersCommand(String group, Socket socket, boolean clientCommand) {

        File file = new File(group + ".txt");
        String fileName = file.getName();

        //Group exists
        if (file.exists()) {

            try {

                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                String line = null;
                //Checking null group
                if (br.readLine() != null) {
                    System.out.println(ReplyCode.OK.getReplyType());
//                        System.out.println("br "+br.readLine());
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                    while ((line = br.readLine()) != null) {
                        if (clientCommand) {
                            displayInClientWindow("@" + line, socket);
                        } else {
                            System.out.println(line);      //Displaying users name in server prompt
                        }
                    }
                    br.close();
                } else {
                    System.out.println(ReplyCode.NORESULT.getReplyType()); //User is not a member of the group
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
        } else {

            if (clientCommand) {

                displayInClientWindow(ReplyCode.ERROR.getReplyType(), socket);
            } else {
                System.out.println(ReplyCode.NORESULT.getReplyType()); //No such group
            }
        }

    }

    public void displayInClientWindow(String message, Socket socket) {

        try {

            OutputStream ostream = socket.getOutputStream();
            PrintWriter pwrite = new PrintWriter(ostream, true);
            pwrite.println(message);
            pwrite.flush();
        } catch (Exception ex) {

        }
    }

    public static void joinUser(File file,String user,String group){
    	
    	String fileName = file.getName();
    	boolean isUserPresent = false;
        /**
         * Group Exists
         */
        if (file.exists()) {

            Scanner scanner = null;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            while (scanner.hasNextLine()) {
                /**
                 * User Already Member
                 */
                if (user.equals(scanner.nextLine().trim())) {
                    // same user found
                    System.out.println(ReplyCode.NORESULT.getReplyType());
                    isUserPresent = true;
                    break;
                } else {

                }

            }

            if (isUserPresent == false) {

                System.out.println(ReplyCode.OK.getReplyType());
                FileWriter ostream = null;
                try {
                    ostream = new FileWriter(fileName, true);
                    BufferedWriter out = new BufferedWriter(ostream);
                    scanner = new Scanner(file);
                    if (scanner.hasNext()) {
                        out.write("\n");
                    }
                    out.write(user);
                    out.flush();
                    out.close();

                } catch (IOException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

            }

        }
        /**
         * Group does not exist
         */
        else {
            System.out.println(ReplyCode.OK.getReplyType());
            try {
                /**
                 * Writing users into individual group file
                 */
                FileWriter writer = new FileWriter(fileName);
                writer.write(user);
                writer.close();

                /**
                 * Writing into Group file
                 */
                FileWriter groupWriter = new FileWriter("Group.txt", true);
                Scanner scanner = new Scanner(new File("Group.txt"));
                if (scanner.hasNext()) {
                    groupWriter.write("\n");
                }
                groupWriter.write(group);
                groupWriter.flush();
                groupWriter.close();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }
    
    public static void leaveGroup(String user, String group, Socket socket, boolean clientCommand) {

        File file = new File(group + ".txt");
        String fileName = file.getName();
        boolean isMember = true;

        //Group exists
        if (file.exists()) {

            Scanner scanner = null;
            BufferedReader br = null;
            FileWriter writer = null;
            List<String> userList = new LinkedList();
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
//                e.printStackTrace();  
            }
            while (scanner.hasNextLine()) {

                if (user.equals(scanner.nextLine().trim())) {
                    // User is a member of the group

                    //Delete the user
                    try {
                        br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));

                        for (String line; (line = br.readLine()) != null; ) {
                            if (!line.equals(user)) {
                                userList.add(line);
                            }
                        }
                        new PrintWriter(fileName).close();
                        writer = new FileWriter(fileName);

                        for (String s : userList) {
                            writer.write(s);
                            writer.write("\n");
                        }
                    } catch (Exception ex) {
//                        ex.printStackTrace();
                    } finally {
                        try {
                            br.close();
                            writer.close();
                        } catch (Exception ex) {
//                            ex.printStackTrace();
                        }
                    }
                    if(!clientCommand){
                    	System.out.println(ReplyCode.OK.getReplyType());
                    }
                    isMember = true;
                    break;
                } else {
                    isMember = false; //user is not memeber(201)
                }

            }

            if (isMember == false) {

                if (clientCommand) {
                    try {
                        OutputStream ostream = socket.getOutputStream();
                        PrintWriter pwrite = new PrintWriter(ostream, true);
                        pwrite.println("Not a member of #" + group);
                        pwrite.flush();
                    } catch (Exception ex) {

                    }
                } else {
                    System.out.println(ReplyCode.NORESULT.getReplyType()); //User is not a member of the group(201)
                }

            }
        }
        //Group not exists
        else {
            System.out.println(ReplyCode.ERROR.getReplyType());
        }
    }
}
