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
package org.finra.jtaf.ewd.widget;

import java.util.List;

/**
 * This is the interface for the DropDown (Select) widget
 * 
 */
public interface IDropDown extends IEditableElement {
    /**
     * Implementing this method would allow for selecting specific items from a
     * dropdown menu
     * 
     * @param option
     *            - the item in the DropDown widget that is to be selected
     * @throws WidgetException
     */
    void selectOption(String option) throws WidgetException;

    /**
     * Implementing this method would allow for obtaining all of the options
     * from the dropdown widget
     * 
     * @return a list of the options that are contained in the dropdown menu
     * @throws WidgetException
     */
    List<String> getOptions() throws WidgetException;

    /**
     * Implementing this method would allow for checking which option was
     * selected from the dropdown menu
     * 
     * @return the option that was selected from the dropdown
     * @throws WidgetException
     */
    String getSelectedOption() throws WidgetException;
}
