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
 * This is the base interface for all editable elements
 * 
 */
public interface IEditableElement extends IInteractiveElement {
    /**
     * Implementing this method for an editable element would allow for setting
     * the
     * 
     * @param value
     *            - the value that the editable element
     * @throws WidgetException
     */
    void setValue(Object value) throws WidgetException;
}
