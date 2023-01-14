package com.first.a1806957


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.api.AGConnectApi
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.result.AuthAccount
import com.huawei.hms.support.account.service.AccountAuthService
import com.huawei.hms.support.api.entity.common.CommonConstant
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper





class MainActivity : AppCompatActivity() {

    private var mAuthService: AccountAuthService? = null

    // Set HUAWEI ID sign-in authorization parameters.
    private var mAuthParam: AccountAuthParams? = null

    // Define the request code for signInIntent.
    private val REQUESTCODESIGNIN = 1000

    // Define the log tag.
    private val TAG = "Account"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        findViewById<View>(R.id.H_btn).setOnClickListener {
            signInwithHID()
        }

    }

    fun signInwithHID() {


        //val signInOption: Unit = HuaweiIdAuthParamsHelper().setId().setProfile().
        mAuthParam = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setEmail()

            .createParams()

        mAuthService = AccountAuthManager.getService(this, mAuthParam)

        // Set the sign-in button click listener
        val task: Task<AuthAccount> = mAuthService!!.silentSignIn()
        task.addOnSuccessListener { authAccount -> // The silent sign-in is successful. Process the returned AuthAccount object to obtain the HUAWEI ID information.

            dealwithSignInResult(authAccount)

            AGConnectAuth.getInstance().signIn(this, AGConnectAuthCredential.HMS_Provider)


                }.addOnFailureListener { e ->
                // onFailure

                if (e is ApiException) {
                    val apiException = e
                    val signInIntent = mAuthService!!.signInIntent

                    signInIntent.putExtra(CommonConstant.RequestParams.IS_FULL_SCREEN, true)
                    startActivityForResult(signInIntent, REQUESTCODESIGNIN)

                }
            }

        }







private fun dealwithSignInResult(authAccount: AuthAccount) {
    // Obtain the HUAWEI ID information.

    Log.i(TAG, "display name:" + authAccount.displayName)
    val nextIntent = Intent(this, SecondActivity::class.java)
    nextIntent.putExtra("name", authAccount.displayName)
    startActivity(nextIntent)
   // nextIntent.putExtra("Hello " + authAccount.displayName + ".", R.id.text_view_userinfo)


    // TODO: Implement service logic after the HUAWEI ID information is obtained.
}



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AGConnectApi.getInstance().activityLifecycle()
            .onActivityResult(requestCode, resultCode, data)
      if (requestCode == REQUESTCODESIGNIN) {
        Log.i(TAG, "onActivityResult of sigInInIntent, request code: " + REQUESTCODESIGNIN)
        val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
        if (authAccountTask.isSuccessful) {

                // The sign-in is successful, and the authAccount object that contains the HUAWEI ID information is obtained.
            val authAccount = authAccountTask.result
            dealwithSignInResult(authAccount)
            Log.i(TAG, "onActivityResult of sigInInIntent, request code: " + REQUESTCODESIGNIN)
        } else {
            // The sign-in failed. Find the failure cause from the status code. For more information, please refer to Error Codes.

            Log.e(TAG, "sign in failed : " + (authAccountTask.exception as ApiException).statusCode)
        }
      }
    }



}





