package com.example.project.ui.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.project.R
import com.example.project.data.model.Game
import com.example.project.databinding.FragmentGamesBinding

class GamesFragment : Fragment() {

    /*private var _homeViewModel: GamesViewModel? = null
    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel get() = _homeViewModel!!

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeViewModel = ViewModelProvider(this)[GamesViewModel::class.java]

        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getAllGames().observe(viewLifecycleOwner) {
            val allGames: List<Game> = it
            binding?.gamesRecyclerView?.adapter = GamesAdapter(allGames,
                itemClickedListener = {game->
                    val bundle = bundleOf(
                        "game" to game
                    )
                    findNavController()
                        .navigate(
                            R.id.action_nav_games_to_gameFragment,
                            bundle,
                            null
                        )
                }, view.context
            )
        }
        binding.createGame.setOnClickListener {
            findNavController().navigate(
                R.id.action_nav_games_to_createGameFragment)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/
}