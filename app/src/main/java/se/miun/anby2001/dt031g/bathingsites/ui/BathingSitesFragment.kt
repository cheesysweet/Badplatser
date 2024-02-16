package se.miun.anby2001.dt031g.bathingsites.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import se.miun.anby2001.dt031g.bathingsites.R
import se.miun.anby2001.dt031g.bathingsites.database.BathingSite
import se.miun.anby2001.dt031g.bathingsites.databinding.FragmentBathingSitesBinding
import se.miun.anby2001.dt031g.bathingsites.models.BathingSiteViewModel
import kotlin.random.Random


/**
 * Main fragment to display the home page
 */
class BathingSitesFragment : Fragment() {

    private var progressBar: ProgressBar? = null
    private var _binding: FragmentBathingSitesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BathingSiteViewModel by viewModels()
    private var storedSites: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBathingSitesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        progressBar = binding.processBar

        // displays random site if the img is pressed
        binding.ivBathingSign.setOnClickListener {
            getRandomSite()
        }

        observeBathSite()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    /*
    Creates functions for the items on the option menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_bathing_sites -> {
                startActivity(Intent(activity, BathingSiteLogActivity::class.java))
                true
            }
            R.id.action_fetch_bathing_sites -> {
                viewModel.fetchBathingSites()
                progressBar?.visibility = View.VISIBLE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*
    gets a random bathing site
     */
    private fun getRandomSite() {
        if (storedSites < 1) {
            Toast.makeText(activity, R.string.no_bathing_sites, Toast.LENGTH_LONG).show()
        } else {
            viewModel.getRandomSite(Random.nextInt(storedSites))
        }

    }

    /*
    formats the bathing site into a string
     */
    private fun formatBathingSite(bathingSite: BathingSite) {
        displayRandomSite(getString(
            R.string.bathing_site_toast,
            bathingSite.name,
            bathingSite.description,
            bathingSite.address,
            bathingSite.longitude,
            bathingSite.latitude,
            String.format("%.1f",bathingSite.grade),
            String.format("%.1f",bathingSite.temp),
            bathingSite.date)
        )
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

    /*
    observes the number of added bathing sites
     */
    private fun observeBathSite() {
        viewModel.bathingSitesAmount.observe(viewLifecycleOwner) { amount ->
            binding.tvBathingSites.text = getString(R.string.s_bathing_sites, amount)
            storedSites = amount
        }
        viewModel.amountStored.observe(viewLifecycleOwner) { message ->
            //Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
            progressBar?.visibility = View.INVISIBLE
        }
        viewModel.randomBathingSite.observe(viewLifecycleOwner) { bathingSite ->
            formatBathingSite(bathingSite)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}