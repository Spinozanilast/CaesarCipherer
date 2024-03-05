package com.caesarcipherer.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.caesarcipherer.historyComponents.HistoryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс DBHelper для работы с базой данных SQLite в Android.
 *
 * @author Жегздринь А.Р.
 * @version 1.0
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    /**
     * Имя базы данных.
     */
    private static final String DATABASE_NAME = "caesarCipherer.db";

    /**
     * Версия базы данных.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Имя таблицы.
     */
    public static final String TABLE_NAME = "CiphererHistory";

    /**
     * Идентификатор столбца.
     */
    public static final String COLUMN_ID = "_id";

    /**
     * Столбец для текста, который нужно обработать.
     */
    public static final String COLUMN_TEXT_TO_PROCESS = "text_to_process";

    /**
     * Столбец для обработанного текста.
     */
    public static final String COLUMN_PROCESSED_TEXT = "processed_text";

    /**
     * Столбец для определения, является ли операция шифрованием.
     */
    public static final String COLUMN_IS_ENCRYPTING = "is_encrypting";

    /**
     * Столбец для даты создания записи.
     */
    public static final String COLUMN_CREATED_AT = "created_at";

    /**
     * Скрипт для создания базы данных.
     */
    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + TABLE_NAME + " (" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TEXT_TO_PROCESS
            + " text, " + COLUMN_PROCESSED_TEXT + " text, " + COLUMN_IS_ENCRYPTING + " integer, "
            + COLUMN_CREATED_AT + " datetime default current_timestamp);";

    /**
     * Конструктор класса DBHelper.
     *
     * @param context контекст приложения.
     */
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Метод вызывается при создании базы данных.
     *
     * @param db экземпляр базы данных.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    /**
     * Метод вызывается при обновлении версии базы данных.
     *
     * @param db         экземпляр базы данных.
     * @param oldVersion старая версия базы данных.
     * @param newVersion новая версия базы данных.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Метод для добавления записи в базу данных.
     *
     * @param textToProcess текст для обработки.
     * @param processedText обработанный текст.
     * @param isEncrypting  флаг, указывающий, является ли операция шифрованием.
     */
    public void insertData(String textToProcess, String processedText, int isEncrypting) {
        if (isEncrypting != 0 && isEncrypting != 1) {
            throw new IllegalArgumentException("Value must be 0 or 1");
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TEXT_TO_PROCESS, textToProcess);
        contentValues.put(COLUMN_PROCESSED_TEXT, processedText);
        contentValues.put(COLUMN_IS_ENCRYPTING, isEncrypting);
        db.insert(TABLE_NAME, null, contentValues);
    }

    /**
     * Метод для получения первых трех записей из базы данных.
     *
     * @return Список первых трех элементов истории.
     */
    public List<HistoryItem> getFirstThreeRecords() {
        List<HistoryItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_ID + " ASC", "3");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String textToProcess = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT_TO_PROCESS));
                String processedText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROCESSED_TEXT));
                int isEncrypting = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ENCRYPTING));
                String dateOfCreating = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT));

                HistoryItem item = new HistoryItem(id, textToProcess, processedText, isEncrypting != 0, dateOfCreating);

                items.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return items;
    }

    /**
     * Метод для получения всей истории из базы данных.
     *
     * @return Список всех элементов истории.
     */
    public List<HistoryItem> getAllHistory() {
        List<HistoryItem> items = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_ID);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String textToProcess = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT_TO_PROCESS));
                String processedText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROCESSED_TEXT));
                int isEncrypting = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_ENCRYPTING));
                String dateOfCreating = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT));

                HistoryItem item = new HistoryItem(id, textToProcess, processedText, isEncrypting != 0, dateOfCreating);

                items.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return items;
    }

    /**
     * Метод для очистки данных таблицы.
     *
     * @param db Объект SQLiteDatabase для взаимодействия с базой данных.
     */
    public void clearTableData(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + TABLE_NAME + "'");
    }
}
