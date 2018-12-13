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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Your name: ");
        String name = scanner.nextLine().trim();

        try {
            ChatServer chatServer = (ChatServer) Naming.lookup(BINDING_NAME);
            new Thread(new ChatClientImpl(chatServer, name)).start();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
