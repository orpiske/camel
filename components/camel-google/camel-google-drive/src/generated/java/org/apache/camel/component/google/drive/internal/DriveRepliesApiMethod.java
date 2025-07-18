/*
 * Camel ApiMethod Enumeration generated by camel-api-component-maven-plugin
 */
package org.apache.camel.component.google.drive.internal;

import java.lang.reflect.Method;
import java.util.List;

import com.google.api.services.drive.Drive.Replies;

import org.apache.camel.support.component.ApiMethod;
import org.apache.camel.support.component.ApiMethodArg;
import org.apache.camel.support.component.ApiMethodImpl;

import static org.apache.camel.support.component.ApiMethodArg.arg;
import static org.apache.camel.support.component.ApiMethodArg.setter;

/**
 * Camel {@link ApiMethod} Enumeration for com.google.api.services.drive.Drive$Replies
 */
public enum DriveRepliesApiMethod implements ApiMethod {

    CREATE(
        com.google.api.services.drive.Drive.Replies.Create.class,
        "create",
        arg("fileId", String.class),
        arg("commentId", String.class),
        arg("content", com.google.api.services.drive.model.Reply.class)),

    DELETE(
        com.google.api.services.drive.Drive.Replies.Delete.class,
        "delete",
        arg("fileId", String.class),
        arg("commentId", String.class),
        arg("replyId", String.class)),

    GET(
        com.google.api.services.drive.Drive.Replies.Get.class,
        "get",
        arg("fileId", String.class),
        arg("commentId", String.class),
        arg("replyId", String.class),
        setter("includeDeleted", Boolean.class)),

    LIST(
        com.google.api.services.drive.Drive.Replies.List.class,
        "list",
        arg("fileId", String.class),
        arg("commentId", String.class),
        setter("includeDeleted", Boolean.class),
        setter("pageSize", Integer.class),
        setter("pageToken", String.class)),

    UPDATE(
        com.google.api.services.drive.Drive.Replies.Update.class,
        "update",
        arg("fileId", String.class),
        arg("commentId", String.class),
        arg("replyId", String.class),
        arg("content", com.google.api.services.drive.model.Reply.class));

    private final ApiMethod apiMethod;

    DriveRepliesApiMethod(Class<?> resultType, String name, ApiMethodArg... args) {
        this.apiMethod = new ApiMethodImpl(Replies.class, resultType, name, args);
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
