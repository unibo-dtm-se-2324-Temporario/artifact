package com.example.temporario.Home

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.temporario.Events.Event
import com.example.temporario.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime

class InitializationFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private var eventId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_initialization, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dates = arrayOf<LocalDateTime>(
            LocalDateTime.of(2024, 4, 3, 20, 20),
            LocalDateTime.of(2024, 4, 10, 8, 0),
            )

        database = Firebase.database.reference

        for (i in dates.indices) {
            val date = dates[i]
            val userUID: String = if (i == 0) {
                "ZGgBhrKmpyeppRUFZP24diupQGX2"
            } else {
                "Y3Uh0J00ibaSbBWC4NGwxv3yo473"
            }
            val event = Event(eventId.toString(), userUID,"event nr $eventId", date, i + 1)
            database.child("Events").child(eventId.toString()).setValue(event).addOnSuccessListener {
                Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }

            eventId += 1
        }
    }

}