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
package org.finra.jtaf.ewd.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.impl.DefaultSessionFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;

/**
 * SessionManager for the testing framework. Uses a ThreadLocal so each thread
 * of test execution has its own manager instance.
 * 
 */
public class SessionManager {

    private static final Log log = LogFactory.getLog(SessionManager.class);
    private Map<String, ExtWebDriver> sessions = new HashMap<String, ExtWebDriver>();

    private final static String DEFAULT_SESSION = "default";

    private final static int MAX_RETRIES = 5;

    private String currentSessionId = DEFAULT_SESSION;
    private int nextCustomSessionId = 1;

    private boolean doCleanup = true;

    private SessionManager() {

    }

    private static ThreadLocal<SessionManager> sessionManager = new ThreadLocal<SessionManager>() {
        protected synchronized SessionManager initialValue() {
            return new SessionManager();
        }
    };
    
    private static ThreadLocal<SessionFactory> sessionFactory = new ThreadLocal<SessionFactory>() {
        protected synchronized SessionFactory initialValue() {
            return new DefaultSessionFactory();
        }
    };


    /**
     * Obtain the ThreadLocal instance of SessionManager. Configures the
     * instance to use DefaultSessionFactory()
     * 
     * @return SessionManager, the ThreadLocal instance of SessionManager
     * 
     * @see setSessionFactory
     */

    public static SessionManager getInstance() {
    	return sessionManager.get();
    }

    /**
     * Configure the current instance of SessionManager to use the given
     * SessionFactory instance as its SessionFactory, returning the newly
     * configured instance. With this method, obtaining a SessionManager
     * configured to use a custom SessionFactory can be done with
     * SessionManager.getInstance().setSessionFactory(new
     * ustomSessionFactory()).
     * 
     * @param impl
     *            a SessionFactory instance to be associated with this manager
     * @return SessionManager
     * @see getInstance
     */

    public SessionManager setSessionFactory(SessionFactory impl) {
        sessionFactory.set(impl);
        return this;
    }

    /**
     * Get the current session associated with this thread. Because a
     * SessionManager instance is thread-local, the notion of current is also
     * specific to a thread.
     * 
     * 
     * @param createIfNotFound
     *            set to true if a session should be created if no session is
     *            associated with the current sessionId
     * @return ExtWebDriver an ExtWebDriver instance
     * @see getCurrentSession(), getSession(String), switchToDefaultSession(),
     *      switchToSession(String)
     */

    public ExtWebDriver getCurrentSession(boolean createIfNotFound) {

        for (int i = 0; i < MAX_RETRIES; i++) {
            ExtWebDriver sel = sessions.get(currentSessionId);
            try {
                if ((sel == null) && (createIfNotFound)) {
                    sel = getNewSession();
                }
                return sel;
            } catch (Exception e) {
                // if the exception is of type UnreachableBrowserException, try
                // again
                if (!(e instanceof UnreachableBrowserException)) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Convenience method for getting the current ExtWebDriver session
     * associated with this SessionManager, creating a new session if the
     * session does not exist.
     * 
     * @return ExtWebDriver an instance of ExtWebDriver
     */

    public ExtWebDriver getCurrentSession() {
        return getCurrentSession(true);
    }

    /**
     * Get an existing ExtWebDriver session with the given ID.
     * 
     * @param sessionId
     * @return
     */

    public ExtWebDriver getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Get the Map of all ExtWebDriver sessions associated with this
     * SessionManager instance.
     * 
     * @return
     */

    public Map<String, ExtWebDriver> getSessions() {
        return sessions;
    }

    /**
     * Switch the current session to be the ExtWebDriver session with the given
     * ID. A session with this ID should have already been previously created.
     * 
     * @param sessionId
     */

    public void switchToSession(String sessionId) {
        currentSessionId = sessionId;
    }

    /**
     * Switch the current session to be the provided ExtWebDriver instance. This
     * method assumes that the provided session was created within the scope of
     * the current thread (since session IDs are only required to be
     * thread-local, as are sessions themselves).
     * 
     * @param sessionId
     */

    public void switchToSession(ExtWebDriver ewd) {
        currentSessionId = ewd.getSessionId();
    }

    /**
     * Get the ID of the current ExtWebDriver session associated with this
     * SessionManager
     * 
     * @return
     */
    public String getCurrentSessionId() {
        return currentSessionId;
    }

    /**
     * Remove a session with the given ID from this SessionManager
     * 
     * @param sessionId
     *            the ID of the session to be removed
     */

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    /**
     * Remove the given session from SessionManager based on its stored ID. Note
     * that the session must have been created with this same thread as sessions
     * and unique IDs are only required to be thread-local and not global.
     * 
     * @param session
     *            the ExtWebDriver session to be removed
     */

    public void removeSession(ExtWebDriver session) {
        sessions.remove(session.getSessionId());
    }

    /**
     * Create and return a new ExtWebDriver session with default options, and
     * set it as the current session for this SessionManager.
     * 
     * @return A new ExtWebDriver instance with auto-generated ID. You can
     *         obtain the Session ID with ExtWebDriver.getSessionId().
     * @throws Exception
     */

    public ExtWebDriver getNewSession() throws Exception {
        return getNewSession(true);
    }

    /**
     * Create and return new ExtWebDriver instance with default options. If
     * setAsCurrent is true, set the new session as the current session for this
     * SessionManager.
     * 
     * @param setAsCurrent
     *            set to true if the new session should become the current
     *            session for this SessionManager
     * @return A new ExtWebDriver session
     * @throws Exception
     */

    public ExtWebDriver getNewSession(boolean setAsCurrent) throws Exception {
        Map<String, String> options = sessionFactory.get().createDefaultOptions();
        return getNewSessionDo(options, setAsCurrent);
    }

    /**
     * Create and return a new ExtWebDriver instance. The instance is
     * constructed with default options, with the provided key/value pair
     * overriding the corresponding key and value in the options, and will
     * become the current session. This is a convenience method for use when
     * only a single option needs to be overridden. If overriding multiple
     * options, you must use getNewSession(Map<String, String>, boolean)
     * instead.
     * 
     * @param key
     *            The key whose default value will be overridden
     * @param value
     *            The value to be associated with the provided key
     * @return A new ExtWebDriver instance which is now the current session
     * @throws Exception
     */

    public ExtWebDriver getNewSession(String key, String value) throws Exception {
        return getNewSession(key, value, true);
    }

    /**
     * Create and return a new ExtWebDriver instance. The instance is
     * constructed with default options, with the provided key/value pair
     * overriding the corresponding key and value in the options. This is a
     * convenience method for use when only a single option needs to be
     * overridden. If overriding multiple options, you must use
     * getNewSession(Map<String, String>, boolean) instead.
     * 
     * @param key
     *            The key whose default value will be overridden
     * @param value
     *            The value to be associated with the provided key
     * @param setAsCurrent
     *            set to true if the new session should become the current
     *            session for this SessionManager
     * @return A new ExtWebDriver instance
     * @throws Exception
     */

    public ExtWebDriver getNewSession(String key, String value, boolean setAsCurrent)
            throws Exception {

        /**
         * This is where the clientPropertiesFile is parsed and key-value pairs
         * are added into the options map
         */
        Map<String, String> options = sessionFactory.get().createDefaultOptions();
        options.put(key, value);

        return getNewSessionDo(options, setAsCurrent);
    }

    /**
     * Create and return a new ExtWebDriver instance. The instance is
     * constructed with default options, with the provided Map of key/value
     * pairs overriding the corresponding pairs in the options. This new
     * ExtWebDriver instance will then become the current session.
     * 
     * @param override
     *            A Map of options to be overridden
     * @return A new ExtWebDriver instance which is now the current session
     * @throws Exception
     */

    public ExtWebDriver getNewSession(Map<String, String> override) throws Exception {
        return getNewSession(override, true);
    }

    /**
     * Create and return a new ExtWebDriver instance. The instance is
     * constructed with default options, with the provided Map of key/value
     * pairs overriding the corresponding pairs in the options.
     * 
     * @param override
     *            A Map of options to be overridden
     * @param setAsCurrent
     *            set to true if the new session should become the current
     *            session for this SessionManager
     * @return A new ExtWebDriver instance
     * @throws Exception
     */

    public ExtWebDriver getNewSession(Map<String, String> override, boolean setAsCurrent)
            throws Exception {

        Map<String, String> options = sessionFactory.get().createDefaultOptions();

        for (Entry<String, String> opt : override.entrySet()) {
            options.put(opt.getKey(), opt.getValue());
        }

        return getNewSessionDo(options, setAsCurrent);
    }

    private ExtWebDriver getNewSessionDo(Map<String, String> options, boolean setAsCurrent)
            throws Exception {

        if (doCleanup) {
            sessionFactory.get().cleanup(options);
            doCleanup = false;
        }

        // Get capabilities
        DesiredCapabilities dc = sessionFactory.get().createCapabilities(options);

        // Get driver instance
        WebDriver innerDriver = sessionFactory.get().createInnerDriver(options, dc);

        // Inject as wrapped driver
        ExtWebDriver sel = sessionFactory.get().createNewSession(options, innerDriver);

        String sessionId = getNextCustomSessionId();
        if (setAsCurrent) {
            currentSessionId = sessionId;
        }

        // Store the session in sessions Map
        sessions.put(sessionId, sel);

        // Pass ID to the EWD instance
        sel.setSessionId(sessionId);

        return sel;
    }

    /**
     * 
     * @return String of the next session Id
     */
    private String getNextCustomSessionId() {
        String id = "custom_" + nextCustomSessionId;
        nextCustomSessionId++;
        return id;
    }
}
