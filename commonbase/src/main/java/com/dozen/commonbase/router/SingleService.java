package com.dozen.commonbase.router;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * 测试单类注入
 *
 * @author zhilong <a href="mailto:zhilong.lzl@alibaba-inc.com">Contact me.</a>
 * @version 1.0
 * @since 2017/4/24 下午9:04
 */
@Route(path = "/chat/single")
public class SingleService implements IProvider {

    Context mContext;

    public void sayHello(String name) {

    }

    @Override
    public void init(Context context) {
        mContext = context;
    }
}
