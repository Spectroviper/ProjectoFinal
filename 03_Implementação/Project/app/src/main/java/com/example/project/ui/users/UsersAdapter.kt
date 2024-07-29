package com.example.project.ui.users

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.data.model.Game
import com.example.project.data.model.User
import com.example.project.databinding.ItemGameBinding
import com.example.project.databinding.ItemUserBinding
import com.example.project.ui.events.OnItemClickedListener

class UsersAdapter (
    private val allUsersList: List<User>,
    private val itemClickedListener: OnItemClickedListener? = null,
    private val context: Context
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>()
{
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val viewBinding = ItemUserBinding.bind(itemView)
        fun bindView(userItem: User, itemClickedListener: OnItemClickedListener?) {

            viewBinding.user = userItem
            itemView.setOnClickListener {
                //Toast.makeText(itemView.context, String.format("Click in %s Region", regionItem.name), Toast.LENGTH_LONG).show()
                itemClickedListener?.invoke(userItem)
            };
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemAllGames = allUsersList[position]
        holder.bindView(itemAllGames, itemClickedListener)
    }

    override fun getItemCount(): Int {
        return allUsersList.size
    }

}