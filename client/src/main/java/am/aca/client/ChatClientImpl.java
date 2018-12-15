package am.aca.client;

import am.aca.common.ChatClient;
import am.aca.common.ChatServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Scanner;

public class ChatClientImpl extends UnicastRemoteObject implements ChatClient, Runnable {

    private ChatServer server;
    private String clientName;


    private static final String QUIT = "QUIT";
    private static final String LIST = "LIST";

    protected ChatClientImpl(ChatServer server, String clientName) throws RemoteException {
        this.clientName = clientName;
        this.server = server;
        this.server.connect(clientName, this);

    }


    @Override
    public synchronized void update(String name, String message) throws RemoteException {
        if (!this.clientName.equals(name)) {
            System.out.println(name + ": " + message);
        }
    }

    @Override
    public String getName() throws RemoteException {
        return this.clientName;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String message;

        while (true) {
            message = scanner.nextLine().trim();

            switch (message) {
                case QUIT:
                    try {
                        server.disconnect(this);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    scanner.close();
                    System.exit(1);
                    break;
                case LIST:
                    try {
                        server.list(this);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    if (!message.equals("")) {
                        try {
                            server.broadcast(clientName, message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }
}
