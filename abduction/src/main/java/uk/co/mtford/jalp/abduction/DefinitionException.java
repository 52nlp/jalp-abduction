package uk.co.mtford.jalp.abduction;

import uk.co.mtford.jalp.JALPException;

/**
 * Created with IntelliJ IDEA.
 * User: mtford
 * Date: 19/05/2012
 * Time: 13:53
 * To change this template use File | Settings | File Templates.
 */
public class DefinitionException extends JALPException {

    public DefinitionException() {
    }

    public DefinitionException(String s) {
        super(s);
    }

    public DefinitionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DefinitionException(Throwable throwable) {
        super(throwable);
    }
}
