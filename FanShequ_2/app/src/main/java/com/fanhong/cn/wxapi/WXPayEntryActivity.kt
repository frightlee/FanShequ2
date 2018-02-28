//package com.fanhong.cn.wxapi
//
//import android.app.Activity
//import android.app.AlertDialog
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//
//import com.fanhong.cn.R
//import com.fanhong.cn.SampleConnection
//import com.fanhong.cn.pay.ParameterConfig
//import com.tencent.mm.opensdk.constants.ConstantsAPI
//import com.tencent.mm.opensdk.modelbase.BaseReq
//import com.tencent.mm.opensdk.modelbase.BaseResp
//import com.tencent.mm.opensdk.openapi.IWXAPI
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
//import com.tencent.mm.opensdk.openapi.WXAPIFactory
//
///**
// * Created by Administrator on 2017/9/26.
// */
//
//class WXPayEntryActivity : Activity(), IWXAPIEventHandler {
//
//    private var api: IWXAPI? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.pay_result)
//
//        api = WXAPIFactory.createWXAPI(this, ParameterConfig.WX_APPID)
//        api!!.handleIntent(intent, this)
//    }
//
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        setIntent(intent)
//        api!!.handleIntent(intent, this)
//    }
//
//    fun onReq(baseReq: BaseReq) {
//
//    }
//
//    fun onResp(baseResp: BaseResp) {
//        //        Log.i("xqWXPay", "onPayFinish, errCode = " + baseResp.errCode);
//
//        if (baseResp.getType() === ConstantsAPI.COMMAND_PAY_BY_WX) {
//            //            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            //            builder.setTitle("提示");
//            //            builder.setMessage(getString(R.string.wx_pay_result)+"："+baseResp.errCode);
//            //            builder.show();
//
//            val intent = Intent()
//            intent.action = SampleConnection.MYPAY_RECEIVER
//            intent.putExtra("status", baseResp.errCode)
//            intent.putExtra("msg", baseResp.errStr)
//            sendBroadcast(intent)
//        }
//        finish()
//        //在需要处理支付结果的地方注册广播来接收消息
//    }
//}
