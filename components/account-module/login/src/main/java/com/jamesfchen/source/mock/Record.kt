package com.jamesfchen.source.mock

import com.jamesfchen.modle.Profile


/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawks.jamesf
 * @since: Oct/23/2018  Tue
 */
data class Record(

        val profile: Profile,
        var password: String,
        val code:Int
) {

//        fun getOrCreateToken(): String {
//            if (token == null) {
//                token = UUID.randomUUID().toString()
//            }
//            return token!!
//        }
//
//        fun getOrCreatRefreshToken(): String {
//            if (refreshToken == null) {
//                refreshToken == UUID.randomUUID().toString()
//            }
//            return refreshToken!!
//
//        }

}