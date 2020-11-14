package server;

import java.util.Vector;

public class ClientModel {
    private String name;
    private String ipAddress;
    private int socketNumber;
    private final Vector<String> subjectsOfInterest;

    public ClientModel(){
        this.subjectsOfInterest = new Vector<>();
    }

    public ClientModel(String name, String ipAddress, int socketNumber) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.socketNumber = socketNumber;
        this.subjectsOfInterest = new Vector<>();
    }

    public String getName() {
        return name;
    }

    public int getSocketNumber() {
        return socketNumber;
    }

    public void setSocketNumber(int socketNumber) {
        this.socketNumber = socketNumber;
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

    public boolean addSubject(String subject) {
        if (this.subjectAlreadySubscribed(subject)) return false;
        this.subjectsOfInterest.add(subject);
        return true;
    }

    public boolean removeSubject(String subject) {
        if (!this.subjectAlreadySubscribed(subject)) return false;
        this.subjectsOfInterest.remove(subject);
        return true;
    }

    private boolean subjectAlreadySubscribed(String subject) {
        for (String s : this.subjectsOfInterest) {
            if (s.equals(subject))
                return true;
        }
        return false;
    }
}
