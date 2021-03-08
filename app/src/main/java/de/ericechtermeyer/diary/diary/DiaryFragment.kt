package de.ericechtermeyer.diary.diary

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.ui.DayBinder
import de.ericechtermeyer.diary.R
import de.ericechtermeyer.diary.data.DiaryEntry
import de.ericechtermeyer.diary.data.DiaryViewModel
import de.ericechtermeyer.diary.databinding.FragmentDiaryBinding
import kotlinx.coroutines.InternalCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@InternalCoroutinesApi
class DiaryFragment : Fragment() {

    // binding
    private lateinit var binding: FragmentDiaryBinding

    // diaryViewModle to connect database to Fragment
    private lateinit var mDiaryViewModel: DiaryViewModel

    // DialogWindow after entering a new entry
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i("Debug", "DF onCreateView()")

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_diary, container, false)

        // initialize TextViews with random text and the current date
        randomText()
        binding.diaryTV3.text = currentDate()

        mDiaryViewModel = ViewModelProvider(this).get(DiaryViewModel::class.java)

        binding.diaryButton2.setOnClickListener{
            insertDataToDatabase()
        }

        binding.diaryButton1.setOnClickListener {
            findNavController().navigate(R.id.action_diaryFragment_to_homeFragment)
        }

        return binding.root
    }

    // insert data to database, Navigation to home using navigationGraph
    private fun insertDataToDatabase() {
        val entry = binding.diaryEditField.text.toString()

        if (inputCheck(entry)){
            val diaryEntry = DiaryEntry(0, currentDate(), entry)
            mDiaryViewModel.addEntry(diaryEntry)

            findNavController().navigate(R.id.action_diaryFragment_to_homeFragment)

            showProgressOne()

        }else{
            Toast.makeText(context, "Please write something...", Toast.LENGTH_LONG).show()
        }
    }

    // check if the user has written something
    private fun inputCheck(entry: String):Boolean{
        return entry.isNotEmpty()
    }

    // returns current date as a String
    private fun currentDate(): String{
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        return sdf.format(Date()).toString()
    }

    // random Text for TextView
    private fun randomText(){
        when((0..7).random()){
            0 -> binding.diaryTV1.text = "How was you\nday?"
            1 -> binding.diaryTV1.text = "What have you\ndone today?"
            2 -> binding.diaryTV1.text = "How did you\nfeel today?"
            3 -> binding.diaryTV1.text = "What made you\nhappy today?"
            4 -> binding.diaryTV1.text = "What was beautiful today?"
            5 -> binding.diaryTV1.text = "Why do you love this day?"
            6 -> binding.diaryTV1.text = "What made your day?"
            7 -> binding.diaryTV1.text = "What did you enjoy today?"
        }
    }

    // This is probably the most important function for the calendar. It shows a DialogWindow after the user entered some text
    // This function is probably part of the errors
    private fun showProgressOne(){
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.fragment_progress_one, null)

        // update Text Resources
        dialogView.findViewById<TextView>(R.id.progressOneTV2).text = "You have a\nnew streak of ${addStreak()}!"
        dialogView.findViewById<TextView>(R.id.progressOneTV3).text = "You are better than\n${userPercent()}% of our users!"

        // show Calendar .dayBinder is an Error so I have commented it out
        /*
        dialogView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
            }
        }
        */

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    // calculates the ranking of the user from a mathematical function
    private fun userPercent(): Double {
        val sharedPrefInt = requireActivity().getSharedPreferences("onStreakInt", Context.MODE_PRIVATE)
        val days = sharedPrefInt.getInt("Streak", 1)

        val percent = (100-(100-0)*kotlin.math.exp(-0.07*days.toDouble()))

        return (percent * 100).roundToInt().toDouble()/100
    }

    // checks the streak, sharedPref is used to save the last date and if the difference between the currentDate and the last
    // date saved is 1, the Streak is increased by one. The streakInt is also saved as a sharedPref
    private fun addStreak():Int{
        val dateToday = SimpleDateFormat("dd.MM.yyyy").parse(currentDate())

        val sharedPrefDate = requireActivity().getSharedPreferences("onStreakDate", Context.MODE_PRIVATE)
        val lastDateString = sharedPrefDate.getString("Date", currentDate())

        val editorDate = sharedPrefDate.edit()
        editorDate.putString("Date", currentDate())
        editorDate.apply()

        val dateLast = SimpleDateFormat("dd.MM.yyyy").parse(lastDateString)

        val dateDiff = ((dateToday.time - dateLast.time) / (1000 * 60 * 60 * 24)).toInt()

        if (dateDiff == 0){
            val sharedPrefInt = requireActivity().getSharedPreferences("onStreakInt", Context.MODE_PRIVATE)
            return sharedPrefInt.getInt("Streak", 1)

        }else if (dateDiff == 1){
            val sharedPrefInt = requireActivity().getSharedPreferences("onStreakInt", Context.MODE_PRIVATE)
            val streakCount = sharedPrefInt.getInt("Streak", 1) + 1

            val editorInt = sharedPrefInt.edit()
            editorInt.putInt("Streak", streakCount)
            editorInt.apply()

            return streakCount
        }else{
            return 1
        }
    }
}