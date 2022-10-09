package ru.easfa.test_roulette.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import ru.easfa.test_roulette.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    companion object {
        const val URL_Link = "LINK"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val baseURL = intent.getStringExtra(URL_Link)
        binding.idWebview.settings.javaScriptEnabled = true
        binding.idWebview.webViewClient = WebViewClient()
        if (baseURL != null) {
            binding.idWebview.loadUrl(baseURL)
        }
    }

    override fun onBackPressed() {
        if(binding.idWebview.canGoBack())
            binding.idWebview.goBack()
        else {
            val intent = Intent()
            intent.putExtra("SCD_KEY", "SCD_FINISH")
            setResult(RESULT_OK, intent)
            super.onBackPressed()
        }
    }
}