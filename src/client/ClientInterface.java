package client;

import java.util.List;

public interface ClientInterface {

    public void registerToServer();

    public void deregisterToServer();

    public void updateSubjectsOfInterest(List<String> subjectOfInterest);

    public void updateInformationToServer();

    public void publishMessage(String topic, String message);

}
