package client;

import javax.swing.*;
import java.awt.*;
//import java.awt.*;

public class RssClient {
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

        setClientPanel(c);
        setServerPanel(c);
        setTopicPanel(c);
        setMessagePanel(c);
        setLogsPanel(c);

        setTabbedPane();

        frame.getContentPane().add(tabbedPane);
        frame.setVisible(true);
    }

    private static void setClientPanel(GridBagConstraints c) {
        //Client Panel
        clientPanel = new JPanel(new BorderLayout());
        clientPanel.setLayout(new GridBagLayout());
        updateClientPortNumberButton = new JButton("Update Port Number");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        clientPanel.add(new JLabel("IP Address"), c);
        c.gridy = 1;
        clientPanel.add(clientIpAddress, c);
        c.gridy = 2;
        clientPanel.add(new JLabel("Client Port Number"), c);
        c.gridy = 4;
        clientPanel.add(clientPortNumber, c);
        c.gridy = 5;
        clientPanel.add(updateClientPortNumberButton, c);
    }

    private static void setServerPanel(GridBagConstraints c) {
        //Server Panel
        serverPanel = new JPanel(new BorderLayout());
        serverPanel.setLayout(new GridBagLayout());
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        serverPanel.add(new JLabel("Server 1 IP Address"), c);
        c.gridy = 1;
        c.gridwidth = 2;
        serverPanel.add(server1IpAddress, c);
        c.gridy = 2;
        c.gridwidth = 1;
        serverPanel.add(new JLabel("Server 1 Port Number"), c);
        c.gridy = 3;
        c.gridwidth = 2;
        serverPanel.add(server1PortNumber, c);
        c.gridy = 4;
        c.gridwidth = 1;
        serverPanel.add(new JLabel("Server 2 IP Address"), c);
        c.gridy = 5;
        c.gridwidth = 2;
        serverPanel.add(server2IpAddress, c);
        c.gridy = 6;
        c.gridwidth = 1;
        serverPanel.add(new JLabel("Server 2 Port Number"), c);
        c.gridy = 7;
        c.gridwidth = 2;
        serverPanel.add(server2PortNumber, c);
        c.gridy = 9;
        c.gridwidth = 1;
        c.gridx = 0;
        serverPanel.add(registerButton, c);
        c.gridy = 9;
        c.gridx= 2;
        serverPanel.add(deregisterButton, c);
    }


    private static void setTopicPanel(GridBagConstraints c) {
        //Topic Panel
        topicPanel = new JPanel();
        topicPanel.add(new JLabel("Topics you're subscribed"));
    }


    private static void setMessagePanel(GridBagConstraints c) {
        //Messages Panel
        messagePannel = new JPanel();
        messagePannel.add(publishButton);
    }

    private static void setLogsPanel(GridBagConstraints c) {
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
}
