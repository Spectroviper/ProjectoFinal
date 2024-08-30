package com.example.project.ui.achievementAdded

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.project.R
import com.example.project.data.model.Achievement
import com.example.project.databinding.FragmentAchievementAddedBinding
import com.example.project.databinding.FragmentAchievementBinding
import com.example.project.ui.achievement.AchievementViewModel

class AchievementAddedFragment : Fragment() {

    private var _binding: FragmentAchievementAddedBinding? = null
    private val viewModel: AchievementAddedViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAchievementAddedBinding.inflate(inflater, container, false)
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