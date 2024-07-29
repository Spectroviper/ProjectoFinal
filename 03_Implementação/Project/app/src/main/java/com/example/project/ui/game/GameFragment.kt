package com.example.project.ui.game

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.project.R
import com.example.project.data.model.Achievement
import com.example.project.data.model.Game
import com.example.project.databinding.FragmentGameBinding

class GameFragment: Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val viewModel: GameViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //viewModel!!.initViewMode((activity?.application as PokemonApplication).pkContainer.pokemonRepository)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val game = checkNotNull(arguments?.getParcelable("game", Game::class.java))
        binding.game = game
        viewModel.getListAchievementsByGame(game.id).observe(viewLifecycleOwner, Observer {
            val achievements: List<Achievement> = it
            binding?.achievementsRecyclerView?.adapter = GameAdapter(achievements,
                itemClickedListener = { achievement ->
                    val bundle = bundleOf(
                        "achievement" to achievement
                    )
                    findNavController()
                        .navigate(
                            R.id.action_gameFragment_to_achievementFragment,
                            bundle,
                            null
                        )
                }, view.context
            )
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}