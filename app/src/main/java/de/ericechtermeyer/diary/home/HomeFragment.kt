package de.ericechtermeyer.diary.home

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.ericechtermeyer.diary.MainActivity
import de.ericechtermeyer.diary.R
import de.ericechtermeyer.diary.RecyclerAdapter
import de.ericechtermeyer.diary.data.DiaryEntry
import de.ericechtermeyer.diary.data.DiaryViewModel
import de.ericechtermeyer.diary.databinding.FragmentHomeBinding
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

@InternalCoroutinesApi
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mDiaryViewModel: DiaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("Debug", "HF onCreateView()")

        if (!onBoardingFinishedCheck()){
            findNavController().navigate(R.id.action_homeFragment_to_viewPagerFragment)
        }

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.homeButton2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_diaryFragment)
        }

        randomImage()

        // Recycler
        val adapter = RecyclerAdapter()
        val recyclerView = binding.homeRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // DiaryViewModel
        mDiaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        // Add welcome text, SharedPref for Streak (Date, Int)
        if (!addWelcomeFinishedCheck()) {
            addWelcome()
            streakDateSharedPrefCreate()
            streakIntSharedPrefCreate()
        }

        mDiaryViewModel.readAllData.observe(viewLifecycleOwner, Observer { entry ->
            adapter.setData(entry)
        })

        return binding.root
    }

    // random Image for HomeScreen
    private fun randomImage() {
        when ((1..3).random()) {
            1 -> binding.homeIV1.setImageResource(R.drawable.home_img_1)
            2 -> binding.homeIV1.setImageResource(R.drawable.home_img_2)
            3 -> binding.homeIV1.setImageResource(R.drawable.home_img_3)
        }
    }

    // add welcome message for the user, only for the first time
    private fun addWelcome() {
        val entry =
            "Welcome to daily notes we are glad to see you here! You can add an entry by pressing the button in the bottom right corner.\n\nBut watch out! You cannot delete an entry once it has been saved! So write only about thoughts that you would like to read about in the future <3\n\n" +
                    "Happy writing!\n\n"
        val diaryEntry = DiaryEntry(0, currentDate(), entry)

        mDiaryViewModel.addEntry(diaryEntry)
        addWelcomeFinished()
    }

    // currentDate in String
    private fun currentDate(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        return sdf.format(Date()).toString()
    }

    // sharedPref to remember if user has already got a welcome message
    private fun addWelcomeFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onWelcome", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

    // checks if the user has a welcomeMessage
    private fun addWelcomeFinishedCheck(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onWelcome", Context.MODE_PRIVATE)

        return sharedPref.getBoolean("Finished", false)
    }

    // Shared Pref erstellt, um Datum zu speichern
    private fun streakDateSharedPrefCreate(){
        val sharedPref = requireActivity().getSharedPreferences("onStreakDate", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("Date", currentDate())
        editor.apply()
    }

    // Shared Pref erstellt, um Int zu speichern
    private fun streakIntSharedPrefCreate(){
        val sharedPref = requireActivity().getSharedPreferences("onStreakInt", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt("Streak", 1)
        editor.apply()
    }

    // checks if the user has finished onBoarding
    private fun onBoardingFinishedCheck(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)

        return sharedPref.getBoolean("Finished", false)
    }
}