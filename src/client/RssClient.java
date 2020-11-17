package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.*;

public class RssClient implements ActionListener {
    private static JFrame frame;
    private static JPanel clientPanel, serverPanel, topicPanel, messagePannel, logsPanel;
    private static JButton registerButton, deregisterButton, publishButton, updateClientPortNumberButton;
    private static JLabel clientIpAddress;
    private static JTextField server1IpAddress, server2IpAddress, server1PortNumber, server2PortNumber, clientPortNumber;
    private static JTabbedPane tabbedPane;

    public static void main(String[] args) {
        // write your code here
        CliClient cliClient = new CliClient();
//        cliClient.run();
        instantiateGraphicalComponents();
    }

    public static void instantiateGraphicalComponents() {
        //Frame
        frame = new JFrame("Register And Share");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
//        frame.setLayout(null);
        //Buttons
        registerButton = new JButton("Register");
        deregisterButton = new JButton("Deregister");
        publishButton = new JButton("Publish");

        //TextFields
        Dimension textFieldDimension = new Dimension(150, 2);
        clientIpAddress = new JLabel();
        clientPortNumber = new JTextField();
        server1IpAddress = new JTextField(1);
        server1PortNumber = new JTextField(1);
        server2IpAddress = new JTextField(1);
        server2PortNumber = new JTextField(1);
        GridBagConstraints c = new GridBagConstraints();

        setClientPanel();
        setServerPanel();
        setTopicPanel();
        setMessagePanel();
        setLogsPanel();

        setTabbedPane();

        frame.getContentPane().add(tabbedPane);
        frame.setVisible(true);
    }

    private static void setClientPanel() {
        //Client Panel
        clientPanel = new JPanel();
        clientPanel.setLayout(null);


        JLabel clientIp = new JLabel("IP Address");
        clientIp.setBounds(10, 10, 100, 20);
        clientPanel.add(clientIp);

        // Label that shows the client IP Address
        clientIpAddress.setBounds(160, 10, 100, 20);
        clientPanel.add(clientIpAddress);

        JLabel clientPn = new JLabel("Client Port Number");
        clientPn.setBounds(10, 40, 150, 20);
        clientPanel.add(clientPn);

        // Text Field for client Port Number
        clientPortNumber.setBounds(160, 40, 50, 20);
        clientPanel.add(clientPortNumber);

        updateClientPortNumberButton = new JButton("Update Port Number");
        updateClientPortNumberButton.setBounds(10, 75, 150, 20);
        clientPanel.add(updateClientPortNumberButton);
    }

    private static void setServerPanel() {
        //Server Panel
        int textLabelWidth = 150;
        serverPanel = new JPanel(new BorderLayout());
        serverPanel.setLayout(null);
        JLabel server1IpAddressLabel = new JLabel("Server 1 IP Address");
        server1IpAddressLabel.setBounds(10, 10, textLabelWidth, 20);
        serverPanel.add(server1IpAddressLabel);

        server1IpAddress.setBounds(textLabelWidth + 20, 10, textLabelWidth + 50, 20);
        serverPanel.add(server1IpAddress);

        JLabel server1PnLabel = new JLabel("Server 1 Port Number");
        server1PnLabel.setBounds(10, 40, textLabelWidth, 20);
        serverPanel.add(server1PnLabel);

        server1PortNumber.setBounds(textLabelWidth + 20, 40, textLabelWidth + 50, 20);
        serverPanel.add(server1PortNumber);

        JLabel server2IpAddressLabel = new JLabel("Server 2 IP Address");
        server2IpAddressLabel.setBounds(10, 70, textLabelWidth, 20);
        serverPanel.add(server2IpAddressLabel);

        server2IpAddress.setBounds(textLabelWidth + 20, 70, textLabelWidth + 50, 20);
        serverPanel.add(server2IpAddress);

        JLabel server2PnLabel = new JLabel("Server 2 Port Number");
        server2PnLabel.setBounds(10, 100, textLabelWidth, 20);
        serverPanel.add(server2PnLabel);

        server2PortNumber.setBounds(20 + textLabelWidth, 100, textLabelWidth + 50, 20);
        serverPanel.add(server2PortNumber);

        registerButton.setBounds(10, 160, 100, 20);
        serverPanel.add(registerButton);

        deregisterButton.setBounds(120, 160, 100, 20);
        serverPanel.add(deregisterButton);
    }


    private static void setTopicPanel() {
        //Topic Panel
        topicPanel = new JPanel();
        topicPanel.add(new JLabel("Topics you're subscribed"));
    }


    private static void setMessagePanel() {
        //Messages Panel
        messagePannel = new JPanel();
        messagePannel.add(publishButton);
    }

    private static void setLogsPanel() {
        //Logs Panel
        logsPanel = new JPanel();
    }

    private static void setTabbedPane() {
        //Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.add("Client Info", clientPanel);
        tabbedPane.add("Server", serverPanel);
        tabbedPane.add("Topics", topicPanel);
        tabbedPane.add("Message", messagePannel);
        tabbedPane.add("Event Logs", logsPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Something happened");
    }
}
