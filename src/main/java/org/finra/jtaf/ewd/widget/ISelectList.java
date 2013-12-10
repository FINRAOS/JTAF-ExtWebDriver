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
 * The interface for the Select list widget
 * 
 */
public interface ISelectList extends IEditableElement {
    /**
     * Implementing this method would allow for getting all options in the list
     * 
     * @return all of the options in the list as a List of Strings
     * @throws WidgetException
     */
    public List<String> getOptions() throws WidgetException;

    /**
     * Implementing this method would allow for getting the currently selected
     * options in the list
     * 
     * @return a List containing all of the currently selected options as
     *         Strings
     * @throws WidgetException
     */
    public List<String> getSelectedOptions() throws WidgetException;

    /**
     * Implementing this method would allow for selecting a specific option from
     * the list
     * 
     * @param option
     *            - the option that is to be selected
     * @throws WidgetException
     */
    public void selectOption(String option) throws WidgetException;

    /**
     * Implementing this method would allow for getting the text of the
     * currently selected option
     * 
     * @return the text of the currently selected option
     * @throws WidgetException
     */
    public String getSelectedOption() throws WidgetException;

    /**
     * Implementing this method would allow for selecting multiple options in
     * the list based on the text
     * 
     * @param options
     *            - the list containing the text of the elements to be selected
     * @throws WidgetException
     */
    public void selectMultipleOptions(List<String> options) throws WidgetException;

    /**
     * Implementing this method would allow for deselecting any options in the
     * list that have been selected
     * 
     * @throws WidgetException
     */
    public void deselectAllOptions() throws WidgetException;

}
