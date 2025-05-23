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
package org.apache.camel.component.as2.api.entity;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.camel.component.as2.api.AS2MediaType;
import org.apache.camel.component.as2.api.CanonicalOutputStream;
import org.apache.camel.util.ObjectHelper;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;

public class TextPlainEntity extends MimeEntity {

    private String content;

    public TextPlainEntity(String content, String charset, String contentTransferEncoding, boolean isMainBody) {
        super(ContentType.create(AS2MediaType.TEXT_PLAIN, charset), contentTransferEncoding);
        this.content = ObjectHelper.notNull(content, "Content");
        setMainBody(isMainBody);
    }

    public String getText() {
        return content;
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        NoCloseOutputStream ncos = new NoCloseOutputStream(outstream);
        try (CanonicalOutputStream canonicalOutstream = new CanonicalOutputStream(ncos, StandardCharsets.US_ASCII.name())) {

            // Write out mime part headers if this is not the main body of message.
            if (!isMainBody()) {
                for (Header header : getAllHeaders()) {
                    canonicalOutstream.writeln(header.toString());
                }
                canonicalOutstream.writeln(); // ensure empty line between headers and body; RFC2046 - 5.1.1
            }

            // Write out content
            outstream.write(content.getBytes(StandardCharsets.US_ASCII), 0, content.length());
        }
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
}
