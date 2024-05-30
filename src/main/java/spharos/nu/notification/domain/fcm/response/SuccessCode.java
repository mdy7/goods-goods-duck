package spharos.nu.notification.domain.fcm.response;

public enum SuccessCode {
    SELECT_SUCCESS(200, "Select operation successful");


    private final int status;
    private final String message;

    SuccessCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}