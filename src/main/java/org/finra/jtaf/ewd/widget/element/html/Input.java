/*
 * (C) Copyright 2013 Java Test Automation Framework Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.finra.jtaf.ewd.widget.element.html;

import org.finra.jtaf.ewd.widget.ITextField;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.WidgetRuntimeException;
import org.finra.jtaf.ewd.widget.element.InteractiveElement;
import org.openqa.selenium.By;

/**
 * HTML Text Input element
 * 
 */
public class Input extends InteractiveElement implements ITextField {

    public Input(String locator) {
        super(locator);
    }

    /**
     * @param locator
     */
    public Input(By locator) {
        super(locator);
    }



    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IEditableField#setValue(java.lang.String)
     */
    @Override
    public void setValue(Object value) throws WidgetException {
        try {
            if (value instanceof String) {
                type((String) value);
            } else {
                throw new WidgetRuntimeException("Invalid type. 'value' must be a 'String' type", getByLocator());
            }

        } catch (Exception e) {
            throw new WidgetException("Error while typing " + value, getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IEditableField#getValue(java.lang.String)
     */
    @Override
    public String getValue() throws WidgetException {
        try {
            return getAttribute("value");
        } catch (Exception e) {
            throw new WidgetException("Error while getting text value", getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ITextField#typeAppend()
     */
    @Override
    public void typeAppend(String text) throws WidgetException {
        try {
            super.typeAppend(text);
        } catch (Exception e) {
            throw new WidgetException("Error while type appending " + text, getByLocator(), e);
        }
    }

}
