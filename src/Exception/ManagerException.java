package Exception;

import java.io.IOException;

public class ManagerException extends IOException {

    public ManagerException(final Throwable cause){
        super(cause);
    }

    public ManagerException(final String message) {
        super(message);
    }

}
