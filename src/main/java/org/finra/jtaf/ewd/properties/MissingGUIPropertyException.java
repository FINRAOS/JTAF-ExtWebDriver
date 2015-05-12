package org.finra.jtaf.ewd.properties;

public class MissingGUIPropertyException extends RuntimeException {

	private static final long serialVersionUID = 6833953651082027353L;

    public MissingGUIPropertyException(String property, String guiPropFileName) {
        super("The property, " + property + ", was not found in GUIProperties file: " + guiPropFileName);
    }

}
