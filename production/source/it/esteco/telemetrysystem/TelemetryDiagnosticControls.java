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

        telemetryClient.disconnect();

        int retryLeft = 3;
        while (telemetryClient.getOnlineStatus() == false && retryLeft > 0) {
            telemetryClient.connect(DIAGNOSTIC_CHANNEL_CONNECTION_STRING);
            retryLeft -= 1;
        }

        if (telemetryClient.getOnlineStatus() == false) {
            throw new Exception("Unable to connect.");
        }

        telemetryClient.send(DIAGNOSTIC_MESSAGE);
        diagnosticInfo = telemetryClient.receive();
    }

}
