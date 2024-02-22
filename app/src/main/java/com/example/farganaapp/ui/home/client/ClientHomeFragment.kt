package com.example.farganaapp.ui.home.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.farganaapp.R
import com.example.farganaapp.database.AppDatabase
import com.example.farganaapp.databinding.FragmentClientHomeBinding
import com.example.farganaapp.repository.UserRepository
import com.example.farganaapp.ui.home.admin.ui.AdminTrashFragment
import com.example.farganaapp.ui.home.client.ui.AreasFragment
import com.example.farganaapp.ui.home.client.ui.ChatFragment
import com.example.farganaapp.ui.registration.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class ClientHomeFragment : Fragment() {

    private var _binding: FragmentClientHomeBinding? = null
    val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var viewModel: AuthViewModel
    private var auth: FirebaseAuth

    // to check whether sub FAB buttons are visible or not.
    var isAllFabsVisible: Boolean? = null

    init {
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClientHomeBinding.inflate(inflater, container, false)
       val fragment = AreasFragment()

       val ft: FragmentTransaction =
           requireActivity().supportFragmentManager.beginTransaction()
       ft.replace(R.id.fl_main, fragment)
           .addToBackStack(null) // Add the fragment to the back stack
           .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
       binding.textView2.visibility = View.VISIBLE
       binding.cvGreen.visibility = View.VISIBLE

       val bottomNav = binding.navView
       bottomNav.setOnItemSelectedListener {
           when(it.itemId) {
               R.id.areasFragment->{
                   binding.textView2.visibility = View.VISIBLE
                   binding.cvGreen.visibility = View.VISIBLE
                   binding.addPersonFab.show()
                   val fragment = AreasFragment()

                   val ft: FragmentTransaction =
                       requireActivity().supportFragmentManager.beginTransaction()
                   ft.replace(R.id.fl_main, fragment)
                       .addToBackStack(null) // Add the fragment to the back stack
                       .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

                   true
               }
                else->{
                    binding.textView2.visibility = View.GONE
                    binding.cvGreen.visibility = View.GONE
                    val fragment = ChatFragment()

                    val ft: FragmentTransaction =
                        requireActivity().supportFragmentManager.beginTransaction()
                    ft.replace(R.id.fl_main, fragment)
                        .addToBackStack(null) // Add the fragment to the back stack
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()
                    binding.addGas.hide()
                    binding.addElectrFab.hide()
                    binding.addPersonFab.hide()
                    binding.tv1.setVisibility(View.GONE)
                    binding.tv2.setVisibility(View.GONE)
                    true
               }

           }
       }

       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Now set all the FABs and all the action name texts as GONE
        binding.addGas.setVisibility(View.GONE)
        binding.addElectrFab.setVisibility(View.GONE)
        binding.tv1.setVisibility(View.GONE)
        binding.tv2.setVisibility(View.GONE)

        // action name texts and all the sub FABs are invisible
        isAllFabsVisible = false
        // using setOnClickListener you can see below
        // using setOnClickListener you can see below
        binding.addPersonFab.setOnClickListener { view ->
            isAllFabsVisible = if (!isAllFabsVisible!!) {
                // when isAllFabsVisible becomes true make all
                // the action name texts and FABs VISIBLE
                binding.addGas.show()
                binding.addElectrFab.show()
                binding.tv1.setVisibility(View.VISIBLE)
                binding.tv2.setVisibility(View.VISIBLE)
                binding.addGas.setOnClickListener { view ->
                    binding.textView2.visibility = View.GONE
                    binding.cvGreen.visibility = View.GONE
                    
                    val fragment = AdminTrashFragment()

                    val ft: FragmentTransaction =
                        requireActivity().supportFragmentManager.beginTransaction()
                    ft.replace(R.id.fl_main, fragment)
                        .addToBackStack(null) // Add the fragment to the back stack
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

                }
                binding.addElectrFab.setOnClickListener { view ->
                    binding.textView2.visibility = View.GONE
                    binding.cvGreen.visibility = View.GONE
                    val fragment = AdminTrashFragment()

                    val ft: FragmentTransaction =
                        requireActivity().supportFragmentManager.beginTransaction()
                    ft.replace(R.id.fl_main, fragment)
                        .addToBackStack(null) // Add the fragment to the back stack
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit()

                }
                // make the boolean variable true as we
                // have set the sub FABs visibility to GONE
                true
            } else {
                // when isAllFabsVisible becomes true make
                // all the action name texts and FABs GONE.
                binding.addGas.hide()
                binding.addElectrFab.hide()
                binding.tv1.setVisibility(View.GONE)
                binding.tv2.setVisibility(View.GONE)
                // make the boolean variable false as we
                // have set the sub FABs visibility to GONE
                false
            }
        }


        //  myAdapter.submitList(Constants.getClientItems())
//        binding.rvClient.apply {
//            layoutManager = GridLayoutManager(context,2, RecyclerView.VERTICAL,false)
//            adapter=myAdapter
//            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
//        }
//        val c: Date = Calendar.getInstance().getTime()
//        println("Current time => $c")
//
//        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
//        val formattedDate: String = df.format(c)
//        binding.tvDate.text = formattedDate

        val userDao = AppDatabase.getInstance(requireContext()).userDao()
        val userRepository = UserRepository(userDao)
        profileViewModel = ViewModelProvider(
            this,
            ProfileViewModel.ProfileViewModelFactory(userRepository)
        ).get(ProfileViewModel::class.java)

        // Observe the allUsers LiveData
//        profileViewModel.allUsers.observe(viewLifecycleOwner) {
//            // Update UI with user data
//            binding.textView2.text = getString(R.string.salom, it.name!!)
//        }


        viewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(AuthViewModel::class.java)
//        val pref = SharedPreferencesManager.getEmail(requireContext())
//        val email = pref.first
//        val pass = pref.second

//        auth.signInWithEmailAndPassword(email!!, pass!!)
//            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
//                override fun onComplete(p0: Task<AuthResult>) {
//
//                    if (p0.isSuccessful) {
//                        val user = auth.currentUser
//                        val userRef =
//                            FirebaseDatabase.getInstance().getReference("users/${user?.uid}")
//                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                                val userName =
//                                    dataSnapshot.child("name").getValue(String::class.java)!!
//                                Log.d("Tag" + "get UserRepository", userName)
//                                val userSurname =
//                                    dataSnapshot.child("surname").getValue(String::class.java)!!
//                                val userPhoneNumber =
//                                    dataSnapshot.child("number").getValue(String::class.java)!!
//
////                                navUsername.text =
////                                    getString(R.string.full_name, userName, userSurname)
////                                navUserNomer.text = userPhoneNumber
//                            }
//
//                            override fun onCancelled(error: DatabaseError) {
//
//                            }
//
//                        })
//
//                    }
//                }
//            })
//
//        // Set registration flag to false
//        SharedPreferencesManager.setRegistered(requireContext(), true)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}