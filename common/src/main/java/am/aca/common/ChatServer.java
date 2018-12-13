package am.aca.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    void connect(String username, ChatClient c) throws RemoteException;

    void disconnect(ChatClient c) throws RemoteException;

    void broadcast(String clientName, String message) throws RemoteException;

    void list(ChatClient client) throws RemoteException;
}
