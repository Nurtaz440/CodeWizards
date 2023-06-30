package com.example.farganaapp.ui.home.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farganaapp.R
import com.example.farganaapp.adapter.GameAapter
import com.example.farganaapp.databinding.FragmentAdminHomeBinding
import com.example.farganaapp.util.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AdminHomeFragment : Fragment() {
    private var _binding: FragmentAdminHomeBinding? = null
    val binding get() = _binding!!

    private val myAdapter by lazy {
        GameAapter {
            when (it.id) {
                1 -> {
                    findNavController().navigate(R.id.action_adminHomeFragment_to_adminElectricityFragment)
                }
                2 -> {
                    findNavController().navigate(R.id.action_adminHomeFragment_to_adminElectricityFragment)
                }
                3 -> {
                    findNavController().navigate(R.id.action_adminHomeFragment_to_adminElectricityFragment)
                }
                4 -> {
                    findNavController().navigate(R.id.action_adminHomeFragment_to_adminElectricityFragment)
                }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myAdapter.submitList(Constants.getGameItems())
        binding.rv.apply {
            layoutManager = GridLayoutManager(context,2, RecyclerView.VERTICAL,false)
            adapter=myAdapter
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        val c: Date = Calendar.getInstance().getTime()
        println("Current time => $c")

        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)
        binding.tvDate.text = formattedDate
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}