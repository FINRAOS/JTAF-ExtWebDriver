package org.finra.jtaf.ewd.properties;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class MissingGUIPropertyException extends RuntimeException {

	private static final long serialVersionUID = 6833953651082027353L;

    public MissingGUIPropertyException(String property, String guiPropFileName) {
        super("The property, " + property + ", was not found in GUIProperties file: " + guiPropFileName);
    }

    public MissingGUIPropertyException(List<String> properties, List<String> guiPropFileNames) {
        super("The following properties, '" + StringUtils.join(properties, ",") + 
        		"', were not found in any of the following GUIProperties files: '" + 
        			StringUtils.join(guiPropFileNames, ",") + "'");
    }
}
