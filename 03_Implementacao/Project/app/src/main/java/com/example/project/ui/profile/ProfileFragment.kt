package com.example.project.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.project.R
import com.example.project.data.model.Achievement
import com.example.project.data.model.User
import com.example.project.databinding.FragmentAchievementBinding
import com.example.project.databinding.FragmentProfileBinding
import com.example.project.ui.achievement.AchievementViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = checkNotNull(arguments?.getParcelable("user", User::class.java))
        binding.user = user
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}