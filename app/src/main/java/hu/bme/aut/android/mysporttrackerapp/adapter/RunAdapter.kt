package hu.bme.aut.android.mysporttrackerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.mysporttrackerapp.databinding.ItemRunBinding
import hu.bme.aut.android.mysporttrackerapp.model.SportEntity
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : ListAdapter<SportEntity, RunAdapter.ViewHolder>(itemCallback) {

    companion object {
        val itemCallback = object : DiffUtil.ItemCallback<SportEntity>() {
            override fun areItemsTheSame(oldItem: SportEntity, newItem: SportEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SportEntity, newItem: SportEntity): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    }


    var itemClickListener: RunItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            ItemRunBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false))


    inner class ViewHolder(val binding: ItemRunBinding) : RecyclerView.ViewHolder(binding.root) {
        var run: SportEntity? = null

        init {
            itemView.setOnClickListener {
                run?.let { run -> itemClickListener?.onItemClick(run) }
            }

            itemView.setOnLongClickListener { view ->
                run?.let {run -> itemClickListener?.onItemLongClick(adapterPosition, view, run) }
                true
            }
        }
    }

    interface RunItemClickListener {
        fun onItemClick(run: SportEntity)
        fun onItemLongClick(position: Int, view: View, run: SportEntity): Boolean
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val run = this.getItem(position)

        holder.run = run

        val distanceInKm = "${run.distanceInMeters / 1000f}km"
        holder.binding.tvDistance.text = distanceInKm

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = run.timestamp

        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        holder.binding.tvDate.text = dateFormat.format(calendar.time)


    }
}
