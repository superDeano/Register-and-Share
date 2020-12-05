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
import java.util.List;

public class Client extends ClientModel implements ClientInterface {
    private Communication communication;
    private Logger logger;
    private final ServerModel[] servers = new ServerModel[2];
    private int servingServer = -1;
    private int requestNumber = 0;

    public Client() {
        super();
        setUpClient();
    }

    public Client(String name, String ipAddress, int socketNumber) throws UnknownHostException {
        super(name, InetAddress.getByName(ipAddress), socketNumber);
        setUpClient();
    }

    private void setUpClient() {
        logger = new Logger();
        this.servers[0] = new ServerModel("server A");
        this.servers[1] = new ServerModel("server B");
        this.communication = new Communication("client");
//        try {
//            setIpAddress(InetAddress.getByName(communication.getIpAddress()));
//            setSocketNumber(communication.getPortNumber());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void registerToServer() {
        Message message = new Message();
        message.setMsgType(MsgType.REGISTER.toString());
        message.setRequestNumber(requestNumber++);
        message.setName(getName());
        message.setIpAddress(getIpAddress());
        message.setSocketNumber(getSocketNumber());
        sendMessage(message);
    }

    @Override
    public void deregisterToServer() {
        Message message = new Message();
        message.setMsgType(MsgType.DE_REGISTER.toString());
        message.setName(getName());
        message.setRequestNumber(requestNumber++);
        sendMessage(message);
    }

    @Override
    public void updateSubjectsOfInterest(List<String> subjectOfInterest) {
        Message message = new Message();
        message.setMsgType(MsgType.SUBJECTS.toString());
        message.setSubjectsList(subjectOfInterest);
        message.setRequestNumber(requestNumber++);
        message.setName(getName());
        sendMessage(message);
    }

    @Override
    public void updateInformationToServer() {
        Message message = new Message();
        message.setMsgType(MsgType.UPDATE.toString());
        message.setName(getName());
        message.setRequestNumber(requestNumber++);
        message.setIpAddress(communication.getDatagramSocket().getLocalAddress());
        message.setSocketNumber(communication.getPortNumber());
        sendMessage(message);
    }


    @Override
    public void publishMessage(String topic, String message) {
        logger.log("Trying to publish message");
        Message messageT = new Message();
        messageT.setName(getName());
        messageT.setRequestNumber(requestNumber++);
        messageT.setSubject(topic);
        messageT.setMsgType(MsgType.PUBLISH.toString());
        messageT.setText(message);
//        logger.log("Server 1 ip", servers[0].getIpAddress());
//        logger.log("server 1 pn", String.valueOf(servers[0].getSocketNumber()));
        sendMessage(messageT);
    }

    public void sendMessage(Message message) {
        this.communication.sendMessage(Parsing.parseMsgToString(message), servers[0].getIpAddress(), servers[0].getSocketNumber());
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
