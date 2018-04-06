package it.esteco.telemetrysystem;

public class TelemetryDiagnosticControls {

    public static final String DIAGNOSTIC_MESSAGE = "AT#UD";
    private static final String DIAGNOSTIC_CHANNEL_CONNECTION_STRING = "*111#";

    private final TelemetryClient telemetryClient;
    private String diagnosticInfo = "";

    public TelemetryDiagnosticControls(TelemetryClient telemetryClient) {
        this.telemetryClient = telemetryClient;
    }

    public String getDiagnosticInfo() {
        return diagnosticInfo;
    }

    public void setDiagnosticInfo(String diagnosticInfo) {
        this.diagnosticInfo = diagnosticInfo;
    }

    public void checkTransmission() throws Exception {
        diagnosticInfo = "";

        boolean isConnected = telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);

        if (isConnected) {
            telemetryClient.send(DIAGNOSTIC_MESSAGE);
            diagnosticInfo = telemetryClient.receive();
        } else {
            throw new Exception("Unable to connect.");
        }
    }

}
