package com.davis.kevin.technicav2.networking

import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.davis.kevin.technicav2.models.*
import com.davis.kevin.technicav2.ui.praesidium.Functie
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object FirebaseHandler {
    private val storage = Firebase.storage
    val clubText = MutableLiveData<Introductie>()
    val praesidiumList = MutableLiveData<List<Praesidium>>()
    val eventList = MutableLiveData<List<Evenement>>()
    val sponsorList = MutableLiveData<List<Partner>>()
    val vacancieList = MutableLiveData<List<Vacature>>()
    private val db = FirebaseFirestore.getInstance()

    fun getFirebaseData() {
        getHome()
        getPartners()
        getVacancies()
        getClubtext()
        getPraesidium()
    }

    private fun getClubtext() {
        db.collection("ClubTekst").get().addOnSuccessListener{ result ->
                for (document in result) {
                    val clubtext = Introductie(
                        id = document.id,
                        clubText = document.data["text"] as String?
                    )
                    clubText.value = clubtext
                }
            }
            .addOnFailureListener { exception ->
            }
    }

    private fun getPraesidium() {
        // Make a List
        var praesidia = mutableListOf<Praesidium>()
        // The data from the database
        db.collection("Praesidium").get().addOnSuccessListener{ result ->
            // Go through all data objects (document = 1 object)
            for (document in result) {
                // Create an image size
                val ONE_MEGABYTE: Long = 1024 * 1024
                // Use the imageLink variable to find the image in the FireBase Storage
                storage.reference.child(document["imageLink"] as String).getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener { image ->
                        // Create a Praesidium-Object with the data
                        val praesidium = Praesidium(
                            id = document.id,
                            name = document["name"] as String?,
                            surname = document["surName"] as String?,
                            birthday = document["birthday"] as String?,
                            studies = document["studies"] as String?,
                            functie = Functie.LongToEnum(document["function"] as Long?),
                            image = BitmapFactory.decodeByteArray(image, 0, image.size)
                        )
                        // Add the Praesidium-Object to the List
                        praesidia.add(praesidium)
                        // Sort the List when al items are loaded
                        if (document.id == result.last().id) praesidia.sortBy { praesidium -> praesidium.functie!!.value }
                    }.addOnFailureListener { exception ->
                        FirebaseCrashlytics.getInstance().recordException(exception)
                    }
                praesidiumList.value = praesidia
            }
        }
    }

    private fun getHome() {
        val events = mutableListOf<Evenement>()
        db.collection("Events").get().addOnSuccessListener { result ->
            for (document in result) {
                val ONE_MEGABYTE: Long = 1024 * 1024
                storage.reference.child(document["imageLink"] as String)
                    .getBytes(ONE_MEGABYTE).addOnSuccessListener { image ->
                        val dateString = document["date"] as String?
                        val event = Evenement(
                            id = document.id,
                            name = document["name"] as String?,
                            fbLink = document["fbLink"] as String?,
                            date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            image = BitmapFactory.decodeByteArray(image, 0, image.size)
                        )
                        events.add(event)
                        Log.d("Event", eventList.toString())
                    }.addOnFailureListener { exception ->
                        FirebaseCrashlytics.getInstance()
                            .recordException(exception)
                    }
                eventList.value = events
            }
        }
    }

    private fun getPartners() {
        val partners = mutableListOf<Partner>()
        db.collection("Sponsors").get().addOnSuccessListener { result ->
            for (document in result) {
                val ONE_MEGABYTE: Long = 1024 * 1024
                storage.reference.child(document["imageLink"] as String)
                    .getBytes(ONE_MEGABYTE).addOnSuccessListener { image ->
                        val partner = Partner(
                            id = document.id,
                            name = document["name"] as String?,
                            description = document["about"] as String?,
                            website = document["website"] as String?,
                            priority = document["priority"] as Long?,
                            image = BitmapFactory.decodeByteArray(image, 0, image.size)
                        )
                        partners.add(partner)
                        if (document.id == result.last().id) partners.sortBy { partner -> partner.priority }
                    }.addOnFailureListener { exception ->
                        FirebaseCrashlytics.getInstance().recordException(exception)
                    }
            }
        }
        sponsorList.value = partners
    }

    private fun getVacancies() {
        val vacancies = mutableListOf<Vacature>()
        db.collection("Vacatures").get().addOnSuccessListener { result ->
            for (document in result) {
                val vacature = Vacature(
                    id = document.id,
                    companyId = document["sponsorid"] as String?,
                    name = document["name"] as String?,
                    description = document["description"] as String?,
                    link = document["link"] as String?
                )
                vacancies.add(vacature)
            }
            vacancieList.value = vacancies
        }
    }
}