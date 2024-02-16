package se.miun.anby2001.dt031g.bathingsites.database

/**
 * Data class for storing the array of bathing sites fetched from api
 */
data class bathing(
    val bathingSites: ArrayList<BathingSite> = arrayListOf(BathingSite(
        name = "Sundlingska gården",
        description = "",
        address = "",
        latitude = 20.0257,
        longitude = 64.0237,
        grade = 0.0F,
        temp = 0.0,
        date = ""
    ))
)