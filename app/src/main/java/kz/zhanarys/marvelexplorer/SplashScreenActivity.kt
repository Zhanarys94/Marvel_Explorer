package kz.zhanarys.marvelexplorer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 31) {
            val mainIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        } else {
            setContentView(R.layout.splash_screen)
            val splashScreenDuration: Long = 1000
            Handler(Looper.getMainLooper()).postDelayed({
                val mainIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(mainIntent)
                finish()
            }, splashScreenDuration)
        }

    }
}