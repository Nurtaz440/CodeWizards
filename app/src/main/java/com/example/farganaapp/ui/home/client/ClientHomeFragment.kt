package com.example.farganaapp.ui.home.client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farganaapp.R
import com.example.farganaapp.adapter.ClientAdapter
import com.example.farganaapp.adapter.GameAapter
import com.example.farganaapp.database.AppDatabase
import com.example.farganaapp.databinding.FragmentAdminHomeBinding
import com.example.farganaapp.databinding.FragmentClientHomeBinding
import com.example.farganaapp.repository.UserRepository
import com.example.farganaapp.util.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ClientHomeFragment : Fragment() {

    private var _binding: FragmentClientHomeBinding? = null
    val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private val myAdapter by lazy {
        ClientAdapter {
            when (it.id) {
                1 -> {
                    findNavController().navigate(R.id.action_clientHomeFragment_to_adminTrashFragment)
                }
                2 -> {
                    findNavController().navigate(R.id.action_clientHomeFragment_to_adminTrashFragment)
                }
                3 -> {
                    findNavController().navigate(R.id.action_clientHomeFragment_to_adminTrashFragment)
                }
                4 -> {
                    findNavController().navigate(R.id.action_clientHomeFragment_to_adminTrashFragment)
                }

            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClientHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myAdapter.submitList(Constants.getClientItems())
        binding.rvClient.apply {
            layoutManager = GridLayoutManager(context,2, RecyclerView.VERTICAL,false)
            adapter=myAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        val c: Date = Calendar.getInstance().getTime()
        println("Current time => $c")

        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)
        binding.tvDate.text = formattedDate

        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        val userRepository = UserRepository(userDao)
        profileViewModel = ViewModelProvider(
            this,
            ProfileViewModel.ProfileViewModelFactory(userRepository)
        ).get(ProfileViewModel::class.java)

        // Observe the allUsers LiveData
        profileViewModel.allUsers.observe(viewLifecycleOwner) {
            // Update UI with user data
            binding.textView2.text = getString(R.string.salom, it.name!!)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}