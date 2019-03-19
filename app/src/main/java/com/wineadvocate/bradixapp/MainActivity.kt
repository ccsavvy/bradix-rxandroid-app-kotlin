package com.wineadvocate.bradixapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.wineadvocate.model.Photo
import com.wineadvocate.network.RequestInterface
import com.wineadvocate.network.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var listOfDataClassPhotos = ArrayList<Photo>()
    private var adapter:DataClassPhotoAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Debugging purposes
//        Picasso.get().load("https://via.placeholder.com/150/771796").fit().into(imageView)

        // @TODO: load DataClassPhotos from network (nisud here)
        val responsePubObservable = ServiceGenerator
            .createAPIService(RequestInterface::class.java, this)
            .getPhotos()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { listOfDataClassPhotos ->
                    handleResponse(listOfDataClassPhotos = listOfDataClassPhotos as ArrayList<Photo>)
                },
                { error -> handleError(error) }
            )

//        Sample data
//        listOfDataClassPhotos.add(Photo("https://via.placeholder.com/150/92c952", "Title A"))
//        listOfDataClassPhotos.add(Photo("https://via.placeholder.com/150/92c952", "Title B"))
//        listOfDataClassPhotos.add(Photo("https://via.placeholder.com/150/92c952", "Title C"))
//        adapter = DataClassPhotoAdapter(this, listOfDataClassPhotos)
//        listview.adapter = adapter

    }

    private fun handleResponse(listOfDataClassPhotos: ArrayList<Photo>) {

        adapter = DataClassPhotoAdapter(this, listOfDataClassPhotos)
        listview.adapter = adapter
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }

    class DataClassPhotoAdapter: BaseAdapter {

        private var context: Context?= null
        private var listOfDataClassPhotos = ArrayList<Photo>()

        constructor(context:Context, listOfDataClassPhoto: ArrayList<Photo>): super() {
            this.context = context
            this.listOfDataClassPhotos = listOfDataClassPhoto
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val photo = listOfDataClassPhotos[position]
            var photoView = LayoutInflater.from(context).inflate(R.layout.photos, parent, false)

            val thumbNail = photoView.findViewById(R.id._thumb_nail) as ImageView
            val title = photoView.findViewById(R.id._album_title) as TextView

            val picasso: Picasso = Picasso.get()
            picasso.isLoggingEnabled = true

            picasso.load(photo.thumbnailUrl!!)
                .resize(150, 150).centerCrop().into(thumbNail)

            title.text = photo.title!!
            return photoView
        }

        override fun getItem(position: Int): Any {
            return listOfDataClassPhotos[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfDataClassPhotos.size
        }
    }
}
