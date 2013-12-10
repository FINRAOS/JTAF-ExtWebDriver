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

import org.finra.jtaf.ewd.timer.WaitForConditionTimer.ITimerCallback;
import org.finra.jtaf.ewd.timer.WidgetTimeoutException;
import org.finra.jtaf.ewd.widget.ICheckBox;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.WidgetRuntimeException;
import org.finra.jtaf.ewd.widget.element.InteractiveElement;
import org.openqa.selenium.WebElement;

/**
 * 
 *CheckBox widget implementation for checkbox elements
 *
 */
public class CheckBox extends InteractiveElement implements ICheckBox {
    public static final String UNCHECK = "uncheck";
    public static final String CHECK = "check";

    public static final String UNCHECKED = "unchecked";
    public static final String CHECKED = "checked";

    /**
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     */
    public CheckBox(String locator) {
        super(locator);
    }

    /**
     * 
     * @param check
     *            - boolean
     */
    private void doAction(boolean check) {
        try {
            WebElement e = getWebElement();
            if (isSelected(e) == check) {
                click();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the value of the CheckBox
     * 
     * @param value
     *            - String that can be one of two values: CHECK - sets checkbox
     *            from UNCHECKED to CHECK UNCHECK - sets checkbox from CHECKED
     *            to UNCHECKED
     */
    @Override
    public void setValue(Object value) throws WidgetException {
        boolean set = false;
        try {
            if (value instanceof String) {
                if (((String) value).equalsIgnoreCase(UNCHECK)) {
                    doAction(true);
                } else if (((String) value).equalsIgnoreCase(CHECK)) {
                    doAction(false);
                }

                set = true;
            } else {
                throw new WidgetRuntimeException(
                        "value must be a String of either 'check' or 'uncheck'", getLocator());
            }
        } catch (Exception e) {
            throw new WidgetException("Error while checking/unchecking", getLocator(), e);
        }

        if (!set)
            throw new WidgetException(
                    "Invalid set value for checkbox. It must be either 'check' or 'uncheck'",
                    getLocator());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IEditableField#getValue(java.lang.String)
     */
    @Override
    public String getValue() throws WidgetException {
        if (isChecked()) {
            return CHECKED;
        } else {
            return UNCHECKED;
        }
    }

    /**
     * 
     * @param e
     *            WebElement to check
     * @return boolean if attribute is selected
     * @throws Exception
     */
    private static boolean isSelected(WebElement e) throws Exception {
        try {
            return e.isSelected();
        } catch (Exception exception) {
            try {
                String attribute = e.getAttribute("value");
                return attribute != null && attribute.equalsIgnoreCase("on");
            } catch (Exception exception2) {
                throw exception;
            }
        }
    }

    /**
     * Checks to see if the current checkbox is checked
     * 
     * @return - true if the checkbox is checked - false if the checkbox is not
     *         checked.
     */
    public boolean isChecked() throws WidgetException {
        try {
            return isSelected(getWebElement());
        } catch (Exception e) {
            throw new WidgetException("Error while checking if checkbox was checked", getLocator(),
                    e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ICheckBox#check()
     */
    @Override
    public void check() throws WidgetException {
        setValue(CHECK);
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ICheckBox#uncheck()
     */
    @Override
    public void uncheck() throws WidgetException {
        setValue(UNCHECK);
    }

    /**
     * Waits for element at locator to be checked within period of
     * maxRequestTimeout
     * 
     * @throws WidgetException
     */
    public void waitForChecked() throws WidgetException {
        waitForChecked(getGUIDriver().getMaxRequestTimeout());
    }

    /**
     * Waits for element at locator to be checked within period of specified
     * time
     * 
     * @param time
     *            Milliseoncds
     * @throws WidgetTimeoutException
     */
    public void waitForChecked(final long time) throws WidgetException {
        super.waitForCommand(new ITimerCallback() {

            @Override
            public boolean execute() {
                WebElement elem = null;
                try {
                    elem = getWebElement();
                } catch (WidgetException e) {

                }

                boolean isSelected = false;
                try {
                    isSelected = isSelected(elem);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return (elem != null && isSelected);
            }

            @Override
            public String toString() {
                return "Waiting for element with the locator: " + getLocator() + "to be checked";
            }
        }, time);
    }

    /**
     * Waits for element at locator to be not checked within period of
     * maxRequestTimeout
     * 
     */
    public void waitForUnchecked() throws WidgetException {
        waitForUnchecked(getGUIDriver().getMaxRequestTimeout());
    }

    /**
     * Waits for element at locator to be not checked within period of specified
     * time
     * 
     * @param time
     *            Milliseoncds
     * @throws WidgetException
     */
    public void waitForUnchecked(final long time) throws WidgetException {
        super.waitForCommand(new ITimerCallback() {

            @Override
            public boolean execute() {
                WebElement elem = null;
                try {
                    elem = getWebElement();
                } catch (WidgetException e) {
                    throw new RuntimeException(e);
                }
                boolean isSelected = true;
                try {
                    isSelected = isSelected(elem);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return (elem != null && !isSelected);
            }

            @Override
            public String toString() {
                return "Waiting for element with the locator: " + getLocator() + "to be unchecked";
            }
        }, time);
    }

}
