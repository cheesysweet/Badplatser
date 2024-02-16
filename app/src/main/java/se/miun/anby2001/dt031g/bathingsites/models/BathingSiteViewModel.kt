package se.miun.anby2001.dt031g.bathingsites.models

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import se.miun.anby2001.dt031g.bathingsites.database.BathingSite
import se.miun.anby2001.dt031g.bathingsites.database.BathingSiteRepository
import se.miun.anby2001.dt031g.bathingsites.network.BathingSiteRetriever

class BathingSiteViewModel(application: Application): AndroidViewModel(application) {
    private val mainScope = CoroutineScope(Dispatchers.IO)
    private val bathRepository = BathingSiteRepository(application.applicationContext)


    // live updates list of stored bathing sites
    val bathingSites: LiveData<List<BathingSite>> = bathRepository.getAllBathingSites()
    // live updates amount of stored bathing sites
    val bathingSitesAmount: LiveData<Int> = bathRepository.getCount()
    // mutable string to display if the bathing site was added or not
    val storedBathingSite = MutableLiveData<String>()
    val amountStored = MutableLiveData<String>()
    val randomBathingSite = MutableLiveData<BathingSite>()

    private val bathingSiteRetriever: BathingSiteRetriever = BathingSiteRetriever()



    /*
    adds a bathing site to the database and checks if it got saved or not
     */
    fun addBathing(bathSite: BathingSite) {
        val checkInputSuccess = -1
        mainScope.launch {
            val success = withContext(Dispatchers.IO) {
                bathRepository.addBathingSite(bathSite)
            }
            if (success == checkInputSuccess.toLong()) {
                storedBathingSite.postValue("Longitude and latitude already exists")
            } else {
                storedBathingSite.postValue("Bathing site stored")
            }
        }
    }

    /*
    fetches a new random bathing site
     */
    fun getRandomSite(rNum: Int) {
        mainScope.launch {
            val randomSite = withContext(Dispatchers.IO) {
                bathRepository.getRandomBathingSite(rNum)
            }
            randomBathingSite.postValue(randomSite)
        }
    }

    // fetches the bathing sites from the api
    fun fetchBathingSites() {

        var stored = 0
        val checkInputSuccess = -1
        val fetchJob = Job()

        // handles errors
        val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            storedBathingSite.postValue("Error")
        }

        // creates a separate thread for fetching the bathing sites
        val scope = CoroutineScope(fetchJob + Dispatchers.Main)

        // fetches the bathing sites, stores them and checks how many of them got stored
        scope.launch(errorHandler) {
            val bathingSites = bathingSiteRetriever.getBathingSites().bathingSites
            bathingSites.forEach { bathingSite ->
                val success = withContext(Dispatchers.IO) {
                    bathRepository.addBathingSite(bathingSite)
                }
                if (success != checkInputSuccess.toLong()) {
                    stored++
                }
            }
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    getApplication(), "stored $stored bathing site locations",
                    Toast.LENGTH_SHORT).show();
            }
            amountStored.postValue("stored bathingsites")
        }
    }
}