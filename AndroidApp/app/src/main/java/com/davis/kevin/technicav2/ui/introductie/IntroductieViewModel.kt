package com.davis.kevin.technicav2.ui.introductie

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.davis.kevin.technicav2.models.Introductie
import com.davis.kevin.technicav2.networking.FirebaseHandler

class IntroductieViewModel : ViewModel {

    var id : String? = ""
    var text : String? = ""
    private lateinit var mContext: Context

    constructor() : super()
    constructor(clubtext: Introductie): super() {
        this.id = clubtext.id
        this.text = clubtext.clubText
    }

    fun getContext(context: Context) {
        mContext = context
    }

    fun getArray() : MutableLiveData<Introductie>{
        return FirebaseHandler.clubText
    }

}