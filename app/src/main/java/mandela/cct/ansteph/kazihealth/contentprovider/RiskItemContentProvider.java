package mandela.cct.ansteph.kazihealth.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import mandela.cct.ansteph.kazihealth.api.Tables;
import mandela.cct.ansteph.kazihealth.api.columns.RiskItemColumns;
import mandela.cct.ansteph.kazihealth.helper.DbHelper;

public class RiskItemContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://mandela.cct.ansteph.kazihealth.contentprovider.riskitemcontentprovider/kazihealth");
    private SQLiteDatabase db;

    public DbHelper databhelper ;
    public RiskItemContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int retVal = db.delete(Tables.RISK_ITEM_TABLE, selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return retVal;
    }

    @Override
    public String getType(Uri uri) {

        // at the given URI.
        return null;

    }

    @Override
    public Uri insert(Uri uri, ContentValues inValues) {
        ContentValues values = new ContentValues(inValues);

        long rowId = db.insert(Tables.RISK_ITEM_TABLE, RiskItemColumns.NAME, values);

        if(rowId>0){
            Uri insertUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(insertUri, null);

            return insertUri;
        }else{
            throw new SQLException("Failed to insert row into "+ uri);
        }

    }

    @Override
    public boolean onCreate() {
        try {
            databhelper= new DbHelper(getContext());
            databhelper.createDatabase();
        } catch (Exception e) {

            e.printStackTrace();
        }

        db = databhelper.getReadDatabase();

        return (db==null) ? false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String orderBy;
        if(TextUtils.isEmpty(sortOrder))
        {
            orderBy = RiskItemColumns.NAME;
        }else {
            orderBy =sortOrder;
        }

        Cursor c = db.query(Tables.RISK_ITEM_TABLE, projection,selection,selectionArgs,null,null,orderBy);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int retVal = db.update(Tables.RISK_ITEM_TABLE, values, selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return retVal;
    }
}
