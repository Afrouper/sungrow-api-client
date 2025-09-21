package de.afrouper.server.sungrow.api;

import com.google.gson.*;
import de.afrouper.server.sungrow.api.dto.DeviceFaultStatus;
import de.afrouper.server.sungrow.api.dto.DevicePoint;
import de.afrouper.server.sungrow.api.dto.DeviceStatus;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DevicePointAdapter implements JsonDeserializer<DevicePoint>, JsonSerializer<DevicePoint> {

    @Override
    public DevicePoint deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jso = json.getAsJsonObject();
        if (jso.has("device_point")) {
            jso = jso.getAsJsonObject("device_point");
        }

        if (jso == null) {
            return null;
        }

        // bekannte Felder
        DeviceFaultStatus faultStatus = context.deserialize(jso.get("dev_fault_status"), DeviceFaultStatus.class);
        DeviceStatus deviceStatus = context.deserialize(jso.get("dev_status"), DeviceStatus.class);
        String name = context.deserialize(jso.get("device_name"), String.class);
        String serial = context.deserialize(jso.get("device_sn"), String.class);
        String uuid = context.deserialize(jso.get("uuid"), String.class);
        Long deviceUpdateTime = context.deserialize(jso.get("device_time"), Long.class);

        Map<String, String> pointIds = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jso.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (key.matches("p\\d+") && value != null && value.isJsonPrimitive()) {
                pointIds.put(key, value.getAsString());
            }
        }

        return new DevicePoint(faultStatus, deviceStatus, name, serial, deviceUpdateTime, uuid, pointIds);
    }


    @Override
    public JsonElement serialize(DevicePoint src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        // bekannte Felder wieder rausschreiben
        obj.add("dev_fault_status", context.serialize(src.deviceFaultStatus()));
        obj.add("dev_status", context.serialize(src.deviceStatus()));
        obj.add("device_name", context.serialize(src.name()));
        obj.add("device_sn", context.serialize(src.serial()));
        obj.add("device_time", src.deviceUpdateTime() != null
                ? new JsonPrimitive(src.deviceUpdateTime().toString())
                : JsonNull.INSTANCE);
        obj.add("uuid", src.uuid() != null ? new JsonPrimitive(src.uuid()) : JsonNull.INSTANCE);

        // dynamische Felder mitschreiben
        if (src.pointIds() != null) {
            for (Map.Entry<String, String> entry : src.pointIds().entrySet()) {
                obj.addProperty(entry.getKey(), entry.getValue());
            }
        }

        return obj;
    }
}
