package com.nextgendevs.hanchor.presentation.main.fragments.home.gratitude_list

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.nextgendevs.hanchor.business.domain.models.Gratitude
import com.nextgendevs.hanchor.business.domain.models.Todo
import com.nextgendevs.hanchor.databinding.GratitudeBinding
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import java.util.*

class GratitudeListAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"

    private var clickListener: GratitudeClickListener? = null

    private var glideRequestManager: RequestManager

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GlideRequestManagerEntryPoint {
        fun requestManager(): RequestManager
    }

    init {
        val myEntryPoint =
            EntryPointAccessors.fromApplication(context, GlideRequestManagerEntryPoint::class.java)
        glideRequestManager = myEntryPoint.requestManager()
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Gratitude>() {

        override fun areItemsTheSame(oldItem: Gratitude, newItem: Gratitude): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Gratitude, newItem: Gratitude): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(
        TodoRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(diffCallback).build()
    )

    internal inner class TodoRecyclerChangeCallback(private val adapter: GratitudeListAdapter) :
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
        return GratitudeViewHolder(
            GratitudeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GratitudeViewHolder -> {
                when ((position + 1) % 5) {
                    0 -> holder.card.setCardBackgroundColor(Color.parseColor("#D3DDD5"))
                    1 ->  holder.card.setCardBackgroundColor(Color.parseColor("#4CAF50"))
                    2 ->  holder.card.setCardBackgroundColor(Color.parseColor("#FFEB3B"))
                    3 ->  holder.card.setCardBackgroundColor(Color.parseColor("#B1E0FF"))
                    4 ->  holder.card.setCardBackgroundColor(Color.parseColor("#FFB1B1"))
                }
                holder.bind(glideRequestManager, differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Gratitude>?) {
        val newList = list?.toMutableList()
        differ.submitList(newList)
    }

    fun setGratitudeClickListener(todoClickListener: GratitudeClickListener) {
        this.clickListener = todoClickListener
    }

    class GratitudeViewHolder constructor(
        private val binding: GratitudeBinding,
        private val listener: GratitudeClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        val card = binding.cardContainer

        fun bind(glideRequestManager: RequestManager, gratitude: Gratitude) {
            binding.gratitudeMessage.text = gratitude.gratitudeMessage
            if (gratitude.gratitudeImageUrl.isEmpty()) {
                binding.imageContainer.visibility = View.INVISIBLE
            } else {
                binding.imageContainer.visibility = View.VISIBLE
                glideRequestManager.load(gratitude.gratitudeImageUrl).into(binding.image)
            }
            binding.root.setOnClickListener {
                listener?.onItemSelected(gratitude, adapterPosition)
            }
        }
    }
}

interface GratitudeClickListener {
    fun onItemSelected(gratitude: Gratitude, position: Int)
}

//private fun convertExistingDate(currentTime: Long): String {
//    val currentDate = Date(currentTime)
//    val cal = Calendar.getInstance()
//    cal.time = currentDate
//
//    val year = cal.get(Calendar.YEAR)
//    val month = cal.get(Calendar.MONTH)
//    val day = cal.get(Calendar.DAY_OF_MONTH)
//
//    return "$day/${month + 1}/$year"
//}
//
//private fun convertTime(currentTime: Long): String {
//    val hourMinuteFormat  = SimpleDateFormat("HH:mm")
//    val am_pmFormat  = SimpleDateFormat("hh:mm aa")
//
//    val currentDate = Date(currentTime)
//    val cal = Calendar.getInstance()
//    cal.time = currentDate
//
//    val hour = cal.get(Calendar.HOUR_OF_DAY)
//    val minute = cal.get(Calendar.MINUTE)
//
//    val time = "$hour:$minute"
//    var date: Date? = null
//    try {
//        date = hourMinuteFormat.parse(time)
//    } catch (e: ParseException) {
//        e.printStackTrace()
//    }
//
//    return am_pmFormat.format(date!!)
//}
//
