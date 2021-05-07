package com.elegion.recyclertest;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import java.util.concurrent.TimeUnit;

public class BackGroundLoader extends AsyncTaskLoader<String> {
    private String id;
    private Context context;
    public BackGroundLoader(Context context, String id) {
        super(context);
        this.context = context;
        this.id = id;
    }

    @Override
    public String loadInBackground() {
        try {
            TimeUnit.SECONDS.sleep(2);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
                        + ContactsContract.CommonDataKinds.Phone.TYPE + " = ?",
                new String[]{id, String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursor.close();
            return number;
        }
        return null;
    }
}
