package client;

public interface ClientInterface {

    public void registerToServer();

    public void deregisterToServer();

    public void registerToSubjectOfInterest(String subjectOfInterest);

    public void deregisterToSubjectOfInterest(String subjectOfInterest);

    public void updateInformationToServer(String name, String ipAddress, int socketNumber);

    public void publishMessage(String topic, String message);

}
