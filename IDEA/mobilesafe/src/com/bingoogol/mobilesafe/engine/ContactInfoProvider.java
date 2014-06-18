package com.bingoogol.mobilesafe.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.bingoogol.mobilesafe.domain.ContactInfo;
import com.bingoogol.mobilesafe.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bingoogol@sina.com 14-2-15.
 */
public class ContactInfoProvider {
    private static final String TAG = "ContactInfoProvider";
    public static List<ContactInfo> getContactInfos(Context context) {
        // 1.查询raw_contact表获取联系人的id
        ContentResolver resolver = context.getContentResolver();
        // 获取raw_contact表对应的uri
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        Cursor cursor = resolver.query(uri, null, null, null, null);
        List<ContactInfo> infos = new ArrayList<ContactInfo>();

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("contact_id"));
            if (id != null) {
                Cursor dataCursor = resolver.query(dataUri, null,
                        "raw_contact_id=?", new String[] { id }, null);
                ContactInfo info = new ContactInfo();
                // String[] names = dataCursor.getColumnNames();
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(dataCursor
                            .getColumnIndex("data1"));
                    String mimetype = dataCursor.getString(dataCursor
                            .getColumnIndex("mimetype"));
                    if(mimetype.contains("phone")){
                        info.phone = data1;
                    }else if(mimetype.contains("name")){
                        info.name = data1;
                    }
                }
                infos.add(info);
                dataCursor.close();
            }
        }
        cursor.close();
        Logger.i(TAG,infos.size() + "");
        return infos;
    }
}
