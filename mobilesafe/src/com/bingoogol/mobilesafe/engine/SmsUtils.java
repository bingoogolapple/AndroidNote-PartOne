package com.bingoogol.mobilesafe.engine;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;
import android.widget.Toast;
import com.bingoogol.mobilesafe.util.ToastUtil;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by bingoogol@sina.com on 14-3-23.
 */
public class SmsUtils {
    public interface BackUpsmsListener{
        /**
         * 在短信备份之前调用的代码
         * @param max 一共有多少条短信
         */
        public void beforeSmsBackup(int max);

        /**
         * 在短信备份的时候调用的方法
         * @param process 当前进度
         */
        public void onSmsBackup(int process);
    }


    /**
     *
     * @param context 上下文
     */
    public static void backUpSms(final Activity context, BackUpsmsListener listener) {
        try {
            XmlSerializer serializer = Xml.newSerializer();
            File file = new File(Environment.getExternalStorageDirectory(),
                    "backup2.xml");
            FileOutputStream os = new FileOutputStream(file);
            // 初始化 序列号器 指定xml数据写入到哪个文件 并且指定文件的编码方式
            serializer.setOutput(os, "utf-8");
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "smss");
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(Uri.parse("content://sms/"), null, null, null, null);
            if(listener!=null)
                listener.beforeSmsBackup(cursor.getCount());
            int total = 0;
            while(cursor.moveToNext()) {
                serializer.startTag(null, "sms");
                serializer.attribute(null, "id", cursor.getString(cursor.getColumnIndex("_id")));

                serializer.startTag(null, "body");
                serializer.text(cursor.getString(cursor.getColumnIndex("body")));
                serializer.endTag(null, "body");

                serializer.startTag(null, "address");
                serializer.text(cursor.getString(cursor.getColumnIndex("address")));
                serializer.endTag(null, "address");

                serializer.startTag(null, "type");
                serializer.text(cursor.getString(cursor.getColumnIndex("type")));
                serializer.endTag(null, "type");

                serializer.startTag(null, "date");
                serializer.text(cursor.getString(cursor.getColumnIndex("date")));
                serializer.endTag(null, "date");
                Thread.sleep(200);
                serializer.endTag(null, "sms");
                total++;
                if(listener!=null)
                    listener.onSmsBackup(total);
            }
            cursor.close();
            serializer.endTag(null, "smss");
            serializer.endDocument();

            os.close();

        } catch (Exception e) {
            e.printStackTrace();
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.makeText(context, "备份失败");
                }
            });
        }
    }
}
