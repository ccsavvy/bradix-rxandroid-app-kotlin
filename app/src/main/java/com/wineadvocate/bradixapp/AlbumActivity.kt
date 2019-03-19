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
import com.wineadvocate.model.DataClassPhoto
import com.wineadvocate.network.RequestInterface
import com.wineadvocate.network.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

/**
 *  Created by Christian on Tuesday Mar, 2019
 */

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AlbumActivity : AppCompatActivity() {

    private var adapter: PhotoAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle: Bundle = intent.extras
        val albumId:String = bundle.getString("albumId")

        val responsePubObservable = ServiceGenerator
            .createAPIService(RequestInterface::class.java, this)
            .getAlbum(albumId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { listOfDataClassPhotos ->
                    handleResponse(listOfDataClassPhotos = listOfDataClassPhotos as ArrayList<DataClassPhoto>)
                },
                { error -> handleError(error) }
            )
    }

    private fun handleResponse(listOfDataClassPhotos: ArrayList<DataClassPhoto>) {

        adapter = PhotoAdapter(this, listOfDataClassPhotos)
        listview.adapter = adapter
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }


    inner class PhotoAdapter: BaseAdapter {

        private var context: Context? = null
        private var listOfDataClassPhotos = ArrayList<DataClassPhoto>()

        constructor(context: Context, listOfDataClassPhoto: ArrayList<DataClassPhoto>): super() {

            this.context = context
            this.listOfDataClassPhotos = listOfDataClassPhoto
        }

        @SuppressLint("ViewHolder")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val photo = listOfDataClassPhotos[position]
            var photoView = LayoutInflater.from(context).inflate(R.layout.photos, parent, false)

            val thumbNail = photoView.findViewById(R.id._thumb_nail) as ImageView
            val title = photoView.findViewById(R.id._album_title) as TextView
            val albumId = photoView.findViewById<TextView>(R.id._album_id)

            val picasso: Picasso = Picasso.get()
            picasso.isLoggingEnabled = true

            picasso.load(photo.thumbnailUrl!!)
                .resize(150, 150).centerCrop().into(thumbNail)

            title.text = photo.title!!
            albumId.text = "Album id: #${photo.albumId!!}"

//            photoView.setOnClickListener{
//                val intent = Intent(context, AlbumActivity::class.java)
//                intent.putExtra("albumId", photo.albumId)
//                context!!.startActivity(intent)
//            }

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

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.no_change, R.anim.slide_out_to_right)
    }
}