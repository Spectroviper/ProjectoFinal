package com.example.project.ui.myProfile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.project.R
import com.example.project.data.model.User
import com.example.project.databinding.FragmentMyProfileBinding
import com.example.project.databinding.FragmentProfileBinding
import com.example.project.ui.profile.ProfileViewModel

class MyProfileFragment : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val viewModel: MyProfileViewModel by viewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = checkNotNull(arguments?.getParcelable("user", User::class.java))
        binding.user = user
        binding.logout.setOnClickListener {
            findNavController().navigate(
                R.id.action_myProfileFragment_to_signInActivity)
        }
        binding.editUser.setOnClickListener {
            findNavController().navigate(
                R.id.action_myProfileFragment_to_editProfileFragment)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}