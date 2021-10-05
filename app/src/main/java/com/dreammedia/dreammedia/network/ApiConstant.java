package com.dreammedia.dreammedia.network;

public class ApiConstant {

 // public static final String BASE_URL        = "https://api.webhelpers.in/";
    public static final String BASE_URL        = "https://api.dreammedia.in/";

    public static final String API_            = "api/v1/";
    public static final String API_USER        = "api/v1/user/";
    public static final String API_POST        = "api/v1/post/";
    public static final String API_REPORT      = "api/v1/report/";
    public static final String API_FOLLOW      = "api/v1/follow/";
    public static final String API_INQUIRY     = "api/v1/inquiry/";

    public static final String REGISTER        = API_USER+"register";
    public static final String LOGIN           = API_USER+"login";
    public static final String LOGIN_SOCIAL    = "api/v1/social/login";
    public static final String OTP_VERIFY      = API_USER+"otp-verify";
    public static final String FORGOT_PASSWORD = API_USER+"forgot-password";
    public static final String UPDATE_PASSWORD = API_USER+"update-password";
    public static final String LISTUSER        = API_USER+"list-user";
    public static final String CHANGE_PASSWORD = API_USER+"change-password";
  // public static final String USER_PROFILE    = API_USER+"find/133";

    public static final String ADDFOLLOW             = API_FOLLOW+"add";
    public static final String INQUIRY               = API_INQUIRY+"add";

    public static final String FAQ                   = API_+"faq";
    public static final String PLAN                  = API_+"plan";

    public static final String WORKING_TEST          = API_USER+"working-test";
    public static final String LOGOUT                = API_USER+"logout";

    public static final String ADDPOST               = API_POST+"add";
    public static final String REPORT                = API_REPORT+"add";

    public static final String DASHBOARD_SLIDER      = "api/v1/dashboard/sliders";
    public static final String DASHBOARD             = "api/v1/dashboard/main";
    public static final String LISTFOLLOWING         = "api/v1/follow/listfollowing";
    public static final String LISTFOLLOWRS          = "api/v1/follow/listfollower";
    public static final String DELETE_POST           = "api/v1/post/delete";
    public static final String USERMAIN              = "api/v1/dashboard/usermain";
    public static final String LIKEPOST              = "api/v1/postlike/add";
    public static final String UPDATE_PROFILE        = "api/v1/user/profile-update";
    public static final String SEARCH_API            = "api/v1/search/getall";
    public static final String UPDATE_PROFILE_COVER  = "api/v1/user/profile-images";
    public static final String UPDATE_COVER          = "api/v1/user/profile-cover";
    public static final String LIKEREMOVE            = "api/v1/postlike/remove";
    public static final String VIDEOCOUNT            = "api/v1/post/videoview";

        public static final String GERCATEGORIES         = "api/v1/category";
    public static final String CATEGORY_SLIDER       = "api/v1/category/categoryslider";
    public static final String TEMPLATS_LIST_MODEL   = "api/v1/category/template";

    public static final String WALLET_AMOUNT         = "api/v1/wallet/getdata";

   public static final String FOLLOW_REMOVE          = "api/v1/follow/remove";
    public static final String SHARE_POST            = "api/v1/postshare/add";

    public static final String APIKEY                = "apiKey";
    public static final String APIKEY_VALUE          = "apikeybyrkcomeshere";
    public static final String TOKEN                 = "x-access-token";

    public static final String FULLNAME              = "fullname";
    public static final String EMAIL                 = "email";
    public static final String MOBILE                = "mobile";
    public static final String PASSWORD              = "password";
    public static final String USER_NAME             = "username";
    public static final String REFERRAL              = "used_referral";
    public static final String OTP                   = "otp";
    public static final String USERID                = "userid";

    public static final String KEY                   = "key";
    public static final String FORGOTPASSWORD        = "ForgotPasswordFragment";
    public static final String SIGNUP                = "SignUpFragment";

}
