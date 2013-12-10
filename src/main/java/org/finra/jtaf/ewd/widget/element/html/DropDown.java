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

/**
 * HTML Select element representing drop down
 * 
 */
import java.util.List;

import org.finra.jtaf.ewd.widget.IDropDown;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.WidgetRuntimeException;
import org.finra.jtaf.ewd.widget.element.InteractiveElement;

public class DropDown extends InteractiveElement implements IDropDown {

    public DropDown(String locator) {
        super(locator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IEditableElement#setValue(java.lang.String
     * )
     */
    @Override
    public void setValue(Object value) throws WidgetException {
        try {
            if (value instanceof String) {
                Select select = new Select(getLocator());
                select.selectOption((String) value);
            } else {
                throw new WidgetRuntimeException("value must be a String type", getLocator());
            }
        } catch (Exception e) {
            throw new WidgetException("Error while seleting option on drop down", getLocator(), e);
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
            return getSelectedOption();
        } catch (Exception e) {
            throw new WidgetException("Error while getting dropdown value", getLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IDropDown#selectOption(java.lang.String)
     */
    @Override
    public void selectOption(String option) throws WidgetException {
        setValue(option);
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IDropDown#getOptions()
     */
    @Override
    public List<String> getOptions() throws WidgetException {
        try {
            Select select = new Select(getLocator());
            return select.getOptions();
        } catch (Exception e) {
            throw new WidgetException("Error while fetching drop down options", getLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IDropDown#getSelectedOption()
     */
    @Override
    public String getSelectedOption() throws WidgetException {
        try {
            Select select = new Select(getLocator());
            List<String> selectedOptions = select.getSelectedOptions();
            if (selectedOptions != null && selectedOptions.size() > 0) {
                return selectedOptions.get(0);
            } else {
                return "";
            }
        } catch (Exception e) {
            throw new WidgetException("Error while fetching selected options", getLocator(), e);
        }
    }

}
