package ru.yandex.tasktreker.adapter;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;


public class DurationTypeAdapter extends TypeAdapter<Duration> implements JsonSerializer<Duration>,
        JsonDeserializer<Duration> {
    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        out.value(value.toString());
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        return Duration.parse(in.nextString());
    }

    @Override
    public Duration deserialize(JsonElement jsonElement, Type type,
                                JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Duration.parse(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Duration duration, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(duration.toString());
    }
}