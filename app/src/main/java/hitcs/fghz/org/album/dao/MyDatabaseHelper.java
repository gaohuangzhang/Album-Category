package hitcs.fghz.org.album.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by me on 17-1-3.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_ALBUM_PHOTOS = "create table AlbumPhotos ("
            + "id integer primary key autoincrement, "
            + "album_name text, "
            + "url text)";
    public static final String CREATE_ALBUM = "create table Album ("
            + "id integer primary key autoincrement, "
            + "album_name text, "
            + "show_image text)";
    private Context mContext;
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory
            factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ALBUM_PHOTOS);
        db.execSQL(CREATE_ALBUM);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists AlbumPhotos");
        db.execSQL("drop table if exists Album");
        onCreate(db);
    }
}
