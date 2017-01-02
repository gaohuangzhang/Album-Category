package hitcs.fghz.org.album.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.tensorflow.demo.Classifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hitcs.fghz.org.album.Config;
import hitcs.fghz.org.album.MainActivity;
import hitcs.fghz.org.album.dao.MyDatabaseHelper;

/**
 * Created by me on 16-12-31.
 */

public  class ImagesScaner {
//    /**
//     * 异步扫描SD卡多媒体文件,不阻塞当前线程
//     * n
//     * @param ctx
//     * @param file 扫描的指定文件
//     */
//    public static void scanMediaAsync(Context ctx, File file) {
//        Intent scanner = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        scanner.setData(Uri.fromFile(file));
//        ctx.sendBroadcast(scanner);
//    }
    // get all image information in db
    private static List<Classifier.Recognition> results;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void prepareForApplication(Context ctx) {
        Config.dbHelper = new MyDatabaseHelper(ctx, "Album.db", null, Config.dbversion);
        SQLiteDatabase db = Config.dbHelper.getWritableDatabase();
        List<Map> tmp = getMediaImageInfo(ctx);
        Log.d("ALBUM DATA", String.valueOf(tmp));
        String url = null;
        Cursor cursor = null;
        Cursor cursor_album = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        for (Map<String, String> map : tmp) {
            url = map.get("_data");
            cursor = db.query("AlbumPhotos", null, "url = '" + url + "'", null, null, null, null);
            Log.d("Detail of photo", url);

            if (!cursor.moveToFirst()) {
                Log.d("Detail of photo", "not in album");
                ContentValues values = new ContentValues();
                values.put("url", url);

                if (Config.classifier != null) {
                    // get bitmap
                    bitmap = BitmapFactory.decodeFile(url, options);
                    do_tensorflow(bitmap);

                    values.put("album_name", results.get(0).getTitle());
                    Log.d("ADD INTO ALBUM", url + "  " + results.get(0).getTitle());
                    cursor_album = db.query("Album", null, "album_name ='" +  results.get(0).getTitle() + "'", null, null, null, null);
                    if (!cursor_album.moveToFirst()) {
                        ContentValues values_ablum = new ContentValues();
                        values_ablum.put("album_name", results.get(0).getTitle());
                        values_ablum.put("show_image", url);
                        db.insert("Album", null, values_ablum);
                    } else {
                        String album = cursor_album.getString(cursor_album.getColumnIndex("album_name"));
                        String url_album = cursor_album.getString(cursor_album.getColumnIndex("show_image"));

                        Log.d("ALBUM have album", album + " " + url_album);
                    }
                    cursor_album.close();
                }
                db.insert("AlbumPhotos", null, values);
                values.clear();
            }
            else {
                String name = cursor.getString(cursor.getColumnIndex("album_name"));
                String url_new = cursor.getString(cursor.getColumnIndex("url"));

                Log.d("ALBUM_PHOTO have photos", name + " " + url_new);
            }
            cursor.close();

        }


    }
    // use tf to deal image
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private static void do_tensorflow(Bitmap bitmap) {
        // resize
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) Config.INPUT_SIZE) / width;
        float scaleHeight = ((float) Config.INPUT_SIZE) / height;
        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        // get results
        results = Config.classifier.recognizeImage(newbm);
        Log.d("Result", String.valueOf(results));
    }

    public static List<Map<String, String>> getAlbumPhotos(Context ctx, String name) {
        List<Map<String, String>> result = null;
        Config.dbHelper = new MyDatabaseHelper(ctx, "AlbumPhotos.db", null, Config.dbversion);
        SQLiteDatabase db = Config.dbHelper.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.query("AlbumPhotos", null, "album_name = '" + name + "'", null, null, null, null);
        if (!cursor.moveToFirst()) {
            do {
                Map<String, String> tmp = null;
                String album_name = cursor.getString(cursor.getColumnIndex("album_name"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                tmp.put("album_name", album_name);
                tmp.put("url", url);
                result.add(tmp);
            } while (cursor.moveToNext());
        }

        return result;
    }
    public static List<Map<String, String>> getAlbumInfo(Context ctx) {
        List<Map<String, String>> result = null;
        Config.dbHelper = new MyDatabaseHelper(ctx, "Album.db", null, Config.dbversion);
        SQLiteDatabase db = Config.dbHelper.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.query("Album", null, null, null, null, null, null);
        if (!cursor.moveToFirst()) {
            do {
                Map<String, String> tmp = null;
                String album_name = cursor.getString(cursor.getColumnIndex("album_name"));
                String url = cursor.getString(cursor.getColumnIndex("show_image"));
                tmp.put("album_name", album_name);
                tmp.put("show_image", url);
                result.add(tmp);
            } while (cursor.moveToNext());
        }
        return result;
    }
    public static List<Map> getMediaImageInfo(Context ctx) {
        //可以手动指定获取的列
        String[] columns = new String[]{
                MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DISPLAY_NAME
                , MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DATE_TAKEN};

        ContentResolver contentResolver = ctx.getContentResolver();
        //外部存储的SD卡的访问Uri
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        //  MediaStore.Images.Media.INTERNAL_CONTENT_URI
        Cursor cursor = contentResolver.query(uri, null, null, null, null);//获取全部列
        //  Cursor cursor = contentResolver.query(uri, columns, null, null, null);//获取指定列
        if (cursor != null) {
            Map<String, String> item = null;
            List<Map> result = new ArrayList<>();
            while (cursor.moveToNext()) {
                String[] columnNames = cursor.getColumnNames();
                item = new HashMap<>();
                for (String colnmnName : columnNames) {
                    int columnIndex = cursor.getColumnIndex(colnmnName);
                    String columnValue = cursor.getString(columnIndex);
                    item.put(colnmnName, columnValue);
                }
                result.add(item);
            }
            Log.d("debug", "getMediaImageInfo() size=" + result.size() + ", result=" + result);
            cursor.close();
            return result;
        }
        cursor.close();
        return null;
    }
    public static Map<String, String> getInformation(Context ctx, String url) {
        // information will be looked up
        String[] columns = new String[]{
                MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DISPLAY_NAME
                , MediaStore.Images.Media.LATITUDE, MediaStore.Images.Media.LONGITUDE, MediaStore.Images.Media.DATE_TAKEN};
        ContentResolver contentResolver = ctx.getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Log.d("getInformation", url);
        Cursor cursor = contentResolver.query(uri, null, MediaStore.Images.Media.DATA+"='" + url + "'", null, null);//获取全部列
        Map<String, String> result = null;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String[] columnNames = cursor.getColumnNames();
                result = new HashMap<>();
                for (String colnmnName : columnNames) {
                    int columnIndex = cursor.getColumnIndex(colnmnName);
                    String columnValue = cursor.getString(columnIndex);
                    result.put(colnmnName, columnValue);
                }
            }
            Log.d("debug", "getMediaImageInfo() size=" + result.size() + ", result=" + result);
            cursor.close();
        }

        return result;
    }

    public static ArrayList<HashMap<String,String>> getAllPictures(Context context) {
        ArrayList<HashMap<String,String>> picturemaps = new ArrayList<>();
        HashMap<String,String> picturemap = null;
        ContentResolver cr = context.getContentResolver();
        //先得到缩略图的URL和对应的图片id
        Cursor cursor = cr.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Thumbnails.IMAGE_ID,
                        MediaStore.Images.Thumbnails.DATA
                },
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            do {
                picturemap = new HashMap<>();
                picturemap.put("image_id_path",cursor.getInt(0)+"");
                picturemap.put("thumbnail_path",cursor.getString(1));
                picturemaps.add(picturemap);

            } while (cursor.moveToNext());
            cursor.close();
        }
        //再得到正常图片的path
        for (int i = 0;i<picturemaps.size();i++) {

            picturemap = picturemaps.get(i);
            String media_id = picturemap.get("image_id_path");
            cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media.DATA
                    },
                    MediaStore.Images.Media._ID+"="+media_id,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                do {
                    picturemap.put("image_id",cursor.getString(0));
                    picturemaps.set(i,picturemap);
                } while (cursor.moveToNext());
                cursor.close();
            }
            Log.d("PPPP", String.valueOf(picturemap));
        }

        return picturemaps;
    }
    // update db: add a new image
    //filename是我们的文件全名，包括后缀哦
    public static void updateGallery(Context con, String filename)
    {
        MediaScannerConnection.scanFile(con,
            new String[] { filename }, null,
            new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
    }
    // get thumbnail image of fileName
    public static Bitmap getBitmap(Context ctx, String fileName) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        //select condition.
        String whereClause = MediaStore.Images.Media.DATA + " = '" + fileName + "'";

        //colection of results.
        ContentResolver cr = ctx.getContentResolver();
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,new String[] { MediaStore.Images.Media._ID }, whereClause,null, null);
        if (cursor == null || cursor.getCount() == 0) {
            if(cursor != null)
                cursor.close();
            Log.d("THUM", "null");
            return null;
        }
        cursor.moveToFirst();
        //image id in image table.
        String videoId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        cursor.close();
        if (videoId == null) {
            Log.d("THUM", "null");
            return null;
        }
        long videoIdLong = Long.parseLong(videoId);
        //via imageid get the bimap type thumbnail in thumbnail table.
        bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, videoIdLong, MediaStore.Images.Thumbnails.MINI_KIND, options);
        if (bitmap == null) {
            Log.d("THUM", "null");
        } else {
            Log.d("THUM","" + bitmap.getHeight() + " " + bitmap.getWidth());
        }
        return bitmap;
    }
}
