package com.example.sipclient.config;

import java.time.Duration;
import java.util.Objects;

/**
 * Immutable configuration that captures how the SIP客户端 connects to MSS.
 */
public final class SipConfig {

    private final String userAddress;
    private final String password;
    private final String localIp;
    private final int localPort;
    private final Duration registerTimeout;

    private SipConfig(Builder builder) {
        this.userAddress = builder.userAddress;
        this.password = builder.password;
        this.localIp = builder.localIp;
        this.localPort = builder.localPort;
        this.registerTimeout = builder.registerTimeout == null ? Duration.ofSeconds(10) : builder.registerTimeout;
        validate();
    }

    private void validate() {
        Objects.requireNonNull(userAddress, "userAddress");
        Objects.requireNonNull(password, "password");
        Objects.requireNonNull(localIp, "localIp");
        Objects.requireNonNull(registerTimeout, "registerTimeout");
        if (localPort <= 0 || localPort > 65535) {
            throw new IllegalArgumentException("localPort must be within UDP port range");
        }
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getPassword() {
        return password;
    }

    public String getLocalIp() {
        return localIp;
    }

    public int getLocalPort() {
        return localPort;
    }

    public Duration getRegisterTimeout() {
        return registerTimeout;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String userAddress;
        private String password;
        private String localIp = "127.0.0.1";
        private int localPort = 5070;
        private Duration registerTimeout = Duration.ofSeconds(10);

        private Builder() {
        }

        public Builder userAddress(String userAddress) {
            this.userAddress = userAddress;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder localIp(String localIp) {
            this.localIp = localIp;
            return this;
        }

        public Builder localPort(int localPort) {
            this.localPort = localPort;
            return this;
        }

        public Builder registerTimeout(Duration timeout) {
            this.registerTimeout = timeout;
            return this;
        }

        public SipConfig build() {
            return new SipConfig(this);
        }
    }
}
