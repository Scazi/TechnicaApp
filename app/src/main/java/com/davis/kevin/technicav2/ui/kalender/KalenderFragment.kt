package com.davis.kevin.technicav2.ui.kalender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ExpandableListView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.davis.kevin.technicav2.R

class KalenderFragment : Fragment() {

    private lateinit var kalenderViewModel: KalenderViewModel
    private lateinit var KalenderLV: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        kalenderViewModel =
            ViewModelProviders.of(this).get(KalenderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_kalender, container, false)



        return root
    }

    private fun initListData() {
        //listGroup.add(getString(R.string))

    }
}