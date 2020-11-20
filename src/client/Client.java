package client;

import communication.Communication;
import server.ClientModel;
import server.ServerModel;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client extends ClientModel implements ClientInterface{
    private final Communication communication;
    private final ServerModel[] servers = new ServerModel[2];
    private int servingServer = -1;

    public Client(){
        super();
        this.servers[0] = new ServerModel("server one");
        this.servers[1] = new ServerModel("server two");
        this.communication = new Communication("client");
    }

    public Client(String name, String ipAddress, int socketNumber) throws UnknownHostException {
        super(name, InetAddress.getByName(ipAddress), socketNumber);
        this.servers[0] = new ServerModel("server one");
        this.servers[1] = new ServerModel("server two");
        this.communication = new Communication("client");
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
//        communication.sendMessage();
    }

    @Override
    public void publishMessage(String topic, String message) {

    }

    public ServerModel[] getServers(){
        return this.servers;
    }

    public String listen(){
        //TODO check what the message is and take appropriate action
        return communication.waitForMessage();
    }

    public String getClientPortNumber(){
        return (String.valueOf(this.communication.getPortNumber()));
    }

    public String getClientIpAddress(){
        return this.communication.getIpAddress();
    }

}
