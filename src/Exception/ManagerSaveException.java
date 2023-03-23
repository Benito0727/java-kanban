package Exception;

import java.io.IOException;

public class ManagerSaveException extends IOException {

    public ManagerSaveException(final Throwable cause){
        super(cause);
    }

    public ManagerSaveException(final String message){
        super(message);
    }
}
