package net.minespree.mango.serialization;

import com.google.common.base.Preconditions;
import com.google.gson.*;

/**
 * @since 09/02/2018
 */
public final class JsonUtils {
    private JsonUtils() {}

    private static final Gson GSON = new Gson();
    private static final Gson PRETTY_GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser PARSER = new JsonParser();

    public static Gson gson() {
        return GSON;
    }

    public static Gson pretty() {
        return PRETTY_GSON;
    }

    /**
     * @throws JsonSyntaxException if the JSON data isn't valid
     */
    public static JsonElement parse(String data) {
        Preconditions.checkNotNull(data);
        return PARSER.parse(data);
    }

    public static String toJson(Object source) {
        return GSON.toJson(source);
    }

    public static JsonObject parseObject(String data) {
        return parse(data).getAsJsonObject();
    }

    public static JsonArray parseArray(String data) {
        return parse(data).getAsJsonArray();
    }

    public static String prettify(String ugly) {
        return PRETTY_GSON.toJson(PRETTY_GSON.fromJson(ugly, JsonElement.class));
    }
}
