package client;

import communication.Communication;
import logger.Logger;
import message.Message;
import message.MsgType;
import message.Parsing;
import server.ClientModel;
import server.ServerModel;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client extends ClientModel implements ClientInterface {
    private final Communication communication;
    private Logger logger;
    private final ServerModel[] servers = new ServerModel[2];
    private int servingServer = -1;
//    private SynchronousQueue<String> messages;

    public Client() {
        super();
        logger = new Logger();
        this.servers[0] = new ServerModel("server A");
        this.servers[1] = new ServerModel("server B");
//        messages = new SynchronousQueue<>();
        this.communication = new Communication("client");
    }

    public Client(String name, String ipAddress, int socketNumber) throws UnknownHostException {
        super(name, InetAddress.getByName(ipAddress), socketNumber);
        this.logger = new Logger();
        this.servers[0] = new ServerModel("server one");
        this.servers[1] = new ServerModel("server two");
//        messages = new SynchronousQueue<>();
        this.communication = new Communication("client");
    }

    @Override
    public void registerToServer() {
        Message message = new Message();
        message.setMsgType(MsgType.REGISTER.toString());
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
        logger.log("Trying to publish message");
        Message messageT = new Message();
        messageT.setName(getName());
        messageT.setRequestNumber(0);
        messageT.setSubject(topic);
        messageT.setMsgType(MsgType.PUBLISH.toString());
        messageT.setText(message);
        logger.log("Server 1 ip", servers[0].getIpAddress());
        logger.log("server 1 pn", String.valueOf(servers[0].getSocketNumber()));
        this.communication.sendMessage(Parsing.parseMsgToString(messageT), servers[0].getIpAddress(), servers[0].getSocketNumber());

    }

    public ServerModel[] getServers() {
        return this.servers;
    }

    public void listen(DefaultListModel<String> messages) {
        //TODO check what the message is and take appropriate action
         communication.waitForMessage(messages);
    }

    public String getClientPortNumber() {
        return (String.valueOf(this.communication.getPortNumber()));
    }

    public String getClientIpAddress() {
        return this.communication.getIpAddress();
    }

}
