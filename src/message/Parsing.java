package message;

import logger.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static message.MsgType.*;

public class Parsing {

    private static Logger logger;

    public static String parseMsgToString(Message msg) {
        String parsedString = "";

        switch (msg.getMsgType()) {
            case REGISTER:
                parsedString = REGISTER + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getIpAddress().toString() + ";" + msg.getSocketNumber();
                break;
            case REGISTER_DENIED:
                parsedString = REGISTER_DENIED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getReason();
                break;
            case REGISTERED:
                parsedString = REGISTERED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
                break;
            case DE_REGISTER:
                parsedString = DE_REGISTER.toString() + ";" + msg.getRequestNumber() + ";" + msg.getName();
                break;
            case UPDATE:
                parsedString = UPDATE + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
                break;
            case UPDATE_CONFIRMED:
                parsedString = UPDATE_CONFIRMED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
                break;
            case UPDATE_DENIED:
                parsedString = UPDATE_DENIED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getReason();
                break;
            case SUBJECTS:
                parsedString = SUBJECTS + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getSubjectStringList();
                break;
            case SUBJECTS_UPDATED:
                parsedString = SUBJECTS_UPDATED.toString() + ";" + msg.getName() + ";" + msg.getSubjectStringList();
                break;
            case SUBJECTS_REJECTED:
                parsedString = SUBJECTS_REJECTED.toString() + ";" + msg.getName() + ";" + msg.getSubjectStringList();
                break;
            case PUBLISH:
                parsedString = PUBLISH + ";" + msg.getRequestNumber() + ";" + msg.getName() + ";" + msg.getSubject() + ";" + msg.getText();
                break;
            case PUBLISH_DENIED:
                parsedString = PUBLISH_DENIED.toString() + ";" + msg.getRequestNumber() + ";" + msg.getReason();
                break;
            case CHANGE_SERVER:
                parsedString = CHANGE_SERVER.toString() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
                break;
            case UPDATE_SERVER:
                parsedString = UPDATE_SERVER.toString() + ";" + msg.getIpAddress() + ";" + msg.getSocketNumber();
                break;
            case SWITCH_SERVER:
                parsedString = SWITCH_SERVER + ";";
                break;
            default:
                parsedString = "";
                break;
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
//            try {
            parsedMsg.setIpAddress(array1[3]);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//                logger.log("Exception Caught in Parsing String to message: " + e.toString());
//            }
            parsedMsg.setSocketNumber(Integer.parseInt(array1[4]));
        } else if (array1[0].equals(REGISTERED.toString()) && array1.length == 5) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
//            try {
            parsedMsg.setIpAddress(array1[3]);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//                logger.log("Exception Caught in Parsing String to message: " + e.toString());
//            }
            parsedMsg.setSocketNumber(Integer.parseInt(array1[4]));
        } else if (array1[0].equals(REGISTER_DENIED.toString()) && array1.length == 5) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
//            try {
            parsedMsg.setIpAddress(array1[3]);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//                logger.log("Exception Caught in Parsing String to message: " + e.toString());
//            }
            parsedMsg.setSocketNumber(Integer.parseInt(array1[4]));
        } else if (array1[0].equals(DE_REGISTER.toString()) && array1.length == 3) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
        } else if (array1[0].equals(UPDATE.toString()) && array1.length == 5) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
//            try {
            parsedMsg.setIpAddress(array1[3]);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//                logger.log("Exception Caught in Parsing String to message: " + e.toString());
//            }
            parsedMsg.setSocketNumber(Integer.parseInt(array1[4]));
        }
        else if (array1[0].equals(UPDATE_CONFIRMED) && array1.length == 5) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
//            try {
            parsedMsg.setIpAddress(array1[3]);
//            } catch (UnknownHostException e) {
//                e.printStackTrace();
//                logger.log("Exception Caught in Parsing String to message: " + e.toString());
//            }
            parsedMsg.setSocketNumber(Integer.parseInt(array1[4]));
        }

        else if (array1[0].equals(SUBJECTS.toString()) && array1.length == 4) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
            String initial = array1[3];
            initial = initial.replace("[", "");
            initial = initial.replace("]", "");
            List<String> subjectList = new LinkedList<String>(Arrays.asList(initial.split(",")));
            parsedMsg.setSubjectsList(subjectList);
        } else if (array1[0].equals(PUBLISH.toString()) && array1.length == 5) {
            parsedMsg.setRequestNumber(Integer.parseInt(array1[1]));
            parsedMsg.setName(array1[2]);
            parsedMsg.setSubject(array1[3]);
            parsedMsg.setText(array1[4]);
        } else if (array1[0].equals(MESSAGE.toString()) && array1.length == 4) {
            parsedMsg.setName(array1[1]);
            parsedMsg.setSubject(array1[2]);
            parsedMsg.setText(array1[3]);
        } else if (array1[0].equals(SWITCH_SERVER) && array1.length == 1) parsedMsg.setMsgType(SWITCH_SERVER);
        else if (array1[0].equals(CHANGE_SERVER) && array1.length == 3){
            parsedMsg.setIpAddress(array1[1]);
            parsedMsg.setSocketNumber(Integer.parseInt(array1[2]));
        }
        else if (array1[0].equals(UPDATE_SERVER) && array1.length == 3){
            parsedMsg.setIpAddress(array1[1]);
            parsedMsg.setSocketNumber(Integer.parseInt(array1[2]));
        }


        return parsedMsg;
    }
}
