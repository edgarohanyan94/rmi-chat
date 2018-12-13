package am.aca.server;


import am.aca.common.ChatServer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    private static final int PORT = 1099;
    private static final String BINDING_NAME = "RMI_chat";

    public static void main(String[] args) {
        try {
            ChatServer chatServer = new ChatServerImpl();

            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.rebind(BINDING_NAME, chatServer);
            System.out.println("Server has started");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
