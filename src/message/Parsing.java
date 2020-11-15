package message;

import static message.msgType.*;

public class Parsing implements ParsingInterface {

    @Override
    public Message parseStringToMsg(String msg) {
        String array1[] = msg.split(",");

        Message parsedMsg = new Message();

        if (array1[0].equals(REGISTER.toString())) {
            parsedMsg.setRegister(array1[0]);
            parsedMsg.setRQ(array1[1]);
            parsedMsg.setName(array1[2]);
            parsedMsg.setIPaddress(array1[3]);
            parsedMsg.setSocket(array1[4]);
        }

        if (array1[0].equals(REGISTERED.toString()) && array1.length == 5) {
            parsedMsg.setRegister(array1[0]);
            parsedMsg.setRQ(array1[1]);
            parsedMsg.setName(array1[2]);
            parsedMsg.setIPaddress(array1[3]);
            parsedMsg.setSocket(array1[4]);
        }

        if (array1[0].equals(REGISTER_DENIED.toString()) && array1.length == 5) {
            parsedMsg.setRegister(array1[0]);
            parsedMsg.setRQ(array1[1]);
            parsedMsg.setName(array1[2]);
            parsedMsg.setIPaddress(array1[3]);
            parsedMsg.setSocket(array1[4]);
        }

        if (array1[0].equals(DE_REGISTER.toString()) && array1.length == 3) {
            parsedMsg.setRegister(array1[0]);
            parsedMsg.setRQ(array1[1]);
            parsedMsg.setName(array1[2]);
        }

        if (array1[0].equals(UPDATE.toString()) && array1.length == 5) {
            parsedMsg.setRegister(array1[0]);
            parsedMsg.setRQ(array1[1]);
            parsedMsg.setName(array1[2]);
            parsedMsg.setIPaddress(array1[3]);
            parsedMsg.setSocket(array1[4]);
        }

        if (array1[0].equals(SUBJECTS.toString()) && array1.length == 4) {
            parsedMsg.setRegister(array1[0]);
            parsedMsg.setRQ(array1[1]);
            parsedMsg.setName(array1[2]);
            parsedMsg.setList_of_subjects(array1[3]);
        }

        if (array1[0].equals(PUBLISH.toString()) && array1.length == 5) {
            parsedMsg.setRegister(array1[0]);
            parsedMsg.setRQ(array1[1]);
            parsedMsg.setName(array1[2]);
            parsedMsg.setSubject(array1[3]);
            parsedMsg.setText(array1[4]);
        }

        if (array1[0].equals(MESSAGE.toString()) && array1.length == 4) {
            parsedMsg.setRegister(array1[0]);
            parsedMsg.setName(array1[1]);
            parsedMsg.setSubject(array1[2]);
            parsedMsg.setText(array1[3]);
        }

        return parsedMsg;
    }

    @Override
    public String parseMsgToString(Message msg) {
        String parsedString = "";

        if (msg.getRegister() == REGISTERED.toString() && msg.getName() == "") {
            parsedString = REGISTERED.toString() + "," + msg.getRQ();
        }
        if (msg.getRegister() == REGISTER_DENIED.toString() && msg.getName() == "") {
            parsedString = REGISTER_DENIED.toString() + "," + msg.getRQ() + "," + msg.getReason();
        }

        if (msg.getRegister() == REGISTERED.toString() && msg.getName() != "") {
            parsedString = REGISTERED.toString() + "," + msg.getRQ() + "," + msg.getName() + "," + msg.getIPaddress() + "," + msg.getSocket();
        }

        if (msg.getRegister() == REGISTER_DENIED.toString() && msg.getName() != "") {
            parsedString = REGISTER_DENIED.toString() + "," + msg.getRQ() + "," + msg.getName() + "," + msg.getIPaddress() + "," + msg.getSocket();
        }

        if (msg.getRegister() == DE_REGISTER.toString()) {
            parsedString = DE_REGISTER.toString() + "," + msg.getName();
        }

        if (msg.getRegister() == UPDATE_CONFIRMED.toString()) {
            parsedString = UPDATE_CONFIRMED.toString() + "," + msg.getRQ() + "," + msg.getName() + "," + msg.getIPaddress() + "," + msg.getSocket();
        }
        if (msg.getRegister() == UPDATE_DENIED.toString()) {
            parsedString = UPDATE_DENIED.toString() + "," + msg.getRQ() + "," + msg.getReason();
        }
        if (msg.getRegister() == SUBJECTS_UPDATED.toString()) {
            parsedString = SUBJECTS_UPDATED.toString() + "," + msg.getName() + "," + msg.getList_of_subjects();
        }
        if (msg.getRegister() == SUBJECTS_REJECTED.toString()) {
            parsedString = SUBJECTS_REJECTED.toString() + "," + msg.getName() + "," + msg.getList_of_subjects();
        }
        if (msg.getRegister() == PUBLISH_DENIED.toString()) {
            parsedString = PUBLISH_DENIED.toString() + "," + msg.getRQ() + "," + msg.getReason();
        }
        if (msg.getRegister().equals(CHANGE_SERVER.toString())) {
            parsedString = CHANGE_SERVER.toString() + "," + msg.getIPaddress() + "," + msg.getSocket();
        }
        if (msg.getRegister().equals(UPDATE_SERVER.toString())) {
            parsedString = UPDATE_SERVER.toString() + "," + msg.getIPaddress() + "," + msg.getSocket();
        }
        return parsedString;
    }
}
