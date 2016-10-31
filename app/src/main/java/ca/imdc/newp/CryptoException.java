package ca.imdc.newp;

/**
 * Created by imdc on 18/08/2016.
 */
public class CryptoException extends Exception {
    public CryptoException() {
    }

    public CryptoException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
