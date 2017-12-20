package top.alau.contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by alcanderian on 2017/12/19.
 */

public class ContactDb extends SQLiteOpenHelper {
    public static final String tb_contact = "contact";
    public static final String c_cid = "cid";
    public static final String c_name = "name";
    public static final String c_birth = "birth";
    public static final String c_gift = "gift";
    private static SQLiteDatabase db;

    public ContactDb(Context context) {
        super(context, context.getString(R.string.db_name), null, 1);
        if (db == null)
            db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE contact (cid INTEGER PRIMARY KEY, name TEXT, birth TEXT, gift TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insert(String name, String birth, String gift) {
        ContentValues cv = new ContentValues();
        cv.put(c_name, name);
        cv.put(c_birth, birth);
        cv.put(c_gift, gift);
        return (int) db.insert(tb_contact, null, cv);
    }

    public void update(Integer cid, String name, String birth, String gift) {
        String where = "cid = ?";
        String[] args = {cid.toString()};
        ContentValues cv = new ContentValues();
        cv.put(c_name, name);
        cv.put(c_birth, birth);
        cv.put(c_gift, gift);
        db.update(tb_contact, cv, where, args);
    }

    public void delete(Integer cid) {
        String where = "cid = ?";
        String[] args = {cid.toString()};
        db.delete(tb_contact, where, args);
    }

    public ContactItem select(Integer cid) {
        String where = "cid = ?";
        String[] args = {cid.toString()};
        Cursor cursor = db.query(
                tb_contact,
                new String[]{c_cid, c_name, c_birth, c_gift},
                where,
                args, null, null, null);
        ContactItem ret = null;
        if (cursor.moveToNext())
            ret = curToItem(cursor);
        cursor.close();
        return ret;
    }

    public ArrayList<ContactItem> queryAll() {
        ArrayList<ContactItem> ret = new ArrayList<>();
        Cursor cursor = db.query(
                tb_contact,
                new String[]{c_cid, c_name, c_birth, c_gift},
                null, null, null, null, null);
        while (cursor.moveToNext())
            ret.add(curToItem(cursor));
        cursor.close();
        return ret;
    }

    private ContactItem curToItem(Cursor cursor) {
        return new ContactItem(
                cursor.getInt(cursor.getColumnIndex(c_cid)),
                cursor.getString(cursor.getColumnIndex(c_name)),
                cursor.getString(cursor.getColumnIndex(c_birth)),
                cursor.getString(cursor.getColumnIndex(c_gift)));
    }
}
