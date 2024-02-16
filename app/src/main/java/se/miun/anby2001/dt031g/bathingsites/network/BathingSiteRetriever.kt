package se.miun.anby2001.dt031g.bathingsites.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.miun.anby2001.dt031g.bathingsites.database.bathing

/**
 * retrieves the bathing sites
 */
class BathingSiteRetriever {
    private val networkInterface : NetworkInterface

    // url for website
    companion object {
        var BaseUrl = "https://dt031g.programvaruteknik.nu/bathingsites/"
    }

    // creates interface to fetch bathing sites
    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkInterface = retrofit.create(NetworkInterface::class.java)
    }

    // fetches bathing sites
    suspend fun getBathingSites(): bathing {
        return networkInterface.getBathingSites()
    }
}