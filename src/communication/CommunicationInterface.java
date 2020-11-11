package communication;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public interface CommunicationInterface {

    final int byteSize = 65535;

    /**
     * Method that when called waits for an incoming message and passes it to the caller as a string
     *
     * @return string
     */
    String waitForMessage ();

    /**
     * Method that when called sends the desired message to the desired location
     *
     * @param message string, desired message
     * @param ip InetAddress, desired ip address where to send the message
     * @param port int, desired port where to send the message
     */
    void sendMessage (String message , InetAddress ip , int port );

    /**
     * Getter for Connection Name
     *
     * @return string connectionName
     */
    String getConnectionName();

    /**
     * Getter for the Datagram Packet
     *
     * @return DatagramPacket DatagramPacket
     */
    DatagramPacket getDatagramPacket();

    /**
     * Getter for the Datagram Socket
     *
     * @return DatagramSocket DatagramSocket
     */
    DatagramSocket getDatagramSocket();

    /**
     * Getter for the ReceiveByte
     *
     * @return byte[] RecieverByte
     */
    byte[] getReceiveByte();

    /**
     * Getter for the ByteSize
     *
     * @return int ByteSize
     */
    int getByteSize();

    /**
     * Getter for the IpAddress
     *
     * @return String IpAddress
     */
    String getIpAddress ();

    /**
     * Getter for the PortNumber
     *
     * @return int PortNumber
     */
    int getPortNumber ();

    /**
     * Setter for the ConnectionName
     *
     * @param connectionName string
     */
    void setConnectionName(String connectionName);

    /**
     * Setter for the DatagramPacket
     *
     * @param datagramPacket DatagramPacket
     */
    void setDatagramPacket(DatagramPacket datagramPacket);

    /**
     * Setter for the DatagramSocket
     *
     * @param datagramSocket DatagramSocket
     */
    void setDatagramSocket(DatagramSocket datagramSocket);

    /**
     * Setter for the recieveByte
     *
     * @param receiveByte byte[]
     */
    void setReceiveByte(byte[] receiveByte);

}