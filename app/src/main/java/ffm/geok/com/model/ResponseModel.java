package ffm.geok.com.model;

public class ResponseModel {

    /**
     * status : true
     * message :
     * data :
     */

    private boolean status;
    private String message;
    private String data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
