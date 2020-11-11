package server;

public interface ServerInterface {
    //Server starts serving clients
    public void startServing();

    //Server goes to sleep, wakes up the other
    public void stopServing();

    public void registerUser(String requestNumber, String name, String ipAddress, String socketNumber);

    public void deregisterUser(String requestNumber, String name);

    public void updateUserInformation(String requestNumber, String name, String ipAddress, String socketNumber);

    public void updateSubjectOfInterestToUser(String requestNumber, String name, String listOfSubjects);

    public void updateUserPublishedMessage(String requestNumber, String name, String subject, String text);
}
