package server;

import communication.Communication;
import logger.Logger;
import message.Message;
import message.Parsing;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

import static message.msgType.*;

public class Server extends ServerModel implements ServerInterface {
    private final Logger logger;
    private List<ClientModel> clients;
    private Communication communication;
    private final SynchronousQueue<String> messageQueue;
    private boolean isServing;
    private InetAddress otherServerIp;
    private int otherServerPort;
    private long startTime;
    private long currentTime;

    public Server(String connectionName, InetAddress otherServerIp, int otherServerPort, boolean isServing) {
        this.isServing =isServing;
        this.otherServerIp = otherServerIp;
        this.otherServerPort = otherServerPort;
        this.clients = new LinkedList<ClientModel>();
        this.logger = new Logger();
        this.communication = new Communication(connectionName);
        this.messageQueue = new SynchronousQueue<>();
        updateServerInfo();
        listen();
        serveClients();
        if (isServing){
            startServing();
        }
    }

    private void serveClients() {
        Runnable takeCareOfMessages = () -> {
            while (true) {
                if (this.messageQueue.isEmpty()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (String msg: messageQueue
                         ) {
                           Message message = Parsing.parseStringToMsg(msg);
                           handleMessage(message);
                    }
                }
                currentTime = System.nanoTime();
                if ((currentTime - startTime)/1000000000 == 300){
                    stopServing();
                }
            }
        };

        Thread servingThread = new Thread(takeCareOfMessages);
        servingThread.start();
    }
    private void handleMessage(Message msg) {

        String msgType= msg.getMsgType();

        switch (msgType) {
            case "REGISTER" -> register(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
            case "REGISTERED" -> registered(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
            case "REGISTER-DENIED" -> logger.log("Server received register deny for user: " + msg.getName());
            case "DE-REGISTER" -> deRegister(msg.getRequestNumber(), msg.getName());
            case "UPDATE" -> update(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
            case "UPDATE-CONFIRMED" -> updateConfirmed(msg.getRequestNumber(), msg.getName(), msg.getIpAddress(), msg.getSocketNumber());
            case "SUBJECTS" -> subjects(msg.getRequestNumber(), msg.getName(), msg.getSubjectsList());
            case "SUBJECTS-UPDATED" -> subjectsUpdated(msg.getRequestNumber(), msg.getName(), msg.getSubjectsList());
            case "PUBLISH" -> publish(msg.getRequestNumber(), msg.getName(), msg.getSubject(), msg.getText());
            case "SWITCH-SERVER" -> switchServer();
            default -> logger.log("Unknown message has been received");
        }

    }


    private void listen() {
        Thread listeningThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        messageQueue.put(communication.waitForMessage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        listeningThread.start();
    }


    @Override
    public void startServing() {

        logger.log(super.getName() + "started serving");
        startTime = System.nanoTime();
        isServing = true;

    }

    @Override
    public void stopServing() {
        logger.log(super.getName() + "stopped serving");

        Message changeServer = new Message();
        changeServer.setMsgType(CHANGE_SERVER.toString());
        changeServer.setIpAddress(otherServerIp);
        changeServer.setSocketNumber(otherServerPort);
        String message = Parsing.parseMsgToString(changeServer);

        for (ClientModel clients: clients
             ) {
            communication.sendMessage(message,
                    clients.getIpAddress(),
                    clients.getSocketNumber());
        }

        Message switchServe = new Message();
        changeServer.setMsgType(SWITCH_SERVER.toString());
        String switchMessage = Parsing.parseMsgToString(switchServe);
        communication.sendMessage(switchMessage,
                otherServerIp,
                otherServerPort);

        isServing = false;
    }

    @Override
    public void register(int requestNumber, String name, InetAddress ipAddress, int socketNumber) {
        if (!clientNameExist(name)) {
            this.clients.add(new ClientModel(name, ipAddress, socketNumber));
            //TODO write to db
            logger.log("ADDING USER", "User successfully added!");

            Message clientAssert = new Message();
            clientAssert.setMsgType(REGISTERED.toString());
            clientAssert.setRequestNumber(requestNumber);
            String message = Parsing.parseMsgToString(clientAssert);

            communication.sendMessage(message,ipAddress,socketNumber);

            Message serverAssert = new Message();
            serverAssert.setMsgType(REGISTERED.toString());
            serverAssert.setRequestNumber(requestNumber);
            serverAssert.setName(name);
            serverAssert.setIpAddress(ipAddress);
            serverAssert.setSocketNumber(socketNumber);
            String serverMsg = Parsing.parseMsgToString(serverAssert);

            communication.sendMessage(serverMsg,
                    otherServerIp,
                    otherServerPort);
        }
        else{
            logger.log("ADDING USER", "Chosen name already exists!");

            Message clientAssert = new Message();
            clientAssert.setMsgType(REGISTER_DENIED.toString());
            clientAssert.setRequestNumber(requestNumber);
            clientAssert.setReason("Name is already in use");
            String message = Parsing.parseMsgToString(clientAssert);

            communication.sendMessage(message,
                    ipAddress,
                    socketNumber);

            Message serverAssert = new Message();
            serverAssert.setMsgType(REGISTER_DENIED.toString());
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
    public void registered( int requestNumber, String name, InetAddress ipAddress, int socketNumber){

        this.clients.add(new ClientModel(name, ipAddress, socketNumber));
        //TODO add write to db

    }

    @Override
    public void deRegister(int requestNumber, String name) {
        if (clientNameExist(name)) {
            ClientModel toDelete = new ClientModel();
            if (isServing) {
                for (ClientModel client : clients
                ) {
                    if (client.getName() == name) {
                        toDelete = client;
                    }
                }
            }
            this.removeClientWithName(name);
            //TODO remove from db
            logger.log("REMOVING USER", "User successfully deleted!");

            if (isServing) {
                Message clientAssert = new Message();
                clientAssert.setMsgType(DE_REGISTER.toString());
                clientAssert.setRequestNumber(requestNumber);
                clientAssert.setName(name);
                String message = Parsing.parseMsgToString(clientAssert);

                communication.sendMessage(message,
                        toDelete.getIpAddress(),
                        toDelete.getSocketNumber());

                Message serverAssert = new Message();
                serverAssert.setMsgType(DE_REGISTER.toString());
                serverAssert.setName(name);
                String serverMessage = Parsing.parseMsgToString(serverAssert);

                communication.sendMessage(message,
                        otherServerIp,
                        otherServerPort);
            }
        } else {
            logger.log("REMOVING USER", "User does not exist!");
        }
    }

    @Override
    public void update(int requestNumber, String name, InetAddress ipAddress, int socketNumber) {
        if (clientNameExist(name)) {

            for (ClientModel client : clients
            ) {
                if (client.getName().equals(name)) {
                    client.setIpAddress(ipAddress);
                    client.setSocketNumber(socketNumber);
                }
            }

                //TODO Update db
                logger.log("UPDATING USER", "User successfully updated!");


            Message clientAssert = new Message();
            clientAssert.setMsgType(UPDATE_CONFIRMED.toString());
            clientAssert.setRequestNumber(requestNumber);
            clientAssert.setName(name);
            clientAssert.setIpAddress(ipAddress);
            clientAssert.setSocketNumber(socketNumber);
            String message = Parsing.parseMsgToString(clientAssert);

            communication.sendMessage(message,ipAddress,socketNumber);
            communication.sendMessage(message,otherServerIp,otherServerPort);

        } else {

            Message clientAssert = new Message();
            clientAssert.setMsgType(UPDATE_DENIED.toString());
            clientAssert.setRequestNumber(requestNumber);
            clientAssert.setReason("User with this name: " + name + " does not exist");
            String message = Parsing.parseMsgToString(clientAssert);

            communication.sendMessage(message,ipAddress,socketNumber);

            logger.log("UPDATING USER", "User does not exist!");
        }
    }

    @Override
    public void updateConfirmed(int requestNumber, String name, InetAddress ipAddress, int socketNumber) {
        for (ClientModel client : clients
        ) {
            if (client.getName().equals(name)) {
                client.setIpAddress(ipAddress);
                client.setSocketNumber(socketNumber);
            }
        }
        //TODO update db
    }

    @Override
    public void subjects(int requestNumber, String name, List<String> listOfSubjects) {
        if (clientNameExist(name)) {

            List <String> accepted = new LinkedList<String>();
            List <String> denied = new LinkedList<String>();;

            for (ClientModel client : clients
            ) {
                if (client.getName().equals(name)) {
                    for (String subject:listOfSubjects
                         ) {
                        if(client.addSubject(subject)){
                            //TODO Update db
                            accepted.add(subject);
                            logger.log("Subject: " + subject + " has been added");
                        }
                        else{
                            denied.add(subject);
                            logger.log("Subject: " + subject + " was already subscribed to");
                        }
                    }

                    Message clientAssert = new Message();
                    clientAssert.setMsgType(SUBJECTS_UPDATED.toString());
                    clientAssert.setRequestNumber(requestNumber);
                    clientAssert.setName(name);
                    clientAssert.setSubjectsList(accepted);
                    String message = Parsing.parseMsgToString(clientAssert);

                    communication.sendMessage(message,
                            client.getIpAddress(),
                            client.getSocketNumber());

                    communication.sendMessage(message,
                            otherServerIp,
                            otherServerPort);


                    Message clientDeny = new Message();
                    clientDeny.setMsgType(SUBJECTS_REJECTED.toString());
                    clientDeny.setRequestNumber(requestNumber);
                    clientDeny.setName(name);
                    clientDeny.setSubjectsList(denied);
                    String denyMessage = Parsing.parseMsgToString(clientDeny);

                    communication.sendMessage(denyMessage,
                            client.getIpAddress(),
                            client.getSocketNumber());
                }
            }

        } else {
            //TODO ask for how to reach back client with wrong name
            logger.log("UPDATING SUBJECTS OF INTEREST", "User does not exist! And cannot be reached back.");
        }
    }

    @Override
    public void subjectsUpdated(int requestNumber, String name, List<String> subjectsList) {
        for (ClientModel client : clients
        ) {
            if (client.getName().equals(name)) {
                for (String subject : subjectsList
                ) {
                    if (client.addSubject(subject)) {
                        //TODO Update db
                        logger.log("Subject: " + subject + " has been added");
                    }
                }
            }
        }

    }

    @Override
    public void publish(int requestNumber, String name, String subject, String text) {
        if (clientNameExist(name)) {
            boolean subjectFound = false;
            boolean messageSent = false;

            for (ClientModel client : clients
            ) {
                if (client.getName().equals(name)) {
                    for (String sub: client.getSubjectsOfInterest()
                         ) {
                        if (sub.equals(subject)){
                            subjectFound = true;
                            for (ClientModel cli : clients
                                 ) {
                                for (String subj: cli.getSubjectsOfInterest()
                                     ) {
                                    if (subj.equals(subject)){
                                        messageSent = true;
                                        Message clientPublish = new Message();
                                        clientPublish.setMsgType(MESSAGE.toString());
                                        clientPublish.setName(name);
                                        clientPublish.setSubject(subject);
                                        clientPublish.setText(text);
                                        String message = Parsing.parseMsgToString(clientPublish);

                                        communication.sendMessage(message,
                                                cli.getIpAddress(),
                                                cli.getSocketNumber());

                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!subjectFound){
                for (ClientModel client : clients
                ) {
                    if (client.getName().equals(name)) {
                        Message clientDeny = new Message();
                        clientDeny.setMsgType(PUBLISH_DENIED.toString());
                        clientDeny.setRequestNumber(requestNumber);
                        clientDeny.setReason("Subject is not within the subjects of interests of user: " + name);
                        String message = Parsing.parseMsgToString(clientDeny);

                        communication.sendMessage(message,
                                client.getIpAddress(),
                                client.getSocketNumber());
                    }
                }
            }

            if (messageSent){
                logger.log("PUBLISHING MESSAGE", "Message has been Published !");
            }


        } else {
            //TODO ask for how to reach back client with wrong name
            logger.log("PUBLISHING MESSAGE", "User does not exist!");
        }
    }

    @Override
    public void switchServer() {
        startServing();
        isServing = true;
        logger.log("Server Serving", "Server Started Serving");
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

    private void updateServerInfo() {

        Message changeServer = new Message();
        changeServer.setMsgType(UPDATE_SERVER.toString());
        changeServer.setIpAddress(otherServerIp);
        changeServer.setSocketNumber(otherServerPort);
        String message = Parsing.parseMsgToString(changeServer);

        communication.sendMessage(message,otherServerIp,otherServerPort);
    }
}
