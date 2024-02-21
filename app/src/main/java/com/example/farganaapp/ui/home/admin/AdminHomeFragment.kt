package com.example.farganaapp.ui.home.admin

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.farganaapp.R
import com.example.farganaapp.adapter.GameAapter
import com.example.farganaapp.databinding.FragmentAdminHomeBinding
import com.example.farganaapp.ui.registration.viewModel.AuthViewModel
import com.example.farganaapp.util.Constants
import com.example.farganaapp.util.SharedPreferencesManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AdminHomeFragment : Fragment() {
    private var _binding: FragmentAdminHomeBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: AuthViewModel
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
        SharedPreferencesManager.setRegistered(requireContext(), true)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(AuthViewModel::class.java)

        binding.ivSignOut.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext())

            // set message of alert dialog
            dialogBuilder
                // if the dialog is cancelable
                .setCancelable(false)
                // positive button text and action
                .setPositiveButton(
                    "Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        viewModel.signOut()

                        // Set registration flag to false
                        SharedPreferencesManager.setRegistered(
                            requireContext(),
                            false
                        )

                        viewModel.getUserLogged().observe(viewLifecycleOwner) {
                            if (it) {
                                findNavController().navigate(R.id.action_adminHomeFragment_to_signInFragment)
                            }
                        }

                    })
                // negative button text and action
                .setNegativeButton(
                    "Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

            // create dialog box
            val alert = dialogBuilder.create()
            // set title for alert dialog box
            alert.setTitle("Leave of Your Account?")

            // show alert dialog
            alert.show()

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}