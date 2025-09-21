package de.afrouper.server.sungrow.api;

import com.google.gson.*;
import de.afrouper.server.sungrow.api.dto.DevicePointInfo;
import de.afrouper.server.sungrow.api.dto.DevicePointInfoList;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class DevicePointInfoListAdapter implements JsonSerializer<DevicePointInfoList>, JsonDeserializer<DevicePointInfoList> {

    @Override
    public DevicePointInfoList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        json = json.getAsJsonObject().get("pageList");
        if (json == null || json.isJsonNull()) {
            return null;
        }

        if (!json.isJsonArray()) {
            throw new JsonParseException("Expected JSON array for DevicePointInfo list");
        }

        JsonArray arr = json.getAsJsonArray();
        Map<String, DevicePointInfo> devicePointInfoMap = new HashMap<>(arr.size());
        for (JsonElement el : arr) {
            DevicePointInfo info = context.deserialize(el, DevicePointInfo.class);
            devicePointInfoMap.put(info.pointId(), info);
        }
        return new DevicePointInfoList(devicePointInfoMap);
    }

    @Override
    public JsonElement serialize(DevicePointInfoList src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.add("pageList", context.serialize(src.devicePointInfoList()));
        return obj;
    }
}
