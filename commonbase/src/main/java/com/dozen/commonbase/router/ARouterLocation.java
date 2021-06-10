package com.dozen.commonbase.router;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/3/26
 */
public class ARouterLocation {

    private static final String app_main="/dozen";

    private static final String login_act ="/login";

    public static final String login_feedback= login_act +"/feedback";

    public static final String login_complaint= login_act +"/complaint";

    public static final String login_h5_zf= login_act +"/zf";

    public static final String login_version_info= login_act +"/version";

    public static final String login_sign= login_act +"/sign";

    public static final String login_register= login_act +"/register";

    public static final String login_password= login_act +"/password";

    public static final String app_url_show_android_js = login_act + "/url/show/androidjs";

    public static final String app_url_show= login_act +"/url/show";

    public static final String app_shares = app_main +"/shares";

    public static final String app_shares_line = app_shares + "/line";
}
