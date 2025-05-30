package com.example.ecoplant.converter;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageListConverter {
    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        if (value == null || value.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(value.split(",")));
    }

    @TypeConverter
    public static String fromList(ArrayList<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join(",", list);
    }
}
