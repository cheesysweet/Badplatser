package se.miun.anby2001.dt031g.bathingsites.ui

import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.model.LatLng
import se.miun.anby2001.dt031g.bathingsites.R
import se.miun.anby2001.dt031g.bathingsites.database.BathingSite
import se.miun.anby2001.dt031g.bathingsites.databinding.FragmentAddBathingSitesBinding
import se.miun.anby2001.dt031g.bathingsites.models.BathingSiteViewModel
import java.text.SimpleDateFormat
import java.util.*


/**
 * Fragment page used to add a bathing site
 */
class AddBathingSitesFragment : Fragment() {

    private var _binding: FragmentAddBathingSitesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BathingSiteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentAddBathingSitesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true) //Displays the option menu

        initView()
    }

    // initiates observers and sets the current date
    private fun initView() {
        observeAddBathingSite()
        setCurrentDate()
    }

    // displays menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_bathing_site, menu)
    }

    /*
    Creates functions for the items on the option menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> { // runs clearInputs if clear action is pressed
                clearInputs()
                true
            }
            R.id.action_save -> {
                addBathingSite() // runs addBathingSite if save action is pressed
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*
    Adds a new bathing site with the values entered
     */
    private fun addBathingSite() {
        addSite(binding.etName.text.toString(),
            binding.etDescription.text.toString(),
            binding.etAddress.text.toString(),
            float(binding.etLatitude),
            float(binding.etLongitude),
            binding.ratingBar.rating,
            float(binding.etWaterTemp),
            binding.etDateTemp.text.toString())
    }


    /*
    Forces the water temp input to be a float and sets default to 0 if the user don't enter a temp
     */
    private fun float(temp: EditText): Double {
        return if (temp.text.isBlank()) {
            0.0
        } else temp.text.toString().toDouble()
    }

    /*
    Creates the new bathing site and validates that the inputs is correct
     */
    private fun addSite(
        name: String, description: String, address: String, latitude: Double,
        longitude: Double, grade: Float, temp: Double, date: String) {
        val newBathingSite = BathingSite(
            name = name,
            description = description,
            address = address,
            latitude = latitude,
            longitude = longitude,
            grade = grade,
            temp = temp,
            date = date
        )

        if (isNotValid(newBathingSite)) { // checks so all items are valid
            viewModel.addBathing(newBathingSite) // adds the new bathing site
        }
    }

    /*
    checks so name and address fields are entered
     */
    private fun isNotValid(bathSite: BathingSite): Boolean {
        return when {
            bathSite.name.isBlank() || bathSite.address.isNullOrBlank() &&
                    (bathSite.latitude.equals(0.0) || bathSite.longitude.equals(0.0)) -> {
                binding.etName.error = "Name is required!"
                binding.etAddress.error = "Address is required!"
                binding.etLatitude.error = "Latitude is required!"
                binding.etLongitude.error = "Longitude is required!"
                false
            }
            bathSite.latitude.equals(0.0) && bathSite.longitude.equals(0.0) -> {
                // checks if the address exists
                val value = context?.let { getLocationByAddress(it, bathSite.address) }
                if (value != null) {
                    bathSite.latitude = value.latitude
                    bathSite.longitude = value.longitude
                    true
                } else {
                    false
                }
            }
            else -> true
        }
    }


    /*
    sets the lat and long value if the address exists other wise the location is not added
     */
    private fun getLocationByAddress(context: Context, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        try {
            val address = coder.getFromLocationName(strAddress, 5) ?: return null
            val location = address.first()
            return LatLng(location.latitude, location.longitude)
        } catch (e: Exception) {
            Toast.makeText(activity, "Address don´t exists", Toast.LENGTH_LONG).show()
        }
        return null
    }

    /*
    clear all input fields
     */
    private fun clearInputs() {
        binding.etName.text.clear()
        binding.etDescription.text.clear()
        binding.etAddress.text.clear()
        binding.etLatitude.text.clear()
        binding.etLongitude.text.clear()
        binding.ratingBar.rating = (0.0F)
        binding.etWaterTemp.text.clear()
        setCurrentDate()
    }

    /*
    sets the current date
     */
    private fun setCurrentDate() {
        binding.etDateTemp.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))
    }


    /*
    observes if the bathing site was added or not and displays a message and clears the
    input fields if the site was added
     */
    private fun observeAddBathingSite() {
        viewModel.storedBathingSite.observe(viewLifecycleOwner) { message ->
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
            if (message == getString(R.string.bathing_site_stored)) {
                clearInputs()
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
