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

package org.finra.jtaf.ewd.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;

/**
 * Enables access and parameterization of property values.
 * 
 */
public class GUIProperties {
    private static final String DEFAULT_BUNDLE_NAME = "gui.properties";
    private final PropertyResourceBundle RESOURCE_BUNDLE;

    /**
     * Constructs a {@code GUIProperties} using the default bundle name.
     * 
     */
    public GUIProperties() {
        InputStream is = GUIProperties.class.getClassLoader().getResourceAsStream(
                DEFAULT_BUNDLE_NAME);
        try {
            if (is == null) {
                is = new FileInputStream(new File(DEFAULT_BUNDLE_NAME));
            }
            RESOURCE_BUNDLE = new PropertyResourceBundle(is);
        } catch (IOException e) {
            throw new RuntimeException("Could not load :" + DEFAULT_BUNDLE_NAME, e);
        }
    }

    /**
     * Constructs a {@code GUIProperties} from the given file.
     * 
     * @param guiPropertiesFileName
     *            the file to be loaded
     */
    public GUIProperties(String guiPropertiesFileName) {
        InputStream is = GUIProperties.class.getClassLoader().getResourceAsStream(
                guiPropertiesFileName);
        try {
            if (is == null) {
                is = new FileInputStream(new File(guiPropertiesFileName));
            }
            RESOURCE_BUNDLE = new PropertyResourceBundle(is);
        } catch (IOException e) {
            throw new RuntimeException("Could not load :" + guiPropertiesFileName, e);
        }
    }

    private String getString(String key, Object params[]) throws Exception {
        try {
            if ((params != null) && (params.length > 0)) {
                return MessageFormat.format(RESOURCE_BUNDLE.getString(key), params).trim();
            } else {
                return RESOURCE_BUNDLE.getString(key).trim();
            }
        } catch (MissingResourceException e) {
            throw e;
        } catch (IllegalArgumentException ie) {
            throw ie;
        }
    }

    /**
     * Retrieves the property associated with the given key as a {@code String}.
     * 
     * @param key
     *            the key to be retrieved
     * @param params
     *            instances of the {@code String} literal <code>"{n}"</code> in
     *            the retrieved value will be replaced by {@code params[n]}
     * @return the parameterized property associated with the given key
     * @throws Exception
     */
    public String getPropertyValue(String key, String... params) throws Exception {
        return getString(key, params);
    }

    /**
     * Retrieves the property associated with the given key as a {@code List} of
     * {@code String}s.
     * 
     * @param key
     *            the key to be retrieved
     * @param params
     *            instances of the {@code String} literal <code>"{n}"</code> in
     *            the retrieved value will be replaced by {@code params[n]}
     * @return the parameterized property list associated with the given key
     * @throws Exception
     */
    public List<String> getPropertyValueAsList(String key, String... params) throws Exception {
        try {
            String properties[] = RESOURCE_BUNDLE.getStringArray(key);
            if ((params != null) && (params.length > 0)) {
                List<String> returnProperties = new ArrayList<String>();
                for (String property : properties) {
                    returnProperties.add(MessageFormat.format(property, (Object[]) params).trim());
                }
                return returnProperties;
            } else {
                return Arrays.asList(properties);
            }
        } catch (MissingResourceException e) {
            throw e;
        } catch (IllegalArgumentException ie) {
            throw ie;
        }
    }
}
