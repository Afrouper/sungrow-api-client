package de.afrouper.server.sungrow.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.afrouper.server.sungrow.api.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class SerializationTests {

    private final Gson gson;

    SerializationTests() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(DevicePoint.class, new DevicePointAdapter())
                .registerTypeAdapter(DevicePointInfoList.class, new DevicePointInfoListAdapter())
                .create();
    }

    @Test
    void localDateTimeAdapter() {
        String json = "{\"time\":\"2025-08-01T19:42:55+08:00\"}";

        TimeObject time = gson.fromJson(json, TimeObject.class);
        Assertions.assertEquals(Month.AUGUST, time.time.getMonth());
        Assertions.assertEquals(1, time.time.getDayOfMonth());

        String jsonString = gson.toJson(time);
        Assertions.assertTrue(jsonString.contains("2025-08-01"));
    }

    @Test
    void deviceList() {
        Map<String, DevicePointInfo> points = new HashMap<>();
        points.put("p1", new DevicePointInfo(
                "p1",
                "Remark",
                "storageUnit",
                "Show Unit",
                DeviceType.Inverter,
                "Point Name"));
        DevicePointInfoList pointInfoList = new DevicePointInfoList(points);

        String json = gson.toJson(pointInfoList);
        Assertions.assertTrue(json.contains("storageUnit"));
        DevicePointInfoList newPointInfoList = gson.fromJson(json, DevicePointInfoList.class);

        Assertions.assertEquals(pointInfoList, newPointInfoList);
    }

    @Test
    void devicePoint() {
        DevicePoint devicePoint = new DevicePoint(
                DeviceFaultStatus.Normal,
                DeviceStatus.Deployed,
                "Point Name",
                "Point serial",
                42L,
                "uuid",
                Map.of("p1", "v1")
        );

        String json = gson.toJson(devicePoint);
        Assertions.assertTrue(json.contains("Point serial"));
        DevicePoint newDevicePoint = gson.fromJson(json, DevicePoint.class);

        Assertions.assertEquals(devicePoint, newDevicePoint);
    }

    private static class TimeObject {
        private LocalDateTime time;
    }
}
