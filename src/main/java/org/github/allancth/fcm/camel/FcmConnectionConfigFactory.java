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