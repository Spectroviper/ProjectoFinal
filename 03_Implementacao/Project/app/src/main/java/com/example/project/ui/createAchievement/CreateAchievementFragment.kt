package com.example.project.ui.createAchievement

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.project.R
import com.example.project.data.model.Achievement
import com.example.project.databinding.FragmentAchievementBinding
import com.example.project.databinding.FragmentCreateAchievementBinding
import com.example.project.ui.achievement.AchievementViewModel

class CreateAchievementFragment : Fragment() {

    private var _binding: FragmentCreateAchievementBinding? = null
    private val viewModel: AchievementViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAchievementBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val achievement = checkNotNull(arguments?.getParcelable("achievement", Achievement::class.java))
        binding.achievement = achievement*/
        binding.achievementCreateFinish.setOnClickListener {
            //Add Finish Code Here
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}