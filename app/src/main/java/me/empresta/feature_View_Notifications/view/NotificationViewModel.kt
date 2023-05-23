package me.empresta.feature_View_Notifications.view

import android.provider.Settings.Global
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import me.empresta.DAO.InfoRequest
import me.empresta.DI.Repository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import me.empresta.feature_QRCode_Connection.view.UserPreviewView

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: Repository

): ViewModel() {

    public var info: List<InfoRequest>? = null

    init{
        // Mock data
        GlobalScope.launch{
            repository.deleteAllInfoRequests()

            repository.insertInfoRequest(
                InfoRequest(
                    "putas",
                    "vinho verde"
                )
            )

            repository.insertInfoRequest(
                InfoRequest(
                    "putas",
                    "mais vinho verde"
                )
            )

            repository.insertInfoRequest(
                InfoRequest(
                    "mais putas",
                    "vinho branco"
                )
            )

            repository.insertInfoRequest(
                InfoRequest(
                    "mais putas",
                    "tintoooo"
                )
            )

            val a = getInfoRequests()
        }
    }





     suspend fun getInfoRequests() {
         GlobalScope.launch {
             info = repository.getAllInfoRequests()
         }
         return

    }


}