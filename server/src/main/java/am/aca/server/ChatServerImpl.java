package am.aca.server;

import am.aca.common.ChatClient;
import am.aca.common.ChatServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private List<ChatClient> clients;
    private List<String> clientsName;

    protected ChatServerImpl() throws RemoteException {
        this.clients = new ArrayList<>();
        this.clientsName = new ArrayList<>();
    }

    @Override
    public void connect(String username, ChatClient c) throws RemoteException {
        for (ChatClient client : clients) {
            client.update("server", username + " is joining now...");
        }

        clientsName.add(username);
        clients.add(c);

        int count = clients.size();

        StringBuffer msg = new StringBuffer("Welcome")
                .append(username)
                .append(", ");
        msg.append("There ")
                .append((count == 1) ? "is " : "are ")
                .append(count)
                .append((count == 1) ? " user: " : " users: ");
        msg.append(clientsName.toString());
        c.update("server", msg.toString());
    }

    @Override
    public synchronized void disconnect(ChatClient client) throws RemoteException {
        clients.remove(client);
        clientsName.remove(client.getName());

        for (ChatClient c : clients) {
            client.update("server", client.getName() + " has left");
        }


    }
}
