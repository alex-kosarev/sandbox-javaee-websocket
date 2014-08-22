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
package org.acruxsource.sandbox.websocket.domain;

import java.util.Date;

/**
 *
 * @author Alexander Kosarev <akosarev@acruxsource.org>
 */
public class PrivateChatMessage extends Message {

    private String receiver;
    private String message;

    public PrivateChatMessage() {
    }

    public PrivateChatMessage(String sender, String receiver, String message, Date sendDate) {
        super(sender, sendDate);
        this.receiver = receiver;
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UserChatMessage{" + "receiver=" + receiver + ", message=" + message + '}';
    }
}
