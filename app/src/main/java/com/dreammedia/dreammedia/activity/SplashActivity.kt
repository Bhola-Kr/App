package com.dreammedia.dreammedia.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.config.Config
import com.dreammedia.dreammedia.loginProcess.LoginSignupActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class SplashActivity : AppCompatActivity() {

    var iv_app_logo: AppCompatImageView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        iv_app_logo = findViewById(R.id.iv_app_logo)

     // get_KEY_Hashes()
        val handler = Handler()
        handler.postDelayed({

            Config.init(this)
            Log.e("getisLogin", "onCreate: " +Config.getisLogin())

            if (Config.getisLogin()){
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, LoginSignupActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 3000)

        hashFromSHA1("66:CE:8F:C3:E3:89:39:B6:10:28:8C:48:78:AF:CD:ED:D8:79:16:81");


        val animatorSet = AnimatorSet()

     //   animatorSet.interpolator = DescelerateInterpolator()
        animatorSet.duration = 2000
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                val animatorSet2 = AnimatorSet()
                animatorSet2.playTogether(
                    ObjectAnimator.ofFloat(iv_app_logo, "scaleX", 1f, 0.5f, 1f),
                    ObjectAnimator.ofFloat(iv_app_logo, "scaleY", 1f, 0.5f, 1f)
                )
                animatorSet2.interpolator = AccelerateInterpolator()
                animatorSet2.duration = 3000
                animatorSet2.start()
            }
        })
        animatorSet.start()

    }

    fun hashFromSHA1(sha1: String) {
        val arr = sha1.split(":".toRegex()).toTypedArray()
        val byteArr = ByteArray(arr.size)
        for (i in arr.indices) {
            byteArr[i] = Integer.decode("0x" + arr[i]).toByte()
        }
        Log.e("hash : ", Base64.encodeToString(byteArr, Base64.NO_WRAP))
    }

    private fun get_KEY_Hashes() {
        try {
            val info = packageManager.getPackageInfo("com.dreammedia.dreammedia", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hash = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                // KeyHash.addKeyHash(hash);
                Log.d("KeyHash:", hash)
            }
        } catch (e: PackageManager.NameNotFoundException) { Log.e("PackageInfoError:", "NameNotFoundException")
        } catch (e: NoSuchAlgorithmException) { Log.e("PackageInfoError:", "NoSuchAlgorithmException") }
    }

}