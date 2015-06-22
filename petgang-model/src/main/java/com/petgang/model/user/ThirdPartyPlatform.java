package com.petgang.model.user;

public enum ThirdPartyPlatform {

    QQ("qq"), QQ_Zone("qq_zone"), WeiBo("weibo"), WeChat("wechat");

    private String plarform;

    private ThirdPartyPlatform(String platform) {
        this.plarform = platform;
    }

    public String getPlatform() {
        return this.plarform;
    }

    public static ThirdPartyPlatform get(String platform) {
        for (ThirdPartyPlatform tmp : ThirdPartyPlatform.values()) {
            if (tmp.equals(platform)) {
                return tmp;
            }
        }
        return null;
    }
}
