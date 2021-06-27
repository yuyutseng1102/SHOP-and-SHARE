package com.chloe.shopshare.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.chloe.shopshare.NavigationDirections
import com.chloe.shopshare.databinding.FragmentSearchBinding
import com.chloe.shopshare.ext.getVmFactory
import com.chloe.shopshare.host.CategoryType
import com.chloe.shopshare.host.CountryType


class SearchFragment : Fragment() {
    private val viewModel by viewModels<SearchViewModel> { getVmFactory() }
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        var lastCheckedId = View.NO_ID

        binding.apply {
            chipGroupCategory.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == View.NO_ID) {
                    group.check(lastCheckedId)
                    return@setOnCheckedChangeListener
                }
                lastCheckedId = checkedId
                when (checkedId) {
                    chipWoman.id -> viewModel?.selectCategory(CategoryType.WOMAN.category)
                    chipMan.id -> viewModel?.selectCategory(CategoryType.MAN.category)
                    chipChild.id -> viewModel?.selectCategory(CategoryType.CHILD.category)
                    chipShoesBag.id -> viewModel?.selectCategory(CategoryType.SHOES_BAG.category)
                    chipMakeup.id -> viewModel?.selectCategory(CategoryType.MAKEUP.category)
                    chipHealth.id -> viewModel?.selectCategory(CategoryType.HEALTH.category)
                    chipFood.id -> viewModel?.selectCategory(CategoryType.FOOD.category)
                    chipLiving.id -> viewModel?.selectCategory(CategoryType.LIVING.category)
                    chipAppliance.id -> viewModel?.selectCategory(CategoryType.APPLIANCE.category)
                    chipPet.id -> viewModel?.selectCategory(CategoryType.PET.category)
                    chipStationary.id -> viewModel?.selectCategory(CategoryType.STATIONARY.category)
                    chipSport.id -> viewModel?.selectCategory(CategoryType.SPORT.category)
                    chipComputer.id -> viewModel?.selectCategory(CategoryType.COMPUTER.category)
                    chipTicket.id -> viewModel?.selectCategory(CategoryType.TICKET.category)
                    chipOther.id -> viewModel?.selectCategory(CategoryType.OTHER.category)
                }
            }

            chipGroupCountry.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == View.NO_ID) {
                    group.check(lastCheckedId)
                    return@setOnCheckedChangeListener
                }
                lastCheckedId = checkedId
                when (checkedId) {
                    chipTaiwan.id -> viewModel?.selectCountry(CountryType.TAIWAN.country)
                    chipJapan.id -> viewModel?.selectCountry(CountryType.JAPAN.country)
                    chipKorea.id -> viewModel?.selectCountry(CountryType.KOREA.country)
                    chipChina.id -> viewModel?.selectCountry(CountryType.CHINA.country)
                    chipUsa.id -> viewModel?.selectCountry(CountryType.USA.country)
                    chipCanada.id -> viewModel?.selectCountry(CountryType.CANADA.country)
                    chipEu.id -> viewModel?.selectCountry(CountryType.EU.country)
                    chipAustralia.id -> viewModel?.selectCountry(CountryType.AUSTRALIA.country)
                    chipSouthEastAsia.id -> viewModel?.selectCountry(CountryType.SOUTH_EAST_ASIA.country)
                    chipOther.id -> viewModel?.selectCountry(CountryType.OTHER.country)
                }
            }
        }

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    NavigationDirections.navigateToResultFragment(it.category ?: 0, it.country ?: 0)
                )
                viewModel.onDetailNavigated()
            }
        })
        
        return binding.root
    }
}