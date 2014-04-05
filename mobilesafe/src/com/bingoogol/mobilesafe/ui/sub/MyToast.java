package com.bingoogol.mobilesafe.ui.sub;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bingoogol.mobilesafe.R;

/**
 * Created by bingoogol@sina.com on 14-4-5.
 */
public class MyToast {
    /**
     * 显示一个自定义风格的土司
     *
     * @param context
     *            上下文
     * @param text
     *            土司的文本
     */
    public static void show(Context context, String text) {
        View view = View.inflate(context, R.layout.my_toast, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast_info);
        tv.setText(text);

        Toast toast = new Toast(context);
        toast.setView(view);
        if (text.length() > 10) {
            toast.setDuration(Toast.LENGTH_LONG);
        }else{
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
