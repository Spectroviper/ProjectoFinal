package com.example.project.ui.scoreboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.project.R
import com.example.project.data.model.Game
import com.example.project.data.model.User
import com.example.project.databinding.FragmentScoreboardBinding
import com.example.project.ui.users.UsersViewModel

class ScoreboardFragment : Fragment() {

    private var _homeViewModel: ScoreboardViewModel? = null
    private var _binding: FragmentScoreboardBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel get() = _homeViewModel!!

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeViewModel = ViewModelProvider(this)[ScoreboardViewModel::class.java]

        _binding = FragmentScoreboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getAllScores().observe(viewLifecycleOwner) {
            val allScores: List<User> = it
            binding?.scoreboardRecyclerView?.adapter = ScoreboardAdapter(allScores,
                itemClickedListener = {user->
                    val bundle = bundleOf(
                        "user" to user
                    )
                    findNavController()
                        .navigate(
                            R.id.action_nav_ranking_to_profileFragment,
                            bundle,
                            null
                        )
                }, view.context
            )
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}