package server;

import communication.Communication;
import logger.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Server extends ServerModel implements ServerInterface {
    private final Logger logger;
    List<ClientModel> clients;
    Communication communication;

    public Server(String connectionName) {
        this.clients = new LinkedList<ClientModel>();
        this.logger = new Logger();
        this.communication = new Communication(connectionName);
    }

    @Override
    public void startServing() {
        logger.log(super.getName() + "started serving");
    }

    @Override
    public void stopServing() {
        logger.log(super.getName() + "stopped serving");
    }

    @Override
    public void registerUser(String requestNumber, String name, String ipAddress, int socketNumber) {
        if (!clientNameExist(name)) {
            this.clients.add(new ClientModel(name, ipAddress, socketNumber));
            logger.log("ADDING USER", "User successfully added!");

            //TODO don't forget to send message
        }
        logger.log("ADDING USER", "Chosen name already exists!");
        //TODO don't forget to send message
    }

    @Override
    public void deregisterUser(String requestNumber, String name) {
        if (clientNameExist(name)) {
            this.removeClientWithName(name);
            logger.log("REMOVING USER", "User successfully deleted!");
            //TODO don't forget to send message
        }
        logger.log("REMOVING USER", "User does not exist!");
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
                logger.log("UPDATING USER", "User successfully updated!");
            }
            //TODO don't forget to send message
        } else {
            logger.log("UPDATING USER", "User does not exist!");
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
                logger.log("UPDATING SUBJECTS OF INTEREST", "Subjects of interest successfully updated!");
            }
            //TODO don't forget to send message
        } else {
            //TODO don't forget to send message
            logger.log("UPDATING SUBJECTS OF INTEREST", "User does not exist!");
        }
    }

    @Override
    public void userPublishesMessage(String requestNumber, String name, String subject, String text) {
        if (clientNameExist(name)) {
            //TODO: send message to clients who subscriped to subject
            logger.log("PUBLISHING MESSAGE", "Message published successfully!");
        } else {
            logger.log("PUBLISHING MESSAGE", "User does not exist!");
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
