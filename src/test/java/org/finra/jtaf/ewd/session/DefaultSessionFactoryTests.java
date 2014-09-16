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
import org.finra.jtaf.ewd.impl.DefaultSessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DefaultSessionFactoryTests {

	public static SessionFactory sf = new DefaultSessionFactory();

	private DesiredCapabilities capabilityTest(String property) throws Exception{
		Map<String, String> opts = new HashMap<String, String>();
		opts.put("client", "sessionfactory/" + property + ".properties");
		DesiredCapabilities dc = sf.createCapabilities(opts);
		Assert.assertNotNull(dc);
		return dc;
	}

	@Test
	public void createCapabilitiesTest() throws Exception {
		//THESE USE GRID
		//browser=ie
		Assert.assertEquals("internet explorer", capabilityTest("ie").getBrowserName());
		//browser=iexplore, also proxy
		DesiredCapabilities ie2 = capabilityTest("ie2");
		Assert.assertEquals("internet explorer", ie2.getBrowserName());
		Assert.assertNotNull(ie2.getCapability(CapabilityType.PROXY));

		//browser=*iexplore
		Assert.assertEquals("internet explorer", capabilityTest("ie3").getBrowserName());
		//browser=firefox
		Assert.assertEquals("firefox", capabilityTest("firefox").getBrowserName());
		//browser=*firefox
		Assert.assertEquals("firefox", capabilityTest("firefox2").getBrowserName());
		//browser=chrome
		Assert.assertEquals("chrome", capabilityTest("chrome").getBrowserName());
		//browser=safari
		Assert.assertEquals("safari", capabilityTest("safari").getBrowserName());
		//browser=opera
		Assert.assertEquals("opera", capabilityTest("opera").getBrowserName());
		//browser=iphone
		Assert.assertEquals("iPhone",capabilityTest("iphone").getBrowserName());
		//browser=ipad
		Assert.assertEquals("iPad", capabilityTest("ipad").getBrowserName());
		//browser=android
		Assert.assertEquals("android", capabilityTest("android").getBrowserName());

		//THESE DO NOT USE GRID
		DesiredCapabilities nonChrome = capabilityTest("safari2");
		Assert.assertNotNull(nonChrome.getCapability("browser.download.dir"));
		Assert.assertNotNull(nonChrome.getCapability(CapabilityType.PROXY));

		Assert.assertNotNull(capabilityTest("chrome2").getCapability("chrome.switches"));
	}

	@Test(expected=Exception.class)
	public void createCapabilitiesTestException() throws Exception {
		capabilityTest("htmlunit");
	}

	@Test(expected=Exception.class)
	public void createCapabilitiesTestGridurlException() throws Exception {
		capabilityTest("gridfail1");
	}

	@Test(expected=Exception.class)
	public void createCapabilitiesTestGridBrowserException() throws Exception {
		capabilityTest("gridfail2");
	}

	@Test(expected=Exception.class)
	public void createCapabilitiesTestGridPlatformException() throws Exception {
		capabilityTest("gridfail3");
	}

	@Test
	public void createDefaultOptionsTest() throws Exception {
		Map<String, String> defaultOpts = sf.createDefaultOptions();
		Assert.assertNotNull(defaultOpts);
	}

	@Test
	public void testCreateInnerDriver() throws Exception{
		Map<String, String> defaultOpts = sf.createDefaultOptions();
		DesiredCapabilities dc = sf.createCapabilities(defaultOpts);

		WebDriver innerDriverTest = sf.createInnerDriver(defaultOpts, dc);

		Assert.assertNotNull(innerDriverTest);
	}

	@Test
	public void createNewSessionTest() throws Exception {
		Map<String, String> defaultOpts = sf.createDefaultOptions();
		DesiredCapabilities dc = sf.createCapabilities(defaultOpts);

		WebDriver wd = sf.createInnerDriver(defaultOpts, dc);

		ExtWebDriver wdTest = sf.createNewSession(defaultOpts, wd);

		Assert.assertNotNull(wdTest);
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
}
