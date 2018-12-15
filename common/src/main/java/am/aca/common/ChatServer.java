package am.aca.common;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface ChatServer extends Remote {

    void connect(String username, ChatClient c) throws RemoteException;

    void disconnect(ChatClient c) throws RemoteException;

    void broadcast(String clientName, String message) throws RemoteException;

    void list(ChatClient client) throws RemoteException;

    boolean checkUsername(String username) throws RemoteException;

    void registred(String name) throws RemoteException;

}
