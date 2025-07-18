/*
 * Camel ApiMethod Enumeration generated by camel-api-component-maven-plugin
 */
package org.apache.camel.component.twilio.internal;

import java.lang.reflect.Method;
import java.util.List;

import com.twilio.rest.api.v2010.account.Message;

import org.apache.camel.support.component.ApiMethod;
import org.apache.camel.support.component.ApiMethodArg;
import org.apache.camel.support.component.ApiMethodImpl;

import static org.apache.camel.support.component.ApiMethodArg.arg;
import static org.apache.camel.support.component.ApiMethodArg.setter;

/**
 * Camel {@link ApiMethod} Enumeration for com.twilio.rest.api.v2010.account.Message
 */
public enum MessageApiMethod implements ApiMethod {

    CREATOR(
        com.twilio.rest.api.v2010.account.MessageCreator.class,
        "creator",
        arg("to", com.twilio.type.PhoneNumber.class),
        arg("from", com.twilio.type.PhoneNumber.class),
        arg("body", String.class)),

    CREATOR_1(
        com.twilio.rest.api.v2010.account.MessageCreator.class,
        "creator",
        arg("to", com.twilio.type.PhoneNumber.class),
        arg("from", com.twilio.type.PhoneNumber.class),
        arg("mediaUrl", java.util.List.class)),

    CREATOR_2(
        com.twilio.rest.api.v2010.account.MessageCreator.class,
        "creator",
        arg("to", com.twilio.type.PhoneNumber.class),
        arg("messagingServiceSid", String.class),
        arg("body", String.class)),

    CREATOR_3(
        com.twilio.rest.api.v2010.account.MessageCreator.class,
        "creator",
        arg("to", com.twilio.type.PhoneNumber.class),
        arg("messagingServiceSid", String.class),
        arg("mediaUrl", java.util.List.class)),

    CREATOR_4(
        com.twilio.rest.api.v2010.account.MessageCreator.class,
        "creator",
        arg("pathAccountSid", String.class),
        arg("to", com.twilio.type.PhoneNumber.class),
        arg("from", com.twilio.type.PhoneNumber.class),
        arg("body", String.class)),

    CREATOR_5(
        com.twilio.rest.api.v2010.account.MessageCreator.class,
        "creator",
        arg("pathAccountSid", String.class),
        arg("to", com.twilio.type.PhoneNumber.class),
        arg("from", com.twilio.type.PhoneNumber.class),
        arg("mediaUrl", java.util.List.class)),

    CREATOR_6(
        com.twilio.rest.api.v2010.account.MessageCreator.class,
        "creator",
        arg("pathAccountSid", String.class),
        arg("to", com.twilio.type.PhoneNumber.class),
        arg("messagingServiceSid", String.class),
        arg("body", String.class)),

    CREATOR_7(
        com.twilio.rest.api.v2010.account.MessageCreator.class,
        "creator",
        arg("pathAccountSid", String.class),
        arg("to", com.twilio.type.PhoneNumber.class),
        arg("messagingServiceSid", String.class),
        arg("mediaUrl", java.util.List.class)),

    DELETER(
        com.twilio.rest.api.v2010.account.MessageDeleter.class,
        "deleter",
        arg("pathSid", String.class)),

    DELETER_1(
        com.twilio.rest.api.v2010.account.MessageDeleter.class,
        "deleter",
        arg("pathAccountSid", String.class),
        arg("pathSid", String.class)),

    FETCHER(
        com.twilio.rest.api.v2010.account.MessageFetcher.class,
        "fetcher",
        arg("pathSid", String.class)),

    FETCHER_1(
        com.twilio.rest.api.v2010.account.MessageFetcher.class,
        "fetcher",
        arg("pathAccountSid", String.class),
        arg("pathSid", String.class)),

    READER(
        com.twilio.rest.api.v2010.account.MessageReader.class,
        "reader"),

    READER_1(
        com.twilio.rest.api.v2010.account.MessageReader.class,
        "reader",
        arg("pathAccountSid", String.class)),

    UPDATER(
        com.twilio.rest.api.v2010.account.MessageUpdater.class,
        "updater",
        arg("pathSid", String.class)),

    UPDATER_1(
        com.twilio.rest.api.v2010.account.MessageUpdater.class,
        "updater",
        arg("pathAccountSid", String.class),
        arg("pathSid", String.class));

    private final ApiMethod apiMethod;

    MessageApiMethod(Class<?> resultType, String name, ApiMethodArg... args) {
        this.apiMethod = new ApiMethodImpl(Message.class, resultType, name, args);
    }

    @Override
    public String getName() { return apiMethod.getName(); }

    @Override
    public Class<?> getResultType() { return apiMethod.getResultType(); }

    @Override
    public List<String> getArgNames() { return apiMethod.getArgNames(); }

    @Override
    public List<String> getSetterArgNames() { return apiMethod.getSetterArgNames(); }

    @Override
    public List<Class<?>> getArgTypes() { return apiMethod.getArgTypes(); }

    @Override
    public Method getMethod() { return apiMethod.getMethod(); }
}
