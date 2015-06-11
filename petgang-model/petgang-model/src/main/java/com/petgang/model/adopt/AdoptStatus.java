package com.petgang.model.adopt;

public enum AdoptStatus {
    /** 未发布 */
    Publishing(0, "未发布"), //
    /** 已发布 */
    Published(1, "已发布"), //
    /** 等待领养 */
    Adopting(2, "等待领养"), //
    /** 已被领养 */
    Adopted(3, "已被领养");

    private int status;

    private String name;

    private AdoptStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public static AdoptStatus getAdoptStatus(int status) {
        for (AdoptStatus tmp : AdoptStatus.values()) {
            if (tmp.status == status) {
                return tmp;
            }
        }
        return null;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

}
