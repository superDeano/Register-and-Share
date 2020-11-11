package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CliClient {
    private Client client;
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public CliClient() {
        this.client = new Client();
        System.out.println("\nRegister And Share!");
//        System.out.println("Client");
    }

    public void run() {
        while (true) {
            try {
                mainMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayMenuOptions() {
        System.out.println("\n~~~~~~~~~Main Menu~~~~~~~~~");
        System.out.println("\nPlease choose one of the following options (Pick only the number)" +
                "\n1 - Register to server" +
                "\n2 - Deregister to server" +
                "\n3 - Update your information" +
                "\n4 - Update your list of subjects" +
                "\n5 - Publish a message to a topic you're subscribed" +
                "\n6 - Listen to Incoming messages");
    }


    private void mainMenu() throws IOException {
        displayMenuOptions();
        String ans = reader.readLine();
        switch (ans) {
            case "1":
                registerToServer();
                break;
            case "2":
                deregisterToServer();
                break;
            case "3":
                updateClientInformation();
                break;
            case "4":
                updateListOfSubjectsSubscribed();
                break;
            case "5":
                publishMessage();
                break;
            case "6":
                listenToMessages();
                break;
            default:
                break;
        }
    }


    private void updateClientInformation() throws IOException {
        System.out.println("\nUpdate your information");
        updateClientName();
        updateClientIpAddress();
        updateClientSocketNumber();
        this.client.updateInformationToServer();
    }

    private void updateClientName() throws IOException {
        System.out.println("Do you want to update your name? (yes/no)");
        String ans = reader.readLine();
        if(ans.contains("yes")){
            System.out.println("What is your new name?");
            this.client.setName(reader.readLine());
        }
    }

    private void updateClientIpAddress() throws IOException {
        System.out.println("Do you want to update your ipAddress? (yes/no)");
        String ans = reader.readLine();
        if(ans.contains("yes")){
            System.out.println("What is your new ipAddress?");
            this.client.setIpAddress(reader.readLine());
        }
    }

    private void updateClientSocketNumber() throws IOException {
        System.out.println("Do you want to update your port number? (yes/no)");
        String ans = reader.readLine();
        if(ans.contains("yes")){
            System.out.println("What is your new port number? ");
            this.client.setSocketNumber(Integer.parseInt(reader.readLine()));
        }
    }

    private void registerToServer() {
        System.out.println("\nRegistering to server");
        this.client.registerToServer();
    }

    private void updateListOfSubjectsSubscribed() {

    }

    private void deregisterToServer() {
        System.out.println("\nDeregistering to server");
        this.client.deregisterToServer();
    }

    private void registerToSubject() {
        System.out.println("\nRegister to Subject");
    }

    private void deregisterToSubject() {
        System.out.println("\nDeregister to Subject");
    }

    private void publishMessage() {
        System.out.println("\nPublish A Message");
    }

    private void listenToMessages(){


    }


}
