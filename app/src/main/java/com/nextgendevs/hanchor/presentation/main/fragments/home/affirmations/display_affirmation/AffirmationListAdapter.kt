package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.*
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.business.domain.models.Affirmation
import com.nextgendevs.hanchor.databinding.AffirmationBinding

class AffirmationListAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"

    private var clickListener: AffirmationClickListener? = null

    private val diffCallback = object : DiffUtil.ItemCallback<Affirmation>() {

        override fun areItemsTheSame(oldItem: Affirmation, newItem: Affirmation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Affirmation, newItem: Affirmation): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(
        AffirmationRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(diffCallback).build()
    )

    internal inner class AffirmationRecyclerChangeCallback(private val adapter: AffirmationListAdapter) :
        ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AffirmationViewHolder(
            AffirmationBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AffirmationViewHolder -> {
                when ((position + 1) % 6) {
                    0 -> holder.card.background = AppCompatResources.getDrawable(context, R.drawable.ic_affirm_one)
                    1 -> holder.card.background = AppCompatResources.getDrawable(context, R.drawable.ic_affirm_two)
                    2 -> holder.card.background = AppCompatResources.getDrawable(context, R.drawable.ic_affirm_three)
                    3 -> holder.card.background = AppCompatResources.getDrawable(context, R.drawable.ic_affirm_four)
                    4 -> holder.card.background = AppCompatResources.getDrawable(context, R.drawable.ic_affirm_five)
                    5 -> holder.card.background = AppCompatResources.getDrawable(context, R.drawable.ic_affirm_six)
                }
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Affirmation>?) {
        val newList = list?.toMutableList()
        differ.submitList(newList)
    }

    fun setAffirmationClickListener(affirmationClickListener: AffirmationClickListener) {
        this.clickListener = affirmationClickListener
    }

    class AffirmationViewHolder constructor(
        private val binding: AffirmationBinding,
        private val listener: AffirmationClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        val card = binding.constraintContainer

        fun bind(affirmation: Affirmation) {
            binding.affirmation.text = affirmation.affirmation
            binding.root.setOnClickListener {
                listener?.onItemSelected(affirmation, adapterPosition)
            }
        }
    }
}

interface AffirmationClickListener {
    fun onItemSelected(affirmation: Affirmation, position: Int)
}

