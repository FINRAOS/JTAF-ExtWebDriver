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

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;

import org.apache.commons.lang.StringUtils;

/**
 * Collects a group of {@link GUIProperties}.
 * 
 */
public class GUIHierarchyConcatenationProperties {
    private List<GUIProperties> propertyFiles = new ArrayList<GUIProperties>();
    private List<String> propertyFilesNames = new ArrayList<String>();

    /**
     * Constructs a {@code GUIHierarchyConcatenationProperties} from the given
     * files.
     * <p>
     * The order in which the {@code List} of {@code GUIProperties} are searched
     * when retrieving is preserved from the order in which the files are
     * listed.
     * 
     * @param files
     *            the files to be loaded
     */
    public GUIHierarchyConcatenationProperties(String... files) {
        for (String fileName : files) {
            // Only add a file once
            if (!propertyFilesNames.contains(fileName)) {
                propertyFilesNames.add(fileName);
                propertyFiles.add(new GUIProperties(fileName));
            }
        }
    }

    /**
     * Searches over the group of {@code GUIProperties} for a property
     * corresponding to a hierarchical concatenation of the given names.
     * <p>
     * Concatenation of property names is done from high index to low. That is
     * to say, for the array {@code ["a", "b", "c"]}, the names searched will be
     * {@code "cba"}, {@code "cb"}, {@code "c"} in that order.
     * 
     * @param propertyNames
     *            names to be concatenated and searched for
     * @return the first property found associated with a concatenation of the
     *         given names
     * @throws Exception
     */
    public String getPropertyValue(String[] propertyNames) throws Exception {
        return getPropertyValue(propertyNames, (String[]) null);
    }

    /**
     * Searches over the group of {@code GUIProperties} for a property list
     * corresponding to a hierarchical concatenation of the given names.
     * <p>
     * Concatenation of property names is done from high index to low. That is
     * to say, for the array {@code ["a", "b", "c"]}, the names searched will be
     * {@code "cba"}, {@code "cb"}, {@code "c"} in that order.
     * 
     * @param propertyNames
     *            names to be concatenated and searched for
     * @return the first property list found associated with a concatenation of
     *         the given names
     * @throws Exception
     */
    public List<String> getPropertyValueAsList(String[] propertyNames) throws Exception {
        return getPropertyValueAsList(propertyNames, (String[]) null);
    }

    /**
     * Searches over the group of {@code GUIProperties} for a property
     * corresponding to the given name.
     * 
     * @param propertyName
     *            property name to be found
     * @return the first property found associated with a concatenation of the
     *         given names
     * @throws Exception
     */
    public String getPropertyValue(String propertyName) throws Exception {
        String[] propertyNames = new String[1];
        propertyNames[0] = propertyName;

        return getPropertyValue(propertyNames);
    }

    /**
     * Searches over the group of {@code GUIProperties} for a property list
     * corresponding to the given name.
     * 
     * @param propertyName
     *            property name to be found
     * @return the first property list found associated with a concatenation of
     *         the given names
     * @throws Exception
     */
    public List<String> getPropertyValueAsList(String propertyName) throws Exception {
        String[] propertyNames = new String[1];
        propertyNames[0] = propertyName;

        return getPropertyValueAsList(propertyNames);
    }

    /**
     * Searches over the group of {@code GUIProperties} for a property
     * corresponding to a hierarchical concatenation of the given names.
     * <p>
     * Concatenation of property names is done from high index to low. That is
     * to say, for the array {@code ["a", "b", "c"]}, the names searched will be
     * {@code "cba"}, {@code "cb"}, {@code "c"} in that order.
     * 
     * @param propertyNames
     *            names to be concatenated and searched for
     * @param parameters
     *            instances of the {@code String} literal <code>"{n}"</code> in
     *            the retrieved value will be replaced by {@code params[n]}
     * @return the first property found associated with a concatenation of the
     *         given names
     * @throws Exception
     */
    public String getPropertyValue(String[] propertyNames, String... parameters) throws Exception {
        // Create possible combinations of property names
        String value;
        List<String> possiblePropertyName = new ArrayList<String>();
        StringBuffer fullName = new StringBuffer();
        for (int i = 0; i < propertyNames.length; i++) {
            fullName.append(propertyNames[propertyNames.length - i - 1]);
            possiblePropertyName.add(fullName.toString());
        }

        // Try to find the property
        for (int i = 0; i < possiblePropertyName.size(); i++) {
            String propertyNameCurrent = possiblePropertyName.get(possiblePropertyName.size() - i
                    - 1);

            for (int y = 0; y < propertyFiles.size(); y++) {
                try {
                    GUIProperties propertyFile = propertyFiles.get(y);
                    if (parameters != null && parameters.length > 0) {
                        value = propertyFile.getPropertyValue(propertyNameCurrent, parameters);
                    } else {
                        value = propertyFile.getPropertyValue(propertyNameCurrent);
                    }
                    return value;
                } catch (MissingResourceException e) {
                    // Ignore and continue searching
                }
            }
        }

        throw new Exception("Can't find '" + StringUtils.join(possiblePropertyName, ",")
                + "' property(ies) in '" + StringUtils.join(propertyFilesNames, ",")
                + "' property file(s)");
    }

    /**
     * Searches over the group of {@code GUIProperties} for a property list
     * corresponding to a hierarchical concatenation of the given names.
     * <p>
     * Concatenation of property names is done from high index to low. That is
     * to say, for the array {@code ["a", "b", "c"]}, the names searched will be
     * {@code "cba"}, {@code "cb"}, {@code "c"} in that order.
     * 
     * @param propertyNames
     *            names to be concatenated and searched for
     * @param parameters
     *            instances of the {@code String} literal <code>"{n}"</code> in
     *            the retrieved value will be replaced by {@code params[n]}
     * @return the first property list found associated with a concatenation of
     *         the given names
     * @throws Exception
     */
    public List<String> getPropertyValueAsList(String[] propertyNames, String... parameters)
            throws Exception {
        // Create possible combinations of property names
        List<String> value;
        List<String> possiblePropertyName = new ArrayList<String>();
        StringBuffer fullName = new StringBuffer();
        for (int i = 0; i < propertyNames.length; i++) {
            fullName.append(propertyNames[propertyNames.length - i - 1]);
            possiblePropertyName.add(fullName.toString());
        }

        // Try to find the property
        for (int i = 0; i < possiblePropertyName.size(); i++) {
            String propertyNameCurrent = possiblePropertyName.get(possiblePropertyName.size() - i
                    - 1);

            for (int y = 0; y < propertyFiles.size(); y++) {
                try {
                    GUIProperties propertyFile = propertyFiles.get(y);
                    if (parameters != null && parameters.length > 0) {
                        value = propertyFile
                                .getPropertyValueAsList(propertyNameCurrent, parameters);
                    } else {
                        value = propertyFile.getPropertyValueAsList(propertyNameCurrent);
                    }
                    return value;
                } catch (MissingResourceException e) {
                    // Ignore and continue searching
                }
            }
        }

        throw new Exception("Can't find '" + StringUtils.join(possiblePropertyName, ",")
                + "' property(ies) in '" + StringUtils.join(propertyFilesNames, ",")
                + "' property file(s)");
    }

    /**
     * Searches over the group of {@code GUIProperties} for a property
     * corresponding to the given key.
     * 
     * @param key
     *            key to be found
     * @param parameters
     *            instances of the {@code String} literal <code>"{n}"</code> in
     *            the retrieved value will be replaced by {@code params[n]}
     * @return the first property found associated with a concatenation of the
     *         given names
     * @throws Exception
     */
    public String getPropertyValue(String key, String... parameters) throws Exception {
        return getPropertyValue(new String[] { key }, parameters);
    }

    /**
     * Searches over the group of {@code GUIProperties} for a property list
     * corresponding to the given key.
     * 
     * @param key
     *            key to be found
     * @param parameters
     *            instances of the {@code String} literal <code>"{n}"</code> in
     *            the retrieved value will be replaced by {@code params[n]}
     * @return the first property list found associated with a concatenation of
     *         the given names
     * @throws Exception
     */
    public List<String> getPropertyValueAsList(String key, String... parameters) throws Exception {
        return getPropertyValueAsList(new String[] { key }, parameters);
    }

    /**
     * Searches over the group of {@code GUIProperties} for a property
     * corresponding to the given key.
     * 
     * @param key
     *            key to be found
     * @param parameters
     *            instances of the {@code String} literal <code>"{n}"</code> in
     *            the retrieved value will be replaced by the {@code String}
     *            representation of {@code params[n]}
     * @return the first property found associated with a concatenation of the
     *         given names
     * @throws Exception
     */
    public String getPropertyValue(String key, Object[] parameters) throws Exception {
        if (parameters != null && parameters.length > 0) {
            String parameters2[] = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                if (parameter != null) {
                    parameters2[i] = String.valueOf(parameter);
                }
            }
            return getPropertyValue(new String[] { key }, parameters2);
        } else {
            return getPropertyValue(key);
        }
    }

    /**
     * Searches over the group of {@code GUIProperties} for a property list
     * corresponding to the given key.
     * 
     * @param key
     *            key to be found
     * @param parameters
     *            instances of the {@code String} literal <code>"{n}"</code> in
     *            the retrieved value will be replaced by the {@code String}
     *            representation of {@code params[n]}
     * @return the first property list found associated with a concatenation of
     *         the given names
     * @throws Exception
     */
    public List<String> getPropertyValueAsList(String key, Object[] parameters) throws Exception {
        if (parameters != null && parameters.length > 0) {
            String parameters2[] = new String[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Object parameter = parameters[i];
                if (parameter != null) {
                    parameters2[i] = String.valueOf(parameter);
                }
            }
            return getPropertyValueAsList(new String[] { key }, parameters2);
        } else {
            return getPropertyValueAsList(key);
        }
    }
}