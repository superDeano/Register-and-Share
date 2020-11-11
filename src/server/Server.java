package server;

import communication.Communication;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Server implements ServerInterface {
    List<ClientModel> clients;
    Communication communication;

    public Server(String connectionName) {
        this.clients = new LinkedList<ClientModel>();
        this.communication = new Communication(connectionName);
    }

    @Override
    public void startServing() {

    }

    @Override
    public void stopServing() {

    }

    @Override
    public void registerUser(String requestNumber, String name, String ipAddress, int socketNumber) {
        if (!clientNameExist(name)) {
            this.clients.add(new ClientModel(name, ipAddress, socketNumber));
            //TODO don't forget to send message
        }
        //TODO don't forget to send message
    }

    @Override
    public void deregisterUser(String requestNumber, String name) {
        if (clientNameExist(name)) {
            this.removeClientWithName(name);
            //TODO don't forget to send message
        }
        //TODO don't forget to send message
    }

    @Override
    public void updateUserInformation(String requestNumber, String name, String ipAddress, int socketNumber) {
        if (clientNameExist(name)) {
            ClientModel existingClient = null;
            Optional<ClientModel> ghostClient = this.clients.stream().filter(c -> c.getName().equals(name)).findFirst();
            if (ghostClient.isPresent()) {
                existingClient = ghostClient.get();
                existingClient.setName(name);
                existingClient.setIpAddress(ipAddress);
                existingClient.setSocketNumber(socketNumber);
            }
            //TODO don't forget to send message
        } else {
            //TODO don't forget to send message
        }
    }

    @Override
    public void updateSubjectOfInterestToUser(String requestNumber, String name, String listOfSubjects) {
        if (clientNameExist(name)) {
            ClientModel existingClient = null;
            Optional<ClientModel> ghostClient = this.clients.stream().filter(c -> c.getName().equals(name)).findFirst();
            if (ghostClient.isPresent()) {
                existingClient = ghostClient.get();
               //TODO: format list of subjects.
            }
            //TODO don't forget to send message
        } else {
            //TODO don't forget to send message
        }
    }

    @Override
    public void userPublishesMessage(String requestNumber, String name, String subject, String text) {
        if (clientNameExist(name)){
            //TODO: send message to clients who subscriped to subject
        }
    }

    private boolean clientNameExist(String name) {
        for (ClientModel c : this.clients) {
            if (c.getName().equals(name)) return true;
        }
        return false;
    }

    private void removeClientWithName(String name) {
        this.clients.removeIf(c -> c.getName().equals(name));
    }
}
