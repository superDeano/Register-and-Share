package communication;

import logger.Logger;

import java.io.IOException;
import java.net.*;

public class Communication implements CommunicationInterface {

    private String connectionName;
    private DatagramSocket serverDatagramSocket;
    private byte[] receiveByte;
    private DatagramPacket receivedDatagramPacket;
    private Logger logger;

    /**
     * Default constructor for a communication object
     *
     * @param connectionName basic naming attribute
     */
    public Communication(String connectionName) {
        try {
            this.serverDatagramSocket = new DatagramSocket(2313, InetAddress.getLocalHost());
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            logger.log("Exception Caught in Communication Constructor: " + e.toString());
        }
        this.connectionName = connectionName;
        logger = new Logger();
        this.receiveByte = new byte[byteSize];
        this.receivedDatagramPacket = new DatagramPacket(receiveByte, receiveByte.length);
    }

    /**
     * Custom constructor with a specific port
     *
     * @param port           specified desired port
     * @param connectionName basic naming attribute
     */
    public Communication(int port, String connectionName) {
        try {
            this.serverDatagramSocket = new DatagramSocket(port, InetAddress.getLocalHost());
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            logger.log("Exception Caught in Communication Constructor: " + e.toString());
        }
        logger = new Logger();
        this.connectionName = connectionName;
        this.receiveByte = new byte[byteSize];
        this.receivedDatagramPacket = new DatagramPacket(receiveByte, receiveByte.length);
    }

    /**
     * Method that when called waits for an incoming message and passes it to the caller as a string
     *
     * @return string
     */
    @Override
    public String waitForMessage() {
        while (true) {
            try {

                serverDatagramSocket.receive(receivedDatagramPacket);
                receiveByte = new byte[byteSize];
                String m = toStringBuilder(receiveByte).toString();
                logger.log("received", m);
                return m;
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
        return serverDatagramSocket.getLocalSocketAddress().toString();
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
    public void setPort(int port) {
        try {
            this.serverDatagramSocket = new DatagramSocket(port, InetAddress.getLocalHost());
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            logger.log("Exception Caught in Communication Port Setter: " + e.toString());
        }
    }

}
