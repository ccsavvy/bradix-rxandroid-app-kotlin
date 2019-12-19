package com.wineadvocate.bradixapp

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import com.wineadvocate.model.Album
import com.wineadvocate.network.RequestInterface
import com.wineadvocate.network.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private var adapter: DataClassAlbumAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Debugging purposes
//        Picasso.get().load("https://via.placeholder.com/150/771796").fit().into(imageView)

        // @TODO: load DataClassAlbum/Album from network (nisud here)
        val responsePubObservable = ServiceGenerator
            .createAPIService(RequestInterface::class.java, this)
            .getAlbum()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { listOfAlbums ->
                    handleResponse(listofAlbums = listOfAlbums as ArrayList<Album>)
                },
                { error -> handleError(error) }
            )

        disposable.add(responsePubObservable)

//        Sample data
//        listOfDataClassAlbums.add(Album("https://via.placeholder.com/150/92c952", "Title A"))
//        listOfDataClassAlbums.add(Album("https://via.placeholder.com/150/92c952", "Title B"))
//        listOfDataClassAlbums.add(Album("https://via.placeholder.com/150/92c952", "Title C"))
//        adapter = DataClassAlbumAdapter(this, listOfDataClassAlbums)
//        listview.adapter = adapter

    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    private fun handleResponse(listofAlbums: ArrayList<Album>) {

        adapter = DataClassAlbumAdapter(this, listofAlbums)
        albumList.adapter = adapter
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }

    class DataClassAlbumAdapter(context: Context, listOfDataClassAlbum: ArrayList<Album>) :
        BaseAdapter() {

        private var context: Context? = context
        private var listOfAlbums = listOfDataClassAlbum

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val albumViewHolder: AlbumViewHolder
            if (convertView == null) {
                albumViewHolder = AlbumViewHolder()
                albumViewHolder.position = position
                convertView?.tag = albumViewHolder
            } else {
                albumViewHolder = convertView.tag as AlbumViewHolder
            }

            albumViewHolder.albumTitle?.text = listOfAlbums[position].title
            albumViewHolder.userId?.text = listOfAlbums[position].userId
            albumViewHolder.position = position
            return convertView
        }

        override fun getItem(position: Int): Any {
            return listOfAlbums[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfAlbums.size
        }
    }

    internal class AlbumViewHolder {
        val albumTitle: TextView? = null
        val userId: TextView? = null
        var position: Int = 0
    }
}
