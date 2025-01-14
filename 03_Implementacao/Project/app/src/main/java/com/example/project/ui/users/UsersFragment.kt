package com.example.project.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.project.AppDomain
import com.example.project.R
import com.example.project.data.model.Game
import com.example.project.data.model.User
import com.example.project.databinding.FragmentGamesBinding
import com.example.project.databinding.FragmentUsersBinding
import com.example.project.ui.games.GamesAdapter
import com.example.project.ui.games.GamesViewModel
import com.example.project.ui.scoreboard.ScoreboardFragment
import com.example.project.ui.scoreboard.ScoreboardViewModel

class UsersFragment : Fragment() {

    private var _homeViewModel: UsersViewModel? = null
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel get() = _homeViewModel!!

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeViewModel = ViewModelProvider(this)[UsersViewModel::class.java]

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.fetchAllUsers()

        homeViewModel.users.observe(viewLifecycleOwner) { users ->
            binding.usersRecyclerView.adapter = UsersAdapter(
                users,
                itemClickedListener = { user ->
                    if (user.id == homeViewModel.getUserId()) {
                        val bundle = bundleOf("user" to user)
                        findNavController().navigate(
                            R.id.action_nav_users_to_myProfileFragment,
                            bundle
                        )
                    } else {
                        val bundle = bundleOf("user" to user)
                        findNavController().navigate(
                            R.id.action_nav_users_to_profileFragment,
                            bundle
                        )
                    }
                },
                view.context
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}