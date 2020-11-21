package dao;

import server.ClientModel;
import server.ServerModel;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerStorage implements ServerStorageInterface {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private final String currentServerName;
    private final String url = "jdbc:mysql://register-mysql.mysql.database.azure.com:3306/register-server?useSSL=true";

    public ServerStorage(String currentServerName) throws SQLException {
//        Class.forName("com.mysql.jdbc.Driver");
        this.currentServerName = currentServerName;
        this.connection = DriverManager.getConnection(url, "supermysql", "dypqiC-nokroh-2xypnu");
    }

    @Override
    public void addClient(ClientModel client) {
        StringBuilder query = new StringBuilder("INSERT INTO `clients` (name, ipAddress, portNumber, serverName) VALUES (");
        query.append(client.getName() + ", " + client.getIpAddress().toString() + ", " + client.getSocketNumber() + ", " + currentServerName + ")");
        try {
            statement = connection.createStatement();
            statement.execute(query.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateClientInfo(ClientModel client) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE clients c SET c.ipAddress = ?, c.portNumber = ? where c.name = ?;");
            preparedStatement.setString(1, client.getIpAddress().toString());
            preparedStatement.setString(2, String.valueOf(client.getSocketNumber()));
            preparedStatement.setString(3, client.getName());
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteClient(String name) {
        try {
            preparedStatement = connection.prepareStatement("DELETE CLIENTS c, client-subjects cs FROM c inner join on c.name = cs.clientName where c.name = ?;");
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<ClientModel> getAllClients() {
        List<ClientModel> clientList = new ArrayList<>();

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select c.clientName, c.ipAddress, c.portNumber from clients c");
            while (resultSet.next()) {
                clientList.add(new ClientModel(resultSet.getString("clientName"), InetAddress.getByName(resultSet.getString("ipAddress")), resultSet.getInt("portNumber")));
            }
            getClientsSubjects(clientList);
        } catch (SQLException | UnknownHostException throwables) {
            throwables.printStackTrace();
        }

        return clientList;
    }

    private void getClientsSubjects(List<ClientModel> clientModelList) {
        for (ClientModel clientModel : clientModelList) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("select cs.subject from client-subjects cs where cs.serverName = " + currentServerName + " and cs.clientName = " + clientModel.getName() + ";");

                while (resultSet.next()) {
                    clientModel.addSubject(resultSet.getString("subjects"));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void clientSubscribeToSubjects(String clientName, List<String> subjects) {

    }

    @Override
    public void clientUnsubscribeToSubjects(String clientName, List<String> subjects) {

    }

    @Override
    public void updateOtherServerIpAddressAndPortNumber(String ipAddress, int portNumber) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE servers s set s.otherServerIpAddress = ?, s.otherServerPortNumber = ? where s.serverName = ?;");
            preparedStatement.setString(1, ipAddress);
            preparedStatement.setString(2, String.valueOf(portNumber));
            preparedStatement.setString(3, currentServerName);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public ServerModel getOtherServerInfo() {
        ServerModel otherServer = new ServerModel();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT s.otherServerIpAddress, s.otherServerPortNumber from servers s where s.serverName = " + currentServerName + ";");

            otherServer.setSocketNumber(resultSet.getInt("otherServerPortNumber"));
            otherServer.setIpAddress(resultSet.getString("otherServerIpAddress"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return otherServer;
    }
}
