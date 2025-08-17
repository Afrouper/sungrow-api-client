package de.afrouper.server.sungrow.api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.*;
import java.time.format.DateTimeFormatter;

final class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String s = json.getAsString();
        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Exception e1) {
            try {
                Instant inst = Instant.parse(s);
                OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(inst, ZoneOffset.UTC);
                return offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            } catch (Exception e2) {
                try {
                    return LocalDateTime.parse(s);
                } catch (Exception e3) {
                    throw new JsonParseException("Cannot parse OffsetDateTime: " + s, e3);
                }
            }
        }
    }
}
