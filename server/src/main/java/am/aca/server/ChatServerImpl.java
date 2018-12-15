package am.aca.server;


import am.aca.common.ChatClient;
import am.aca.common.ChatServer;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServerImpl extends UnicastRemoteObject implements ChatServer {
    private List<ChatClient> clients;
    private List<String> clientsName;

    private static Connection connection = getConnection();

    private static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/chat",
                    "postgres",
                    "root"
            );

            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

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

        StringBuffer msg = new StringBuffer("Welcome ")
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
            c.update("server", client.getName() + " has left");
        }


    }

    @Override
    public void broadcast(String clientName, String message) throws RemoteException {
        for (ChatClient client : clients) {
            client.update(clientName, message);
        }
    }

    @Override
    public void list(ChatClient client) throws RemoteException {
        client.update("server", " Active users: " + clientsName.toString());
    }

    @Override
    public boolean checkUsername(String name) throws RemoteException {
        String sql = "SELECT username FROM users WHERE username = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public void registred(String name) throws RemoteException {
        String sql = "INSERT INTO users(username) VALUES (?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

//            preparedStatement.setInt(1, 3);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void getUsers(ChatClient client) throws RemoteException {
//        try {
//            Connection connection = DriverManager.getConnection(
//                    "jdbc:postgresql://localhost:5432/chat",
//                    "postgres",
//                    "root"
//            );
//
//            PreparedStatement statement = connection.prepareStatement("SELECT * FROM chat");
//            ResultSet resultSet = statement.executeQuery();
//
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String username = resultSet.getString("username");
//                client.print(username);
//
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//
//        }
//
//    }


}
