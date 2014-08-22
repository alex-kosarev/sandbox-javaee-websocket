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

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Alexander Kosarev <akosarev@acruxsource.org>
 */
@ServerEndpoint(value = "/sandbox")
public class SimpleSandboxEndpoint {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("New Session opened");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        session.getBasicRemote().sendText("Client wrote text: " + message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("Error occured: " + throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Session closed: " + closeReason.getReasonPhrase());
    }

}
