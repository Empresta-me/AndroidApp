package me.empresta.feature_QRCode_Connection.view

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.empresta.RemoteAPI.DTO.CommunityInfo
import me.empresta.feature_QRCode_Connection.use_case.ConnectToCommunity
import me.empresta.feature_QRCode_Connection.use_case.IDP.IDPAuthenticator
import javax.inject.Inject

@HiltViewModel
class CommunityPreviewView @Inject constructor(
    private val connectToCommunity_useCase: ConnectToCommunity
) : ViewModel() {


    private val _state = mutableStateOf(CommunityInfo())
    val state : CommunityInfo = _state.value
    var reached = true
    var usesIDP = false

    fun getInfo(url: String,usesIDP: Boolean){
        this.usesIDP = usesIDP
        connectToCommunity_useCase.seturl("http://$url/")
        GlobalScope.launch{
            try {
                val result = connectToCommunity_useCase.getInfo()
                if (result != null){
                    _state.value =  result
                    reached = true
                }
                else {reached = false}
            }
            catch (e:Exception){
                reached = false
            }
        }

    }

    fun connectWithCommunity(password: String,context: Context,url: String){
        val authorizationCode = ""
        /*if (usesIDP) {
            Log.d("USE_IDP", "use idp: $usesIDP")
            startIDPoauth2(context,url)
        }*/

        GlobalScope.launch {
            print(_state)
            if (! connectToCommunity_useCase.challengeCommunity()){
                /*TODO*/
                // Launch an exception Screen
            }
            Log.d("USE_IDP", "use idp: $usesIDP")
            if (!_state.value.title?.let {
                    _state.value.public_key?.let { it1 ->
                       /* if (usesIDP) {
                            Log.d("USE_IDP", "use idp: $usesIDP")
                            connectToCommunity_useCase.associateWithIDP(
                                it, it1, context = context
                            )
                        }
                        else{*/
                            Log.d("USE_IDP", "use idp: $usesIDP")
                            connectToCommunity_useCase.associate(password,
                                it, it1
                            )
                        //}
                    }
                }!!){
                /*TODO*/
                // Launch an exception Screen
            }
        }
    }

    fun startIDPoauth2(context: Context,url: String) {
        val auth = IDPAuthenticator(context,url)
        val apiService = auth.createApiService()
        auth.associateWithIDP(apiService, context)
    }

    fun get() = _state.value
}