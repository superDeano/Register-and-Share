package message;

import java.util.LinkedList;
import java.util.List;

public class Message {

    String msgType;
    int requestNumber;
    String name;
    String ipAddress;
    int socketNumber;
    String reason;
    List <String> subjectsList;
    String subject;
    String text;


    public Message() {
        this.msgType = "";
        this.requestNumber = -1;
        this.name = "";
        this.socketNumber = -1;
        this.reason = "";
        this.subjectsList = new LinkedList<String>();
        this.subject = "";
        this.text = "";
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getSocketNumber() {
        return socketNumber;
    }

    public void setSocketNumber(int socketNumber) {
        this.socketNumber = socketNumber;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(List<String> subjectsList) {
        this.subjectsList = subjectsList;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubjectStringList(){
        return subjectsList.toString();
    }
}
