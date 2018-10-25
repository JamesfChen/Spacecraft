package com.hawksjamesf.simpleweather.ui.login

import android.support.annotation.MainThread
import com.hawksjamesf.simpleweather.data.bean.login.ClientException
import com.hawksjamesf.simpleweather.data.bean.login.Profile
import com.hawksjamesf.simpleweather.data.bean.login.SignInReq
import com.hawksjamesf.simpleweather.data.bean.login.SignUpReq
import com.hawksjamesf.simpleweather.data.source.DataSource
import com.orhanobut.logger.Logger
import io.reactivex.Single

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawks.jamesf
 * @since: Oct/24/2018  Wed
 */
class Client(
        private var dataSource: DataSource
) : ObservableClient() {
    private val TAG = "Client"

    init {
        /**
         *refresh token，token
         * token存在三种状态：NONE、UNAUTHORIZED(token到期)、OTHERS
         * refresh token也存在三种状态：OK，REFRESH_FAILURE，OTHERS
         *
         * 1.自动登入：判断token是否存在
         * 2.自动刷新token，如果token为UNAUTHORIZED，也就是token到期，则会自动调用refresh token接口刷新token，
         * 如果刷新失败(REFRESH_FAILURE),自动退出。
         *
         */
    }

    @MainThread
    override fun signUp(signUpReq: SignUpReq): Single<Profile> {
        Logger.t(TAG).d("sign up")
        return dataSource.signUp(signUpReq)
    }

    @MainThread
    override fun signIn(signInReq: SignInReq): Single<Profile> {
        Logger.t(TAG).d("sign in--->req:$signInReq")
        stateData.apply {
            signinginDisposable?.dispose()
            signingupDisposable?.dispose()
        }
        return dataSource.signIn(signInReq)
                .onErrorResumeNext {
                    Single.error(it as? ClientException ?: ClientException.Unknown)
                }


    }

    @MainThread
    override fun signOut() {
       Logger.t(TAG).d("sign out")
        stateData.apply {
            signinginDisposable?.dispose()
            signingupDisposable?.dispose()
        }
        stateData = StateData()
        dataSource.signOut()
    }


}