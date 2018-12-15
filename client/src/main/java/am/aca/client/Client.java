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


//        System.err.println("write \"LIST\" to see active users");
//        System.err.println("write \"QUIT\" to exit from chat");

        try {
            ChatServer chatServer = (ChatServer) Naming.lookup(BINDING_NAME);

            System.out.println("(S) -> Sing in");
            System.out.println("(R) -> Registration");
            System.out.println("(Q) -> Quit");


            String command = scanner.nextLine().toUpperCase().trim();

            switch (command) {
                case "S":
                    System.out.print("Your username: ");
                    String username = scanner.nextLine().trim();
                    singIn(chatServer, username);
                    break;
                case "R":
                    String name;
                    while (true) {
                        System.out.print("Your new username: ");
                        String newUsername = scanner.nextLine().trim();
                        name = newUsername;
                        if (registre(chatServer, newUsername)) {
                            break;
                        }
                    }
                    singIn(chatServer,name);
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

    private static void singIn(ChatServer chatServer, String username) throws RemoteException {
        if (chatServer.checkUsername(username)) {
            ChatClientImpl client = new ChatClientImpl(chatServer, username);
            System.err.println("write \"LIST\" to see active users");
            System.err.println("write \"QUIT\" to exit from chat");
            new Thread(client).start();

        } else {
            System.err.println("please,register");
        }
    }

    private static boolean registre(ChatServer chatServer, String newUsername) throws RemoteException {

        if (chatServer.checkUsername(newUsername)) {
            System.err.println("username exists,please try again");
            return false;
        } else {
            chatServer.registred(newUsername);
            System.out.println("registration completed successfully");
            return true;
        }

    }
}

