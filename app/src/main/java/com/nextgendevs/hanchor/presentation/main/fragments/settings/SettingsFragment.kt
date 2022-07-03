package com.nextgendevs.hanchor.presentation.main.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextgendevs.hanchor.BuildConfig
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.databinding.FragmentSettingsBinding
import com.nextgendevs.hanchor.presentation.auth.AuthActivity
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.logoutUser
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : BaseSettingsFragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.displayProgressBar(false)

        val copyrightIcon = getContext.resources.getString(R.string.copyright_c)
        binding.copyright.text = "Copyright$copyrightIcon ${Calendar.getInstance().get(Calendar.YEAR)}  NextGenDevs"
        binding.version.text = BuildConfig.VERSION_NAME
        binding.catalogDate.text = dateCatalog(Calendar.getInstance())


        binding.logout.setOnClickListener {
            activity?.logoutUser(mySharedPreferences)
        }

        binding.license.setOnClickListener {
            visitUrl("https://www.apache.org/licenses/LICENSE-2.0")
        }

    }

    private fun dateCatalog(calendar: Calendar): String {
        val dateCatalog  = SimpleDateFormat("EEEE, MMMM dd, yyyy HH:mm:ss")
        return dateCatalog.format(calendar.time)
    }

    fun visitUrl(url: String) {
        val link  = if (!url.startsWith("http://") && !url.startsWith("https://"))
            "http://$url"
        else
            url

        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        val chooser = Intent.createChooser(i, "Update App")
        activity?.startActivity(chooser)
        Log.d(TAG, "updateApp: $link")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}