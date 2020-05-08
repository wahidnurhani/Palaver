package de.unidue.palaver.system.retrofit;

class SendMessageServerResponseData {

    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "SendMessageServerResponseData{" +
                "dateTime='" + dateTime + '\'' +
                '}';
    }
}
