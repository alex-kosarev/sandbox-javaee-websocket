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
package org.acruxsource.sandbox.websocket;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.acruxsource.sandbox.websocket.decoders.MessageTextStreamDecoder;
import org.acruxsource.sandbox.websocket.domain.ChatMessage;
import org.acruxsource.sandbox.websocket.domain.LoginMessage;
import org.acruxsource.sandbox.websocket.domain.Message;
import org.acruxsource.sandbox.websocket.domain.PrivateChatMessage;
import org.acruxsource.sandbox.websocket.domain.ServerActionMessage;
import org.acruxsource.sandbox.websocket.encoders.ChatMessageEncoder;
import org.acruxsource.sandbox.websocket.encoders.ChatParticipantsMessageEncoder;
import org.acruxsource.sandbox.websocket.encoders.LoginMessageEncoder;
import org.acruxsource.sandbox.websocket.encoders.PrivateChatMessageEncoder;
import org.acruxsource.sandbox.websocket.encoders.ServerActionMessageEncoder;
import org.acruxsource.sandbox.websocket.session.WebsocketSessionHolder;

/**
 *
 * @author Alexander Kosarev <akosarev@acruxsource.org>
 * @since 2014-08-20
 */
@ServerEndpoint(
        value = "/chat",
        encoders = {
            ChatMessageEncoder.class,
            PrivateChatMessageEncoder.class,
            LoginMessageEncoder.class,
            ServerActionMessageEncoder.class,
            ChatParticipantsMessageEncoder.class
        },
        decoders = {
            MessageTextStreamDecoder.class
        }
)
public class EncodedSandboxEndpoint {

    @Inject
    private WebsocketSessionHolder websocketSessionHolder;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        websocketSessionHolder.addSession(session);
    }

    @OnMessage
    public void onMessage(Message message, Session session) {
        try {
            if (message instanceof PrivateChatMessage) {
                onMessage((PrivateChatMessage) message, session);
            } else if (message instanceof LoginMessage) {
                onMessage((LoginMessage) message, session);
            } else if (message instanceof ChatMessage) {
                websocketSessionHolder.sendMessage((ChatMessage) message);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void onMessage(PrivateChatMessage message, Session session) throws Exception {
        websocketSessionHolder.sendPrivateMessage(message);
    }

    public void onMessage(LoginMessage message, Session session) throws Exception {
        websocketSessionHolder.sendLoginMessage(message, session);
        websocketSessionHolder.sendServerActionMessage(new ServerActionMessage("login", message.getSender(), message.getSendDate()));
        websocketSessionHolder.sendChatParticipantsMessage();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error occured: " + throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        websocketSessionHolder.removeSession(session);
    }

}
