package it.esteco.telemetrysystem;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TelemetryDiagnosticControlsTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final String EXPECTED_DIAGNOSTIC_INFO = "LAST TX rate................ 100 MBPS\r\n"
            + "HIGHEST TX rate............. 100 MBPS\r\n"
            + "LAST RX rate................ 100 MBPS\r\n"
            + "HIGHEST RX rate............. 100 MBPS\r\n"
            + "BIT RATE.................... 100000000\r\n"
            + "WORD LEN.................... 16\r\n"
            + "WORD/FRAME.................. 511\r\n"
            + "BITS/FRAME.................. 8192\r\n"
            + "MODULATION TYPE............. PCM/FM\r\n"
            + "TX Digital Los.............. 0.75\r\n"
            + "RX Digital Los.............. 0.10\r\n"
            + "BEP Test.................... -5\r\n"
            + "Local Rtrn Count............ 00\r\n"
            + "Remote Rtrn Count........... 00";

    @Test
    public void sendDiagnosticMessageAndReceiveStatusResponse() throws Exception {
        TelemetryDiagnosticControls telemetryDiagnosticControls = new TelemetryDiagnosticControls(new FakeTelemetryClient());
        telemetryDiagnosticControls.checkTransmission();
        assertEquals(EXPECTED_DIAGNOSTIC_INFO, telemetryDiagnosticControls.getDiagnosticInfo());
    }

    @Test
    public void receiveResponse() throws Exception {
        TelemetryClient telemetryClient = mock(TelemetryClient.class);
        when(telemetryClient.getOnlineStatus()).thenReturn(true);
        when(telemetryClient.receive()).thenReturn("a");

        TelemetryDiagnosticControls telemetryDiagnosticControls = new TelemetryDiagnosticControls(telemetryClient);

        telemetryDiagnosticControls.checkTransmission();

        inOrder(telemetryClient).verify(telemetryClient).disconnect();
        inOrder(telemetryClient).verify(telemetryClient, times(2)).getOnlineStatus();
        inOrder(telemetryClient).verify(telemetryClient).send(anyString());
        inOrder(telemetryClient).verify(telemetryClient).receive();

        assertEquals("a", telemetryDiagnosticControls.getDiagnosticInfo());
    }

    @Test
    public void connectionFail() throws Exception {
        expectedException.expect(Exception.class);
        expectedException.expectMessage("Unable to connect.");

        TelemetryClient telemetryClient = mock(TelemetryClient.class);
        when(telemetryClient.getOnlineStatus()).thenReturn(false);

        TelemetryDiagnosticControls telemetryDiagnosticControls = new TelemetryDiagnosticControls(telemetryClient);

        telemetryDiagnosticControls.checkTransmission();

        inOrder(telemetryClient).verify(telemetryClient).disconnect();
        inOrder(telemetryClient).verify(telemetryClient, times(4)).getOnlineStatus();
    }
}