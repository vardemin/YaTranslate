package vardemin.com.yatranslate.events;

public class ErrorMsg {
    private String errorMessage;

    public ErrorMsg(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
