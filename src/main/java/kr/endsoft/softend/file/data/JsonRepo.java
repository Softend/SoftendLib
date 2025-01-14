package kr.endsoft.softend.file.data;

import com.google.gson.JsonObject;

public abstract class JsonRepo<T> {

    public abstract JsonObject toJson(T data);
    public abstract T fromJson(JsonObject json);

}
