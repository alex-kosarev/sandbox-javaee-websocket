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
package org.acruxsource.sandbox.websocket.decoders;

import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import org.acruxsource.sandbox.websocket.domain.ChatMessage;
import org.acruxsource.sandbox.websocket.domain.LoginMessage;
import org.acruxsource.sandbox.websocket.domain.Message;
import org.acruxsource.sandbox.websocket.domain.PrivateChatMessage;
import org.acruxsource.sandbox.websocket.encoders.ChatMessageEncoder;
import org.acruxsource.sandbox.websocket.encoders.LoginMessageEncoder;
import org.acruxsource.sandbox.websocket.encoders.PrivateChatMessageEncoder;

/**
 *
 * @author Alexander Kosarev <akosarev@acruxsource.org>
 */
public class MessageTextStreamDecoder implements Decoder.TextStream<Message> {

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Message decode(Reader r) throws DecodeException {
        Message message;
        try (JsonReader messageReader = Json.createReader(r)) {
            JsonObject messageObject = messageReader.readObject();
            switch (messageObject.getString("_class")) {
                case LoginMessageEncoder.MESSAGE_CLASS_NAME:
                    message = decodeLoginMessage(messageObject.getJsonObject("message"));
                    break;
                case ChatMessageEncoder.MESSAGE_CLASS_NAME:
                    message = decodeChatMessage(messageObject.getJsonObject("message"));
                    break;
                case PrivateChatMessageEncoder.MESSAGE_CLASS_NAME:
                    message = decodeUserChatMessage(messageObject.getJsonObject("message"));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown message type: " + messageReader.readObject().getString("class"));
            }
        }

        return message;
    }

    private ChatMessage decodeChatMessage(JsonObject messageObject) {
        ChatMessage chatMessage;
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        chatMessage = new ChatMessage();
        chatMessage.setSender(messageObject.getString("sender"));
        chatMessage.setMessage(messageObject.getString("message"));
        try {
            chatMessage.setSendDate(dateFormat.parse(messageObject.getString("sendDate")));
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        return chatMessage;
    }

    private PrivateChatMessage decodeUserChatMessage(JsonObject messageObject) {
        PrivateChatMessage chatMessage;
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        chatMessage = new PrivateChatMessage();
        chatMessage.setSender(messageObject.getString("sender"));
        chatMessage.setReceiver(messageObject.getString("receiver"));
        chatMessage.setMessage(messageObject.getString("message"));
        try {
            chatMessage.setSendDate(dateFormat.parse(messageObject.getString("sendDate")));
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        return chatMessage;
    }

    private LoginMessage decodeLoginMessage(JsonObject messageObject) {
        LoginMessage message;
        DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        message = new LoginMessage();
        message.setSender(messageObject.getString("sender"));
        try {
            message.setSendDate(dateFormat.parse(messageObject.getString("sendDate")));
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        return message;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

}
