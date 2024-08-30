package com.example.project.ui.editAchievement

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.project.R
import com.example.project.databinding.FragmentCreateGameBinding
import com.example.project.databinding.FragmentEditAchievementBinding
import com.example.project.ui.achievement.AchievementViewModel

class EditAchievementFragment : Fragment() {

    private var _binding: FragmentEditAchievementBinding? = null
    private val viewModel: AchievementViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAchievementBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*val achievement = checkNotNull(arguments?.getParcelable("achievement", Achievement::class.java))
        binding.achievement = achievement*/
        binding.achievementEditFinish.setOnClickListener {
            //Add Finish Code Here
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}