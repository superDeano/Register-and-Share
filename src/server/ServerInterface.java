package server;

import message.Message;

import java.util.List;

public interface ServerInterface {
    //Server starts serving clients
    public void startServing();

    //Server goes to sleep, wakes up the other
    public void stopServing();

    public void register(int requestNumber, String name, String ipAddress, int socketNumber);

    public void registered( int requestNumber, String name, String ipAddress, int socket);

    public void deRegister(Message message);

    public void update(int requestNumber, String name, String ipAddress, int socketNumber);

    public void updateConfirmed(int requestNumber, String name, String ipAddress, int socketNumber);

    public void subjects(Message message);

    public void subjectsUpdated(int requestNumber, String name, List<String> subjectsList);

    public void publish(Message message);

    public void switchServer();

}
