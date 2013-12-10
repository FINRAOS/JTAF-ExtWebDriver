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
 * This is the interface for the Radio button widget
 * 
 */
public interface IRadioGroup extends IEditableElement {
    /**
     * Implementing this method would allow for selecting a radio button with matching value on the RadioGroup widget based on text value
     * 
     * @throws WidgetException
     */
    public void select(String value) throws WidgetException;

    /**
     * Implementing this method would allow for returning selected value on the RadioGroup widget
     * 
     * @return selected option value
     * @throws WidgetException
     */
    public String getSelectedValue() throws WidgetException;

    /**
     * Implementing this method would allow for returning available values on the RadioGroup widget
     *
     * @return available options value
     * @throws WidgetException
     */
    public List<String> getValues() throws WidgetException;
}
