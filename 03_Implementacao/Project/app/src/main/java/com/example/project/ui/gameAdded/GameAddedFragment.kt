package com.example.project.ui.gameAdded

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import com.example.project.databinding.FragmentGameAddedBinding
import com.example.project.databinding.FragmentGameBinding
import com.example.project.ui.game.GameAdapter
import com.example.project.ui.game.GameViewModel

class GameAddedFragment : Fragment() {

    private var _binding: FragmentGameAddedBinding? = null
    private val viewModel: GameAddedViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameAddedBinding.inflate(inflater, container, false)
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
            binding?.achievementsRecyclerView?.adapter = GameAddedAdapter(achievements,
                itemClickedListener = { achievement ->
                    val bundle = bundleOf(
                        "achievement" to achievement
                    )
                    findNavController()
                        .navigate(
                            R.id.action_gameAddedFragment_to_achievementAddedFragment,
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