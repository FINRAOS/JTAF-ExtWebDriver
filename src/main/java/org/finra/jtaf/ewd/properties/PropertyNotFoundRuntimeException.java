package org.finra.jtaf.ewd.properties;

public class PropertyNotFoundRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 6833953651082027353L;

    public PropertyNotFoundRuntimeException(String property, String guiPropFileName) {
        super("The property, " + property + ", was not found in " + guiPropFileName);
    }

}
