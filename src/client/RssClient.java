package client;

import message.Message;
import message.MsgType;
import server.ServerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static message.MsgType.*;
//import java.awt.*;

public class RssClient implements ActionListener {
    private static JFrame frame;
    private static JPanel clientPanel, serverPanel, topicPanel, messagePannel, logsPanel;
    private static JButton registerButton, deregisterButton, publishButton, updateClientPortNumberButton, updateClientInfoButton, clearAllLogsButton;
    private static JLabel actualClientIpAddressLabel, actualClientPortNumberLabel;
    protected static JTextField server1IpAddressTF;
    protected static JTextField server2IpAddressTF;
    protected static JTextField server1PortNumberTF;
    protected static JTextField server2PortNumberTF;
    private static JTextField clientNameTF;
    private static JTextArea publishMessageTA, topicsSendingLabel;
    //    private static JRadioButton subscribeRadioButton, unsubscribeRadioButton;
    private static JCheckBox[] topicCheckBoxes;
    private static JTabbedPane tabbedPane;
    private static JComboBox<String> topicsComboBox;
    private static Client client;
    private static DefaultListModel<String> logs;
    private static ConcurrentLinkedQueue<Message> messages;
    private static boolean confirmedWithServer = false;
    private static String[] topics = {"Education", "Politics", "Pop", "Technology", "Science", "Sports", "World"};

    public static void main(String[] args) {
        // write your code here
//        CliClient cliClient = new CliClient();
//        cliClient.run();
        instantiateGraphicalComponents();
        startClient();

    }

    private static void startClient() {
        client = new Client();
//        String ip = client.getClientIpAddress();
        actualClientIpAddressLabel.setText(client.getClientIpAddress());
        actualClientPortNumberLabel.setText(client.getClientPortNumber());
        //client.setServersInfoTF(server1IpAddressTF, server1PortNumberTF, server2IpAddressTF, server2PortNumberTF);
        messages = new ConcurrentLinkedQueue<>();
        startListening();
        takeAction();
    }

    private static void instantiateGraphicalComponents() {
        //Frame
        frame = new JFrame("Register And Share");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        //Buttons
        registerButton = new JButton("Register");
        deregisterButton = new JButton("Deregister");
        publishButton = new JButton("Publish");

        //TextFields
        actualClientIpAddressLabel = new JLabel();
//        clientPortNumberLabel = new JTextField();
        server1IpAddressTF = new JTextField(1);
        server1PortNumberTF = new JTextField(1);
        server2IpAddressTF = new JTextField(1);
        server2PortNumberTF = new JTextField(1);

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

        JLabel clientIpAddressLabel = new JLabel("IP Address");
        clientIpAddressLabel.setBounds(10, 10, 200, 20);
        clientPanel.add(clientIpAddressLabel);

        // Label that shows the client IP Address
        actualClientIpAddressLabel.setBounds(160, 10, 300, 20);
        clientPanel.add(actualClientIpAddressLabel);

        JLabel clientPortNumberLabel = new JLabel("Port Number");
        clientPortNumberLabel.setBounds(10, 40, 150, 20);
        clientPanel.add(clientPortNumberLabel);

        // Text Field for client Port Number
        actualClientPortNumberLabel = new JLabel();
        actualClientPortNumberLabel.setBounds(160, 40, 50, 20);
        clientPanel.add(actualClientPortNumberLabel);

        JLabel clientNameLabel = new JLabel("Name");
        clientNameLabel.setBounds(10, 70, 150, 20);
        clientPanel.add(clientNameLabel);

        clientNameTF = new JTextField();
        clientNameTF.setBounds(160, 70, 150, 20);
        clientPanel.add(clientNameTF);

        JButton saveNameButton = new JButton("Save Name");
        saveNameButton.setBounds(10, 110, 150, 20);
        saveNameButton.addActionListener(new RssClient());
        clientPanel.add(saveNameButton);

        updateClientPortNumberButton = new JButton("Update Information");
        updateClientPortNumberButton.setBounds(180, 110, 150, 20);
        updateClientPortNumberButton.addActionListener(new RssClient());
        clientPanel.add(updateClientPortNumberButton);


//        updateClientInfoButton = new JButton("Save Number");
    }

    private static void setServerPanel() {
        //Server Panel
        int textLabelWidth = 150;
        serverPanel = new JPanel(new BorderLayout());
        serverPanel.setLayout(null);
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

        registerButton.setBounds(10, 220, 100, 20);
        registerButton.addActionListener(new RssClient());
        serverPanel.add(registerButton);

        deregisterButton.setBounds(120, 220, 100, 20);
        deregisterButton.addActionListener(new RssClient());
        serverPanel.add(deregisterButton);
    }


    private static void setTopicPanel() {
        //Topic Panel
        topicPanel = new JPanel();
        topicPanel.setLayout(null);
        topicPanel.add(new JLabel("Topics you're subscribed"));
        topicCheckBoxes = new JCheckBox[topics.length];
        JButton checkAllButton = new JButton("Check All");
        checkAllButton.setBounds(10, 10, 100, 20);
        checkAllButton.addActionListener(new RssClient());
        topicPanel.add(checkAllButton);
        JButton unCheckAllButton = new JButton("Uncheck All");
        unCheckAllButton.setBounds(120, 10, 100, 20);
        unCheckAllButton.addActionListener(new RssClient());
        topicPanel.add(unCheckAllButton);

        for (int i = 0; i < topics.length; i++) {
            topicCheckBoxes[i] = new JCheckBox(topics[i]);
            topicCheckBoxes[i].setBounds(10, (i * 20) + 50, 150, 20);
            topicCheckBoxes[i].addActionListener(new RssClient());
            topicPanel.add(topicCheckBoxes[i]);
        }

        topicsSendingLabel = new JTextArea();
        topicsSendingLabel.setBounds(10, 280, 350, 40);
        topicsSendingLabel.setWrapStyleWord(true);
        topicsSendingLabel.setLineWrap(true);
        topicsSendingLabel.setOpaque(false);
        topicsSendingLabel.setEditable(false);
        topicsSendingLabel.setFocusable(false);
        topicPanel.add(topicsSendingLabel);

        JButton sendTopicButton = new JButton("Send Topic");
        sendTopicButton.setBounds(350, 280, 120, 20);
        sendTopicButton.addActionListener(new RssClient());
        topicPanel.add(sendTopicButton);
    }


    private static void setMessagePanel() {
        //Messages Panel
        messagePannel = new JPanel();
        messagePannel.setLayout(null);
        JLabel selectTopicLabel = new JLabel("Select a Topic");
        selectTopicLabel.setBounds(10, 5, 100, 25);
        messagePannel.add(selectTopicLabel);

        topicsComboBox = new JComboBox<>(topics);
        topicsComboBox.setBounds(110, 5, frame.getWidth() - 140, 25);
        messagePannel.add(topicsComboBox);

        publishMessageTA = new JTextArea();
        publishMessageTA.setBounds(10, 45, frame.getWidth() - 40, frame.getHeight() - 160);
        publishMessageTA.setWrapStyleWord(true);
        publishMessageTA.setLineWrap(true);
        publishMessageTA.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.blue), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        messagePannel.add(publishMessageTA);

        publishButton.setBounds(frame.getWidth() - 120, frame.getHeight() - 100, 80, 20);
        publishButton.addActionListener(new RssClient());
        messagePannel.add(publishButton);
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
        MyCellRenderer cellRenderer = new MyCellRenderer(350);
        jList.setCellRenderer(cellRenderer);
//        jList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//        jList.setVisibleRowCount(-1);
//
//        jList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        JScrollPane scrollPane = new JScrollPane(jList);
        scrollPane.setBounds(1, 30, frame.getWidth() - 30, frame.getHeight() - 120);
        logsPanel.add(scrollPane);
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


    /**
     * Starts a separate thread that listens for incoming messages
     * Then updates the list of Logs
     */
    private static void startListening() {
        Thread listeningThread = new Thread() {
            public void run() {
                client.listen(messages, logs);
            }
        };
        listeningThread.start();
    }

    private static void takeAction() {
        Thread takeActionThread = new Thread() {
            public void run() {
                while (true) {
                    if (messages.isEmpty()) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Message msg = messages.poll();
                        takeAction(msg);
                    }
                }
            }
        };
        takeActionThread.run();
    }

    private static void takeAction(Message m) {
        switch (m.getMsgType()) {
            case PUBLISH_DENIED:
                client.serverReplied(m);
                break;
            case MsgType.UPDATE_DENIED:
            case MsgType.REGISTER_DENIED:
                handleRegisterAndUpdate(m, false);
                break;
            case MsgType.UPDATE_CONFIRMED:
            case MsgType.REGISTERED:
                handleRegisterAndUpdate(m, true);
                break;
            case CHANGE_SERVER:
                client.changeServer(m);
                break;
            case SWITCH_SERVER:
                client.switchServer();
                break;
            default:
                test();
                break;
        }
    }


    private static void test() {

    }

    private static void handleRegisterAndUpdate(Message message, boolean confirmed) {
        confirmedWithServer = confirmed;
        client.serverReplied(message);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "Update Information":
                updateClientInfo();
                break;
            case "Register":
                registerClient();
                break;
            case "Deregister":
                deregisterClient();
                break;
            case "Save Servers":
                saveServersInfo();
                break;
            case "Clear":
                clearAllLogs();
                break;
            case "Save Name":
                saveName();
                break;
            case "Check All": {
                checkAllTopicBoxes(true);
                setTopicMessage();
            }
            break;
            case "Uncheck All": {
                checkAllTopicBoxes(false);
                setTopicMessage();
            }
            break;
            case "Subscribe":
            case "Unsubscribe":
            case "Education":
            case "Politics":
            case "Pop":
            case "Technology":
            case "Science":
            case "Sports":
            case "World": {
                setTopicMessage();
            }
            break;
            case "Send Topic": {
                sendTopics();
            }
            break;
            case "Publish":
                publishMessage();
                break;
            default:
                System.out.println("Something else happened!");
                break;
        }
    }

    private void setTopicMessage() {
        String message;
//        if (subscribeRadioButton.isSelected()) {
        message = "New List: " + getSelectedTopicAsString();
//        } else {
        //Unsubscribe
//            message = "UNSUBSCRIBE TO " + getSelectedTopicAsString();
//        }
        topicsSendingLabel.setText(message);
    }

    private String getSelectedTopicAsString() {
        StringBuilder topic = new StringBuilder();
        Arrays.stream(topicCheckBoxes).filter(t -> t.isSelected()).forEach(t -> topic.append(t.getActionCommand()).append(", "));
        return topic.toString();
    }

    private void saveServersInfo() {
        if (server1IpAddressTF.getText().equals("") || server1PortNumberTF.getText().equals("") || server2IpAddressTF.getText().equals("") || server2PortNumberTF.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Servers information missing.\nNeed to enter server information", "Updating User Information", JOptionPane.WARNING_MESSAGE);
        } else {
            ServerModel[] servers = client.getServers();
            servers[0].setIpAddress(server1IpAddressTF.getText());
            servers[0].setSocketNumber(Integer.parseInt(server1PortNumberTF.getText()));
            servers[1].setIpAddress(server2IpAddressTF.getText());
            servers[1].setSocketNumber(Integer.parseInt(server2PortNumberTF.getText()));
            logs.addElement("Servers Info saved!");
        }
    }

    private void publishMessage() {
        if (!confirmedWithServer) {
            JOptionPane.showMessageDialog(frame, "Not confirmed with server.\nNeed to update your information", "Publish Message", JOptionPane.WARNING_MESSAGE);
        } else if (publishMessageTA.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Cannot send blank texts", "Publishing Message", JOptionPane.WARNING_MESSAGE);
        } else {
            client.publishMessage(topics[topicsComboBox.getSelectedIndex()], publishMessageTA.getText());
            publishMessageTA.setText("");
        }
    }

    private void checkAllTopicBoxes(boolean enabled) {
        Arrays.stream(topicCheckBoxes).forEach(checkBox -> {
            checkBox.setSelected(enabled);
        });
    }

    private void clearAllLogs() {
        logs.clear();
    }


    private void sendTopics() {
        if (!confirmedWithServer) {
            JOptionPane.showMessageDialog(frame, "Not confirmed with server.\nNeed to update your information", "Updating List of Subjects", JOptionPane.WARNING_MESSAGE);
        } else {
            List<String> selectedTopics = new ArrayList<>();
            Arrays.stream(topicCheckBoxes).filter(t -> t.isSelected()).forEach(t -> selectedTopics.add(t.getActionCommand()));
//        if (subscribeRadioButton.isSelected())
            client.updateSubjectsOfInterest(selectedTopics);
//        else client.deregisterToSubjectOfInterest(selectedTopics);
        }
    }

    private void saveName() {
        String name = clientNameTF.getText();
        if (name.equals("")) {
            JOptionPane.showMessageDialog(frame, "Name cannot be blank!", "Saving", JOptionPane.WARNING_MESSAGE);
        } else {
            client.setName(name);
        }
    }

    private void updateClientInfo() {
        if (server1IpAddressTF.getText().equals("") || server1PortNumberTF.getText().equals("") || server2IpAddressTF.getText().equals("") || server2PortNumberTF.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Servers information missing.\nNeed to enter server information", "Updating User Information", JOptionPane.WARNING_MESSAGE);
        } else if (clientNameTF.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Need to enter a proper name", "Client Name", JOptionPane.WARNING_MESSAGE);
        } else {
//            client.setSocketNumber(Integer.parseInt(actualClientPortNumberLabel.getText()));
            client.setName(clientNameTF.getText());
            client.updateInformationToServer();
        }
    }

    private void registerClient() {
        if (server1IpAddressTF.getText().equals("") || server1PortNumberTF.getText().equals("") || server2IpAddressTF.getText().equals("") || server2PortNumberTF.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Servers information missing.\nNeed to enter server information", "Registering", JOptionPane.WARNING_MESSAGE);
        } else if (client.getName() == null || client.getName().equals("")) {
            JOptionPane.showMessageDialog(frame, "Name cannot be blank!", "Registering", JOptionPane.WARNING_MESSAGE);
        } else {
            client.registerToServer();
            System.out.println("Button to register client pressed");
//            logs.addElement("Button to register client pressed");
        }
    }

    private void deregisterClient() {
        if (server1IpAddressTF.getText().equals("") || server1PortNumberTF.getText().equals("") || server2IpAddressTF.getText().equals("") || server2PortNumberTF.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Server information missing.\nNeed to enter server information", "Deregistering", JOptionPane.WARNING_MESSAGE);
        } else if (client.getName().equals("") || client.getName() == null) {
            JOptionPane.showMessageDialog(frame, "Name cannot be blank!", "Deregistering", JOptionPane.WARNING_MESSAGE);
        } else {
            client.deregisterToServer();
            confirmedWithServer = false;
        }
    }

    public static class MyCellRenderer extends DefaultListCellRenderer {
        public static final String HTML_1 = "<html><body style='width: ";
        public static final String HTML_2 = "px'>";
        public static final String HTML_3 = "</html>";
        private int width;

        public MyCellRenderer(int width) {
            this.width = width;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            String text = HTML_1 + String.valueOf(width) + HTML_2 + value.toString()
                    + HTML_3;
            return super.getListCellRendererComponent(list, text, index, isSelected,
                    cellHasFocus);
        }
    }
}
