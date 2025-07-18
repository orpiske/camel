/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.freemarker;

import java.net.URL;
import java.util.Map;

import freemarker.cache.NullCacheStorage;
import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import org.apache.camel.Endpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.annotations.Component;
import org.apache.camel.support.DefaultComponent;
import org.apache.camel.support.ResourceHelper;
import org.apache.camel.util.ObjectHelper;

/**
 * Freemarker component.
 */
@Component("freemarker")
public class FreemarkerComponent extends DefaultComponent {

    @Metadata(defaultValue = "true", description = "Sets whether to use resource content cache or not")
    private boolean contentCache = true;
    @Metadata
    private boolean allowTemplateFromHeader;
    @Metadata
    private boolean allowContextMapAll;
    @Metadata
    private boolean localizedLookup;
    @Metadata(label = "advanced")
    private Configuration configuration;
    private Configuration noCacheConfiguration;

    public FreemarkerComponent() {
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        // should we use regular configuration or no cache (content cache is default true)
        Configuration config;
        String encoding = getAndRemoveParameter(parameters, "encoding", String.class);
        boolean cache = getAndRemoveParameter(parameters, "contentCache", Boolean.class, contentCache);
        int templateUpdateDelay = getAndRemoveParameter(parameters, "templateUpdateDelay", Integer.class, 0);
        if (cache) {
            config = getConfiguration();
            if (templateUpdateDelay > 0) {
                config.setTemplateUpdateDelay(templateUpdateDelay);
            }
        } else {
            config = getNoCacheConfiguration();
        }

        FreemarkerEndpoint endpoint = new FreemarkerEndpoint(uri, this, remaining);
        if (ObjectHelper.isNotEmpty(encoding)) {
            endpoint.setEncoding(encoding);
        }
        endpoint.setAllowTemplateFromHeader(allowTemplateFromHeader);
        endpoint.setAllowContextMapAll(allowContextMapAll);
        endpoint.setContentCache(cache);
        endpoint.setConfiguration(config);
        endpoint.setTemplateUpdateDelay(templateUpdateDelay);

        setProperties(endpoint, parameters);

        // if its a http resource then append any remaining parameters and update the resource uri
        if (ResourceHelper.isHttpUri(remaining)) {
            remaining = ResourceHelper.appendParameters(remaining, parameters);
            endpoint.setResourceUri(remaining);
        }

        return endpoint;
    }

    public Configuration getConfiguration() {
        lock.lock();
        try {
            if (configuration == null) {
                configuration = new Configuration(Configuration.VERSION_2_3_34);
                configuration.setLocalizedLookup(isLocalizedLookup());
                configuration.setTemplateLoader(new URLTemplateLoader() {

                    @Override
                    protected URL getURL(String name) {
                        try {
                            return ResourceHelper.resolveMandatoryResourceAsUrl(getCamelContext(), name);
                        } catch (Exception e) {
                            // freemarker prefers to ask for locale first (eg xxx_en_GB, xxX_en), and then fallback without locale
                            // so we should return null to signal the resource could not be found
                            return null;
                        }
                    }
                });
            }
            return (Configuration) configuration.clone();
        } finally {
            lock.unlock();
        }
    }

    /**
     * To use an existing {@link freemarker.template.Configuration} instance as the configuration.
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public boolean isContentCache() {
        return contentCache;
    }

    /**
     * Sets whether to use resource content cache or not
     */
    public void setContentCache(boolean contentCache) {
        this.contentCache = contentCache;
    }

    public boolean isAllowTemplateFromHeader() {
        return allowTemplateFromHeader;
    }

    /**
     * Whether to allow to use resource template from header or not (default false).
     *
     * Enabling this allows to specify dynamic templates via message header. However this can be seen as a potential
     * security vulnerability if the header is coming from a malicious user, so use this with care.
     */
    public void setAllowTemplateFromHeader(boolean allowTemplateFromHeader) {
        this.allowTemplateFromHeader = allowTemplateFromHeader;
    }

    public boolean isAllowContextMapAll() {
        return allowContextMapAll;
    }

    /**
     * Sets whether the context map should allow access to all details. By default only the message body and headers can
     * be accessed. This option can be enabled for full access to the current Exchange and CamelContext. Doing so impose
     * a potential security risk as this opens access to the full power of CamelContext API.
     */
    public void setAllowContextMapAll(boolean allowContextMapAll) {
        this.allowContextMapAll = allowContextMapAll;
    }

    public boolean isLocalizedLookup() {
        return localizedLookup;
    }

    /**
     * Enables/disables localized template lookup. Disabled by default.
     */
    public void setLocalizedLookup(boolean localizedLookup) {
        this.localizedLookup = localizedLookup;
    }

    private Configuration getNoCacheConfiguration() {
        lock.lock();
        try {
            if (noCacheConfiguration == null) {
                // create a clone of the regular configuration
                noCacheConfiguration = (Configuration) getConfiguration().clone();
                // set this one to not use cache
                noCacheConfiguration.setCacheStorage(new NullCacheStorage());
            }
            return noCacheConfiguration;
        } finally {
            lock.unlock();
        }
    }

}
