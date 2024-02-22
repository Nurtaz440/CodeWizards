package com.example.farganaapp.ui.home.admin.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.farganaapp.R
import com.example.farganaapp.databinding.FragmentAdminTrashBinding
import com.example.farganaapp.databinding.FragmentClientHomeBinding
import com.example.farganaapp.ui.home.client.ui.AreasFragment


class AdminTrashFragment : Fragment() {


    private var _binding: FragmentAdminTrashBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAdminTrashBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener {
            val fragment = AreasFragment()

            val ft: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            ft.replace(R.id.fl_main, fragment)
                .addToBackStack(null) // Add the fragment to the back stack
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
        }
    }
}