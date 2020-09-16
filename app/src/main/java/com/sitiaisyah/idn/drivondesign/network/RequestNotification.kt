package com.sitiaisyah.idn.drivondesign.network

import com.google.gson.annotations.SerializedName
import com.sitiaisyah.idn.drivondesign.model.Booking

class RequestNotification {

    @SerializedName("o")
    var token: String? = null

    @SerializedName("data")
    var sendNotificationModel: Booking? = null
}