package com.davis.kevin.technicav2.ui.sponsors

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.davis.kevin.technicav2.R
import com.davis.kevin.technicav2.adapters.CustomPartnerAdapter
import kotlinx.android.synthetic.main.fragment_praesidium.*
import kotlinx.android.synthetic.main.fragment_sponsors.*
import me.relex.circleindicator.CircleIndicator3

class SponsorsFragment : Fragment() {

    private lateinit var sponsorsViewModel: SponsorsViewModel
    private var PartnerVP : ViewPager2? = null
    private lateinit var viewOfLayout : View
    private var customPartnerAdapter : CustomPartnerAdapter? = null
    private lateinit var ctx : Context
    private var arrayList = ArrayList<SponsorsViewModel>()

    companion object {
        var sponsorId: String? = null
        fun navigateToVacatures(navController: NavController, nav_id: Int, sponsorId: String?) {
            this.sponsorId = sponsorId
            navController.navigate(nav_id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        ctx = requireActivity().applicationContext

        viewOfLayout = inflater.inflate(R.layout.fragment_sponsors, container, false)
        PartnerVP = viewOfLayout.findViewById(R.id.partner_VP)

        sponsorsViewModel = ViewModelProviders.of(this).get(SponsorsViewModel::class.java)
        sponsorsViewModel.getContext(ctx)
        sponsorsViewModel.getArray().observe(viewLifecycleOwner, Observer { partners ->
            for(partner in partners){
                val partnerViewModel = SponsorsViewModel(partner)
                arrayList.add(partnerViewModel)
            }
            customPartnerAdapter = CustomPartnerAdapter(ctx, arrayList, this.findNavController())
            PartnerVP!!.adapter = customPartnerAdapter

            val indicator = viewOfLayout.findViewById<CircleIndicator3>(R.id.indicator)
            indicator.setViewPager(partner_VP)
        })

        return viewOfLayout
    }
}