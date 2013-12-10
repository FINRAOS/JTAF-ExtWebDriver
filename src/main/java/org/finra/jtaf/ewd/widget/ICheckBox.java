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

import org.finra.jtaf.ewd.timer.WidgetTimeoutException;

/**
 * This is the interface for CheckBox widgets
 * 
 */
public interface ICheckBox extends IEditableElement {

    /**
     * Implementing this method would allow for checking the CheckBox widget
     * 
     * @throws WidgetException
     */
    public void check() throws WidgetException;


    /**
     * Implementing this method would allow for unchecking the CheckBox widget
     * 
     * @throws WidgetException
     */
    public void uncheck() throws WidgetException;

    /**
     * Implementing this method would allow being able to tell if the CheckBox
     * widget is checked or not
     * 
     * @return true if checked and false if it isn't
     * @throws WidgetException
     */
    public boolean isChecked() throws WidgetException;

    /**
     * Implementing this method would allow for waiting until the CheckBox has
     * been checked
     * 
     * @throws WidgetException
     */
    public void waitForChecked() throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for waiting until the CheckBox has
     * been unchecked
     * 
     * @throws WidgetException
     */
    public void waitForUnchecked() throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for waiting until the CheckBox has
     * been checked
     * 
     * @param timeout
     *            - the designated length of time that this method will wait for
     *            the CheckBox to be checked
     * @throws WidgetException
     */
    public void waitForChecked(final long timeout) throws WidgetException, WidgetTimeoutException;

    /**
     * Implementing this method would allow for waiting until the CheckBox has
     * been unchecked
     * 
     * @param timeout
     *            - the designated length of time that this method will wait for
     *            the CheckBox to be unchecked
     * @throws WidgetException
     */
    public void waitForUnchecked(final long timeout) throws WidgetException, WidgetTimeoutException;

}
