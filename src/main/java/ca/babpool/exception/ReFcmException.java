package ca.babpool.exception;

import com.google.firebase.messaging.MessagingErrorCode;

public class ReFcmException extends RuntimeException {

    private MessagingErrorCode status;

    public ReFcmException(MessagingErrorCode status) {
        super(String.valueOf(status));
        this.status = status;
    }

    public MessagingErrorCode getStatus() {
        return status;
    }
}
