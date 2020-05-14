package ru.javawebinar.basejava.util;

import com.google.gson.*;

import java.lang.reflect.Type;

public class SectionClassAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private static final String CLASSNAME = "Classname";
    private static final String INSTANCE = "Instance";

    @Override
    public JsonElement serialize(T section, Type type, JsonSerializationContext context) {
        JsonObject returnValue = new JsonObject();
        returnValue.addProperty(CLASSNAME, section.getClass().getName());
        JsonElement element = context.serialize(section);
        returnValue.add(INSTANCE, element);
        return returnValue;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
        JsonPrimitive primitive = obj.getAsJsonPrimitive(CLASSNAME);
        String className = primitive.getAsString();
        try {
            Class clazz = Class.forName(className);
            return context.deserialize(obj.get(INSTANCE), clazz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
