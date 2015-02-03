package se.dev.iprytz.sqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by isak.prytz on 02/02/15 Week: 6.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "products.db";
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCTNAME = "productname";


    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COLUMN_PRODUCTNAME + " TEXT " +
                ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public void addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCTNAME, product.get_productName());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public int deleteProduct(String productName) {
        SQLiteDatabase db = getWritableDatabase();

        int rowsAffected = db.delete(TABLE_PRODUCTS, COLUMN_PRODUCTNAME + " = \"" + productName + "\"", null);

        return rowsAffected;
    }

    public ArrayList<String> databaseToStringArray() {
        ArrayList<String> products = new ArrayList<String>();
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1"; //1 = where every condition is met (all rows)

        //Cursor point to a location in results
        Cursor cursor = db.rawQuery(query, null);
        //move to first point in results
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("productname")) != null) {
                dbString = cursor.getString(cursor.getColumnIndex("productname"));
                products.add(dbString);
            }
            cursor.moveToNext();
        }
        db.close();

        return products;
    }

    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("productname")) != null) {
                dbString += cursor.getString(cursor.getColumnIndex("productname"));
                dbString += "\n";
            }
            cursor.moveToNext();
        }
        db.close();

        return dbString;
    }


    public ArrayList<Product> databaseToProductArray() {
        ArrayList<Product> products = new ArrayList<Product>();
        Product product;
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("productname")) != null) {
                product = new Product(cursor.getString(cursor.getColumnIndex("productname")));
                products.add(product);
            }
            cursor.moveToNext();
        }

        db.close();

        return products;
    }

}
