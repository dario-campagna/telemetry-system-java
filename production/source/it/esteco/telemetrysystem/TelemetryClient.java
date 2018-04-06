package it.esteco.telemetrysystem;

public interface TelemetryClient {
    boolean getOnlineStatus();

    boolean connect(String telemetryServerConnectionString);

    void disconnect();

    void send(String message);

    String receive();
}
