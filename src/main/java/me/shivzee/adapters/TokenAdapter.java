package me.shivzee.adapters;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * The TokenAdapter tries to push bearerToken field during deserialization of the json
 *
 * @see com.google.gson.TypeAdapterFactory
 */
public class TokenAdapter implements TypeAdapterFactory {

    private String bearerToken;

    public TokenAdapter(String bearerToken){
        this.bearerToken = bearerToken;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        TypeAdapter<T> delegate = gson.getDelegateAdapter(this , typeToken);

        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter jsonWriter, T t) throws IOException {
                delegate.write(jsonWriter , t);
            }

            @Override
            public T read(JsonReader jsonReader) throws IOException {
                JsonElement json = JsonParser.parseReader(jsonReader);

                if(json.isJsonObject()){
                    JsonObject jsonObject = json.getAsJsonObject();
                    if(!jsonObject.has("bearerToken")){
                        jsonObject.addProperty("bearerToken" , bearerToken);
                    }
                }

                return delegate.fromJsonTree(json);
            }
        };
    }
}
