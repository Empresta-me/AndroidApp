package me.empresta.feature_QRCode_Connection.use_case

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.empresta.Crypto.Base58
import me.empresta.Crypto.KeyGen
import me.empresta.Crypto.Utils
import me.empresta.DAO.Friend
import me.empresta.DI.Repository
import me.empresta.PubSub.PubSub
import me.empresta.RemoteAPI.DTO.VouchBody
import java.security.interfaces.ECPublicKey
import javax.inject.Inject

class VouchUseCase @Inject constructor(private val repository: Repository,private val pubSub: PubSub) {

    suspend fun connect(pubkey: String, communities: List<LinkedHashMap<String, Any>>, description:String, value:Int) : Boolean{
        try {

            val keybytes = Utils.uncompressedToCompressed(Utils.toUncompressedPoint(KeyGen.getPubKey() as ECPublicKey?))
            val mykeyBase58 = Base58.encode(keybytes)



            GlobalScope.launch(){
                var myCommunities = repository.getCommunities()

                for ( community in myCommunities){

                    // Publish Vouch Message in my exchange
                    PubSub.Publish_Vouch(community.url.substring(7,community.url.length - 6), mykeyBase58,pubkey,"my communities", communities,description,value)

                    var otherComs = ""

                    for (map in communities) {
                        val url = map["url"] as String
                        otherComs += url.substring(7, url.length - 6) + " "
                    }

                    val vouch = VouchBody("VOUCH","clock","hash","nonce","signature",mykeyBase58,pubkey,"my communities",otherComs,value,description)

                    repository.postVouch(community.url,vouch);


                    var communityUrl = "";

                    // Starts to listen to friend exchange
                    if (communities.isNotEmpty()){
                        communityUrl = communities[0].get("url") as String
                        pubSub.start_listening(pubkey,communityUrl.substring(7,community.url.length - 6))
                    }

                    // Adds person to list of friends so it can subscribe to it later
                    repository.insertFriend(Friend(pubkey,communityUrl.substring(7,community.url.length - 6)));


                }
            }


            return true
        }
        catch (e:Exception){
            println(e)
            return false
        }
    }

}