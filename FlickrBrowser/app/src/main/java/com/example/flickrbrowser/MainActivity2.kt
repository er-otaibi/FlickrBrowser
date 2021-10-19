package com.example.flickrbrowser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.pic_row.*
import kotlinx.android.synthetic.main.pic_row.view.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class MainActivity2 : AppCompatActivity() {
    var picList = arrayListOf<FlickrPic>()
    var name = ""
    lateinit var rvMain: RecyclerView
    lateinit var clMain: ConstraintLayout
    lateinit var imageView3: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        supportActionBar?.hide()
        rvMain = findViewById(R.id.rvMain)
        clMain = findViewById(R.id.clMain)
        imageView3 = findViewById(R.id.imageView3)
        rvMain.adapter =PicAdapter(this,picList)
        rvMain.layoutManager = LinearLayoutManager(this)

        val intent = intent
        name = intent.getStringExtra("searchItem").toString().replace(" " , "&")

        requestApi()
    }
    fun display(photo: String){
        rvMain.isVisible = false
        imageView3.isVisible = true
        Glide.with(this)
            .load("$photo")
            .into(imageView3)
    }
    private fun requestApi() {

        CoroutineScope(Dispatchers.IO).launch {

            val data = async {

                fetchFlickerPic()

            }.await()

            if (data.isNotEmpty())
            {

                updateFlickr(data)
            }

        }

    }

    private suspend fun updateFlickr(data: String) {

        withContext(Dispatchers.Main)
        {

            val jsonObj = JSONObject(data)
            val photos = jsonObj.getJSONObject("photos")
            val photoArray = photos.getJSONArray("photo")

            for ( i in 0 until photoArray.length()){

                var photoTitle = photoArray.getJSONObject(i).getString("title")
                var image = photoArray.getJSONObject(i).getString("url_t")
                picList.add(FlickrPic(image, photoTitle))
                rvMain.adapter?.notifyDataSetChanged()
            }
           
//            rvMain.scrollToPosition(picList.size - 1)



        }

    }

    private fun fetchFlickerPic(): String {
        var response=""
        try {
            response =
                URL("https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=82c22bad9d298cbfe97e7ec9a30bd942&tags=$name&format=json&nojsoncallback=1&extras=url_t").readText(Charsets.UTF_8)

        }catch (e:Exception)
        {
            println("Error $e")

        }
        return response

    }
}

data class FlickrPic(val imageView: String, val title: String)