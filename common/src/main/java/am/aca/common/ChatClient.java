package am.aca.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface ChatClient extends Remote {
    void update(String name, String message) throws RemoteException;

    String getName() throws RemoteException;

    void getUsers() throws RemoteException, SQLException;


}
