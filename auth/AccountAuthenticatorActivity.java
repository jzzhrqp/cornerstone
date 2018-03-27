package com.cornerstone.auth;

import android.os.Bundle;

/**
 * Created by Administrator on 2018/3/22.
 * AccountAuthenticatorActivity 是整个auth过程中唯一直接与用户交互的 Activity 。
 * Authenticator 首先调用这个 Activity ，此 Activity 将展现一个用户登录表单，发送到服务器鉴权用户，
 * 并将结果传给 authenticator 。我们继承 AccountAuthenticatorActivity ，不仅要实现常规Activity的功能，
 * 还要实现 setAccountAuthenticatorResult() 方法。
 * 此方法负责将鉴权过程的结果发送给 Authenticator。此方法也为我们省掉了与 Authenticator 直接交互。
 * 一般是登录界面继承 AccountAuthenticatorActivity
 */

public class AccountAuthenticatorActivity extends android.accounts.AccountAuthenticatorActivity {

}
