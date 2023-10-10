package ca.babpool.exception;

import com.google.firebase.messaging.MessagingErrorCode;

public class FcmException extends RuntimeException {

    private MessagingErrorCode status;

    public FcmException(MessagingErrorCode status) {
        super(String.valueOf(status));
        this.status = status;
    }

    public MessagingErrorCode getStatus() {
        return status;
    }
}
