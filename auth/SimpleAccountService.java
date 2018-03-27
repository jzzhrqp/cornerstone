package com.cornerstone.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2018/3/21.
 * 本地保存用户名和密码的最佳实践，
 * 使用AccountManger是存储凭证的最佳选择。
 * 该SampleSyncAdapter(https://github.com/googlesamples/android-BasicSyncAdapter/#readme)提供了如何使用它的一个例子。
 * 详情指导：
 * 1.http://blog.csdn.net/wy3243996/article/details/52411139
 * (原文：http://blog.udinic.com/2013/04/24/write-your-own-android-authenticator/)
 *
 * 2.https://developer.android.com/reference/android/accounts/AccountManager.html
 *
 * 使用步骤：
 * 1.在AndroidManifest.xml中添加如下：
     <service android:name="com.aoshuotec.iron.Smartcontrol.model.AccountService">
     <!-- Required filter used by the system to launch our account service. -->
     <intent-filter>
     <action android:name="android.accounts.AccountAuthenticator" />
     </intent-filter>
     <!-- This points to an XMLf ile which describes our account service. -->
     <meta-data android:name="android.accounts.AccountAuthenticator"
     android:resource="@xml/authenticator" />
     </service>
   2.在res/xml/目录下新建一个文件，名为：authenticator.xml,然后在该文件中添加如下内容：
     <account-authenticator xmlns:android="http://schemas.android.com/apk/res/android"
     android:accountType="com.example.android.basicsyncadapter.account"
     android:icon="@drawable/ic_launcher"
     android:smallIcon="@drawable/ic_launcher"
     android:label="@string/app_name"
     />

 这里是给出的一个示例，在其他项目中要实际参考这个类，重新写一个新的，否则这个类被用在多个项目中会冲突

 */
public class SimpleAccountService extends Service {
    private static final String TAG = "AccountService";
    public static final String ACCOUNT_NAME = "Account";
    private Authenticator mAuthenticator;
    private Context context;
    public static String authTokenType="com.aoshuotec.smartControl.account";

    @Override
    public void onCreate() {
        Log.i(TAG, "Service created");
        mAuthenticator = new Authenticator(this);
        context=this;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }

    public class Authenticator extends AbstractAccountAuthenticator {
        public Authenticator(Context context) {
            super(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                     String s) {
            throw new UnsupportedOperationException();
        }

        /**
         当用户打算登录并在一个设备上新建账户时，会调用这个方法。
         我们需要返回一个Bundle，其中包含一个会启动我们自己的
         _ AccountAuthenticatorActivity （稍后解释）的Intent，
         这个方法在app通过调用 AccountManager#addAccount() (需要特殊权限)时被调用。
         或者在手机设置中，用户点击“添加新用户“时被调用
         */
        @Override
        public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                 String s, String s2, String[] strings, Bundle bundle)
                throws NetworkErrorException {
            final Intent intent = new Intent(context, AccountAuthenticatorActivity.class);
//            intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, accountType);
//            intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
//            intent.putExtra(AuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);
            bundle = new Bundle();
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                         Account account, Bundle bundle)
                throws NetworkErrorException {//自己实现：验证用户的密码
            return null;
        }

        /**
         获取特定帐户的指定类型的身份验证令牌，并在必要时提示用户输入凭据。
         此方法适用于在前台运行的应用程序，它可以直接询问用户密码。
         如果先前生成的身份验证令牌针对此帐户和类型进行了缓存，则会返回该令牌
         。否则，如果保存的密码可用，则将其发送到服务器以生成新的身份验证令牌。
         否则，将提示用户输入密码。 一些认证者具有认证令牌类型，其值依赖于认证者。
         某些服务使用不同的令牌类型来访问不同的功能 - 例如，Google使用不同的身份验证
         令牌访问同一个帐户的Gmail和Google日历。注意：如果您的应用定位在API级别22以及之前
         ，那么这些平台需要USE_CREDENTIALS权限。请参阅API级别22中有关此功能的文档。
         可以从任何线程调用此方法，但返回的线程 AccountManagerFuture不得在主线程中使用。
         */
        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                   Account account, String s, Bundle bundle)
                throws NetworkErrorException {////自己完成获取鉴权token的流程
             bundle = new Bundle();

            final AccountManager am = AccountManager.get(context);

            String authToken = am.peekAuthToken(account, authTokenType);
            if (TextUtils.isEmpty(authToken)) {
                final String password = am.getPassword(account);
                if (password != null) {
                   //登录
                }
            }

            if (!TextUtils.isEmpty(authToken)) {
                final Bundle result = new Bundle();
                result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
                result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
                result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                return result;
            }

            //打开登录界面
            final Intent intent = new Intent(context, AccountAuthenticatorActivity.class);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, accountAuthenticatorResponse);
            bundle.putParcelable(AccountManager.KEY_INTENT, intent);
            return bundle;
        }

        @Override
        public String getAuthTokenLabel(String s) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                        Account account, String s, Bundle bundle)
                throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                  Account account, String[] strings)
                throws NetworkErrorException {
            throw new UnsupportedOperationException();
        }
    }

}

