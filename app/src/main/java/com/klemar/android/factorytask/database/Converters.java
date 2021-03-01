package com.klemar.android.factorytask.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.klemar.android.factorytask.model.SourceModel;

import org.threeten.bp.OffsetDateTime;

import java.sql.Date;

public class Converters {

    @TypeConverter
    public static SourceModel getSource(String value) {
        return new Gson().fromJson(value, SourceModel.class);
    }

    @TypeConverter
    public static String saveSource(SourceModel source) {
        return new Gson().toJson(source, SourceModel.class);
    }

    @TypeConverter
    public static OffsetDateTime fromStringToOffsetDateTime(String value) {
        return value == null ? null : OffsetDateTime.parse(value);
    }

    @TypeConverter
    public static String fromOffsetDateTimeToString(OffsetDateTime date) {
        return date == null ? null : date.toString();
    }

}
