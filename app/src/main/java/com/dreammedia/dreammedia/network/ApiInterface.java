package com.dreammedia.dreammedia.network;

import com.dreammedia.dreammedia.model.AddFollowResponse;
import com.dreammedia.dreammedia.model.CategoryModel;
import com.dreammedia.dreammedia.model.CategorySliderModel;
import com.dreammedia.dreammedia.model.ChangePassword;
import com.dreammedia.dreammedia.model.ContactUsResponce;
import com.dreammedia.dreammedia.model.DashBoardResponse;
import com.dreammedia.dreammedia.model.DashBoardUserMainResponse;
import com.dreammedia.dreammedia.model.DeletePostResponse;
import com.dreammedia.dreammedia.model.FaqResponse;
import com.dreammedia.dreammedia.model.FollowModel;
import com.dreammedia.dreammedia.model.ForgotPasswordResponse;
import com.dreammedia.dreammedia.model.GetFollowingResponse;
import com.dreammedia.dreammedia.model.LoginModel;
import com.dreammedia.dreammedia.model.NotificationResponse;
import com.dreammedia.dreammedia.model.OtpVerifyModel;
import com.dreammedia.dreammedia.model.PostAddResponse;
import com.dreammedia.dreammedia.model.ProfileResponse;
import com.dreammedia.dreammedia.model.SearchPostResponse;
import com.dreammedia.dreammedia.model.SignUpModel;
import com.dreammedia.dreammedia.model.SocialLoginModel;
import com.dreammedia.dreammedia.model.TempletsListModel;
import com.dreammedia.dreammedia.model.UpdatePasswordResponse;
import com.dreammedia.dreammedia.model.UpdateProfileResponse;
import com.dreammedia.dreammedia.model.WalletAmountResponse;
import com.dreammedia.dreammedia.model.WalletResponse;
import com.dreammedia.dreammedia.model.WhoToFollowResponse;
import com.dreammedia.dreammedia.requestModel.VerifyOtpRequest;
import com.dreammedia.dreammedia.utlis.GetProfileInstaModel;
import com.dreammedia.dreammedia.utlis.InstaAccessToken;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.dreammedia.dreammedia.network.ApiConstant.ADDFOLLOW;
import static com.dreammedia.dreammedia.network.ApiConstant.ADDPOST;
import static com.dreammedia.dreammedia.network.ApiConstant.APIKEY;
import static com.dreammedia.dreammedia.network.ApiConstant.CATEGORY_SLIDER;
import static com.dreammedia.dreammedia.network.ApiConstant.CHANGE_PASSWORD;
import static com.dreammedia.dreammedia.network.ApiConstant.DASHBOARD;
import static com.dreammedia.dreammedia.network.ApiConstant.DASHBOARD_SLIDER;
import static com.dreammedia.dreammedia.network.ApiConstant.DELETE_POST;
import static com.dreammedia.dreammedia.network.ApiConstant.EMAIL;
import static com.dreammedia.dreammedia.network.ApiConstant.FAQ;
import static com.dreammedia.dreammedia.network.ApiConstant.FOLLOW_REMOVE;
import static com.dreammedia.dreammedia.network.ApiConstant.FORGOT_PASSWORD;
import static com.dreammedia.dreammedia.network.ApiConstant.FULLNAME;
import static com.dreammedia.dreammedia.network.ApiConstant.GERCATEGORIES;
import static com.dreammedia.dreammedia.network.ApiConstant.INQUIRY;
import static com.dreammedia.dreammedia.network.ApiConstant.LIKEPOST;
import static com.dreammedia.dreammedia.network.ApiConstant.LIKEREMOVE;
import static com.dreammedia.dreammedia.network.ApiConstant.LISTFOLLOWING;
import static com.dreammedia.dreammedia.network.ApiConstant.LISTFOLLOWRS;
import static com.dreammedia.dreammedia.network.ApiConstant.LISTUSER;
import static com.dreammedia.dreammedia.network.ApiConstant.LOGIN;
import static com.dreammedia.dreammedia.network.ApiConstant.LOGIN_SOCIAL;
import static com.dreammedia.dreammedia.network.ApiConstant.MOBILE;
import static com.dreammedia.dreammedia.network.ApiConstant.OTP;
import static com.dreammedia.dreammedia.network.ApiConstant.OTP_VERIFY;
import static com.dreammedia.dreammedia.network.ApiConstant.PASSWORD;
import static com.dreammedia.dreammedia.network.ApiConstant.PLAN;
import static com.dreammedia.dreammedia.network.ApiConstant.REFERRAL;
import static com.dreammedia.dreammedia.network.ApiConstant.REGISTER;
import static com.dreammedia.dreammedia.network.ApiConstant.REPORT;
import static com.dreammedia.dreammedia.network.ApiConstant.SEARCH_API;
import static com.dreammedia.dreammedia.network.ApiConstant.SHARE_POST;
import static com.dreammedia.dreammedia.network.ApiConstant.TEMPLATS_LIST_MODEL;
import static com.dreammedia.dreammedia.network.ApiConstant.TOKEN;
import static com.dreammedia.dreammedia.network.ApiConstant.UPDATE_COVER;
import static com.dreammedia.dreammedia.network.ApiConstant.UPDATE_PASSWORD;
import static com.dreammedia.dreammedia.network.ApiConstant.UPDATE_PROFILE;
import static com.dreammedia.dreammedia.network.ApiConstant.UPDATE_PROFILE_COVER;
import static com.dreammedia.dreammedia.network.ApiConstant.USERID;
import static com.dreammedia.dreammedia.network.ApiConstant.USERMAIN;
import static com.dreammedia.dreammedia.network.ApiConstant.USER_NAME;
import static com.dreammedia.dreammedia.network.ApiConstant.VIDEOCOUNT;
import static com.dreammedia.dreammedia.network.ApiConstant.WALLET_AMOUNT;

public interface ApiInterface {

    @FormUrlEncoded
    @POST(LOGIN)
    Call<LoginModel> doLogin(
            @Header(APIKEY)  String token ,
            @Field(USER_NAME) String username,
            @Field(PASSWORD) String password );


    @FormUrlEncoded
    @POST(LOGIN_SOCIAL)
    Call<SocialLoginModel> doSocialLogin(
            @Header(APIKEY)  String token ,
            @Field("social_id") String socilID,
            @Field("verificationkey") String veificaKE,
            @Field("fullname") String name,
            @Field("email") String email ,
            @Field("profile_pic") String pic );

    @FormUrlEncoded
    @POST(REGISTER)
    Call<SignUpModel> doSignUp(
            @Header(APIKEY)  String token   ,
            @Field(FULLNAME) String fullname,
            @Field(EMAIL) String email ,
            @Field(MOBILE) String mobile ,
            @Field(REFERRAL) String used_referral ,
            @Field(PASSWORD) String password );

    @POST(OTP_VERIFY)
    Call<OtpVerifyModel> doVerifyOtp(
            @Header(APIKEY)  String token   ,
            @Body VerifyOtpRequest verifyOtpRequest);

    @FormUrlEncoded
    @POST(FORGOT_PASSWORD)
    Call<ForgotPasswordResponse> doForgotPassword(
            @Header(APIKEY)  String token   ,
            @Field(USER_NAME) String password );

    @FormUrlEncoded
    @POST(UPDATE_PASSWORD)
    Call<UpdatePasswordResponse> doUpdatePassword(
            @Header(APIKEY)  String token,
            @Field(OTP) String otp ,
            @Field(USERID) String userId ,
            @Field(EMAIL) String emaail );

    @FormUrlEncoded
    @POST(LISTUSER)
    Call<WhoToFollowResponse> getListUsers(
            @Header(APIKEY)  String key,
            @Header(TOKEN)  String token ,
            @Field("user_id")  String iserid ,
            @Field("page")  String ppg );

    @FormUrlEncoded
    @POST(ADDFOLLOW)
    Call<AddFollowResponse> addFollow(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)  String token ,
            @Field("user_id")  String id ,
            @Field("receiver_id")  String receverID );

    @GET(FAQ)
    Call<FaqResponse> addFaq(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)  String token );

    @GET(PLAN)
    Call<WalletResponse> getPlan(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)  String token );


    @GET("api/v1/user/find/{id}")
    Call<ProfileResponse> getUserProfile(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)  String token ,
            @Path("id") String id);

/*
    @GET("api/v1/follow/listfollower")
    Call<FollowrsResponse> getFollowrs(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)  String token ,
            @Path FollowrsRequest iserid );
*/

    @GET("api/v1/notification/getall/{id}")
    Call<NotificationResponse> getNotification(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)  String token ,
            @Path("id") String id);

    @FormUrlEncoded
    @POST(CHANGE_PASSWORD)
    Call<ChangePassword> addChangePasswrd(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)  String token ,
            @Field("user_id")  String iserid ,
            @Field("oldpassword")  String oldpassword ,
            @Field("newpassword")  String newPass);

    @FormUrlEncoded
    @POST(INQUIRY)
    Call<ContactUsResponce> Inguiry(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)   String token ,
            @Field("user_id")String iserid ,
            @Field("fname")  String fname ,
            @Field("lname")  String lname ,
            @Field("email")  String email ,
            @Field("contact")  String contact ,
            @Field("message")  String message);

    @Multipart
    @POST(ADDPOST)
    Call<PostAddResponse> addPost(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)   String token ,
            @Part MultipartBody.Part[] image ,
            @Part("user_id") RequestBody user_id ,
            @Part("description") RequestBody description );

    @FormUrlEncoded
    @POST(SHARE_POST)
    Call<PostAddResponse> sharePost(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)   String token ,
            @Field("user_id") String user_id ,
            @Field("post_id") String postIds );

    @Multipart
    @POST(ADDPOST)
    Call<PostAddResponse> addVideoPost(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)   String token ,
            @Part MultipartBody.Part image ,
            @Part("user_id") RequestBody user_id ,
            @Part("description") RequestBody description );

    @FormUrlEncoded
    @POST(DASHBOARD)
    Call<DashBoardResponse> getDashBoard(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)   String token ,
            @Field("page")  String pg ,
            @Field("user_id")  String iserid );

    @GET(DASHBOARD_SLIDER)
    Call<DashBoardResponse> getGetSlidwr(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)   String token );

    @FormUrlEncoded
    @POST(LISTFOLLOWING)
    Call<GetFollowingResponse> getFollowing(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)   String token ,
            @Field("page")  String pg ,
            @Field("user_id")  String iserid ,
            @Field("login_id")  String login_id );

    @FormUrlEncoded
    @POST(LISTFOLLOWRS)
    Call<GetFollowingResponse> getFollowrs(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)      String token ,
            @Field("page")      String pg ,
            @Field("user_id")   String iserid ,
            @Field("login_id")  String login_id );

    @FormUrlEncoded
    @POST(DELETE_POST)
    Call<DeletePostResponse> deletePost(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)   String token ,
            @Field("post_id")  String postId );

    @FormUrlEncoded
    @POST(REPORT)
    Call<DeletePostResponse> repoartPost(
            @Header(APIKEY)    String key     ,
            @Header(TOKEN)     String token   ,
            @Field("user_id")  String user_id ,
            @Field("post_id")  String post_id ,
            @Field("remark")   String remark );

    @FormUrlEncoded
    @POST(USERMAIN)
    Call<DashBoardUserMainResponse> getUserMain(
            @Header(APIKEY)    String key  ,
            @Header(TOKEN)     String token ,
            @Field("user_id")  String user_id ,
            @Field("otheruser_id")  String otherId ,
            @Field("page")   String remark );

    @FormUrlEncoded
    @POST(LIKEPOST)
    Call<DeletePostResponse> likePost(
            @Header(APIKEY)    String key  ,
            @Header(TOKEN)     String token ,
            @Field("user_id")  String user_id ,
            @Field("post_id")   String remark );

    @FormUrlEncoded
    @POST(SEARCH_API)
    Call<SearchPostResponse> searchPostApi(
            @Header(APIKEY)   String key  ,
            @Header(TOKEN)    String token ,
            @Field("user_id") String user_id ,
            @Field("page")    String pg ,
            @Field("search")  String sea ,
            @Field("type")    String typ );

    @FormUrlEncoded
    @POST(SEARCH_API)
    Call<WhoToFollowResponse> searchUserApi(
            @Header(APIKEY)   String key   ,
            @Header(TOKEN)    String token ,
            @Field("user_id") String user_id ,
            @Field("page")    String pg  ,
            @Field("search")  String sea ,
            @Field("type")    String typ );

    @FormUrlEncoded
    @POST(FOLLOW_REMOVE)
    Call<AddFollowResponse> followRemove(
            @Header(APIKEY)     String key     ,
            @Header(TOKEN)      String token   ,
            @Field("user_id")   String user_id ,
            @Field("follow_id") String typ );

    @FormUrlEncoded
    @POST(UPDATE_PROFILE)
    Call<UpdateProfileResponse> updateProfile(
            @Header(APIKEY)  String key  ,
            @Header(TOKEN)   String token ,
            @Field("profile_image") String pp ,
            @Field("cover_pic") String cover ,
            @Field("user_id") String userId ,
            @Field("fullname") String name ,
            @Field("username") String userName ,
            @Field("website") String website ,
            @Field("workdetail") String workdetail ,
            @Field("dob") String dob ,
            @Field("place") String place ,
            @Field("phone_no") String phon,
            @Field("bio") String boss);

    @Multipart
    @POST(UPDATE_PROFILE_COVER)
    Call<UpdateProfileResponse> updateProfile(
            @Header(APIKEY)  String key    ,
            @Header(TOKEN)   String token  ,
            @Part MultipartBody.Part image ,
            @Part("user_id") RequestBody user_id );

    @Multipart
    @POST(UPDATE_COVER)
    Call<UpdateProfileResponse> updateColver(
            @Header(APIKEY)  String key    ,
            @Header(TOKEN)   String token  ,
            @Part MultipartBody.Part cover ,
            @Part("user_id") RequestBody user_id );

    @FormUrlEncoded
    @POST(LIKEREMOVE)
    Call<DeletePostResponse> likeRemove(
            @Header(APIKEY)  String key     ,
            @Header(TOKEN)   String token   ,
            @Field("user_id") String userId ,
            @Field("post_id") String postId );

    @FormUrlEncoded
    @POST(VIDEOCOUNT)
    Call<DeletePostResponse> addVideoCount(
            @Header(APIKEY)  String key     ,
            @Header(TOKEN)   String token   ,
            @Field("post_id") String postId );

    @FormUrlEncoded
    @POST("oauth/access_token")
    Call<InstaAccessToken> getAccessToken(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("grant_type") String grant_type,
            @Field("redirect_uri") String redirect_uri,
            @Field("code") String code);

    @GET("me")
    Call<GetProfileInstaModel> getINstaInfo(
            @Query("fields") String fieldss ,
            @Query("access_token") String token );

    @FormUrlEncoded
    @POST(GERCATEGORIES)
    Call<CategoryModel> getCategories(
            @Header(APIKEY)  String key ,
            @Header(TOKEN) String token ,
            @Field("page")  String pg );

    @GET(CATEGORY_SLIDER)
    Call<CategorySliderModel> getCategorySlider(
            @Header(APIKEY)  String key ,
            @Header(TOKEN) String token );

    @FormUrlEncoded
    @POST(TEMPLATS_LIST_MODEL)
    Call<TempletsListModel> getTemplets(
            @Header(APIKEY)     String key   ,
            @Header(TOKEN)      String token ,
            @Field("page")      String pg    ,
            @Field("category")  String cat   );

    @FormUrlEncoded
    @POST(WALLET_AMOUNT)
    Call<WalletAmountResponse> getWalletAmount(
            @Header(APIKEY)     String key   ,
            @Header(TOKEN)      String token ,
            @Field("user_id")  String userId   );

}

// -------------------------- This is api access token and key -------------------------------------
/*
   apiKey : apikeybyrkcomeshere
   x-access-token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Ijg5NTUxNzE5MTkiLCJpc19sb2dpbiI6MSwiaWF0IjoxNjMwMDkzNjI4LCJleHAiOjE2ODE5MzM2Mjh9.gUPZ2aNbBDAKeRdhg9N-ZinHY4sjWb0M_Rp3TmV4IgY

 */







