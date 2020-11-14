package client;

import communication.Communication;
import server.ClientModel;

import java.util.Vector;

public class Client extends ClientModel implements ClientInterface{
    private Communication communication;

    public Client(){
        super();
    }

    public Client(String name, String ipAddress, int socketNumber) {
        super(name, ipAddress, socketNumber);
    }

    @Override
    public void registerToServer() {

    }

    @Override
    public void deregisterToServer() {

    }

    @Override
    public void registerToSubjectOfInterest(String subjectOfInterest) {

    }

    @Override
    public void deregisterToSubjectOfInterest(String subjectOfInterest) {

    }

    @Override
    public void updateInformationToServer() {

    }

    @Override
    public void publishMessage(String topic, String message) {

    }

}
