package logger;

public class Logger {

    public void log(String topic, String message) {
        System.out.println(topic + " - " + message);
    }

    public void log(String message) {
        System.out.println(message);
    }

    public void logBug(String message){
            System.out.println("BUG - " + message);
    }
}
