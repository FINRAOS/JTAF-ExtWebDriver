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

/**
 * This is the interface for Calendar widgets
 * 
 */
public interface ICalendar extends IEditableElement {

    /**
     * Implementing this method would allow for checking to see if the Calendar
     * widget is opened or closed
     * 
     * @return true if the Calendar widget is open and false if it is closed.
     * @throws WidgetException
     */
    boolean isOpen() throws WidgetException;

    /**
     * Implementing this method would allow for closing the Calendar widget
     * 
     * @throws WidgetException
     */
    void close() throws WidgetException;

    /**
     * Implementing this method would allow for selecting the current date in
     * the Calendar widget
     * 
     * @throws WidgetException
     */
    void chooseToday() throws WidgetException;

    /**
     * Implementing this method would allow for inputting a specific date into
     * the Calendar widget
     * 
     * @param value
     *            - the desired date to be entered into the Calendar widget
     * @throws WidgetException
     */
    void enterDate(String value) throws WidgetException;
}
