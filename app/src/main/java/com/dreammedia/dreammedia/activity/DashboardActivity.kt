package com.dreammedia.dreammedia.activity

import android.content.*
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.databinding.ActivityDashboardBinding
import com.dreammedia.dreammedia.loginProcess.LoginSignupActivity
import com.dreammedia.dreammedia.model.ProfileResponse
import com.dreammedia.dreammedia.network.ApiClient
import com.dreammedia.dreammedia.network.ApiConstant
import com.dreammedia.dreammedia.network.ApiInterface
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerLayout: DrawerLayout ;
    lateinit var tvNavigation: ImageView ;
    lateinit var binding : ActivityDashboardBinding
    lateinit var appBar : AppBarLayout
    lateinit var toolbar : Toolbar
    lateinit var mainMenu : RelativeLayout

    lateinit var profile : ImageView
    lateinit var navigation : ImageView
    lateinit var closeNavigation : ImageView
    lateinit var home : ImageView
    lateinit var tvNotification : ImageView
    lateinit var search : ImageView

    lateinit var nav_profile : LinearLayout
    lateinit var nav_Wallet : LinearLayout
    lateinit var nav_Faq : LinearLayout
    lateinit var nav_ContactUs : LinearLayout
    lateinit var nav_RateUs : LinearLayout
    lateinit var nav_change_password : LinearLayout
    lateinit var nav_Log_out : LinearLayout
    lateinit var tcShareApp : TextView


    lateinit var mainLy : RelativeLayout
    lateinit var icBack : ImageView
    lateinit var tvTitle : TextView
    lateinit var tvTileMain : TextView
    lateinit var refralCode : TextView

    lateinit var header_layout : CircleImageView
    lateinit var tv_name : TextView
    lateinit var tv_designa : TextView
    lateinit var tvFollowing : TextView
    lateinit var tvFollowrs : TextView

    lateinit var version : TextView

    lateinit var navController : NavController

    var count: Int = 0

    var isHomeChek: Boolean = false
    var isDashCheck: Boolean = false
    var isPorfileCheck: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Config.init(this)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.itemIconTintList = null
        navigationView.setNavigationItemSelectedListener(this)

        navController                  = findNavController(R.id.nav_host_fragment)
        navigationView.setupWithNavController(navController)

        toolbar = findViewById(R.id.toolbar)
        (this as AppCompatActivity).setSupportActionBar(toolbar)

        setNav()

        version        = findViewById(R.id.version)
        tvNotification = findViewById(R.id.tvNotification)
        drawerLayout   = findViewById(R.id.drawer_layout)
        tvNavigation   = findViewById(R.id.tvNavigation)
        search         = findViewById(R.id.search)
        refralCode     = findViewById(R.id.refralCode)

        mainLy     = findViewById(R.id.mainLy)
        icBack     = findViewById(R.id.icBack)
        tvTitle    = findViewById(R.id.tvTitle)
        tvTileMain = findViewById(R.id.tvTileMain)

        try {

            val manager: PackageManager = getPackageManager()
            val info: PackageInfo = manager.getPackageInfo(getPackageName(), 0)
            val versisson: String = info.versionName

            version.setText("Version "+versisson)

            Log.e("getRefralCode", "onCreate: "+Config.getRefralCode() )
            refralCode.setText("Referral code "+Config.getRefralCode()+"")


        } catch (e: Exception) { }

        refralCode.setOnClickListener {

            setClipboard(this@DashboardActivity ,Config.getRefralCode() )

        }



        icBack.setOnClickListener { onBackPressed() }

        mainMenu = findViewById(R.id.mainMenu)

        closeNavigation = findViewById(R.id.closeNavigation)
        profile = findViewById(R.id.profile)
        navigation = findViewById(R.id.navigation)
        home = findViewById(R.id.home)

        drawerLayout.closeDrawer(GravityCompat.END)

        tvNavigation.setOnClickListener {
            drawerLayout.openDrawer(Gravity.RIGHT)
        }

        closeNavigation.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        home.setColorFilter(ContextCompat.getColor(this, R.color.menu_color), android.graphics.PorterDuff.Mode.SRC_IN);
        navigation.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
        profile.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);

        navigation.setOnClickListener {
            isDashCheck=true
            navigation.setColorFilter(ContextCompat.getColor(this, R.color.menu_color), android.graphics.PorterDuff.Mode.SRC_IN);
            profile.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
            home.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
            navController.navigate(R.id.HomeFragment)
            //Toast.makeText(application, "Coming Soon!" as String?, Toast.LENGTH_SHORT).show()
        }

        profile.setOnClickListener {

         /* profile.setColorFilter(ContextCompat.getColor(this, R.color.menu_color), android.graphics.PorterDuff.Mode.SRC_IN)

            navigation.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN)
            home.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN)
*/          isPorfileCheck=true
            Config.init(this)
            val vv = Bundle()
            vv.putString("key", Config.getUserID())
            navController.navigate(R.id.ProfileFragment ,vv)


        }

        home.setOnClickListener {
            isHomeChek=true
            home.setColorFilter(ContextCompat.getColor(this, R.color.menu_color), android.graphics.PorterDuff.Mode.SRC_IN);
            navigation.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
            profile.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
            navController.navigate(R.id.DashboardFragment)
        }

        tvNotification.setOnClickListener {
            navController.navigate(R.id.NotificationFragment)
        }

        search.setOnClickListener {
            navController.navigate(R.id.SearchFragment)
        }

        //manage who to follow screen
        if (Config.getWhoFollow()){
            home.setColorFilter(ContextCompat.getColor(this, R.color.menu_color), android.graphics.PorterDuff.Mode.SRC_IN);
            navigation.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
            profile.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
            navController.popBackStack()
            navController.navigate(R.id.DashboardFragment)
        }

        getUserProfile()

    }

    override fun onResume() {
        super.onResume()

//        getUserProfile()


    }


    private fun setClipboard(context: Context, text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
            clipboard.text = text
        } else {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
        }

        Toast.makeText(this@DashboardActivity, "code copy", Toast.LENGTH_SHORT).show()
    }

    private fun setNav() {
        nav_profile         = findViewById(R.id.nav_profile)
        nav_Wallet          = findViewById(R.id.nav_my_location)
        nav_Faq             = findViewById(R.id.nav_saved_post)
        nav_ContactUs       = findViewById(R.id.nav_settings)
        nav_RateUs          = findViewById(R.id.nav_about_us)
        nav_change_password = findViewById(R.id.nav_change_password)
        nav_Log_out         = findViewById(R.id.nav_Log_out)
        tcShareApp          = findViewById(R.id.tcShareApp)

        nav_profile.setOnClickListener {

           navController.navigate(R.id.ProfileFragment)
            drawerLayout.closeDrawer(GravityCompat.END)

        }
        nav_Wallet.setOnClickListener {

           // Toast.makeText(this, "coming soon!", Toast.LENGTH_SHORT).show()
              navController.navigate(R.id.WalletFragment)
              drawerLayout.closeDrawer(GravityCompat.END)

        }

        nav_Faq.setOnClickListener {
            navController.navigate(R.id.FAQFragment)
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        nav_ContactUs.setOnClickListener {
            navController.navigate(R.id.ContactUsFragment)
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        nav_RateUs.setOnClickListener {

           // navController.navigate(R.id.RateUSFragment)
           // drawerLayout.closeDrawer(GravityCompat.END)

            launchMarket()

        }

        nav_change_password.setOnClickListener {
            navController.navigate(R.id.ChangePasswordFragment)
            drawerLayout.closeDrawer(GravityCompat.END)
        }

        nav_Log_out.setOnClickListener {

            Config.clearPreferences()
            val intent = Intent(this, LoginSignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        tcShareApp.setOnClickListener {

            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.dreammedia.dreammedia"+
                    "\n "+"use this Referral code - " +
                    ""+Config.getRefralCode())
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }

    }

    private fun launchMarket() {
        val uri: Uri = Uri.parse("market://details?id=" + getPackageName())
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            // Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()

        Config.init(this)
        if (Config.getCheck()=="yes"){
            navigation.setColorFilter(ContextCompat.getColor(this, R.color.menu_color), android.graphics.PorterDuff.Mode.SRC_IN);
            profile.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
            home.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }else if(isDashCheck==true && isPorfileCheck==true){
            navigation.setColorFilter(ContextCompat.getColor(this, R.color.menu_color), android.graphics.PorterDuff.Mode.SRC_IN);
            profile.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
            home.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }else{
            home.setColorFilter(ContextCompat.getColor(this, R.color.menu_color), android.graphics.PorterDuff.Mode.SRC_IN);
            navigation.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
            profile.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
            drawerLayout.closeDrawer(GravityCompat.END)
        }
        isDashCheck=false
        isHomeChek=false
        isPorfileCheck=false
        Config.setCheck("no")
//        if (count==2){
//            navigation.setColorFilter(ContextCompat.getColor(this, R.color.menu_color), android.graphics.PorterDuff.Mode.SRC_IN);
//            profile.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
//            home.setColorFilter(ContextCompat.getColor(this, R.color.menu_unselect_color), android.graphics.PorterDuff.Mode.SRC_IN);
//        }else{
//
//        }
//        count=0
    }


    private fun getUserProfile() {

        Config.init(this)
        val apiService: ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call: Call<ProfileResponse> = apiService.getUserProfile(ApiConstant.APIKEY_VALUE, Config.getAccessToken() ,Config.getUserID())

        call.enqueue(object : Callback<ProfileResponse?> {
            override fun onResponse(call: Call<ProfileResponse?>, response: Response<ProfileResponse?>) {

                Log.e("onResponse", "onResponse: "+response.body()?.status)
                if (response.body()!!.status){

                    Config.setUserName(response.body()!!.responce.get(0).username)
                    Config.setName(response.body()!!.responce.get(0).fullname)
                    Config.setUserPhoneNumber(response.body()!!.responce.get(0).mobile)
                    Config.setUserEmail(response.body()!!.responce.get(0).email)
                    Config.setFollowing(response.body()!!.responce.get(0).totalFollowing.toString())
                    Config.setFollowrs(response.body()!!.responce.get(0).totalFollower.toString())
                    Config.setUserProfile(response.body()!!.responce.get(0).profileImage)
                    Config.setUserCover(response.body()!!.responce.get(0).coverPic)
                    Config.savePreferences()

                    setNsvdetail()

                }else{  Toast.makeText(this@DashboardActivity, response.body()?.message, Toast.LENGTH_SHORT).show()  }

            }override fun onFailure(call: Call<ProfileResponse?>, t: Throwable) {}
        })
    }

    private  fun setNsvdetail(){

        header_layout = findViewById(R.id.header_layout)
        tv_name       = findViewById(R.id.tv_name)
        tv_designa    = findViewById(R.id.tv_designa)
        tvFollowing   = findViewById(R.id.tvFollowing)
        tvFollowrs    = findViewById(R.id.tvFollowrs)

        Glide.with(this).load(Config.getUserProfile())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_user_)
            .into(header_layout)

        tv_name.setText(Config.getName())
        tv_designa.setText(Config.getUserName())
        tvFollowing.setText(Config.geFollowing())
        tvFollowrs.setText(Config.geFollowrs())

    }

}


