package dao;

import server.ClientModel;
import server.ServerModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class ServerStorage implements ServerStorageInterface{
    private Connection connection = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;


    @Override
    public void addClient(ClientModel client) {

    }

    @Override
    public void deleteClient(String name) {

    }

    @Override
    public List<ClientModel> getAllClients() {
        return null;
    }

    @Override
    public void updateOtherServerIpAddressAndPortNumber(String ipAddress, int portNumber) {

    }

    @Override
    public ServerModel getOtherServerInfo() {
        return null;
    }
}
