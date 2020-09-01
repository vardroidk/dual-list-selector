package com.vardroid.duallistselector.data.provider;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DataProvider<T> {

    private final String assetFileName;

    private final Gson gson;

    public DataProvider(
            String assetFileName) {
        this.assetFileName = assetFileName;
        this.gson = new Gson();
    }

    public List<T> getList(
            Context context,
            Class<T> type) {
        List<T> result;

        AssetManager assetManager = context.getAssets();
        try (
                InputStream inputStream = assetManager.open(assetFileName);
                Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            Type listType = TypeToken.getParameterized(List.class, type).getType();
            result = gson.fromJson(reader, listType);
        } catch (IOException e) {
            result = new ArrayList<>();
        }

        return result;
    }
}
