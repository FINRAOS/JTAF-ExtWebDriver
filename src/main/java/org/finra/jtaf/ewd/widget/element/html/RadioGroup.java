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

import org.finra.jtaf.ewd.widget.IRadioGroup;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.InteractiveElement;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.List;

/**
 * HTML Radio Input element
 * 
 */
public class RadioGroup extends InteractiveElement implements IRadioGroup {

    public RadioGroup(String locator) {
        super(locator);
    }

    public RadioGroup(By locator) {
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
                boolean selected = false;
                List<WebElement> elements = findElements();
                for (WebElement we : elements) {
                    if (we.getAttribute("value").equals(value)) {
                        we.click();
                        selected = true;
                        break;
                    }
                }

                if (!selected)
                    throw new WidgetException("Could not find desired option to select",
                            getByLocator());
            } else {
                throw new WidgetException("Invalid type. 'value' must be a 'String' type of desired option to select",
                        getByLocator());
            }
        } catch (Exception e) {
            throw new WidgetException("Error while selecting option on radio group", getByLocator(),
                    e);
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
        List<WebElement> elements = findElements();
        for (WebElement we : elements) {
            if (we.getAttribute("checked") != null
                    && we.getAttribute("checked").equalsIgnoreCase("true")) {
                return we.getAttribute("value");
            }
        }

        throw new WidgetException("Error while finding selected option on radio group",
                getByLocator());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IRadioGroup#select(java.lang.String)
     */
    @Override
    public void select(String value) throws WidgetException {
        setValue(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IRadioGroup#getSelectedValue()
     */
    @Override
    public String getSelectedValue() throws WidgetException {
        return getValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IRadioGroup#getValues()
     */
    @Override
    public List<String> getValues() throws WidgetException {
        List<String> options = new ArrayList<String>();
        List<WebElement> elements = findElements();
        for (WebElement we : elements) {
            options.add(we.getAttribute("value"));
        }
        return options;
    }

    /**
     * 
     * @return list of web elements found based on all locators
     * @throws WidgetException
     */
    private List<WebElement> findElements() throws WidgetException {
        By locator = getByLocator();
        getGUIDriver().selectLastFrame();
        WebDriver wd = getGUIDriver().getWrappedDriver();

        List<WebElement> webElements = wd.findElements(locator);
        if (webElements != null && webElements.size() > 0) {
            for (WebElement we : webElements) {
                //TODO this looks wrong
                highlight( HIGHLIGHT_MODES.FIND);
            }
            return webElements;
        }
        throw new NoSuchElementException("Could not find elements matching " + locator);
    }
}
