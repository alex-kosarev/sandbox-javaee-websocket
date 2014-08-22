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
package org.acruxsource.sandbox.websocket.session;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import org.acruxsource.sandbox.websocket.domain.ChatMessage;
import org.acruxsource.sandbox.websocket.domain.ChatParticipantsMessage;
import org.acruxsource.sandbox.websocket.domain.LoginMessage;
import org.acruxsource.sandbox.websocket.domain.PrivateChatMessage;
import org.acruxsource.sandbox.websocket.domain.ServerActionMessage;

/**
 *
 * @author Alexander Kosarev <akosarev@acruxsource.org>
 */
@Named
@ApplicationScoped
public class WebsocketSessionHolder {

    private final Set<Session> SESSIONS = Collections.synchronizedSet(new HashSet<Session>());
    private final Map<String, Set<Session>> SESSIONS_BY_USERNAME = Collections.synchronizedMap(new HashMap<String, Set<Session>>());

    public void addSession(Session session) {
        SESSIONS.add(session);
    }

    public void removeSession(Session session) {
        SESSIONS.remove(session);
    }

    public void sendMessage(ChatMessage chatMessage) throws IOException, EncodeException {
        for (Session session : SESSIONS) {
            session.getBasicRemote().sendObject(chatMessage);
        }
    }
    
    public void sendServerActionMessage(ServerActionMessage serverActionMessage) throws IOException, EncodeException {
        for (Session session : SESSIONS) {
            session.getBasicRemote().sendObject(serverActionMessage);
        }
    }

    public void sendLoginMessage(LoginMessage loginMessage, Session session) throws IOException, EncodeException {
        if (!SESSIONS_BY_USERNAME.containsKey(loginMessage.getSender())) {
            SESSIONS_BY_USERNAME.put(loginMessage.getSender(), Collections.synchronizedSet(new HashSet<Session>()));
        }

        SESSIONS_BY_USERNAME.get(loginMessage.getSender()).add(session);

        sendMessage(new ChatMessage("server", loginMessage.getSender() + " entered chat room", new Date()));
    }

    public void sendPrivateMessage(PrivateChatMessage userChatMessage) throws IOException, EncodeException {
        if (!SESSIONS_BY_USERNAME.containsKey(userChatMessage.getReceiver())) {
            SESSIONS_BY_USERNAME.put(userChatMessage.getReceiver(), Collections.synchronizedSet(new HashSet<Session>()));
        }

        for (Session session : SESSIONS_BY_USERNAME.get(userChatMessage.getReceiver())) {
            session.getBasicRemote().sendObject(userChatMessage);
        }
        
        if (!SESSIONS_BY_USERNAME.containsKey(userChatMessage.getSender())) {
            SESSIONS_BY_USERNAME.put(userChatMessage.getSender(), Collections.synchronizedSet(new HashSet<Session>()));
        }

        for (Session session : SESSIONS_BY_USERNAME.get(userChatMessage.getSender())) {
            session.getBasicRemote().sendObject(userChatMessage);
        }
    }
    
    public void sendChatParticipantsMessage() throws IOException, EncodeException {
        for (Session session : SESSIONS) {
            session.getBasicRemote().sendObject(new ChatParticipantsMessage(SESSIONS_BY_USERNAME.keySet().toArray(), "server", new Date()));
        }
    }
}
