package communication;

import logger.Logger;
import message.Message;
import message.Parsing;

import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Communication implements CommunicationInterface {
    private int portNumber = 2313;
    private String connectionName;
    private DatagramSocket serverDatagramSocket;
    private byte[] receiveByte;
    private DatagramPacket receivedDatagramPacket;
    private Logger logger;
    private final int MIN_PORT_NUMBER = 2000;
    private final int MAX_PORT_NUMBER = 10000;

    /**
     * Default constructor for a communication object
     *
     * @param connectionName basic naming attribute
     */
    public Communication(String connectionName) {
        try {
            while (!portIsAvailable(portNumber)) ++portNumber;
            this.serverDatagramSocket = new DatagramSocket(portNumber, InetAddress.getByName(getIpV4Address()));
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            logger.log("Exception Caught in Communication Constructor: " + e.toString());
        }
        this.connectionName = connectionName;
        logger = new Logger();
    }

    /**
     * Custom constructor with a specific port
     *
     * @param port           specified desired port
     * @param connectionName basic naming attribute
     */
    public Communication(int port, String connectionName) {
        this.portNumber = port;
        try {
            this.serverDatagramSocket = new DatagramSocket(portNumber, InetAddress.getByName(getIpV4Address()));
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            logger.log("Exception Caught in Communication Constructor: " + e.toString());
        }
        logger = new Logger();
        this.connectionName = connectionName;
    }

    /**
     * Method that when called waits for an incoming message and passes it to the caller as a string
     *
     * @return string
     */
    @Override
    public void waitForMessage(ConcurrentLinkedQueue<Message> messages, DefaultListModel<String> logs) {
        try {

            receiveByte = new byte[byteSize];
            while (true) {
                receivedDatagramPacket = new DatagramPacket(receiveByte, receiveByte.length);
                serverDatagramSocket.receive(receivedDatagramPacket);
                String m = toStringBuilder(receiveByte).toString();
                logger.log("received", m);
                logs.addElement(m);
                Message message = Parsing.parseStringToMsg(m);
                attachSenderIpAndPortNumberToMessage(message, receivedDatagramPacket);
                messages.add(message);
                receiveByte = new byte[byteSize];
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.log("Exception Caught in Communication wait for message: " + e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
    }

    private void attachSenderIpAndPortNumberToMessage(Message message, DatagramPacket datagramPacket) {

            message.setSenderIpAddress(datagramPacket.getAddress().getHostAddress());
            message.setSenderSocketNumber(datagramPacket.getPort());
    }


    @Override
    public void waitForMessage(DefaultListModel<String> messages) {

        receiveByte = new byte[byteSize];
        while (true) {
            try {
                receivedDatagramPacket = new DatagramPacket(receiveByte, receiveByte.length);
                serverDatagramSocket.receive(receivedDatagramPacket);
                String m = toStringBuilder(receiveByte).toString();
                logger.log("received", m);
                messages.addElement(m);
                receiveByte = new byte[byteSize];
            } catch (IOException e) {
                e.printStackTrace();
                logger.log("Exception Caught in Communication wait for message: " + e.toString());
            }
        }
    }

    /**
     * Method that when called sends the desired message to the desired location
     *
     * @param message string, desired message
     * @param ip      InetAddress, desired ip address where to send the message
     * @param port    int, desired port where to send the message
     */
    @Override
    public void sendMessage(String message, InetAddress ip, int port) {
        byte stringBuffer[] = message.getBytes();
        DatagramPacket messagePacket = new DatagramPacket(stringBuffer, stringBuffer.length, ip, port);
        try {
            serverDatagramSocket.send(messagePacket);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log("Exception Caught in Message Sender: " + e.toString());
        }
    }

    @Override
    public void sendMessage(String message, String ipAddress, int port) {
        try {
            InetAddress ip = InetAddress.getByName(ipAddress);
            sendMessage(message, ip, port);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log("Exception Caught in Message Sender: " + e.toString());
        }
    }

    /**
     * Method converting a byte array to a string
     *
     * @param message byte[] message
     * @return string message
     */
    public static StringBuilder toStringBuilder(byte[] message) {
        if (message == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (message[i] != 0) {
            ret.append((char) message[i]);
            i++;
        }
        return ret;
    }

    /**
     * Getter for Connection Name
     *
     * @return string connectionName
     */
    @Override
    public String getConnectionName() {
        return connectionName;
    }

    /**
     * Getter for the Datagram Packet
     *
     * @return DatagramPacket DatagramPacket
     */
    @Override
    public DatagramPacket getDatagramPacket() {
        return receivedDatagramPacket;
    }

    /**
     * Getter for the Datagram Socket
     *
     * @return DatagramSocket DatagramSocket
     */
    @Override
    public DatagramSocket getDatagramSocket() {
        return serverDatagramSocket;
    }

    /**
     * Getter for the ReceiveByte
     *
     * @return byte[] RecieverByte
     */
    @Override
    public byte[] getReceiveByte() {
        return receiveByte;
    }

    /**
     * Getter for the ByteSize
     *
     * @return int ByteSize
     */
    @Override
    public int getByteSize() {
        return byteSize;
    }

    /**
     * Getter for the IpAddress
     *
     * @return String IpAddress
     */
    @Override
    public String getIpAddress() {
        return serverDatagramSocket.getLocalAddress() != null ? removeFirstCharInIpAddress(serverDatagramSocket.getLocalAddress().toString()) : "Get LocalAddress is Null";
    }

    private String removeFirstCharInIpAddress(String ip) {
        return ip.split("/")[1];
    }

    /**
     * Getter for the PortNumber
     *
     * @return int PortNumber
     */
    @Override
    public int getPortNumber() {
        return (serverDatagramSocket.getLocalPort());
    }

    /**
     * Setter for the ConnectionName
     *
     * @param connectionName string
     */
    @Override
    public void setConnectionName(String connectionName) {
        this.connectionName = connectionName;
    }

    /**
     * Setter for the DatagramPacket
     *
     * @param datagramPacket DatagramPacket
     */
    @Override
    public void setDatagramPacket(DatagramPacket datagramPacket) {
        this.receivedDatagramPacket = datagramPacket;
    }

    /**
     * Setter for the DatagramSocket
     *
     * @param datagramSocket DatagramSocket
     */
    @Override
    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.serverDatagramSocket = datagramSocket;
    }

    /**
     * Setter for the recieveByte
     *
     * @param receiveByte byte[]
     */
    @Override
    public void setReceiveByte(byte[] receiveByte) {
        this.receiveByte = receiveByte;
    }

    /**
     * Setter for the port of the datagram socket
     *
     * @param port int
     */
    @Override
    public boolean setPort(int port) {
        try {
            if (!portIsAvailable(port)) return false;
            this.serverDatagramSocket.close();
            this.serverDatagramSocket = new DatagramSocket(port, InetAddress.getLocalHost());

        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            logger.log("Exception Caught in Communication Port Setter: " + e.toString());
            return false;
        }
        return true;
    }

    public boolean portIsValid(int port) {
        return (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER);
    }

    public boolean portIsAvailable(int port) {
        if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                }
            }
        }

        return false;
    }

    private String getIpV4Address() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // *EDIT*
                    if (addr instanceof Inet6Address) continue;

                    ip = addr.getHostAddress();
                    System.out.println(iface.getDisplayName() + " " + ip);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return ip;
    }

}
