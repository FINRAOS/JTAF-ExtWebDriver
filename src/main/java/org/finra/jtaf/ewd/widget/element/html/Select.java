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

import java.util.ArrayList;
import java.util.List;

import org.finra.jtaf.ewd.widget.ISelectList;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.WidgetRuntimeException;
import org.finra.jtaf.ewd.widget.element.InteractiveElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * HTML Select element
 */
public class Select extends InteractiveElement implements ISelectList {

    public Select(String locator) {
        super(locator);
    }

    
    public Select(By locator) {
        super(locator);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IEditableField#setValue(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setValue(Object value) throws WidgetException {
        try {
            if (value instanceof String) {
                selectSingle((String) value);
            } else if (value instanceof List<?>) {
                selectMultiple((List<String>) value);
            } else {
                throw new WidgetRuntimeException(
                        "Invalid type. 'value' must be either 'String' type of 'List<String>'.", getByLocator());
            }
        } catch (Exception e) {
            throw new WidgetException("Error while selecting option on select", getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.ISelectList#selectOption(java.lang.String)
     */
    @Override
    public void selectOption(String option) throws WidgetException {
        setValue(option);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.ISelectList#selectMultipleOptions(java
     * .util.List)
     */
    @Override
    public void selectMultipleOptions(List<String> options) throws WidgetException {
        try {
            setValue(options);
        } catch (Exception e) {
            throw new WidgetException("Error while selecting multiple options", getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IEditableField#getValue(java.lang.String)
     */
    @Override
    public Object getValue() throws WidgetException {
        List<String> selectedOptions = null;
        try {
            selectedOptions = getSelectedItems_internal();

        } catch (Exception e) {
            throw new WidgetException("Error while fetching selected value on select",
                    getByLocator(), e);
        }

        if (selectedOptions.size() == 0) {
            return null;
        } else if (selectedOptions.size() == 1) {
            return selectedOptions.get(0);
        } else {
            return selectedOptions;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ISelectList#getSelectedOption()
     */
    @Override
    public String getSelectedOption() throws WidgetException {
        Object value = getValue();
        if (value != null) {
            if (value instanceof String) {
                return (String) value;
            } else {
                throw new WidgetException("More than one option selected", getByLocator());
            }
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ISelectList#getOptions()
     */
    @Override
    public List<String> getOptions() throws WidgetException {
        try {
            org.openqa.selenium.support.ui.Select selectBox = new org.openqa.selenium.support.ui.Select(
                    findElement());
            List<WebElement> options = selectBox.getOptions();

            List<String> optionsTextValues = new ArrayList<String>();
            for (WebElement e : options) {
                optionsTextValues.add(e.getText());
            }

            return optionsTextValues;
        } catch (Exception e) {
            throw new WidgetException("Error while fetching select options", getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ISelectList#getSelectedOptions()
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getSelectedOptions() throws WidgetException {
        try {
            Object value = getValue();
            if (value != null) {
                if (value instanceof List<?>) {
                    return (List<String>) value;
                } else {
                    List<String> options = new ArrayList<String>();
                    options.add((String) value);
                    return options;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new WidgetException("Error while fetching selected options", getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.ISelectList#deselectAllOptions()
     */
    @Override
    public void deselectAllOptions() throws WidgetException {
        try {
            org.openqa.selenium.support.ui.Select selectBox = new org.openqa.selenium.support.ui.Select(
                    findElement());
            selectBox.deselectAll();
        } catch (Exception e) {
            throw new WidgetException("Error while deselecting all selected options", getByLocator(),
                    e);
        }
    }
    
    private void selectSingle(String optionText) throws WidgetException {
        org.openqa.selenium.support.ui.Select selectBox = new org.openqa.selenium.support.ui.Select(
                findElement());
        selectBox.selectByVisibleText(optionText);
    }
    
    /**
     * 
     * @param options the options that are being selected
     * @throws WidgetException
     */
    private void selectMultiple(List<String> options) throws WidgetException {
        org.openqa.selenium.support.ui.Select selectBox = new org.openqa.selenium.support.ui.Select(
                findElement());
        if (!selectBox.isMultiple()) {
            throw new WidgetException("This is not multiple select box", getByLocator());
        }

        for (String option : options) {
            selectBox.selectByVisibleText(option);
        }
    }

    /**
     * 
     * @return List of strings that represent the selected items values
     * @throws WidgetException
     */
    private List<String> getSelectedItems_internal() throws WidgetException {
        org.openqa.selenium.support.ui.Select selectBox = new org.openqa.selenium.support.ui.Select(
                findElement());
        List<WebElement> selectedItems = selectBox.getAllSelectedOptions();

        List<String> selectedItemsTextValues = new ArrayList<String>();
        for (WebElement e : selectedItems) {
            selectedItemsTextValues.add(e.getText());
        }

        return selectedItemsTextValues;
    }
}
