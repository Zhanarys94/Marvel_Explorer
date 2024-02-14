package kz.zhanarys.marvelexplorer.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import coil.request.CachePolicy
import kz.zhanarys.domain.models.ComicItemModel
import kz.zhanarys.marvelexplorer.R
import java.util.EnumSet

class ComicsListAdapter : ListAdapter<ComicItemModel, ComicsListAdapter.ComicsViewHolder>(
    DiffCallbackObjComics
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComicsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ComicsViewHolder(inflater.inflate(R.layout.list_item_comic, parent, false))
    }

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        throw UnsupportedOperationException()
    }

    override fun onBindViewHolder(
        holder: ComicsViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.bind(getItem(position), payloads)
    }

    inner class ComicsViewHolder(private val view: View) : ViewHolder(view) {
        fun bind(item: ComicItemModel, payloads: MutableList<Any>) {
            val imageVariant = "/portrait_small"
            val imageUrl = item.imageUrl + imageVariant + item.imageExtension

            val image = view.findViewById<ImageView>(R.id.comicsItemImage)
            val title = view.findViewById<TextView>(R.id.comicsItemTitle).apply {
                text = item.title
            }
            val format = view.findViewById<TextView>(R.id.comicsItemFormat).apply {
                text = item.format
            }

            image.load(imageUrl) {
                crossfade(true)
                diskCachePolicy(CachePolicy.DISABLED)
                placeholder(R.drawable.loading_placeholder)
            }

            val changes = if (payloads.isEmpty()) {
                emptySet<ChangeFieldComics>()
            } else {
                EnumSet.noneOf(ChangeFieldComics::class.java).also { changes ->
                    payloads.forEach { payload ->
                        (payload as? Collection<*>)?.filterIsInstanceTo(changes)
                    }
                }
            }

            if (changes.isEmpty()) {
                title.text = item.title
                image.load(imageUrl) {
                    placeholder(R.drawable.loading_placeholder)
                    crossfade(true)
                }
                format.text = item.format
            }

            if (ChangeFieldComics.TITLE in changes) {
                title.text = item.title
            }
            if (ChangeFieldComics.IMAGE_URL in changes) {
                image.load(imageUrl) {
                    placeholder(R.drawable.loading_placeholder)
                    crossfade(true)
                }
            }
            if (ChangeFieldComics.FORMAT in changes) {
                format.text = item.format
            }
        }
    }
}

object DiffCallbackObjComics : DiffUtil.ItemCallback<ComicItemModel>() {
    override fun areItemsTheSame(oldItem: ComicItemModel, newItem: ComicItemModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ComicItemModel, newItem: ComicItemModel): Boolean {
        return oldItem == newItem
    }
}

enum class ChangeFieldComics {
    TITLE, IMAGE_URL, FORMAT
}