package org.lazydevs.pixelwallet.api.gog;

/**
 * Created with IntelliJ IDEA.
 * User: sasch_000
 * Date: 02.02.13
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class GogApiException extends RuntimeException {

    public GogApiException() {
    }

    public GogApiException(String message) {
        super(message);
    }

    public GogApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public GogApiException(Throwable cause) {
        super(cause);
    }

    public GogApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
