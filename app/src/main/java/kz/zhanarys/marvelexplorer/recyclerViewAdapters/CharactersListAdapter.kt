package kz.zhanarys.marvelexplorer.recyclerViewAdapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import coil.request.CachePolicy
import kz.zhanarys.domain.models.CharacterItemModel
import kz.zhanarys.marvelexplorer.R
import java.util.EnumSet

class CharactersListAdapter : ListAdapter<CharacterItemModel, CharactersListAdapter.CharacterViewHolder>(
    DiffCallbackObj
) {
    private var onItemClickListener: OnItemClickListener? = null
    private var onButtonLikeClickListener: OnButtonLikeClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CharacterViewHolder(inflater.inflate(R.layout.list_item_character, parent, false))
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        throw UnsupportedOperationException()
    }

    override fun onBindViewHolder(
        holder: CharacterViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick((getItem(holder.adapterPosition)) as CharacterItemModel)
        }
        holder.bind(getItem(position), onButtonLikeClickListener, payloads)
        setScaleAnimation(holder.itemView)
    }

    inner class CharacterViewHolder(private val view: View) : ViewHolder(view) {
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

            image.load(imageUrl) {
                diskCachePolicy(CachePolicy.ENABLED)
                crossfade(true)
                placeholder(R.drawable.ic_loading_placeholder)
            }

            likeButton.setImageResource(R.drawable.ic_shiled_grey)
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
                likeButton.setImageResource(
                    if (item.isFavorite) {
                        R.drawable.ic_shield_colored
                    } else R.drawable.ic_shiled_grey
                )
                image.load(imageUrl) {
                    diskCachePolicy(CachePolicy.ENABLED)
                    placeholder(R.drawable.ic_loading_placeholder)
                    crossfade(true)
                }
            }

            if (ChangeField.NAME in changes) {
                name.text = item.name
            }
            if (ChangeField.IMAGE_URL in changes) {
                image.load(imageUrl) {
                    diskCachePolicy(CachePolicy.ENABLED)
                    placeholder(R.drawable.ic_loading_placeholder)
                    crossfade(true)
                }
            }
            if (ChangeField.IS_FAVORITE in changes) {
                likeButton.setImageResource(
                    if (item.isFavorite) {
                        R.drawable.ic_shield_colored
                    } else R.drawable.ic_shiled_grey
                )
            }
        }
    }

    private fun setScaleAnimation(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.6f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.6f, 1f)
        scaleX.duration = 500
        scaleY.duration = 500

        val scaleSet = AnimatorSet()
        scaleSet.play(scaleX).with(scaleY)
        scaleSet.start()
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
        fun onItemClick(item: CharacterItemModel)
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
    NAME, IMAGE_URL, IS_FAVORITE
}