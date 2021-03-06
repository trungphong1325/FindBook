package com.example.findbook.view.listbook

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findbook.R
import com.example.findbook.extentions.inflate
import com.example.findbook.model.IModel
import com.example.findbook.model.ItemResponse
import com.example.findbook.utils.NoteDiffCallBack
import com.example.findbook.view.BookUIModel
import kotlinx.android.synthetic.main.item_book.view.*
import java.util.concurrent.Executors

class BookAdapter(var mListener: BookAdapterListener?) : ListAdapter<IModel, BookAdapter.ViewHolder>(
    AsyncDifferConfig.Builder<IModel>(NoteDiffCallBack())
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_book))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position) as? ItemResponse
        holder.item = model
        holder.favorite.visibility = View.VISIBLE
        holder.name.text = model?.volumeInfo?.title
        holder.description.text = model?.volumeInfo?.description
        Glide.with(holder.itemView.context)
            .load(model?.volumeInfo?.imageLinks?.thumbnail)
            .into(holder.image)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.image
        var name: TextView = view.name
        var description: TextView = view.description
        var favorite: ImageView = view.favorite
        var item: ItemResponse? = null

        init {
            itemView.setOnClickListener {
                mListener?.onClickItem(item?.volumeInfo?.infoLink)
            }
            favorite.setOnClickListener {
                favorite.setImageResource(
                        R.drawable.ic_favorite_border_red_24dp
                )
                mListener?.addFavoriteList(
                    BookUIModel(
                        item?.id?:"",
                        item?.volumeInfo?.title,
                        item?.volumeInfo?.description,
                        item?.volumeInfo?.imageLinks?.thumbnail,
                        item?.volumeInfo?.infoLink ?: ""
                    )
                )
            }
        }
    }

    override fun submitList(list: List<IModel>?) {
        super.submitList(ArrayList<IModel>(list ?: listOf()))
    }

    interface BookAdapterListener {
        fun onClickItem(url: String?)
        fun addFavoriteList(model: BookUIModel)
    }
}