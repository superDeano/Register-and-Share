package client;

import communication.Communication;
import logger.Logger;
import message.Message;
import message.MsgType;
import message.Parsing;
import server.ClientModel;
import server.ServerModel;

import javax.swing.*;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

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
        super(name, ipAddress, socketNumber);
        setUpClient();
    }

    private void setUpClient() {
        logger = new Logger();
        this.servers[0] = new ServerModel("server A");
        this.servers[1] = new ServerModel("server B");
        this.communication = new Communication("client");

    }



    @Override
    public void registerToServer() {
        Message message = new Message();
        message.setMsgType(MsgType.REGISTER);
        message.setRequestNumber(requestNumber++);
        message.setName(getName());
        message.setIpAddress(communication.getIpAddress());
        message.setSocketNumber(communication.getPortNumber());
        sendMessage(message);
    }

    @Override
    public void deregisterToServer() {
        Message message = new Message();
        message.setMsgType(MsgType.DE_REGISTER);
        message.setName(getName());
        message.setRequestNumber(requestNumber++);
        sendMessage(message);
    }

    @Override
    public void updateSubjectsOfInterest(List<String> subjectOfInterest) {
        Message message = new Message();
        message.setMsgType(MsgType.SUBJECTS);
        message.setSubjectsList(subjectOfInterest);
        message.setRequestNumber(requestNumber++);
        message.setName(getName());
        sendMessage(message);
    }

    @Override
    public void updateInformationToServer() {
        Message message = new Message();
        message.setMsgType(MsgType.UPDATE);
        message.setName(getName());
        message.setRequestNumber(requestNumber++);
        message.setIpAddress(communication.getIpAddress());
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
        messageT.setMsgType(MsgType.PUBLISH);
        messageT.setText(message);
        sendMessage(messageT);
    }

    public void sendMessage(Message message) {
        switch (servingServer) {

            case 0:
                this.communication.sendMessage(Parsing.parseMsgToString(message), servers[0].getIpAddress(), servers[0].getSocketNumber());
                break;
            case 1:
                this.communication.sendMessage(Parsing.parseMsgToString(message), servers[1].getIpAddress(), servers[1].getSocketNumber());
                break;
            default: {
                this.communication.sendMessage(Parsing.parseMsgToString(message), servers[0].getIpAddress(), servers[0].getSocketNumber());
                this.communication.sendMessage(Parsing.parseMsgToString(message), servers[1].getIpAddress(), servers[1].getSocketNumber());
            }
            break;
        }

    }

    public ServerModel[] getServers() {
        return this.servers;
    }

    public void switchServer() {
        logger.log("updating serving Server index"," before: "+  servingServer);
        servingServer = (servingServer + 1) % 2;
        logger.log("updating serving Server index"," after: "+  servingServer);
    }

    public void changeServer(Message m) {
        if (servingServer == 0) {
            servers[1].setIpAddress(m.getIpAddress());
            servers[1].setSocketNumber(m.getSocketNumber());
        } else if (servingServer == 1) {
            servers[0].setIpAddress(m.getIpAddress());
            servers[0].setSocketNumber(m.getSocketNumber());
        }
        switchServer();
        displayServersInfo();
    }

    public void serverReplied(Message message) {
        for (int i = 0; i < servers.length; i++) {
            if (message.getSenderIpAddress().equals(servers[i].getIpAddress()) && message.getSenderSocketNumber() == servers[i].getSocketNumber()) {
                servingServer = i;
                logger.log("server replied", "i is "+ i);

            }
        }
    }

    public void listen(ConcurrentLinkedQueue<Message> messages, DefaultListModel<String> logs) {
        //TODO check what the message is and take appropriate action
        communication.waitForMessage(messages, logs);
    }

    public String getClientPortNumber() {
        return (String.valueOf(this.communication.getPortNumber()));
    }

    public String getClientIpAddress() {
        return this.communication.getIpAddress();
    }

    private void displayServersInfo() {
        RssClient.server1IpAddressTF.setText(servers[0].getIpAddress());
        RssClient.server1PortNumberTF.setText(String.valueOf(servers[0].getSocketNumber()));
        RssClient.server2IpAddressTF.setText(servers[1].getIpAddress());
        RssClient.server2PortNumberTF.setText(String.valueOf(servers[1].getSocketNumber()));
    }
}
