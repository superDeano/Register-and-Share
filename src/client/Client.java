package client;

import communication.Communication;

import java.util.Vector;

public class Client implements ClientInterface{
    private int socketNumber;
    private String name;
    private String ipAddress;
    private Communication communication;
    private Vector<String> subjectsInterested;

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

    public int getSocketNumber() {
        return socketNumber;
    }

    public void setSocketNumber(int socketNumber) {
        this.socketNumber = socketNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
