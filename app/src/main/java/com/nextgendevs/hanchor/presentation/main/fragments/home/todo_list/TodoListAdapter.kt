package com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.nextgendevs.hanchor.business.domain.models.Todo
import com.nextgendevs.hanchor.databinding.TodoBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TodoListAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"

    private var clickListener: TodoClickListener? = null

    private val diffCallback = object : DiffUtil.ItemCallback<Todo>() {

        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(
        TodoRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(diffCallback).build()
    )

    internal inner class TodoRecyclerChangeCallback(private val adapter: TodoListAdapter) :
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
        return TodoViewHolder(
            TodoBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TodoViewHolder -> {
//                when ((position + 1) % 5) {
//                    0 -> holder.card.setCardBackgroundColor(Color.parseColor("#D3DDD5"))
//                    1 ->  holder.card.setCardBackgroundColor(Color.parseColor("#4CAF50"))
//                    2 ->  holder.card.setCardBackgroundColor(Color.parseColor("#FFEB3B"))
//                    3 ->  holder.card.setCardBackgroundColor(Color.parseColor("#B1E0FF"))
//                    4 ->  holder.card.setCardBackgroundColor(Color.parseColor("#FFB1B1"))
//                }
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Todo>?) {
        val newList = list?.toMutableList()
        differ.submitList(newList)
    }

    fun setTodoClickListener(todoClickListener: TodoClickListener) {
        this.clickListener = todoClickListener
    }

    class TodoViewHolder constructor(
        private val binding: TodoBinding,
        private val listener: TodoClickListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        val card = binding.cardContainer

        fun bind(todo: Todo) {
            binding.date.text = convertExistingDate(todo.todoDate)
            binding.time.text = convertTime(todo.todoDate)
            binding.todoTask.text = todo.todoTask
            binding.isCompleted.isChecked = todo.todoIsCompleted

            binding.root.setOnClickListener {
                listener?.onItemSelected(todo, adapterPosition)
            }
        }
    }
}

interface TodoClickListener {
    fun onItemSelected(todo: Todo, position: Int)
}

private fun convertExistingDate(currentTime: Long): String {
    val currentDate = Date(currentTime)
    val cal = Calendar.getInstance()
    cal.time = currentDate

    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val day = cal.get(Calendar.DAY_OF_MONTH)

    return "$day/${month + 1}/$year"
}

private fun convertTime(currentTime: Long): String {
    val hourMinuteFormat  = SimpleDateFormat("HH:mm")
    val am_pmFormat  = SimpleDateFormat("hh:mm aa")

    val currentDate = Date(currentTime)
    val cal = Calendar.getInstance()
    cal.time = currentDate

    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)

    val time = "$hour:$minute"
    var date: Date? = null
    try {
        date = hourMinuteFormat.parse(time)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return am_pmFormat.format(date!!)
}

