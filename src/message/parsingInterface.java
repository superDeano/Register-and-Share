package message;

public interface parsingInterface {


    /**
     *
     * @param msg
     * method that when called, the string message will be parsed and returns an object of message
     * @return
     */
    message parseStringToMsg(String msg);


    /**
     * method that when called, the message object will be parsed and will returns a string
     * @param msg
     * @return
     */
    String parseMsgToString(message msg);
}
