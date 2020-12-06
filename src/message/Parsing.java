package message;

import logger.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static message.MsgType.*;

public class Parsing {

    private static Logger logger;

    public static String parseMsgToString(Message msg) {
        String parsedString = "";


        if (msg.getMsgType().equals(REGISTERED.toString()) && Objects.equals(msg.getName(), "")) {
            parsedString = REGISTERED.toString() + ";" + msg.getRequestNumber();
        } else if (msg.getMsgType().equals(REGISTER_DENIED.toString()) && Objects.equals(msg.getName(), "")) {
            parsedString = REGISTER_DENIED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getReason();
        } else if (msg.getMsgType().equals(REGISTERED.toString()) && !Objects.equals(msg.getName(), "")) {
            parsedString = REGISTERED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
        } else if (msg.getMsgType().equals(REGISTER_DENIED.toString()) && !Objects.equals(msg.getName(), "")) {
            parsedString = REGISTER_DENIED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
        } else if (msg.getMsgType().equals(DE_REGISTER.toString())) {
            parsedString = DE_REGISTER.toString() + ";" + msg.getName();
        } else if (msg.getMsgType().equals(UPDATE_CONFIRMED.toString())) {
            parsedString = UPDATE_CONFIRMED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
        } else if (msg.getMsgType().equals(UPDATE_DENIED.toString())) {
            parsedString = UPDATE_DENIED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getReason();
        } else if (msg.getMsgType().equals(SUBJECTS_UPDATED.toString())) {
            parsedString = SUBJECTS_UPDATED.toString() + ";" + msg.getName() + ";" + msg.getSubjectStringList();
        } else if (msg.getMsgType().equals(SUBJECTS_REJECTED.toString())) {
            parsedString = SUBJECTS_REJECTED.toString() + ";" + msg.getName() + ";" + msg.getSubjectStringList();
        } else if (msg.getMsgType().equals(PUBLISH_DENIED.toString())) {
            parsedString = PUBLISH_DENIED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getReason();
        } else if (msg.getMsgType().equals(CHANGE_SERVER.toString())) {
            parsedString = CHANGE_SERVER.toString() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
        } else if (msg.getMsgType().equals(UPDATE_SERVER.toString())) {
            parsedString = UPDATE_SERVER.toString() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
        } else if (msg.getMsgType().equals(SWITCH_SERVER.toString())) {
            parsedString = SWITCH_SERVER;
        } else if (msg.getMsgType().equals(PUBLISH.toString())) {
            parsedString = PUBLISH + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getSubject() + ";" + msg.getText();
        } else if (msg.getMsgType().equals(UPDATE.toString())) {
            parsedString = UPDATE + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
        } else if (msg.getMsgType().equals(SUBJECTS.toString())) {
            parsedString = SUBJECTS + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getSubjectStringList();
        }
        return parsedString;
    }

    /**
     * @param msg method that when called, the string message will be parsed and returns an object of message
     * @return
     */
    public static Message parseStringToMsg(String msg) {
        String array1[] = msg.split(";");

        Message parsedMsg = new Message();
        parsedMsg.setMsgType(array1[0]);


        if (array1[0].equals(REGISTER.toString())) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
            try {
                parsedMsg.setIpAddress(InetAddress.getByName(array1[3]));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                logger.log("Exception Caught in Parsing String to message: " + e.toString());
            }
            parsedMsg.setSocketNumber(Integer.parseInt(array1[4]));
        }

        if (array1[0].equals(REGISTERED.toString()) && array1.length == 5) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
            try {
                parsedMsg.setIpAddress(InetAddress.getByName(array1[3]));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                logger.log("Exception Caught in Parsing String to message: " + e.toString());
            }
            parsedMsg.setSocketNumber(Integer.parseInt(array1[4]));
        }

        if (array1[0].equals(REGISTER_DENIED.toString()) && array1.length == 5) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
            try {
                parsedMsg.setIpAddress(InetAddress.getByName(array1[3]));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                logger.log("Exception Caught in Parsing String to message: " + e.toString());
            }
            parsedMsg.setSocketNumber(Integer.parseInt(array1[4]));
        }

        if (array1[0].equals(DE_REGISTER.toString()) && array1.length == 3) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
        }

        if (array1[0].equals(UPDATE.toString()) && array1.length == 5) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
            try {
                parsedMsg.setIpAddress(InetAddress.getByName(array1[3]));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                logger.log("Exception Caught in Parsing String to message: " + e.toString());
            }
            parsedMsg.setSocketNumber(Integer.parseInt(array1[4]));
        }

        if (array1[0].equals(SUBJECTS.toString()) && array1.length == 4) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
            String initial = array1[3];
            initial = initial.replace("[", "");
            initial = initial.replace("]", "");
            List<String> subjectList = new LinkedList<String>(Arrays.asList(initial.split(",")));
            parsedMsg.setSubjectsList(subjectList);
        }

        if (array1[0].equals(PUBLISH.toString()) && array1.length == 5) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
            parsedMsg.setSubject(array1[3]);
            parsedMsg.setText(array1[4]);
        }

        if (array1[0].equals(MESSAGE.toString()) && array1.length == 4) {
            parsedMsg.setName(array1[1]);
            parsedMsg.setSubject(array1[2]);
            parsedMsg.setText(array1[3]);
        }

        return parsedMsg;
    }
}
