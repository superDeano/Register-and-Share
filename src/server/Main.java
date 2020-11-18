package server;

import client.RssClient;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static JFrame frame;
    private static JPanel serverPanel, logsPanel;
    private static JButton updateClientPortNumberButton, updateClientInfoButton, clearAllLogsButton;
    private static JLabel actualClientIpAddressLabel;
    private static JTextField server1IpAddressTF, server2IpAddressTF, server1PortNumberTF, server2PortNumberTF, clientPortNumberTF, clientNameTF;
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
        actualClientIpAddressLabel = new JLabel();
        clientPortNumberTF = new JTextField();
        server1IpAddressTF = new JTextField(1);
        server1PortNumberTF = new JTextField(1);
        server2IpAddressTF = new JTextField(1);
        server2PortNumberTF = new JTextField(1);

        JLabel server1IpAddressLabel = new JLabel("Server 1 IP Address");
        server1IpAddressLabel.setBounds(10, 10, textLabelWidth, 20);
        serverPanel.add(server1IpAddressLabel);

        server1IpAddressTF.setBounds(textLabelWidth + 20, 10, textLabelWidth + 50, 20);
        serverPanel.add(server1IpAddressTF);

        JLabel server1PnLabel = new JLabel("Server 1 Port Number");
        server1PnLabel.setBounds(10, 40, textLabelWidth, 20);
        serverPanel.add(server1PnLabel);

        server1PortNumberTF.setBounds(textLabelWidth + 20, 40, textLabelWidth + 50, 20);
        serverPanel.add(server1PortNumberTF);

        JLabel server2IpAddressLabel = new JLabel("Server 2 IP Address");
        server2IpAddressLabel.setBounds(10, 70, textLabelWidth, 20);
        serverPanel.add(server2IpAddressLabel);

        server2IpAddressTF.setBounds(textLabelWidth + 20, 70, textLabelWidth + 50, 20);
        serverPanel.add(server2IpAddressTF);

        JLabel server2PnLabel = new JLabel("Server 2 Port Number");
        server2PnLabel.setBounds(10, 100, textLabelWidth, 20);
        serverPanel.add(server2PnLabel);

        server2PortNumberTF.setBounds(20 + textLabelWidth, 100, textLabelWidth + 50, 20);
        serverPanel.add(server2PortNumberTF);

        JButton saveServersInfoButton = new JButton("Save Servers");
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
}
