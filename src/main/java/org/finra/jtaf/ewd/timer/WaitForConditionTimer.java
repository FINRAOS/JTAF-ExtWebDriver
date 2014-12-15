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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.By;

/**
 * Provides a way to block until the element at a given locator meets a given
 * condition held by an {@link ITimerCallback}.
 * 
 */
public class WaitForConditionTimer {

    private ITimerCallback timerCallback;
    private By locator;

    private final Logger logger = LoggerFactory.getLogger(WaitForConditionTimer.class.getPackage()
            .getName());

    /**
     * Constructs a {@code WaitForConditionTimer} with the given locator
     * pointing to an element and the given timerCallback holding a condition to
     * be met.
     * 
     * @param locator
     *            the element locator to be used
     * @param timerCallback
     *            the method called to check the condition to be met
     */
    public WaitForConditionTimer(By locator, ITimerCallback timerCallback) {
        this.timerCallback = timerCallback;
        this.locator = locator;
    }

    /**
     * Holds the method that is called to check the condition associated with a
     * {@code WaitForConditionTimer}.
     * 
     */
    public interface ITimerCallback {
        /**
         * Calls the method held by the {@code ITimerCallback}.
         * 
         * @return {@code true} if and only if the {@code WaitForConditionTimer}
         *         has had its condition met
         * @throws Exception
         */
        public boolean execute() throws Exception;
    }

    /**
     * Blocks for a given time, or until the associated condition is met. The
     * associated condition is checked every half second.
     * <p>
     * Only an {@link InterruptedException} will prematurely halt the waiting
     * process.
     * 
     * @param waitTime
     *            the time to wait in milliseconds
     * @throws WidgetTimeoutException
     */
    public void waitUntil(long waitTime) throws WidgetTimeoutException {

        long currentTimeMillis = System.currentTimeMillis();
        long maxRequestTimeout = waitTime;

        long endTime = currentTimeMillis + maxRequestTimeout;

        while (System.currentTimeMillis() < endTime) {

            try {
                if (timerCallback.execute())
                    return;
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                throw new WidgetTimeoutException("The request has timed out", locator);
            } catch (Exception e) {
                // ignore any other type of Exception and continue
                logger.debug(" Exception while waiting. " + "Ignoring and continuing to wait. ", e);
            }
        }

        throw new WidgetTimeoutException("Timed out performing action " + timerCallback, locator);
    }

}
