package com.bravedroid.jobby.presentation.companion

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bravedroid.jobby.databinding.ItemJobBinding
import com.bravedroid.jobby.domain.entities.Job
import com.bravedroid.jobby.presentation.util.ViewGroupExt.getLayoutInflater
import javax.inject.Inject

class JobsAdapter @Inject constructor(

) : ListAdapter<Job, JobsAdapter.ViewHolder>(DiffUtilCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), position)

    private object DiffUtilCallBack : DiffUtil.ItemCallback<Job>() {
        override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean =
            oldItem.description == newItem.description

        override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean =
            oldItem == newItem
    }

    class ViewHolder(
        private val binding: ItemJobBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(
                parent: ViewGroup,
            ): ViewHolder {
                val binding = ItemJobBinding.inflate(parent.getLayoutInflater(), parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(item: Job, position: Int) {
            binding.role.text = item.role
        }
    }
}
