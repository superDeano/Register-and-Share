package server;

import client.RssClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener {
    private static JFrame frame;
    private static JPanel serverPanel, logsPanel;
    private static JButton updateClientPortNumberButton, clearAllLogsButton;
    private static JLabel actualServerIpAddressLabel;
    private static JComboBox<String> serverNameComboBox;
    private static JTextField otherServerIpAddressTF, currentServerPortNumberTF, otherServerPortNumberTF;
    private static DefaultListModel<String> logs;
    private static String[] serverNames = {"", "A", "B"};
    private static JTabbedPane tabbedPane;

    public static void main(String[] args) {
        // write your code here
        instantiateGraphicalComponents();
    }

    private static void instantiateGraphicalComponents() {
        //Frame
        frame = new JFrame("Register And Share");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        setServerPanel();
        setLogsPanel();
        setTabbedPane();

        frame.getContentPane().add(tabbedPane);
        frame.setVisible(true);
    }

    private static void setServerPanel() {
        //Server Panel
        int textLabelWidth = 150;
        serverPanel = new JPanel(new BorderLayout());
        serverPanel.setLayout(null);

        //TextFields
        actualServerIpAddressLabel = new JLabel();
        otherServerIpAddressTF = new JTextField(1);
        currentServerPortNumberTF = new JTextField(1);
        otherServerIpAddressTF = new JTextField(1);
        otherServerPortNumberTF = new JTextField(1);
        JLabel currentServerLabel = new JLabel("Current Server");
        currentServerLabel.setBounds(10, 0, textLabelWidth,20);
        serverPanel.add(currentServerLabel);

        JLabel currentServerNameLabel = new JLabel("Server");
        currentServerNameLabel.setBounds(10, 30, textLabelWidth, 25);
        serverPanel.add(currentServerNameLabel);

        serverNameComboBox = new JComboBox<>(serverNames);
        serverNameComboBox.setBounds(textLabelWidth+ 20, 30, textLabelWidth, 25);
        serverPanel.add(serverNameComboBox);

        JLabel currentServerIpAddressLabel = new JLabel("IP Address");
        currentServerIpAddressLabel.setBounds(10, 60, textLabelWidth, 20);
        serverPanel.add(currentServerIpAddressLabel);

        actualServerIpAddressLabel.setBounds(textLabelWidth + 20, 60, textLabelWidth + 50, 20);
        serverPanel.add(actualServerIpAddressLabel);

        JLabel currentServerPortNumberLabel = new JLabel("Port Number");
        currentServerPortNumberLabel.setBounds(10, 90, textLabelWidth, 20);
        serverPanel.add(currentServerPortNumberLabel);

        currentServerPortNumberTF.setBounds(textLabelWidth + 20, 90, textLabelWidth + 50, 20);
        serverPanel.add(currentServerPortNumberTF);

        JButton setCurrentPortNumberButton = new JButton("Set Port");
        setCurrentPortNumberButton.setBounds(10, 120, 140, 20);
        serverPanel.add(setCurrentPortNumberButton);

        JButton startServerButton = new JButton("Start Server");
        startServerButton.setBounds(160, 120, 140, 20);
        serverPanel.add(startServerButton);

        JLabel otherServerLabel = new JLabel("Other Server");
        otherServerLabel.setBounds(10, 180, textLabelWidth + 20, 20);
        serverPanel.add(otherServerLabel);
        JLabel otherServerIpAddressLabel = new JLabel("IP Address");
        otherServerIpAddressLabel.setBounds(10, 210, textLabelWidth, 20);
        serverPanel.add(otherServerIpAddressLabel);

        otherServerIpAddressTF.setBounds(textLabelWidth + 20, 210, textLabelWidth + 50, 20);
        serverPanel.add(otherServerIpAddressTF);

        JLabel otherServerPnLabel = new JLabel("Port Number");
        otherServerPnLabel.setBounds(10, 240, textLabelWidth, 20);
        serverPanel.add(otherServerPnLabel);

        otherServerPortNumberTF.setBounds(20 + textLabelWidth, 240, textLabelWidth + 50, 20);
        serverPanel.add(otherServerPortNumberTF);

        JButton saveServersInfoButton = new JButton("Save Server");
        saveServersInfoButton.setBounds(10, 270, 140, 20);
        saveServersInfoButton.addActionListener(new RssClient());
        serverPanel.add(saveServersInfoButton);

    }

    private static void setLogsPanel() {
        //Logs Panel
        logs = new DefaultListModel<>();
        logsPanel = new JPanel();
        logsPanel.setLayout(null);
        clearAllLogsButton = new JButton("Clear");
        clearAllLogsButton.addActionListener(new RssClient());
        clearAllLogsButton.setBounds(10, 1, 80, 20);
        logsPanel.add(clearAllLogsButton);
        JList<String> jList = new JList<>(logs);
        JScrollPane scrollPane = new JScrollPane(jList);
        scrollPane.setBounds(1, 30, frame.getWidth() - 30, frame.getHeight() - 120);
        logsPanel.add(scrollPane);
    }

    private static void setTabbedPane() {
        //Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.add("Server", serverPanel);
        tabbedPane.add("Event Logs", logsPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
