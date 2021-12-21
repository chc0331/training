package com.example.myapplication.rxjavaroom.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.rxjavaroom.Injection
import com.example.myapplication.rxjavaroom.R
import com.example.myapplication.rxjavaroom.databinding.ActivityUserBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserActivity : AppCompatActivity() {

    private var mBinding: ActivityUserBinding? = null

    private lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: UserViewModel by viewModels { viewModelFactory }

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_user)
        mBinding = ActivityUserBinding.inflate(layoutInflater)
        // !! : not null
        setContentView(mBinding!!.root)

        viewModelFactory = Injection.provideViewModelFactory(this)
        mBinding!!.updateUserButton.setOnClickListener { updateUserName() }
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }

    private fun updateUserName() {
        // ?. : 변수가 null이 아닐때만 실행
        val userName = mBinding?.userNameInput?.text.toString()
        mBinding?.updateUserButton?.isEnabled = false
        disposable.add(
            viewModel.updateUserName(userName)
                    //IO thread
                .subscribeOn(Schedulers.io())
                    //main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mBinding?.updateUserButton?.isEnabled = true
                }, { error ->
                    Log.e(TAG, "Unable to update username", error)
                })
        )
    }

    companion object {
        private val TAG = UserActivity::class.java.simpleName
    }
}