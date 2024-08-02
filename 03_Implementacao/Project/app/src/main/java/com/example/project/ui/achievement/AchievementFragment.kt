package com.example.project.ui.achievement

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.project.EqualSpacingItemDecoration
import com.example.project.R
import com.example.project.data.model.Achievement
import com.example.project.data.model.Game
import com.example.project.databinding.FragmentAchievementBinding

class AchievementFragment : Fragment() {

    private var _binding: FragmentAchievementBinding? = null
    private val viewModel: AchievementViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val achievement = checkNotNull(arguments?.getParcelable("achievement", Achievement::class.java))
        binding.achievement = achievement
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}