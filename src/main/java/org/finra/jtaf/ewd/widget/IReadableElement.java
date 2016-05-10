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
 * This is the base interface for all readable elements
 * 
 */
public interface IReadableElement extends IElement {
    /**
     * Implementing this method would allow for getting the value from a
     * ReadableElement as an Object
     * 
     * @return the value of the ReadableElement as an Object
     * @throws WidgetException
     */
    Object getValue() throws WidgetException;

    /**
     * Implementing this method would allow for getting the ReadableElement's
     * label as a String
     * 
     * @return the label of the ReadableElement as a String
     * @throws WidgetException
     */
    String getLabel() throws WidgetException;

}
