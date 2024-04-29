package sit707_week7;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class BodyTemperatureMonitorTest {

    @Test
    public void testStudentIdentity() {
        String studentId = "221270936";
        Assert.assertNotNull("Student ID is null", studentId);
    }

    @Test
    public void testStudentName() {
        String studentName = "Seth";
        Assert.assertNotNull("Student name is null", studentName);
    }
	
    @Test
    public void testReadTemperatureNegative() {
        TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(-1.0);

        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = temperatureMonitor.readTemperature();

        Assert.assertEquals(-1.0, temperature, 0.01);
    }

    @Test
    public void testReadTemperatureZero() {
        TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(0.0);

        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = temperatureMonitor.readTemperature();

        Assert.assertEquals(0.0, temperature, 0.01);
    }

    @Test
    public void testReadTemperatureNormal() {
        TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(36.5);

        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = temperatureMonitor.readTemperature();

        Assert.assertEquals(36.5, temperature, 0.01);
    }

    @Test
    public void testReadTemperatureAbnormallyHigh() {
        TemperatureSensor temperatureSensor = Mockito.mock(TemperatureSensor.class);
        Mockito.when(temperatureSensor.readTemperatureValue()).thenReturn(40.0);

        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(temperatureSensor, null, null);
        double temperature = temperatureMonitor.readTemperature();

        Assert.assertEquals(40.0, temperature, 0.01);
    }

    @Test
    public void testReportTemperatureReadingToCloud() {
        TemperatureReading temperatureReading = new TemperatureReading();
        CloudService cloudService = Mockito.mock(CloudService.class);

        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(null, cloudService, null);
        temperatureMonitor.reportTemperatureReadingToCloud(temperatureReading);

        Mockito.verify(cloudService).sendTemperatureToCloud(temperatureReading);
    }

    @Test
    public void testInquireBodyStatusNormalNotification() {
        CloudService cloudService = Mockito.mock(CloudService.class);
        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("NORMAL");

        NotificationSender notificationSender = Mockito.mock(NotificationSender.class);

        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);
        temperatureMonitor.inquireBodyStatus();

        Mockito.verify(notificationSender).sendEmailNotification(Mockito.any(Customer.class), Mockito.anyString());
    }

    @Test
    public void testInquireBodyStatusAbnormalNotification() {
        CloudService cloudService = Mockito.mock(CloudService.class);
        Mockito.when(cloudService.queryCustomerBodyStatus(Mockito.any(Customer.class))).thenReturn("ABNORMAL");

        NotificationSender notificationSender = Mockito.mock(NotificationSender.class);

        BodyTemperatureMonitor temperatureMonitor = new BodyTemperatureMonitor(null, cloudService, notificationSender);
        temperatureMonitor.inquireBodyStatus();

        Mockito.verify(notificationSender).sendEmailNotification(Mockito.any(FamilyDoctor.class), Mockito.anyString());
    }
}
