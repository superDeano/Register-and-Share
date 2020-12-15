package dao;

import server.ClientModel;
import server.ServerModel;

import java.util.List;

public interface ServerStorageInterface {
    public void addClient(ClientModel client);
    public void updateClientInfo(ClientModel client);
    public void deleteClient(String name);
    public List<ClientModel> getAllClients();
    public void updateClientListOfSubjects(String clientName, List<String> subjects);
    public void deleteClientListOfSubjects(String clientName);
    public void insertOtherServerIpAddressAndPortNumber(String ipAddress, int portNumber);
    public void updateOtherServerIpAddressAndPortNumber(String ipAddress, int portNumber);
    public ServerModel getOtherServerInfo();
}
