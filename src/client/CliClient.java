package client;

public class CliClient {
    private Client client;

    public CliClient(){
        this.client = new Client();
        System.out.println("\nRegister And Share!");
        System.out.println("\nClient");
    }

    public void run(){
        while (true){

        }
    }

    public void updateClientInformation(){
        System.out.println("\nUpdate your information");
    }

    public void registerToServer(){
        System.out.println("\nRegistering to server");
    }

    public void deregisterToServer(){
        System.out.println("\nDeregistering to server");
    }

    public void registerToSubject(){
        System.out.println("\nRegister to Subject");
    }

    public void deregisterToSubject(){
        System.out.println("\nDeregister to Subject");
    }

    public void publishMessage(){
        System.out.println("\nPublish A Message");
    }
}
