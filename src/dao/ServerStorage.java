package dao;

import server.ClientModel;
import server.ServerModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerStorage implements ServerStorageInterface {
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private final String currentServerName;

    public ServerStorage(String currentServerName) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        this.currentServerName = currentServerName;
        String url = "jdbc:mysql://register-mysql.mysql.database.azure.com:3306/register-server?useSSL=true";
        this.connection = DriverManager.getConnection(url, "supermysql", "dypqiC-nokroh-2xypnu");
    }

    @Override
    public void addClient(ClientModel client) {
        try {
            String query = "INSERT INTO `clients` (name, ipAddress, portNumber, serverName) VALUES (?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getIpAddress());
            preparedStatement.setInt(3, client.getSocketNumber());
            preparedStatement.setString(4, currentServerName);
            preparedStatement.executeUpdate();
//        query.append(client.getName() + ", " + client.getIpAddress() + ", " + client.getSocketNumber() + ", " + currentServerName + ")");
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
            preparedStatement = connection.prepareStatement("DELETE CLIENTS c, `client-subjects` cs FROM c inner join on c.name = cs.clientName where c.name = ?;");
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
            resultSet = statement.executeQuery("select c.name, c.ipAddress, c.portNumber from clients c");
            while (resultSet.next()) {
                clientList.add(new ClientModel(resultSet.getString("name"), resultSet.getString("ipAddress"), resultSet.getInt("portNumber")));
            }
            getClientsSubjects(clientList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return clientList;
    }

    private void getClientsSubjects(List<ClientModel> clientModelList) {
        for (ClientModel clientModel : clientModelList) {
            try {
                preparedStatement = connection.prepareStatement("select cs.subject from `client-subjects` cs where cs.serverName = ? and cs.clientName = ?;");
                preparedStatement.setString(1, currentServerName);
                preparedStatement.setString(2, clientModel.getName());
                resultSet = preparedStatement.executeQuery();

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
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO client-subjects(clientName, serverName, subject) VALUES (?,?,?)");
            preparedStatement.setString(1, clientName);
            preparedStatement.setString(2, currentServerName);
            for (String subject : subjects) {
                preparedStatement.setString(3, subject);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void clientUnsubscribeToSubjects(String clientName, List<String> subjects) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `client-subjects` cs where (cs.clientName = ? and cs.serverName = ?) and cs.subject in ? ");
            preparedStatement.setString(1, clientName);
            preparedStatement.setString(2, currentServerName);
            preparedStatement.setArray(3, (Array) subjects);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
            preparedStatement = connection.prepareStatement("SELECT s.otherServerIpAddress, s.otherServerPortNumber from servers s where s.serverName = ?;");
            preparedStatement.setString(1, currentServerName);
            resultSet = preparedStatement.executeQuery();
//            statement = connection.createStatement();
//            resultSet = statement.executeQuery("SELECT s.otherServerIpAddress, s.otherServerPortNumber from servers s where s.serverName = " + currentServerName + ";");

            otherServer.setSocketNumber(resultSet.getInt("otherServerPortNumber"));
            otherServer.setIpAddress(resultSet.getString("otherServerIpAddress"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return otherServer;
    }
}
