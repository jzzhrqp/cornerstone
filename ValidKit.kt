package com.cornerstone

import android.view.View
import android.widget.EditText
import java.util.regex.Pattern

/**
 * Created by iron on 17-12-25.
 * 验证手机号，密码，邮箱等格式是否正确的工具
 */

class ValidKit{
        public fun isPhoneValid(phone: String?): Result {
           if (phone==null||phone.isEmpty()){
               return Result(false, "手机号不能为空")
           }
            var p= Pattern.compile("^1\\d{10}$")
            var m=p.matcher(phone)
            if(m.find()){
              return Result(true, "")
            }else{
                return Result(false, "手机号格式不正确")
            }
        }

        public fun isPasswordValid(password: String): Result {
            if (password==null||password.isEmpty()){
                return Result(false, "密码不能为空")
            }
            if( Pattern.compile("\\s").matcher(password).find()) {
                return Result(false, "密码只能包含非空字符")
            }
            if( Pattern.compile("\\\\").matcher(password).find()) {
                return Result(false, "密码不能包含\\ 反斜杠")
            }
            if( Pattern.compile("[\"']").matcher(password).find()) {
                return Result(false, "密码不能包含\\\"，\\'单、双引号")
            }
            var l=password.length
            if (l<6 ||l>32){
                return Result(false, "密码长度只能在6位到32位之间")
            }

            return if( Pattern.compile("^[\\w!@#\$%^&*()+=;:,.<>?/`~|]*\$").matcher(password).find()) {
                Result(true, "密码合法")
            }else{
                Result(false, "密码似乎包含奇怪的字符")
            }


        }

        private fun isSmsCodeValid(code: String?): Result {
            if (code==null||code.isEmpty()){
                return Result(false, "验证码不能为空")
            }
            return if( Pattern.compile("^[0-9]{6}$").matcher(code).find()) {
                Result(true, "验证码正确")
            }else{
                Result(false, "验证码长度是6位")
            }

        }

        /**
         * 取出合法的手机号输入，如果没有，就给出错误提示
         */
        public fun validPhoneEdit(dt_phone:EditText):String?{
            var b=false
            dt_phone.error= null

            val phone =dt_phone.text.toString()

            var cancel = false
            var focusView: View? = null

            var result=isPhoneValid(phone)
            if (!result.apply){
                cancel = true
                focusView = dt_phone
                dt_phone.error=result.msg
            }

            return if (cancel) {
                focusView?.requestFocus()
                null
            }else{
                phone
            }

        }

        /**
         * 取出合法的密码输入，如果没有，就给出错误提示
         */
        public fun validPasswordEdit(dt_password:EditText):String?{
            dt_password.error= null
            val password =dt_password.text.toString()
            var cancel = false
            var focusView: View? = null

            var result=isPasswordValid(password)
            if (!result.apply){
                cancel = true
                focusView = dt_password
                dt_password.error=result.msg
            }

            return if (cancel) {
                focusView?.requestFocus()
                null
            }else{
                password
            }

        }

        /**
         * 取出合法的验证码输入，如果没有，就给出错误提示
         */
        fun validCodeEdit(et_code: EditText): String? {
            et_code.error= null
            val password =et_code.text.toString()
            var cancel = false
            var focusView: View? = null

            var result=isSmsCodeValid(password)
            if (!result.apply){
                cancel = true
                focusView = et_code
                et_code.error=result.msg
            }

            return if (cancel) {
                focusView?.requestFocus()
                null
            }else{
                password
            }
        }

    class Result(var apply:Boolean,var msg:String)
}
