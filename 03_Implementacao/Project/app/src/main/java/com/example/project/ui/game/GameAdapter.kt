package com.example.project.ui.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.data.model.Achievement
import com.example.project.data.model.Game
import com.example.project.databinding.ItemAchievementBinding
import com.example.project.databinding.ItemGameBinding
import com.example.project.ui.events.OnItemClickedListener

class GameAdapter(private val achievementList: List<Achievement>,
                  private val itemClickedListener: OnItemClickedListener? = null,
                  private val context: Context
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val viewBinding = ItemAchievementBinding.bind(itemView)
        fun bindView(item: Achievement, itemClickedListener: OnItemClickedListener?) {
            viewBinding.achievement = item
            itemView.setOnClickListener{
                //Toast.makeText(itemView.context, String.format("Click in %s Region", regionItem.name), Toast.LENGTH_LONG).show()
                itemClickedListener?.invoke(item)
            };
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_achievement, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = achievementList[position]
        holder.bindView(item,itemClickedListener)
    }
    override fun getItemCount(): Int {
        return achievementList.size
    }
    companion object {
        private val ACHIEVEMENT_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Achievement>() {
            override fun areItemsTheSame(oldItem: Achievement, newItem: Achievement): Boolean =
                oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Achievement, newItem: Achievement): Boolean =
                oldItem == newItem
        }
    }
}