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
 * Thrown by many {@link IElement Widget} interface methods to propagate a
 * {@link RuntimeException}.
 * 
 */
public class WidgetRuntimeException extends Exception {

    private static final long serialVersionUID = 157818658900692467L;

    /**
     * Constructs a {@code WidgetRuntimeException} with the specified detail
     * message and the element locator being used when the {@code Exception} was
     * thrown.
     * 
     * @param message
     *            the detail message
     * @param locator
     *            the element locator being used when the {@code Exception} was
     *            thrown
     */
    public WidgetRuntimeException(String message, String locator) {
        super(message + ": at " + locator);
    }

    /**
     * Constructs a {@code WidgetRuntimeException} with the specified detail
     * message, the element locator being used when the {@code Exception} was
     * thrown, and the {@code Exception} serving as the underlying cause for
     * this {@code WidgetRuntimeException} being thrown.
     * 
     * @param message
     *            the detail message
     * @param locator
     *            the element locator being used when the {@code Exception} was
     *            thrown
     * @param cause
     *            the underlying cause for this {@code WidgetRuntimeException}
     *            being thrown
     */
    public WidgetRuntimeException(String message, String locator, Exception cause) {
        super(message + ": at " + locator, cause);
    }
}
