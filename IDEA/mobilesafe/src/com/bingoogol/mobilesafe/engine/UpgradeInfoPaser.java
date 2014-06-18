package com.bingoogol.mobilesafe.engine;

import android.util.Xml;
import com.bingoogol.mobilesafe.domain.UpgradeInfo;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * 应用升级信息解析器
 *
 * @author bingoogol@sina.com 14-1-23.
 */
public class UpgradeInfoPaser {

    /**
     * @param is 解析的xml的inputstream
     * @return 如果解析成功则返回升级信息，否则返回null
     */
    public static UpgradeInfo getUpgradeInfo(InputStream is) {
        try {
            UpgradeInfo updateInfo = new UpgradeInfo();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(is, "UTF-8");
            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if ("version".equals(parser.getName())) {
                            updateInfo.version = parser.nextText();
                        } else if ("description".equals(parser.getName())) {
                            updateInfo.description = parser.nextText();
                        } else if ("apkurl".equals(parser.getName())) {
                            updateInfo.apkurl = parser.nextText();
                        }
                        break;
                }
                type = parser.next();
            }
            return updateInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
