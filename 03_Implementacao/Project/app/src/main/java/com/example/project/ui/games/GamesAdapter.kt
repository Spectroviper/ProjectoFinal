package com.example.project.ui.games

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.data.model.Game
import com.example.project.databinding.ItemGameBinding
import com.example.project.ui.events.OnItemClickedListener

class GamesAdapter (
    private val allGamesList: List<Game>,
    private val itemClickedListener: OnItemClickedListener? = null,
    private val context: Context
) : RecyclerView.Adapter<GamesAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val viewBinding = ItemGameBinding.bind(itemView)
        fun bindView(gameItem: Game, itemClickedListener: OnItemClickedListener?) {

            viewBinding.game = gameItem
            itemView.setOnClickListener {
                //Toast.makeText(itemView.context, String.format("Click in %s Region", regionItem.name), Toast.LENGTH_LONG).show()
                itemClickedListener?.invoke(gameItem)
            };
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemAllGames = allGamesList[position]
        holder.bindView(itemAllGames, itemClickedListener)
    }

    override fun getItemCount(): Int {
        return allGamesList.size
    }

}