package com.example.farganaapp.util

import com.example.farganaapp.R
import com.example.farganaapp.model.ClientData
import com.example.farganaapp.model.GameData

object Constants {
    fun getGameItems(): ArrayList<GameData> {
        return arrayListOf(
            GameData(
                id = 1,
                R.string.electricity,
                R.drawable.electricity
            ),
            GameData(
                id = 2,
                R.string.gas,
                R.drawable.gas
            ),
            GameData(
                id = 3,
                R.string.trash_transport,
                R.drawable.garbage
            ),
            GameData(
                id = 4,
                R.string.report,
                R.drawable.report
            )
        )
    }
    fun getClientItems(): ArrayList<ClientData> {
        return arrayListOf(
            ClientData(
                id = 1,
                R.string.electricity,
                "on",
                "off"
            ),
            ClientData(
                id = 2,
                R.string.gas,
                "on",
                "on"
            ),
            ClientData(
                id = 3,
                R.string.trash_transport,
                "off",
                "on"
            )
        )
    }
}