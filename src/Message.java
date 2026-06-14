public class Message {
    private String sender;
    private String receiver;
    private String encryptedText;
    private long timestamp;

    public Message(String sender, String receiver, String encryptedText, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.encryptedText = encryptedText;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getEncryptedText() {
        return encryptedText;
    }

    public void setEncryptedText(String encryptedText) {
        this.encryptedText = encryptedText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}