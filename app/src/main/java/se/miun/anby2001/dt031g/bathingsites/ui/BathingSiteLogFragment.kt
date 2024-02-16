package se.miun.anby2001.dt031g.bathingsites.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import se.miun.anby2001.dt031g.bathingsites.R
import se.miun.anby2001.dt031g.bathingsites.database.BathingSite
import se.miun.anby2001.dt031g.bathingsites.databinding.FragmentBathingSiteLogBinding
import se.miun.anby2001.dt031g.bathingsites.models.BathingSiteAdapter
import se.miun.anby2001.dt031g.bathingsites.models.BathingSiteViewModel


class BathingSiteLogFragment : Fragment() {

    private var _binding: FragmentBathingSiteLogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BathingSiteViewModel by viewModels()

    private val bathingSites = arrayListOf<BathingSite>()
    private val bathAdapter = BathingSiteAdapter(bathingSites){
        intentsBathingSite(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBathingSiteLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true) //Displays the option menu

        initView()
    }

    // initiates the recyclerview and observing
    private fun initView() {
        binding.rvBathingSitesLog.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvBathingSitesLog.adapter = bathAdapter
        observeBathingSites()
    }

    // remakes the recyclerview when new bathing site is added
    private fun observeBathingSites() {
        viewModel.bathingSites.observe(viewLifecycleOwner, Observer { bathSites ->
            this@BathingSiteLogFragment.bathingSites.clear()
            this@BathingSiteLogFragment.bathingSites.addAll(bathSites)
            bathAdapter.notifyDataSetChanged()
        })
    }

    /*
    intents the bathing site into a string
     */
    private fun intentsBathingSite(bathingSite: BathingSite) {
        val intent = Intent(activity, MapsActivity::class.java)
        intent.putExtra("bathingSite", bathingSite)
        startActivity(intent)
    }

    /*
    displays the bathing site in a dialog
     */
    private fun displayRandomSite(message: String) {
        AlertDialog.Builder(activity)
            .setTitle(getString(R.string.random_bathing_site))
            .setMessage(message)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
