package com.wineadvocate.bradixapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.hannesdorfmann.mosby3.mvi.MviActivity
import com.squareup.picasso.Picasso
import com.wineadvocate.domain.AlbumViewState
import com.wineadvocate.model.DataClassPhoto
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_album.*

/**
 *  Created by Christian on Tuesday Mar, 2019
 */

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class AlbumActivity : MviActivity<AlbumsView, AlbumsPresenter>(), AlbumsView {


    private var albumId = PublishSubject.create<String>()

    override fun createPresenter() = AlbumsPresenter()

    override fun loadAlbums() = albumId

    override fun render(state: AlbumViewState) {

        when(state) {
            is AlbumViewState.LoadingState -> renderLoadingState()
            is AlbumViewState.DataState -> renderDataState(state)
            is AlbumViewState.ErrorState -> renderErrorState(state)
        }
    }

    private fun renderLoadingState() {
        loadingIndicator.visibility = View.VISIBLE
        listview.visibility = View.INVISIBLE
    }

    private fun renderDataState(dataState: AlbumViewState.DataState) {
        loadingIndicator.visibility = View.INVISIBLE
        listview.apply {
            visibility = View.VISIBLE
            adapter = PhotoAdapter(context, dataState.albums)
        }
    }

    private fun renderErrorState(errorState: AlbumViewState.ErrorState) {
        loadingIndicator.visibility = View.INVISIBLE
        listview.visibility = View.INVISIBLE
        Toast.makeText(this, "error ${errorState.error}", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        val bundle: Bundle = intent.extras
        albumId.onNext(bundle.getString("albumId"))

        // @TODO: MVC architecture design (nisud here)

    }

    inner class PhotoAdapter: BaseAdapter {

        private var context: Context? = null
        private var listOfDataClassPhotos = ArrayList<DataClassPhoto>()

        constructor(context: Context, listOfDataClassPhoto: ArrayList<DataClassPhoto>): super() {

            this.context = context
            this.listOfDataClassPhotos = listOfDataClassPhoto
        }

        @SuppressLint("ViewHolder", "SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val photo = listOfDataClassPhotos[position]
            var photoView = LayoutInflater.from(context).inflate(R.layout.photos, parent, false)

            val thumbNail = photoView.findViewById(R.id._thumb_nail) as ImageView
            val title = photoView.findViewById(R.id._album_title) as TextView
            val albumId = photoView.findViewById<TextView>(R.id._album_id)

            val picasso: Picasso = Picasso.get()
            picasso.isLoggingEnabled = true

            picasso.load(photo.url!!)
                .resize(600, 600).centerCrop().into(thumbNail)

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