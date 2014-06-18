package com.bingoogol.mobilesafe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.bingoogol.mobilesafe.R;
import com.bingoogol.mobilesafe.domain.ContactInfo;
import com.bingoogol.mobilesafe.engine.ContactInfoProvider;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

import java.util.List;

/**
 * @author bingoogol@sina.com 14-2-15.
 */
public class SelectContactActivity extends FinalActivity {

    @ViewInject(id = R.id.lv_select_contact)
    private ListView lv_select_contact;
    @ViewInject(id = R.id.loading)
    private LinearLayout loading;

    private List<ContactInfo> infos;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loading.setVisibility(View.INVISIBLE);
            lv_select_contact.setAdapter(new ContactAdapter());
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        lv_select_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String phone = infos.get(position).phone;
                Intent data = new Intent();
                data.putExtra("phone", phone);
                setResult(0, data);

                finish();
            }

        });

        loading.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                infos = ContactInfoProvider.getContactInfos(SelectContactActivity.this);
                handler.sendEmptyMessage(0);
            }
        }.start();

    }

    private class ContactAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),
                    R.layout.item_contact, null);
            TextView tv_name = (TextView) view.findViewById(R.id.tv_contact_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_contact_phone);
            tv_name.setText(infos.get(position).name);
            tv_phone.setText(infos.get(position).phone);
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }


}