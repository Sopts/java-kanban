package ru.yandex.tasktreker.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {


    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public JsonElement serialize(LocalDateTime dateTime, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        String formattedDateTime = dateTime.format(formatter);
        return new JsonPrimitive(formattedDateTime);
    }

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type,
                                     JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String dateTimeString = jsonElement.getAsString();
        return LocalDateTime.parse(dateTimeString, formatter);
    }
}