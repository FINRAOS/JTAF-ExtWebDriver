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

import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.impl.DefaultExtWebDriver;
import org.finra.jtaf.ewd.impl.DefaultSessionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SessionManagerTests {
	@After
	public void removeAllSessions() {
		boolean sessionsClear = false;
		while (!sessionsClear) {
			Map<String, ExtWebDriver> sessions = SessionManager.getInstance().getSessions();
			if (SessionManager.getInstance().getSessions().size() == 0) {
				sessionsClear = true;
			} else {
				ExtWebDriver ewd = sessions.values().iterator().next();
				SessionManager.getInstance().removeSession(ewd);
			}
		}
		SessionManager.getInstance().setSessionFactory(new DefaultSessionFactory());
	}

	@Test
	public void testGetCurrentSession() {
		ExtWebDriver ewd = SessionManager.getInstance().getCurrentSession();
		Assert.assertNotNull(ewd);
		Assert.assertNotNull(ewd.getWrappedDriver());
	}

	@Test
	public void testGetCurrentSessionNull() throws Exception {
		ExtWebDriver ewd = SessionManager.getInstance().setSessionFactory(new FailFactory()).getCurrentSession();
		Assert.assertNull(ewd);
	}

	@Test
	public void testSessionConstruction() {
		ExtWebDriver ewd = SessionManager.getInstance().setSessionFactory(new TestFactory()).getCurrentSession();
		Assert.assertNull(ewd.getBrowserName());
		Assert.assertNotNull(ewd);
	}

	@Test
	public void testSessionConstructionWithExistingSession() {
		ExtWebDriver ewd = SessionManager.getInstance().setSessionFactory(new TestFactory()).getCurrentSession();
		Assert.assertNotNull(ewd);
		Assert.assertEquals(ewd.getSessionId(), SessionManager.getInstance().getCurrentSessionId());

		ExtWebDriver ewd2 = SessionManager.getInstance().getCurrentSession(true);
		Assert.assertNotNull(ewd2);
		Assert.assertEquals(ewd2.getSessionId(), SessionManager.getInstance().getCurrentSessionId());
	}

	@Test
	public void testNewSessionConstruction() throws Exception {
		ExtWebDriver ewd = SessionManager.getInstance().setSessionFactory(new TestFactory()).getNewSession();
		Assert.assertNotNull(ewd);
	}

	@Test
	public void testNewSessionConstructionWithExistingSession() throws Exception {
		ExtWebDriver ewd = SessionManager.getInstance().setSessionFactory(new TestFactory()).getNewSession();
		Assert.assertNotNull(ewd);
		Assert.assertEquals(ewd.getSessionId(), SessionManager.getInstance().getCurrentSessionId());

		ExtWebDriver ewd2 = SessionManager.getInstance().setSessionFactory(new TestFactory()).getNewSession(true);
		Assert.assertNotNull(ewd2);
		Assert.assertEquals(ewd2.getSessionId(), SessionManager.getInstance().getCurrentSessionId());
	}

	@Test
	public void testNewSessionConstructionWithOverrideOptions() throws Exception {
		SessionManager.getInstance().setSessionFactory(new TestFactory());
		ExtWebDriver ewd = SessionManager.getInstance().getCurrentSession();
		Assert.assertNotNull(ewd);
		Assert.assertTrue(ewd.isJavascriptClickMode());

		Map<String, String> m = new HashMap<String, String>();
		m.put("javascriptClickOn", "false");
		ExtWebDriver ewd3 = SessionManager.getInstance().getNewSession(m, true);
		Assert.assertNotNull(ewd3);
		Assert.assertFalse(ewd3.isJavascriptClickMode());
	}
	
	@Test
    public void testNewSessionConstructionWithOverrideOptionsConvenient() throws Exception {
        ExtWebDriver ewd = SessionManager.getInstance().setSessionFactory(new TestFactory()).getNewSession();
        Assert.assertNotNull(ewd);
        Assert.assertTrue(ewd.isJavascriptClickMode());

        Map<String, String> m = new HashMap<String, String>();
        m.put("javascriptClickOn", "false");
        ExtWebDriver ewd3 = SessionManager.getInstance().getNewSession(m);
        Assert.assertNotNull(ewd3);
        Assert.assertFalse(ewd3.isJavascriptClickMode());
    }


	@Test
	public void testNewSessionConstructionWithClientProperties() throws Exception {
		ExtWebDriver ewd = SessionManager.getInstance().getNewSession("client", "client.properties", true);
		Assert.assertNotNull(ewd);
		Assert.assertEquals("htmlunit", ewd.getBrowserName());
	}
	
	@Test
    public void testNewSessionConstructionWithClientPropertiesConvenient() throws Exception {
        ExtWebDriver ewd = SessionManager.getInstance().getNewSession("client", "client.properties");
        Assert.assertNotNull(ewd);
        Assert.assertEquals("htmlunit", ewd.getBrowserName());
    }

	@Test
	public void testGetSession() throws Exception {
		ExtWebDriver ewd = SessionManager.getInstance().setSessionFactory(new TestFactory()).getNewSession();
		Assert.assertNotNull(ewd);
		ExtWebDriver ewd2 = SessionManager.getInstance().getSession(ewd.getSessionId());
		Assert.assertEquals(ewd.getSessionId(), ewd2.getSessionId());
	}

	@Test
	public void testSwitchToSessionString() throws Exception {
		ExtWebDriver ewd = SessionManager.getInstance().setSessionFactory(new TestFactory()).getNewSession(false);
		String expectedId = ewd.getSessionId();
		Assert.assertNotNull(ewd);
		SessionManager.getInstance().switchToSession(expectedId);
		Assert.assertEquals(expectedId, SessionManager.getInstance().getCurrentSession().getSessionId());
	}

	@Test
	public void testSwitchToSessionEWD() throws Exception {
		ExtWebDriver ewd = SessionManager.getInstance().setSessionFactory(new TestFactory()).getNewSession(false);
		String expectedId = ewd.getSessionId();
		Assert.assertNotNull(ewd);
		SessionManager.getInstance().switchToSession(ewd);
		Assert.assertEquals(expectedId, SessionManager.getInstance().getCurrentSession().getSessionId());	
	}
	
	@Test
	public void testRemoveSessionString() throws Exception{
		ExtWebDriver ewd = SessionManager.getInstance().setSessionFactory(new TestFactory()).getNewSession(false);
		String sessionIdToRemove = SessionManager.getInstance().getCurrentSessionId();
		Assert.assertNotNull(ewd);
		Assert.assertNotNull(sessionIdToRemove);
		SessionManager.getInstance().removeSession(sessionIdToRemove);

		ewd = SessionManager.getInstance().getSession(sessionIdToRemove);
		Assert.assertNull(ewd);
	}
	
	class FailFactory extends DefaultSessionFactory{
		   @Override
		    public Map<String, String> createDefaultOptions() {
		        HashMap<String, String> ret = new HashMap<String, String>();
		        // Add any default options needed here (like path to default properties)
		      
		        ret.put("client", "fail.properties");

		        return ret;
		    }
	}
	class TestFactory implements SessionFactory {

		public TestFactory() {

		}

		@Override
		public void cleanup(Map<String, String> options) throws Exception {
			// TODO Auto-generated method stub

		}

		@Override
		public Map<String, String> createDefaultOptions() {
			Map<String, String> m = new HashMap<String, String>();
			m.put("javascriptClickOn", "true");
			return m;
		}

		@Override
		public DesiredCapabilities createCapabilities(Map<String, String> options) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public WebDriver createInnerDriver(Map<String, String> options, DesiredCapabilities capabilities) throws Exception {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ExtWebDriver createNewSession(Map<String, String> options, WebDriver wd) {
			ExtWebDriver ewd = new DefaultExtWebDriver();
			if (options.get("javascriptClickOn") != null) {
				if (options.get("javascriptClickOn").equals("true")) {
					ewd.setClickMode(true);
				}
			}
			return ewd;
		}

	}
}
