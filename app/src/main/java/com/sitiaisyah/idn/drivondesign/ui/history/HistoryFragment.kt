package com.sitiaisyah.idn.drivondesign.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sitiaisyah.idn.drivondesign.R
import com.sitiaisyah.idn.drivondesign.model.Booking
import com.sitiaisyah.idn.drivondesign.ui.history.adapter.HistoryAdapter
import com.sitiaisyah.idn.drivondesign.utils.Constan
import kotlinx.android.synthetic.main.fragment_history.*
import java.lang.IllegalStateException

class HistoryFragment : Fragment() {

    private var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        auth?.uid?.let { bookingHistoryUser(it) }
    }

    //mengambil data dari firebase
    private fun bookingHistoryUser(uid: String){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Constan.tb_booking)

        val data = ArrayList<Booking>()
        val query = myRef.orderByChild("uid").equalTo(uid)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {

                for (issue in snapshot.children){
                    val dataFirebase = issue.getValue(Booking::class.java)
                    val booking = Booking()

                    booking.tanggal = dataFirebase?.tanggal
                    booking.uid = dataFirebase?.uid
                    booking.lokasiAwal = dataFirebase?.lokasiAwal
                    booking.latAwal = dataFirebase?.latAwal
                    booking.lonAwal = dataFirebase?.lonAwal
                    booking.latTujuan = dataFirebase?.latTujuan
                    booking.lonTujuan = dataFirebase?.lonTujuan
                    booking.lokasiTujuan = dataFirebase?.lokasiTujuan
                    booking.jarak = dataFirebase?.jarak
                    booking.harga = dataFirebase?.harga
                    booking.status = dataFirebase?.status

                    data.add(booking)
                    showData(data)
                }
            }

        })
    }

    //ngeset data rv dari adapter
    private fun showData(data: java.util.ArrayList<Booking>) {

        if (data != null){
            try {
                rv_1.adapter = HistoryAdapter(data)
                rv_1.layoutManager = LinearLayoutManager(context)
            } catch (e: IllegalStateException){

            }
        }
    }
}