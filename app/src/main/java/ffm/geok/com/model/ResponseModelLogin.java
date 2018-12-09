package ffm.geok.com.model;

public class ResponseModelLogin {
    private LoginUser user;
    private String result;
    private String message;
    private String sessionid;
    private String __url;

    public LoginUser getUser() {
        return user;
    }

    public void setUser(LoginUser user) {
        this.user = user;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String get__url() {
        return __url;
    }

    public void set__url(String __url) {
        this.__url = __url;
    }
}
