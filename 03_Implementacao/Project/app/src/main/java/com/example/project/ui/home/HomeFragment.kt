package com.example.project.ui.home

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
import com.example.project.databinding.FragmentHomeBinding
import okhttp3.internal.wait

class HomeFragment : Fragment() {

    private var _homeViewModel: HomeViewModel? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel get() = _homeViewModel!!

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.fetchAllMyGames()

        homeViewModel.myGames.observe(viewLifecycleOwner) { myGames ->
            binding.myGamesRecyclerView.adapter = HomeAdapter(
                myGames,
                itemClickedListener = {game->
                    val bundle = bundleOf(
                        "game" to game
                    )
                    findNavController()
                        .navigate(
                            R.id.action_nav_home_to_gameAddedFragment,
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