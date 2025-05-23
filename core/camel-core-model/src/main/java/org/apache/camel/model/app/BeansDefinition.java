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
package org.apache.camel.model.app;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

import org.w3c.dom.Element;

import jdk.jfr.Description;
import org.apache.camel.model.BeanFactoryDefinition;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.RouteConfigurationDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RouteTemplateDefinition;
import org.apache.camel.model.TemplatedRouteDefinition;
import org.apache.camel.model.rest.RestConfigurationDefinition;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.annotations.DslProperty;
import org.apache.camel.spi.annotations.ExternalSchemaElement;

/**
 * Container for beans, routes, and more.
 *
 * Important this is only supported when using XML DSL with camel-xml-io-dsl. This is NOT for the classic old Spring XML
 * DSL used by Camel 1.x/2.x.
 */
@Metadata(label = "configuration")
@XmlRootElement(name = "beans")
@XmlType(propOrder = {
        "componentScanning",
        "beans",
        "springOrBlueprintBeans",
        "dataFormats",
        "restConfigurations",
        "rests",
        "routeConfigurations",
        "routeTemplates",
        "templatedRoutes",
        "routes"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class BeansDefinition {

    // This class is not meant to be used with Camel Java DSL, but it's needed to generate XML Schema and MX parser methods

    @XmlElement(name = "component-scan")
    private List<ComponentScanDefinition> componentScanning = new ArrayList<>();

    // this is a place for <bean> element definition, without conflicting with <bean> elements referring
    // to "bean processors"
    @XmlElement(name = "bean")
    private List<BeanFactoryDefinition> beans = new ArrayList<>();

    // support for legacy spring <beans> and blueprint <bean> files to be parsed and loaded
    // for migration and tooling effort (need to be in a single @XmlAnyElement as otherwise
    // this causes camel-spring-xml to generate an invalid XSD
    @ExternalSchemaElement(names = { "beans", "bean", "alias" }, names2 = "bean",
                           namespace = "http://www.springframework.org/schema/beans",
                           namespace2 = "http://www.osgi.org/xmlns/blueprint/v1.0.0",
                           documentElement = "beans",
                           documentElement2 = "blueprint")
    @XmlAnyElement
    private List<Element> springOrBlueprintBeans = new ArrayList<>();

    // the order comes from <camelContext> (org.apache.camel.spring.xml.CamelContextFactoryBean)
    // to make things less confusing, as it's not easy to simply tell JAXB to use <xsd:choice maxOccurs="unbounded">
    // over a set of unrelated elements

    // initially we'll be supporting only these elements which are parsed by
    // org.apache.camel.dsl.xml.io.XmlRoutesBuilderLoader in camel-xml-io-dsl

    @XmlElementWrapper(name = "dataFormats")
    @XmlElement(name = "dataFormat")
    @DslProperty(name = "dataFormats") // yaml-dsl
    @Description("Camel data formats")
    private List<DataFormatDefinition> dataFormats;
    @XmlElement(name = "restConfiguration")
    private List<RestConfigurationDefinition> restConfigurations = new ArrayList<>();
    @XmlElement(name = "rest")
    private List<RestDefinition> rests = new ArrayList<>();
    @XmlElement(name = "routeConfiguration")
    private List<RouteConfigurationDefinition> routeConfigurations = new ArrayList<>();
    @XmlElement(name = "routeTemplate")
    private List<RouteTemplateDefinition> routeTemplates = new ArrayList<>();
    @XmlElement(name = "templatedRoute")
    private List<TemplatedRouteDefinition> templatedRoutes = new ArrayList<>();
    @XmlElement(name = "route")
    private List<RouteDefinition> routes = new ArrayList<>();

    public List<ComponentScanDefinition> getComponentScanning() {
        return componentScanning;
    }

    /**
     * Component scanning that can auto-discover Camel route builders from the classpath.
     */
    public void setComponentScanning(List<ComponentScanDefinition> componentScanning) {
        this.componentScanning = componentScanning;
    }

    public List<BeanFactoryDefinition> getBeans() {
        return beans;
    }

    /**
     * List of bean
     */
    public void setBeans(List<BeanFactoryDefinition> beans) {
        this.beans = beans;
    }

    public List<Element> getSpringOrBlueprintBeans() {
        return springOrBlueprintBeans;
    }

    /**
     * Support for legacy Spring beans and Blueprint bean files to be parsed and loaded for migration and tooling
     * effort.
     */
    public void setSpringOrBlueprintBeans(List<Element> springOrBlueprintBeans) {
        this.springOrBlueprintBeans = springOrBlueprintBeans;
    }

    public List<RestConfigurationDefinition> getRestConfigurations() {
        return restConfigurations;
    }

    /**
     * Camel Rest DSL Configuration
     */
    public void setRestConfigurations(List<RestConfigurationDefinition> restConfigs) {
        this.restConfigurations = restConfigs;
    }

    public List<RestDefinition> getRests() {
        return rests;
    }

    /**
     * Camel Rest DSL
     */
    public void setRests(List<RestDefinition> rests) {
        this.rests = rests;
    }

    public List<RouteConfigurationDefinition> getRouteConfigurations() {
        return routeConfigurations;
    }

    /**
     * Camel route configurations
     */
    public void setRouteConfigurations(List<RouteConfigurationDefinition> routeConfigurations) {
        this.routeConfigurations = routeConfigurations;
    }

    public List<RouteTemplateDefinition> getRouteTemplates() {
        return routeTemplates;
    }

    /**
     * Camel route templates
     */
    public void setRouteTemplates(List<RouteTemplateDefinition> routeTemplates) {
        this.routeTemplates = routeTemplates;
    }

    public List<TemplatedRouteDefinition> getTemplatedRoutes() {
        return templatedRoutes;
    }

    /**
     * Camel routes to be created from template
     */
    public void setTemplatedRoutes(List<TemplatedRouteDefinition> templatedRoutes) {
        this.templatedRoutes = templatedRoutes;
    }

    public List<RouteDefinition> getRoutes() {
        return routes;
    }

    /**
     * Camel routes
     */
    public void setRoutes(List<RouteDefinition> routes) {
        this.routes = routes;
    }

    public List<DataFormatDefinition> getDataFormats() {
        return dataFormats;
    }

    /**
     * Camel data formats
     */
    public void setDataFormats(List<DataFormatDefinition> dataFormats) {
        this.dataFormats = dataFormats;
    }

}
