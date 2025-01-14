package com.example.project.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.R
import com.example.project.data.model.Game
import com.example.project.databinding.ItemGameBinding
import com.example.project.ui.events.OnItemClickedListener

class HomeAdapter (
    private val myGamesList: List<Game>,
    private val itemClickedListener: OnItemClickedListener? = null,
    private val context: Context
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>()
    {
        class ViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
            private val viewBinding = ItemGameBinding.bind(itemView)
            fun bindView(gameItem: Game, itemClickedListener: OnItemClickedListener?) {

                viewBinding.game = gameItem

                Glide.with(context)
                    .load(gameItem.imageUrl)
                    .into(viewBinding.gameImage)

                itemView.setOnClickListener {
                    //Toast.makeText(itemView.context, String.format("Click in %s Region", regionItem.name), Toast.LENGTH_LONG).show()
                    itemClickedListener?.invoke(gameItem)
                };
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_game, parent, false)
            return ViewHolder(view, context)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val itemMyGames = myGamesList[position]
            holder.bindView(itemMyGames, itemClickedListener)
        }

        override fun getItemCount(): Int {
            return myGamesList.size
        }

    }