package com.dozen.commonbase.view.suspend;

/**
 * Created by Dozen on 19-7-25.
 * Describe:
 */
public class SuspensionCache {

    private static SizeEntity entity=new SizeEntity(100,100);//初始化悬浮球位置

    public static SizeEntity getSuspendSize() {
        return entity;
    }

    public static void setSuspendSize(SizeEntity sizeEntity) {
        entity=sizeEntity;
    }
}
