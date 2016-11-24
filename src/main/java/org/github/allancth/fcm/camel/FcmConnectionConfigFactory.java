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

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.jivesoftware.smack.ConnectionConfiguration;

public class FcmConnectionConfigFactory {

    private String host;

    private int port;

    private String serviceName;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public ConnectionConfiguration create() throws NoSuchAlgorithmException, KeyManagementException {
        final ConnectionConfiguration connectionConfig = new ConnectionConfiguration(host, port, serviceName);
        connectionConfig.setRosterLoadedAtLogin(false);
        connectionConfig.setSendPresence(false);

        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, new java.security.SecureRandom());
        connectionConfig.setSocketFactory(sslContext.getSocketFactory());

        return connectionConfig;
    }
}
