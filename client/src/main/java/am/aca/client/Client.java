package am.aca.client;

import am.aca.common.ChatServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    private static final int PORT = 1099;
    private static final String BINDING_NAME = "RMI_chat";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Please enter sharing ip address or skip: 127.0.0.1: ");


        String ipAddress = scanner.nextLine().trim();
        String rmiUrl = "";

        if (ipAddress.equals("")) {
            ipAddress = "localhost";
            rmiUrl = "rmi://" + ipAddress + ":" + PORT + "/" + BINDING_NAME;
        } else {
            rmiUrl = "rmi://" + ipAddress + ":" + PORT + "/" + BINDING_NAME;
        }

        try {
            ChatServer chatServer = (ChatServer) Naming.lookup(rmiUrl);

            System.out.println("(S) -> Sing in");
            System.out.println("(C) -> Create account");
            System.out.println("(D) -> Delete account");
            System.out.println("(Q) -> Quit");


            String command = scanner.nextLine().toUpperCase().trim();

            switch (command) {
                case "S":
                    while (true) {
                        System.out.print("Your username: ");
                        String username = scanner.nextLine().trim();
                        System.out.println();
                        System.out.print("Your password: ");
                        String password = scanner.nextLine().trim();
                        if (singIn(chatServer, username, password)) {
                            break;
                        }
                    }
                    break;
                case "C":
                    String name;
                    String pass;
                    while (true) {
                        System.out.print("Your new username: ");
                        String newUsername = scanner.nextLine().trim();
                        System.out.println();
                        System.out.print("Your new password: ");
                        String newPassword = scanner.nextLine().trim();
                        System.out.println();
                        name = newUsername;
                        pass = newPassword;
                        if (registre(chatServer, newUsername, newPassword)) {
                            break;
                        }
                    }
                    singIn(chatServer, name, pass);
                    break;
                case "D":
                    System.out.print("Your username: ");
                    String username = scanner.nextLine().trim();
                    System.out.println();
                    System.out.print("Your password: ");
                    String password = scanner.nextLine().trim();
                    System.out.println();

                    if (hasDelete(chatServer, username, password)) {
                        System.err.println("Account successfully deleted");
                    } else {
                        System.err.println("incorect username or password,please try again");
                    }
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("invalid command");
                    break;
            }
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static boolean singIn(ChatServer chatServer, String username, String password) throws RemoteException {
        if (chatServer.checkUsername(username) && chatServer.checkPassword(username, password)) {

            ChatClientImpl client = new ChatClientImpl(chatServer, username);
            System.err.println("write \"LIST\" to see active users");
            System.err.println("write \"QUIT\" to exit from chat");
            new Thread(client).start();
            return true;

        } else {
            System.err.println("incorect username or password,please try again");
            return false;
        }
    }

    private static boolean registre(ChatServer chatServer, String newUsername, String password) throws RemoteException {

        if (chatServer.checkUsername(newUsername)) {
            System.err.println("username exists,please try again");
            return false;
        } else {
            chatServer.registred(newUsername, password);
            System.out.println("registration completed successfully");
            return true;
        }
    }

    private static boolean hasDelete(ChatServer chatServer, String username, String password) throws RemoteException {
        if (chatServer.hasDelete(username, password)) {
            return true;
        }
        return false;
    }
}

