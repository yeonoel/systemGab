package kernel.tech.systemgab.utils.exception;

import org.aspectj.bridge.IMessage;

public class CompteIntrouvableException extends RuntimeException {
    public CompteIntrouvableException(String message) {
        super(message);
    }

}
