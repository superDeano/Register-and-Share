package server;

import java.net.InetAddress;
import java.util.List;

public interface ServerInterface {
    //Server starts serving clients
    public void startServing();

    //Server goes to sleep, wakes up the other
    public void stopServing();

    public void register(int requestNumber, String name, InetAddress ipAddress, int socketNumber);

    public void registered( int requestNumber, String name, InetAddress ipAddress, int socket);

    public void deRegister(int requestNumber, String name);

    public void update(int requestNumber, String name, InetAddress ipAddress, int socketNumber);

    public void updateConfirmed(int requestNumber, String name, InetAddress ipAddress, int socketNumber);

    public void subjects(int requestNumber, String name, List<String> listOfSubjects);

    public void subjectsUpdated(int requestNumber, String name, List<String> subjectsList);

    public void publish(int requestNumber, String name, String subject, String text);

    public void switchServer();

}
