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

package org.finra.jtaf.ewd.timer;

import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.openqa.selenium.By;

/**
 * Thrown to indicate that a {@link IElement Widget} operation has timed out.
 * 
 */
public class WidgetTimeoutException extends WidgetException {

    private static final long serialVersionUID = -3696853105223276838L;

    /**
     * Constructs a {@code WidgetTimeoutException} with the specified detail
     * message and the element locator being used when the {@code Exception} was
     * thrown.
     * 
     * @param message
     *            the detail message
     * @param locator
     *            the element locator being used when the {@code Exception} was
     *            thrown
     */
    public WidgetTimeoutException(String message, String locator) {
        super(message, locator);
    }
    
    /**
     * Constructs a {@code WidgetTimeoutException} with the specified detail
     * message and the element locator being used when the {@code Exception} was
     * thrown.
     * 
     * @param message
     *            the detail message
     * @param locator
     *            the element locator being used when the {@code Exception} was
     *            thrown
     */
    public WidgetTimeoutException(String message, By locator) {
        super(message, locator);
    }

}
