package kz.zhanarys.marvelexplorer.heroesList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.marvelexplorer.R
import java.util.EnumSet

class HeroesListAdapter : ListAdapter<CharacterItemModel, HeroesListAdapter.HeroViewHolder>(
    DiffCallbackObj
) {
    private var onItemClickListener: OnItemClickListener? = null
    private var onButtonLikeClickListener: OnButtonLikeClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HeroViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HeroViewHolder(inflater.inflate(R.layout.list_item_heroes, parent, false))
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        throw UnsupportedOperationException()
    }

    override fun onBindViewHolder(
        holder: HeroViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick((getItem(holder.adapterPosition)) as CharacterItemModel, holder.adapterPosition)
        }
        holder.bind(getItem(position), onButtonLikeClickListener, payloads)
    }

    inner class HeroViewHolder(private val view: View) : ViewHolder(view) {
        fun bind(
            item: CharacterItemModel,
            onButtonLikeClickListener: OnButtonLikeClickListener?,
            payloads: MutableList<Any>
        ) {
            val imageVariant = "/standard_large"
            val imageUrl = item.imageUrl + imageVariant + item.imageExtension

            val image = view.findViewById<ImageView>(R.id.characterItemImage)
            val name = view.findViewById<TextView>(R.id.characterItemName)
            val likeButton = view.findViewById<ImageButton>(R.id.characterItemLikeButton)

            name.text = item.name
            image.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.loading_placeholder)
            }

            likeButton.setImageResource(R.drawable.shiled_grey)
            likeButton.setOnClickListener {
                onButtonLikeClickListener?.onLikeClick(item)
            }

            val changes = if (payloads.isEmpty()) {
                emptySet<ChangeField>()
            } else {
                EnumSet.noneOf(ChangeField::class.java).also { changes ->
                    payloads.forEach { payload ->
                        (payload as? Collection<*>)?.filterIsInstanceTo(changes)
                    }
                }
            }

            if (changes.isEmpty()) {
                name.text = item.name
                image.load(imageUrl) {
                    placeholder(R.drawable.loading_placeholder)
                    crossfade(true)
                }
            }

            if (ChangeField.NAME in changes) {
                name.text = item.name
            }
            if (ChangeField.IMAGE_URL in changes) {
                image.load(imageUrl) {
                    placeholder(R.drawable.loading_placeholder)
                    crossfade(true)
                }
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnButtonLikeClickListener(onButtonLikeClickListener: OnButtonLikeClickListener) {
        this.onButtonLikeClickListener = onButtonLikeClickListener
    }

    interface OnButtonLikeClickListener {
        fun onLikeClick(item: CharacterItemModel)
    }

    interface OnItemClickListener {
        fun onItemClick(item: CharacterItemModel, position: Int)
    }
}

object DiffCallbackObj : DiffUtil.ItemCallback<CharacterItemModel>() {
    override fun areItemsTheSame(oldItem: CharacterItemModel, newItem: CharacterItemModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CharacterItemModel, newItem: CharacterItemModel): Boolean {
        return oldItem == newItem
    }
}

enum class ChangeField {
    NAME, IMAGE_URL, SHORT_INFO
}