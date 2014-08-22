/*
 *
 * Copyright 2014 Alexander Kosarev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.acruxsource.sandbox.websocket.encoders;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import org.acruxsource.sandbox.websocket.domain.PrivateChatMessage;

/**
 *
 * @author Alexander Kosarev <akosarev@acruxsource.org>
 */
public class PrivateChatMessageEncoder implements Encoder.TextStream<PrivateChatMessage> {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String MESSAGE_CLASS_NAME = "org.acruxsource.sandbox.websocket.domain.PrivateChatMessage";

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public void destroy() {}

    @Override
    public void encode(PrivateChatMessage object, Writer writer) throws EncodeException, IOException {
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

        String jsonString = Json.createObjectBuilder()
                .add("_class", MESSAGE_CLASS_NAME)
                .add("message", Json.createObjectBuilder()
                        .add("sender", object.getSender())
                        .add("receiver", object.getReceiver())
                        .add("message", object.getMessage())
                        .add("sendDate", dateFormat.format(object.getSendDate())))
                .build()
                .toString();
        
        writer.write(jsonString);
    }

}
