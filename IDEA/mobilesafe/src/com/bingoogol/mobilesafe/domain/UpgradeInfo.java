package com.bingoogol.mobilesafe.domain;

/**
 * 应用更新信息
 *
 * @author bingoogol@sina.com 14-1-23.
 */
public class UpgradeInfo {
    //性能优化，避免使用getters/setters存取Field，可以把Field声明为public，直接访问
    public String version;
    public String description;
    public String apkurl;
}
