package server;

import communication.Communication;
import dao.ServerStorage;
import logger.Logger;
import message.Message;
import message.MsgType;
import message.Parsing;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static message.MsgType.*;

public class Server extends ServerModel implements ServerInterface {
    private Logger logger;
    private List<ClientModel> clients;
    private Communication communication;
    private ConcurrentLinkedQueue<Message> messageQueue;
    private boolean isServing;
    private String otherServerIp;
    private int otherServerPort;
    private long startTime;
    private long currentTime;
    private ServerStorage dao;
    //private static JTextField otherServerIpAddressTF, otherServerPortNumberTF;
    private DefaultListModel<String> logs;

    public Server(String connectionName, int portNumber, DefaultListModel<String> logs) {
        super(connectionName);
        this.isServing = false;
        this.communication = new Communication(portNumber, connectionName);
        this.setIpAddress(communication.getIpAddress());
        this.setSocketNumber(portNumber);
        this.logs = logs;
        setUpServer();
    }

//    public Server(String connectionName, String otherServerIp, int otherServerPort, boolean isServing) {
//        super(connectionName);
//        this.isServing = isServing;
//        this.otherServerIp = otherServerIp;
//        this.otherServerPort = otherServerPort;
//
//        this.communication = new Communication(connectionName);
////        updateServerInfo();
//        setUpServer();
//        if (isServing) {
//            startServing();
//        }
//    }


    public void setOtherServerIpAddressTF( JTextField otherServerIpAddressTF) {
         otherServerIpAddressTF = otherServerIpAddressTF;
    }

    public void setOtherServerPortNumberTF(JTextField otherServerPortNumberTF) {
        otherServerPortNumberTF = otherServerPortNumberTF;
    }

    private void setUpServer() {
        try {
            this.dao = new ServerStorage(getName());
//            this.clients = new LinkedList<>();
            getClientListSaved();
            ServerModel otherServer = dao.getOtherServerInfo();
            if (otherServer != null) {
                otherServerIp = otherServer.getIpAddress();
                otherServerPort = otherServer.getSocketNumber();
                displayOtherServerInfo();
            }
            this.messageQueue = new ConcurrentLinkedQueue<>();
            this.logger = new Logger();
            if (this.getName().equals("A")) {
                logger.log("name is A and serving is " + isServing);
                startServing();
            }
            listen();
            serveClients();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    private void getClientListSaved() {
        this.clients = dao.getAllClients();
    }

    private void serveClients() {

        Runnable takeCareOfMessages = () -> {
            while (true) {

                if (this.messageQueue.isEmpty()) {

                    try {
//                        logger.log("Server Serving", "Going to sleep");
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
//                    for (String msg : messageQueue) {
                    logger.log("Server Serving", "Sending Message");
                    Message message = messageQueue.poll();
//                    Message message = Parsing.parseStringToMsg(msg);
                    handleMessage(message);
//                    sendMessage(msg);

//                    }
                }
                if (isServing) {
                    currentTime = System.nanoTime();
                    if ((currentTime - startTime) / 1E9 >= 120) {
                        logger.log("Server switching is starting");
                        stopServing();
                    }
                }
            }
        };

        Thread servingThread = new Thread(takeCareOfMessages);
        servingThread.start();
    }

    private void handleMessage(Message msg) {

        String msgType = msg.getMsgType();

        switch (msgType) {
            case MsgType.REGISTER:
                register(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
                break;
            case REGISTERED:
                registered(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
                break;
            case REGISTER_DENIED:
                logger.log("Server received register deny for user: " + msg.getName());
                break;
            case DE_REGISTER:
                deRegister(msg);
                break;
            case UPDATE:
                updateClientInformation(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
                break;
            case UPDATE_CONFIRMED:
                updateConfirmed(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
                break;
            case SUBJECTS:
                subjects(msg);
                break;
            case SUBJECTS_UPDATED:
                subjectsUpdated(msg.getRequestNumber(), msg.getName(), msg.getSubjectsList());
                break;
            case UPDATE_SERVER:
                updateOtherServerInfo(msg);
                break;
            case PUBLISH:
                publish(msg);
                break;
            case SWITCH_SERVER:
                switchServer();
                break;
            default:
                logger.log("Unknown message has been received");
                break;
        }

    }

    public void setLogs(DefaultListModel<String> logs) {
        this.logs = logs;
    }


    private void listen() {
        Thread listeningThread = new Thread() {
            public void run() {
                logger.log("Started Listening");
//                while (true) {
                try {
                    communication.waitForMessage(messageQueue, logs);
//                        messageQueue.put(message);
                    logger.log("Received message");
//                        logs.addElement(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            }
        };
        listeningThread.start();
    }


    @Override
    public void startServing() {

        logger.log(super.getName() + " started serving");
        startTime = System.nanoTime();
        isServing = true;
    }

    @Override
    public void stopServing() {
        logger.log(super.getName() + " stopped serving");

        Message changeServer = new Message();
        changeServer.setMsgType(CHANGE_SERVER);
        changeServer.setIpAddress(otherServerIp);
        changeServer.setSocketNumber(otherServerPort);
        String message = Parsing.parseMsgToString(changeServer);

        for (ClientModel clients : clients
        ) {
            communication.sendMessage(message,
                    clients.getIpAddress(),
                    clients.getSocketNumber());
        }
        if (otherServerIp != null && otherServerPort != 0) {
            Message switchServe = new Message();
            changeServer.setMsgType(SWITCH_SERVER);
            String switchMessage = Parsing.parseMsgToString(switchServe);
            communication.sendMessage(switchMessage,
                    otherServerIp,
                    otherServerPort);
        }
        isServing = false;
    }

    @Override
    public void register(int requestNumber, String name, String ipAddress, int socketNumber) {
        if (this.isServing) {
            if (getClientWithName(name) == null) {
                ClientModel newClient = new ClientModel(name, ipAddress, socketNumber);
                this.clients.add(newClient);
                dao.addClient(newClient);

                logger.log("ADDING USER", "User successfully added!");

                Message clientAssert = new Message();
                clientAssert.setMsgType(REGISTERED);
                clientAssert.setRequestNumber(requestNumber);
                clientAssert.setName(name);
                clientAssert.setIpAddress(ipAddress);
                clientAssert.setSocketNumber(socketNumber);
                String message = Parsing.parseMsgToString(clientAssert);

                communication.sendMessage(message, ipAddress, socketNumber);
                if (otherServerIp != null && otherServerPort != 0) {
                    Message serverAssert = new Message();
                    serverAssert.setMsgType(REGISTERED);
                    serverAssert.setRequestNumber(requestNumber);
                    serverAssert.setName(name);
                    serverAssert.setIpAddress(ipAddress);
                    serverAssert.setSocketNumber(socketNumber);
                    String serverMsg = Parsing.parseMsgToString(serverAssert);

                    communication.sendMessage(serverMsg,
                            otherServerIp,
                            otherServerPort);
                }
            } else {
                logger.log("ADDING USER", "Chosen name already exists!");

                Message clientAssert = new Message();
                clientAssert.setMsgType(REGISTER_DENIED);
                clientAssert.setRequestNumber(requestNumber);
                clientAssert.setReason("Name is already in use");
                String message = Parsing.parseMsgToString(clientAssert);

                communication.sendMessage(message,
                        ipAddress,
                        socketNumber);

                if (otherServerIp != null && otherServerPort != 0) {
                    Message serverAssert = new Message();
                    serverAssert.setMsgType(REGISTER_DENIED);
                    serverAssert.setRequestNumber(requestNumber);
                    serverAssert.setName(name);
                    serverAssert.setIpAddress(ipAddress);
                    serverAssert.setSocketNumber(socketNumber);
                    String serverMsg = Parsing.parseMsgToString(serverAssert);

                    communication.sendMessage(serverMsg,
                            otherServerIp,
                            otherServerPort);
                }
            }
        }
    }

    @Override
    public void registered(int requestNumber, String name, String ipAddress, int socketNumber) {
        if (!this.isServing) {
            ClientModel newClient = new ClientModel(name, ipAddress, socketNumber);
            this.clients.add(newClient);
            dao.addClient(newClient);
        }
    }

    public void setOtherServerInfo() {
        logger.log("Saving other server ip address and port");
        this.otherServerIp = Main.otherServerIpAddressTF.getText();
        this.otherServerPort = Integer.parseInt(Main.otherServerPortNumberTF.getText());
        dao.updateOtherServerIpAddressAndPortNumber(otherServerIp, otherServerPort);
        logger.log("other server ip & port", this.otherServerIp + ":" + this.otherServerPort);
    }

    @Override
    public void deRegister(Message message) {

        if (removeClientWithName(message.getName())) {
            dao.deleteClient(message.getName());
            logger.log("REMOVING USER", "User successfully deleted!");

            if (this.isServing) {
                if (otherServerIp != null && otherServerPort != 0) {
                    Message serverAssert = new Message();
                    serverAssert.setMsgType(DE_REGISTER.toString());
                    serverAssert.setName(message.getName());
                    String serverMessage = Parsing.parseMsgToString(serverAssert);

                    communication.sendMessage(serverMessage,
                            otherServerIp,
                            otherServerPort);
                }
            }
        } else {
            logger.log("REMOVING USER", "User does not exist!");
        }

    }

    @Override
    public void updateClientInformation(int requestNumber, String name, String ipAddress, int socketNumber) {
        if (this.isServing) {
            ClientModel client = getClientWithName(name);
            if (client != null) {

                client.setIpAddress(ipAddress);
                client.setSocketNumber(socketNumber);


                dao.updateClientInfo(client);
                logger.log("UPDATING USER", "User successfully updated!");

                Message clientAssert = new Message();
                clientAssert.setMsgType(UPDATE_CONFIRMED.toString());
                clientAssert.setRequestNumber(requestNumber);
                clientAssert.setName(name);
                clientAssert.setIpAddress(ipAddress);
                clientAssert.setSocketNumber(socketNumber);
                String message = Parsing.parseMsgToString(clientAssert);

                communication.sendMessage(message, ipAddress, socketNumber);
                if (otherServerIp != null && otherServerPort != 0) {
                    communication.sendMessage(message, otherServerIp, otherServerPort);
                }

            } else {

                Message clientAssert = new Message();
                clientAssert.setMsgType(UPDATE_DENIED);
                clientAssert.setRequestNumber(requestNumber);
                clientAssert.setReason("User with this name: " + name + " does not exist");
                String message = Parsing.parseMsgToString(clientAssert);

                communication.sendMessage(message, ipAddress, socketNumber);

                logger.log("UPDATING USER", "User does not exist!");
            }
        }
    }

    @Override
    public void updateConfirmed(int requestNumber, String name, String ipAddress, int socketNumber) {
        if (!this.isServing) {
            ClientModel clientModel = getClientWithName(name);
            clientModel.setIpAddress(ipAddress);
            clientModel.setSocketNumber(socketNumber);
            dao.updateClientInfo(clientModel);
        }
    }

    @Override
    public void subjects(Message message) {
        if (this.isServing) {
            ClientModel client = getClientWithName(message.getName());
            if (client != null) {

                List<String> listOfSubjects = message.getSubjectsList();
                client.setSubjectsOfInterest(listOfSubjects);

                if (listOfSubjects.isEmpty()) dao.deleteClientListOfSubjects(message.getName());
                else dao.updateClientListOfSubjects(message.getName(), listOfSubjects);

                Message clientAssert = new Message();
                clientAssert.setMsgType(SUBJECTS_UPDATED.toString());
                clientAssert.setRequestNumber(message.getRequestNumber());
                clientAssert.setName(message.getName());
                clientAssert.setSubjectsList(listOfSubjects);
                String messageS = Parsing.parseMsgToString(clientAssert);

                communication.sendMessage(messageS,
                        client.getIpAddress(),
                        client.getSocketNumber());

                if (otherServerIp != null && otherServerPort != 0) {
                    communication.sendMessage(messageS,
                            otherServerIp,
                            otherServerPort);
                }

//                }
//            }

            } else {
                logger.log("UPDATING SUBJECTS OF INTEREST", "User does not exist!");

                Message clientDeny = new Message();
                clientDeny.setMsgType(SUBJECTS_REJECTED);
                clientDeny.setRequestNumber(message.getRequestNumber());
                clientDeny.setName(message.getName());
                clientDeny.setSubjectsList(message.getSubjectsList());
                String denyMessage = Parsing.parseMsgToString(clientDeny);

                communication.sendMessage(denyMessage,
                        message.getIpAddress(),
                        message.getSocketNumber());
            }
        }
    }

    @Override
    public void subjectsUpdated(int requestNumber, String name, List<String> subjectsList) {
        if (!this.isServing) {
            ClientModel client = getClientWithName(name);
            if (client != null) {
                client.setSubjectsOfInterest(subjectsList);
                dao.updateClientListOfSubjects(name, subjectsList);
            }
        }
    }

    @Override
    public void publish(Message message) {
        if (this.isServing) {
            ClientModel client = getClientWithName(message.getName());
            if (client != null) {
                if (client.subscribedToSubject(message.getSubject())) {
                    Message messagePublished = new Message();
                    messagePublished.setMsgType(MESSAGE);
                    messagePublished.setName(message.getName());
                    messagePublished.setSubject(message.getSubject());
                    messagePublished.setText(message.getText());
                    clients.stream().filter(c1 -> !c1.getName().equals(client.getName()) && c1.subscribedToSubject(message.getSubject())).forEach(c2 -> {
                        sendMessage(message, c2.getIpAddress(), c2.getSocketNumber());
                    });
                }
                // Client not subscribed
                else {
                    Message messageClientNotSub = new Message();
                    messageClientNotSub.setMsgType(PUBLISH_DENIED);
                    messageClientNotSub.setRequestNumber(message.getRequestNumber());
                    messageClientNotSub.setReason("User not subscribed to topic");
                    communication.sendMessage(Parsing.parseMsgToString(messageClientNotSub), client.getIpAddress(), client.getSocketNumber());
                }
            }
            // Client with name does not exist
            else {
                Message publishDeniedMessage = new Message();
                publishDeniedMessage.setMsgType(PUBLISH_DENIED);
                publishDeniedMessage.setRequestNumber(message.getRequestNumber());
                publishDeniedMessage.setReason("User does not exist");
                communication.sendMessage(Parsing.parseMsgToString(publishDeniedMessage), message.getIpAddress(), message.getSocketNumber());
                logger.log("PUBLISHING MESSAGE", "User does not exist!");
            }
        }
    }

    @Override
    public void switchServer() {
        logger.log("Received messaged in switchServer () and will: " + (isServing ? " start" : " stop"));
        if (!this.isServing) {
            startServing();
            logger.log("Server Serving", "Server Started Serving");
        }
    }

    private void sendMessage(Message message, String ipAddress, int portNumber) {
        communication.sendMessage(Parsing.parseMsgToString(message), ipAddress, portNumber);
    }

    private ClientModel getClientWithName(String name) {
        for (ClientModel c : this.clients) {
            if (c.getName().equals(name)) return c;
        }
        return null;
    }

    private boolean removeClientWithName(String name) {
        return this.clients.removeIf(c -> c.getName().equals(name));
    }

    private void updateOtherServerInfo(Message message) {
        if (this.isServing) {
            otherServerIp = message.getIpAddress();
            otherServerPort = message.getSocketNumber();
            dao.updateOtherServerIpAddressAndPortNumber(message.getIpAddress(), message.getSocketNumber());
        }
        displayOtherServerInfo();
    }

    public boolean setCurrentServerPort(int port) {
        if (!this.isServing) {
            if (communication.portIsAvailable(port) && communication.portIsValid(port)) {
                if (communication.setPort(port)) {
                    if (otherServerIp != null && otherServerPort != 0) {
                        Message currentServerUpdatedInfo = new Message();
                        currentServerUpdatedInfo.setMsgType(UPDATE_SERVER);
                        currentServerUpdatedInfo.setIpAddress(communication.getIpAddress());
                        currentServerUpdatedInfo.setSocketNumber(communication.getPortNumber());
                        String message = Parsing.parseMsgToString(currentServerUpdatedInfo);
                        communication.sendMessage(message, otherServerIp, otherServerPort);
                        return true;
                    }
                }
                //else return false;
            }
            //else return false;
        }
         return false;

    }

    private void displayOtherServerInfo() {
        Main.otherServerIpAddressTF.setText(otherServerIp);
        Main.otherServerPortNumberTF.setText(String.valueOf(otherServerPort));
    }

    public boolean checkPort(int port) {
        return communication.portIsValid(port) && communication.portIsAvailable(port);
    }

}
