package se.miun.anby2001.dt031g.bathingsites.network

import retrofit2.http.GET
import se.miun.anby2001.dt031g.bathingsites.database.bathing

/**
 * interface to fetch the bathing sites
 */
interface NetworkInterface {

    @GET("api/")
    suspend fun getBathingSites(): bathing
}