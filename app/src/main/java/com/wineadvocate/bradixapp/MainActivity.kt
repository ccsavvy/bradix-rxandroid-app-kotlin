package com.wineadvocate.bradixapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wineadvocate.model.Album
import com.wineadvocate.network.RequestInterface
import com.wineadvocate.network.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val disposable: CompositeDisposable = CompositeDisposable()
    private var adapter: AlbumAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Debugging purposes
//        Picasso.get().load("https://via.placeholder.com/150/771796").fit().into(imageView)

        // @TODO: load DataClassAlbum/Album from network
        val responsePubObservable = ServiceGenerator
            .createAPIService(RequestInterface::class.java)
            .getAlbums("2")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { listOfAlbums ->
                    handleResponse(listOfAlbums = listOfAlbums)
                },
                { error -> handleError(error) }
            )

        disposable.add(responsePubObservable)
        refreshLayout.setOnRefreshListener { refreshLayout.isRefreshing = false }


//        Sample data
//        listOfDataClassAlbums.add(Album("https://via.placeholder.com/150/92c952", "Title A"))
//        listOfDataClassAlbums.add(Album("https://via.placeholder.com/150/92c952", "Title B"))
//        listOfDataClassAlbums.add(Album("https://via.placeholder.com/150/92c952", "Title C"))
//        adapter = AlbumAdapter(this, listOfDataClassAlbums)
//        listView.adapter = adapter

    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    private fun handleResponse(listOfAlbums: List<Album>) {

        listOfAlbums.forEach(System.out::println)
        adapter = AlbumAdapter(this, listOfAlbums)
        albumList.adapter = adapter
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
    }

    class AlbumAdapter(context: Context, list: List<Album>) : BaseAdapter() {

        private val context: Context = context
        private var listOfAlbums = list
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            var view: View? = convertView
            val albumViewHolder: AlbumViewHolder
            if (convertView == null) {
                val mInflater =
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                view = mInflater.inflate(R.layout.item_album, null)
                albumViewHolder = AlbumViewHolder()
                albumViewHolder.albumTitle = view.findViewById(R.id.albumTitle) //?.text = listOfAlbums[position].title
                albumViewHolder.userId = view.findViewById(R.id.userId) //?.text = listOfAlbums[position].userId
                albumViewHolder.position = position
                view?.tag = albumViewHolder
            } else albumViewHolder = view?.tag as AlbumViewHolder


            val album: Album = getItem(position) as Album
            albumViewHolder.albumTitle?.text = album.title
            albumViewHolder.userId?.text = album.userId
            albumViewHolder.position = position

            view?.setOnClickListener {
                val intent = Intent(context, PhotoActivity::class.java)
                intent.putExtra("albumId", album.userId)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)

                val activity: Activity = context as Activity
                activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.fade_out)
            }

            return view
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
        var albumTitle: TextView? = null
        var userId: TextView? = null
        var position: Int = 0
    }
}
