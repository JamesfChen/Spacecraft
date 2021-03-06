package com.jamesfchen.signin

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.jamesfchen.uicomponent.mvp.AutoDisposable
import com.jamesfchen.common.util.TextUtil
import com.jamesfchen.common.util.hideSoftInput
import com.jamesfchen.common.util.openActivity
import com.jamesfchen.common.util.subscribeBy
import com.jamesfchen.login.R
import com.jamesfchen.modle.ClientException
import com.jamesfchen.modle.ClientState
import com.jamesfchen.modle.SignInReqBody
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.editorActions
import com.jakewharton.rxbinding2.widget.textChanges
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import kotlinx.android.synthetic.main.activity_signin.*

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawks.jamesf
 * @since: Oct/23/2018  Tue
 */
@Route(path = "/account/signin")
class SignInActivity : SignInContract.View() {
//    @Inject override lateinit  var presenter:SignInContract.Presenter


//    override fun createPresenter(): SignInContract.Presenter {
//        return SignInPresenter(if (BuildConfig.MOCKED_DATA_ACCESS) {
//            MockSignInDataSource(Util.getApp(), UncertaintyConditions.UncertaintyParams(
//                    0f, 0f, 1500L, 500L
//            ))
//        } else {
//            RemoteSignInDataSource()
//        })
//    }

    private val TAG = "SignInActivity"

    override fun initComponent(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_signin)
//        App.getAppComponent().inject(this)
    }

    override fun handleCallback(autoDisposable: AutoDisposable) {
        tv_signup.clicks()
                .doOnNext { tv_signup.isPressed = true }
                .subscribe({
                    openActivity<SignUpActivity>(false)
                }, { }, {}).autoDisposable()

        Observables.combineLatest(atv_mobile.textChanges(), et_password.textChanges())
                .map { !TextUtil.isEmpty(it.first, it.second) }
                .subscribe { bt_sign_in.isEnabled = it }
                .autoDisposable()

        et_password.editorActions()
                .filter { it == EditorInfo.IME_ACTION_DONE }
                .map { Unit }
                .mergeWith(bt_sign_in.clicks())
                .publish().apply {
                    subscribe { hideSoftInput() }
                    filter { !TextUtil.isEmpty(atv_mobile.text.toString(), et_password.text.toString()) }
                            .subscribe { signin(SignInReqBody(atv_mobile.text.toString(), et_password.text.toString())) }

                }.connect(autoDisposable)


        onBackPress.map { presenter.state == ClientState.SIGNING_IN }
                .publish().apply {
                    filter { it }.subscribe { presenter.signOut() }.autoDisposable()
                    filter { !it }.subscribe { navigateBack() }.autoDisposable()

                }.connect(autoDisposable)

    }

//    private fun isValidate(mobile: CharSequence, password: CharSequence) = mobile.isNotBlank() && password.isNotBlank()

    fun signin(signInReq: SignInReqBody) {
        presenter.signIn(signInReq)
                .subscribeBy(
                        onSubscribe = { presenter.stateData = StateData(signinginDisposable = it) },
                        onSuccess = {
                            presenter.stateData = StateData(profile = it)
                            Logger.t(TAG).d("sign in--->resp:$it \nand state:${presenter.stateData}")
                            //todo:将新的Profile存入到数据库
                        },
                        onError = {
                            presenter.stateData = StateData()
                            presenter.emitSignInFailedEvent(signInReq.mobile, signInReq.password, it as ClientException)
                        }

                )

    }

    override fun onResume(autoDisposable: AutoDisposable) {
        super.onResume(autoDisposable)
        presenter.stateObservable.publish().apply {
            Logger.t(TAG).d("onResume")
            map { it == ClientState.SIGNING_IN }.observeOn(AndroidSchedulers.mainThread()).subscribe {
                pb_signin_progress.visibility = if (it) View.VISIBLE else View.GONE
            }.autoDisposable()
            map { it == ClientState.SIGNED_OUT }.subscribe { ll_signin.visibility }.autoDisposable()
//            filter { it == ClientState.SIGNED_IN }.subscribe { openActivity<HomeActivity>() }.autoDisposable()
            filter { it == ClientState.SIGNING_UP }.subscribe { openActivity<SignUpActivity>(false) }.autoDisposable()

        }.connect(autoDisposable)

        presenter.signInFailedEventObservable.publish().apply {
            map { it.mobile }.subscribe { atv_mobile.text }.autoDisposable()
            map { it.password }.subscribe { et_password.text }.autoDisposable()
            map { it.excep }.observeOn(AndroidSchedulers.mainThread()).subscribe { Toast.makeText(this@SignInActivity, "$it", Toast.LENGTH_SHORT).show() }
        }.connect(autoDisposable)
    }
}