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
package org.github.allancth.fcm.camel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.component.xmpp.XmppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FcmProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(FcmProcessor.class);

    private static final String EXCHANGE_PROPERTY_OPERATION = "operation";

    private static final String MESSAGE_TYPE_ACK = "ack";
    
    private ObjectMapper objectMapper;

    public void setObjectMapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void unmarshal(final Exchange exchange) throws Exception {
        final org.apache.camel.Message in = (org.apache.camel.Message) exchange.getIn();
        final org.jivesoftware.smack.packet.Message message = (org.jivesoftware.smack.packet.Message) in.getHeader(XmppConstants.DOC_HEADER);
        final FcmPacketExtension fcmPacketExtension = message.getExtension(FcmPacketExtension.ELEMENT_NAME, FcmPacketExtension.NAMESPACE);
        final FcmMessage fcmMessage = objectMapper.readValue(fcmPacketExtension.getJson(), new TypeReference<FcmMessage>() {});

        exchange.setProperty(FcmMessage.FROM, fcmMessage.getFrom());
        exchange.setProperty(FcmMessage.MESSAGE_ID, fcmMessage.getMessage_id());

        if (fcmMessage.getMessage_type() == null) {
            in.setBody(fcmMessage);

        } else {
            exchange.setProperty(FcmProcessor.EXCHANGE_PROPERTY_OPERATION, fcmMessage.getMessage_type());
        }
    }

    public void marshal(final Exchange exchange) throws Exception {
        final org.apache.camel.Message in = exchange.getIn();
        final FcmMessage fcmMessage = (FcmMessage) in.getBody();

        final String registrationId = fcmMessage.getFrom();
        final Map<String, Object> data = fcmMessage.getData();

        final String name = (String) data.get("name");
        final String message = (String) data.get("message");
        final String type = (String) data.get("type");

        LOG.debug("[{}] We have just received a {} request from {}, and the message is '{}'", registrationId, type, name, message);

        final FcmMessage out = new FcmMessage();
        out.setTo(registrationId);
        out.setMessage_id(UUID.randomUUID().toString());
        out.setTime_to_live(600L);

        if ("MESSAGE".equals(type)) {
            out.addData("response", "Hello " + name + ". I've received your message '" + message + "'.");

        } else if ("NOTIFICATION".equals(type)) {
            out.setNotificationTitle("Greetings from camel-xmpp");
            out.setNotificationBody("Hello " + name + ".");
        }

        final String json = objectMapper.writeValueAsString(out);
        in.setBody(new FcmPacketExtension(json).toPacket());
    }

    public void ack(final Exchange exchange) throws Exception {
        final org.apache.camel.Message in = exchange.getIn();
        final String from = (String) exchange.getProperty(FcmMessage.FROM);
        final String messageId = (String) exchange.getProperty(FcmMessage.MESSAGE_ID);

        final FcmMessage fcmMessage = new FcmMessage();
        fcmMessage.setTo(from);
        fcmMessage.setMessage_id(messageId);
        fcmMessage.setMessage_type(FcmProcessor.MESSAGE_TYPE_ACK);

        final String json = objectMapper.writeValueAsString(fcmMessage);
        in.setBody(new FcmPacketExtension(json).toPacket());
    }

    @SuppressWarnings("unused")
    @JsonInclude(value = Include.NON_NULL)
    private static class FcmMessage {

        private static final String FROM = "from";

        private static final String MESSAGE_ID = "message_id";

        private String from;

        private String to;

        private Long time_to_live;

        private String message_id;

        private String message_type;

        private String category;

        private String error;

        private String collapse_key;

        private Boolean delay_while_idle;

        private String priority;

        private String control_type;

        private Map<String, Object> data = new HashMap<String, Object>();

        private Map<String, Object> notification = new HashMap<String, Object>();

        public String getFrom() {
            return from;
        }

        public void setFrom(final String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(final String to) {
            this.to = to;
        }

        public Long getTime_to_live() {
            return time_to_live;
        }

        public void setTime_to_live(final Long time_to_live) {
            this.time_to_live = time_to_live;
        }

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(final String message_id) {
            this.message_id = message_id;
        }

        public String getMessage_type() {
            return message_type;
        }

        public void setMessage_type(final String message_type) {
            this.message_type = message_type;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(final String category) {
            this.category = category;
        }

        public String getError() {
            return error;
        }

        public void setError(final String error) {
            this.error = error;
        }

        public String getCollapse_key() {
            return collapse_key;
        }

        public void setCollapse_key(final String collapse_key) {
            this.collapse_key = collapse_key;
        }

        public Boolean getDelay_while_idle() {
            return delay_while_idle;
        }

        public void setDelay_while_idle(final Boolean delay_while_idle) {
            this.delay_while_idle = delay_while_idle;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(final String priority) {
            this.priority = priority;
        }

        public String getControl_type() {
            return control_type;
        }

        public void setControl_type(final String control_type) {
            this.control_type = control_type;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(final Map<String, Object> data) {
            this.data.putAll(data);
        }

        public void addData(final String key, final Object value) {
            data.put(key, value);
        }

        public Object removeData(final String key) {
            return data.remove(key);
        }

        public Object removeData() {
            return data = null;
        }

        public Map<String, Object> getNotification() {
            return notification;
        }

        public void setNotification(final Map<String, Object> notification) {
            this.notification.putAll(notification);
        }

        public void setNotificationTitle(final String title) {
            notification.put("title", title);
        }

        public void setNotificationBody(final String body) {
            notification.put("body", body);
        }

        public void setNotificationIcon(final String icon) {
            notification.put("icon", icon);
        }

        public void setNotificationSound(final String sound) {
            notification.put("sound", sound);
        }

        public void setNotificationTag(final String tag) {
            notification.put("tag", tag);
        }

        public void setNotificationColor(final String color) {
            notification.put("color", color);
        }

        public void setNotificationClickAction(final String clickAction) {
            notification.put("click_action", clickAction);
        }
    }
}
