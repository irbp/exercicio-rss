package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {

    private lateinit var RSS_FEED: String

    //OUTROS LINKS PARA TESTAR...
    //http://rss.cnn.com/rss/edition.rss
    //http://pox.globo.com/rss/g1/brasil/
    //http://pox.globo.com/rss/g1/ciencia-e-saude/
    //http://pox.globo.com/rss/g1/tecnologia/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // obtendo endereço a partir do arquivo strings.xml
        RSS_FEED = getString(R.string.rssfeed)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false)
        conteudoRSS.layoutManager = layoutManager

        swipe_layout.setOnRefreshListener { refreshContent() }
    }

    override fun onStart() {
        super.onStart()
        try {
            refreshContent()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun refreshContent() {
        // Esse código não dá mais pau, pois estamos obtendo o xml de maneira assíncrona
        // utilizando o doAsync do anko
        doAsync {
            // obtém o xml
            val feedXML = getRssFeed(RSS_FEED)
            // faz o parsing do xml
            val itemsRss = ParserRSS.parse(feedXML)
            // populando o recycler view (na ui thread) com o que foi obtido pelo parser
            uiThread {
                conteudoRSS.adapter = RssListAdapter(itemsRss,this@MainActivity)
                swipe_layout.isRefreshing = false
            }
        }
    }

    //Opcional - pesquise outros meios de obter arquivos da internet - bibliotecas, etc.
    @Throws(IOException::class)
    private fun getRssFeed(feed: String): String {
        var inputStream: InputStream? = null
        var rssFeed = ""
        try {
            val url = URL(feed)
            val conn = url.openConnection() as HttpURLConnection
            inputStream = conn.inputStream
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var count = inputStream.read(buffer)
            while (count != -1) {
                out.write(buffer, 0, count)
                count = inputStream.read(buffer)
            }
            val response = out.toByteArray()
            rssFeed = String(response, charset("UTF-8"))
        } finally {
            inputStream?.close()
        }
        return rssFeed
    }
}
