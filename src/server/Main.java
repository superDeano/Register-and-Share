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
    private static JTextField otherServerIpAddressTF, currentServerPortNumberTF, otherServerPortNumberTF;
    private static DefaultListModel<String> logs;
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
//        clientPortNumberTF = new JTextField();
        otherServerIpAddressTF = new JTextField(1);
        currentServerPortNumberTF = new JTextField(1);
        otherServerIpAddressTF = new JTextField(1);
        otherServerPortNumberTF = new JTextField(1);

        JLabel currentServerIpAddressLabel = new JLabel("Current Server IP Address");
        currentServerIpAddressLabel.setBounds(10, 10, textLabelWidth, 20);
        serverPanel.add(currentServerIpAddressLabel);

        actualServerIpAddressLabel.setBounds(textLabelWidth + 20, 10, textLabelWidth + 50, 20);
        serverPanel.add(actualServerIpAddressLabel);

        JLabel currentServerPnLabel = new JLabel("Current Server Port Number");
        currentServerPnLabel.setBounds(10, 40, textLabelWidth, 20);
        serverPanel.add(currentServerPnLabel);

        currentServerPortNumberTF.setBounds(textLabelWidth + 20, 40, textLabelWidth + 50, 20);
        serverPanel.add(currentServerPortNumberTF);

        JLabel otherServerIpAddressLabel = new JLabel("Other Server IP Address");
        otherServerIpAddressLabel.setBounds(10, 70, textLabelWidth, 20);
        serverPanel.add(otherServerIpAddressLabel);

        otherServerIpAddressTF.setBounds(textLabelWidth + 20, 70, textLabelWidth + 50, 20);
        serverPanel.add(otherServerIpAddressTF);

        JLabel otherServerPnLabel = new JLabel("Other Server Port Number");
        otherServerPnLabel.setBounds(10, 100, textLabelWidth, 20);
        serverPanel.add(otherServerPnLabel);

        otherServerPortNumberTF.setBounds(20 + textLabelWidth, 100, textLabelWidth + 50, 20);
        serverPanel.add(otherServerPortNumberTF);

        JButton saveServersInfoButton = new JButton("Save Server");
        saveServersInfoButton.setBounds(10, 140, 200, 20);
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
