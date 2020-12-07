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

    public Server(String connectionName, String otherServerIp, int otherServerPort, boolean isServing) {
        super(connectionName);
        this.isServing = isServing;
        this.otherServerIp = otherServerIp;
        this.otherServerPort = otherServerPort;

        this.communication = new Communication(connectionName);
//        updateServerInfo();
        setUpServer();
        if (isServing) {
            startServing();
        }
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
            }
            this.messageQueue = new ConcurrentLinkedQueue<>();
            this.logger = new Logger();
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
            logger.log("Server Serving", "Started Serving");
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
                currentTime = System.nanoTime();
                if ((currentTime - startTime) / 1000000000 == 300) {
                    stopServing();
                }
            }
        };

        Thread servingThread = new Thread(takeCareOfMessages);
        servingThread.start();
    }

    private void handleMessage(Message msg) {

        String msgType = msg.getMsgType();

        switch (msgType) {
            case MsgType.REGISTER -> register(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
            case REGISTERED -> registered(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
            case REGISTER_DENIED -> logger.log("Server received register deny for user: " + msg.getName());
            case DE_REGISTER -> deRegister(msg);
            case UPDATE -> updateClientInformation(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
            case UPDATE_CONFIRMED -> updateConfirmed(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
            case SUBJECTS -> subjects(msg);
            case SUBJECTS_UPDATED -> subjectsUpdated(msg.getRequestNumber(), msg.getName(), msg.getSubjectsList());
            case UPDATE_SERVER -> updateOtherServerInfo(msg);
            case PUBLISH -> publish(msg);
            case SWITCH_SERVER -> switchServer();
            default -> logger.log("Unknown message has been received");
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

        logger.log(super.getName() + "started serving");
        startTime = System.nanoTime();
        isServing = true;
        serveClients();

    }

    @Override
    public void stopServing() {
        logger.log(super.getName() + "stopped serving");

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

        Message switchServe = new Message();
        changeServer.setMsgType(SWITCH_SERVER);
        String switchMessage = Parsing.parseMsgToString(switchServe);
        communication.sendMessage(switchMessage,
                otherServerIp,
                otherServerPort);

        isServing = false;
    }

    @Override
    public void register(int requestNumber, String name, String ipAddress, int socketNumber) {
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

    @Override
    public void registered(int requestNumber, String name, String ipAddress, int socketNumber) {
        ClientModel newClient = new ClientModel(name, ipAddress, socketNumber);
        this.clients.add(newClient);
        dao.addClient(newClient);
    }

    @Override
    public void deRegister(Message message) {

//        ClientModel client = getClientWithName(name);
//        if (client != null) {
//            ClientModel toDelete = new ClientModel();
//        if (isServing) {
//                for (ClientModel client : clients
//                ) {
//                    if (client.getName().equals(name)) {
//                        toDelete = client;
//                    }
        if (removeClientWithName(message.getName())) {
//            }
//        }
//        this.removeClientWithName(name);
            dao.deleteClient(message.getName());
            logger.log("REMOVING USER", "User successfully deleted!");

//        if (isServing) {
//            Message clientAssert = new Message();
//            clientAssert.setMsgType(DE_REGISTER.toString());
//            clientAssert.setRequestNumber(requestNumber);
//            clientAssert.setName(name);
//            String message = Parsing.parseMsgToString(clientAssert);
//
//            communication.sendMessage(message, null, null);
//                    toDelete.getIpAddress(),
//                    toDelete.getSocketNumber());
            Message serverAssert = new Message();
            serverAssert.setMsgType(DE_REGISTER.toString());
            serverAssert.setName(message.getName());
            String serverMessage = Parsing.parseMsgToString(serverAssert);

            communication.sendMessage(serverMessage,
                    otherServerIp,
                    otherServerPort);
//        }
        } else {
            logger.log("REMOVING USER", "User does not exist!");
        }

    }

    @Override
    public void updateClientInformation(int requestNumber, String name, String ipAddress, int socketNumber) {
        ClientModel client = getClientWithName(name);
        if (client != null) {

//            for (ClientModel client : clients
//            ) {
//                if (client.getName().equals(name)) {
            client.setIpAddress(ipAddress);
            client.setSocketNumber(socketNumber);
//                }
//            }

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
            communication.sendMessage(message, otherServerIp, otherServerPort);

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

    @Override
    public void updateConfirmed(int requestNumber, String name, String ipAddress, int socketNumber) {
//        for (ClientModel client : clients) {
//            if (client.getName().equals(name)) {
//                client.setIpAddress(ipAddress);
//                client.setSocketNumber(socketNumber);
//            }
//        }
        ClientModel clientModel = getClientWithName(name);
        clientModel.setIpAddress(ipAddress);
        clientModel.setSocketNumber(socketNumber);
        dao.updateClientInfo(clientModel);
    }

    @Override
    public void subjects(Message message) {
        ClientModel client = getClientWithName(message.getName());
        if (client != null) {

//            List<String> accepted = new LinkedList<String>();
//            List<String> denied = new LinkedList<String>();
//            ;

//            for (ClientModel client : clients
//            ) {
//                if (client.getName().equals(name)) {
//                    for (String subject : listOfSubjects
//                    ) {
//                        if (client.addSubject(subject)) {

//                            accepted.add(subject);
//                            logger.log("Subject: " + subject + " has been added");
//                        } else {
//                            denied.add(subject);
//                            logger.log("Subject: " + subject + " was already subscribed to");
//                        }
//                    }
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

            communication.sendMessage(messageS,
                    otherServerIp,
                    otherServerPort);

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

    @Override
    public void subjectsUpdated(int requestNumber, String name, List<String> subjectsList) {
//        for (ClientModel client : clients
//        ) {
//            if (client.getName().equals(name)) {
//                for (String subject : subjectsList
//                ) {
//                    if (client.addSubject(subject)) {
//                        logger.log("Subject: " + subject + " has been added");
//                    }
//                }
//            }
//        }
        ClientModel client = getClientWithName(name);
        if (client != null) {
            client.setSubjectsOfInterest(subjectsList);
            dao.updateClientListOfSubjects(name, subjectsList);
        }

    }

    @Override
    public void publish(Message message) {
        ClientModel client = getClientWithName(message.getName());
        if (client != null) {
//            boolean subjectFound = false;
//            boolean messageSent = false;
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
            //Client not subscribed to subject
            else {
                Message messageClientNotSub = new Message();
                messageClientNotSub.setMsgType(PUBLISH_DENIED);
                messageClientNotSub.setRequestNumber(message.getRequestNumber());
                messageClientNotSub.setReason("User not subscribed to topic");
                communication.sendMessage(Parsing.parseMsgToString(messageClientNotSub), client.getIpAddress(), client.getSocketNumber());
            }

//            for (ClientModel client : clients
//            ) {
//                if (client.getName().equals(name)) {
//                    for (String sub : client.getSubjectsOfInterest()
//                    ) {
//                        if (sub.equals(subject)) {
//                            subjectFound = true;
//                            for (ClientModel cli : clients
//                            ) {
//                                for (String subj : cli.getSubjectsOfInterest()
//                                ) {
//                                    if (subj.equals(subject)) {
//                                        messageSent = true;
//                                        Message clientPublish = new Message();
//                                        clientPublish.setMsgType(MESSAGE.toString());
//                                        clientPublish.setName(name);
//                                        clientPublish.setSubject(subject);
//                                        clientPublish.setText(text);
//                                        String message = Parsing.parseMsgToString(clientPublish);
//
//                                        communication.sendMessage(message,
//                                                cli.getIpAddress(),
//                                                cli.getSocketNumber());
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }

//            if (!subjectFound) {
//                for (ClientModel client : clients
//                ) {
//                    if (client.getName().equals(name)) {
//                        Message clientDeny = new Message();
//                        clientDeny.setMsgType(PUBLISH_DENIED.toString());
//                        clientDeny.setRequestNumber(requestNumber);
//                        clientDeny.setReason("Subject is not within the subjects of interests of user: " + name);
//                        String message = Parsing.parseMsgToString(clientDeny);
//
//                        communication.sendMessage(message,
//                                client.getIpAddress(),
//                                client.getSocketNumber());
//                    }
//                }
//            }
//
//            if (messageSent) {
//                logger.log("PUBLISHING MESSAGE", "Message has been Published !");
//            }


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

    @Override
    public void switchServer() {
        startServing();
        isServing = true;
        logger.log("Server Serving", "Server Started Serving");
    }

//    private void sendMessage(String m) {
//        communication.sendMessage(m, "127.0.0.1", 2313);
//    }

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

    public boolean updateServerInfo(int newPortNumber) {
        if (!communication.portIsValid(newPortNumber)) return false;
        if (communication.setPort(newPortNumber)) {
            Message currentServerUpdatedInfo = new Message();
            currentServerUpdatedInfo.setMsgType(UPDATE_SERVER);
            currentServerUpdatedInfo.setIpAddress(communication.getIpAddress());
            currentServerUpdatedInfo.setSocketNumber(communication.getPortNumber());
            String message = Parsing.parseMsgToString(currentServerUpdatedInfo);
            communication.sendMessage(message, otherServerIp, otherServerPort);
            return true;
        } else return false;
    }

    private void updateOtherServerInfo(Message message) {
        otherServerIp = message.getIpAddress();
        otherServerPort = message.getSocketNumber();
        dao.updateOtherServerIpAddressAndPortNumber(message.getIpAddress(), message.getSocketNumber());
    }


}
