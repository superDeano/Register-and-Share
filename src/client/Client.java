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
   // private JTextField server1IpAddressTF, server2IpAddressTF, server1PortNumberTF, server2PortNumberTF;

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

//    public void setServersInfoTF(JTextField server1IpAddressTF, JTextField server1PortNumberTF, JTextField server2IpAddressTF, JTextField server2PortNumberTF) {
//        RssClient.server1IpAddressTF = server1IpAddressTF;
//        RssClient.server1IpAddressTF = server1PortNumberTF;
//        this.server2IpAddressTF = server2IpAddressTF;
//        this.server2PortNumberTF = server2PortNumberTF;
//    }

    @Override
    public void registerToServer() {
        Message message = new Message();
        message.setMsgType(MsgType.REGISTER.toString());
        message.setRequestNumber(requestNumber++);
        message.setName(getName());
        message.setIpAddress(communication.getIpAddress());
        message.setSocketNumber(communication.getPortNumber());
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
        messageT.setMsgType(MsgType.PUBLISH.toString());
        messageT.setText(message);
//        logger.log("Server 1 ip", servers[0].getIpAddress());
//        logger.log("server 1 pn", String.valueOf(servers[0].getSocketNumber()));
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
        servingServer = (servingServer + 1) % 2;
    }

    public void changeServer(Message m) {
        if (servingServer == 0) {
            servers[1].setIpAddress(m.getIpAddress());
            servers[1].setSocketNumber(m.getSocketNumber());
        } else {
            servers[0].setIpAddress(m.getIpAddress());
            servers[0].setSocketNumber(m.getSocketNumber());
        }
        displayServersInfo();
    }

    public void serverReplied(Message message) {
        for (int i = 0; i < servers.length; i++) {
            if (message.getIpAddress().equals(servers[i].getIpAddress()) && message.getSocketNumber() == servers[i].getSocketNumber()) {
                servingServer = i;
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
