package am.aca.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatServer extends Remote {
    void connect(String name, ChatClient c) throws RemoteException;

    void disconnect(ChatClient c) throws RemoteException;
}
