package Exception;

import java.io.IOException;

public class ManagerLoadException extends IOException {

    public ManagerLoadException(final Throwable cause){
        super(cause);
    }

    public ManagerLoadException(final String message) {
        super(message);
    }

}
