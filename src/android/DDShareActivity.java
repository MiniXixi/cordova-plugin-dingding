package __PACKAGE_NAME__;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.dingtalk.share.ddsharemodule.DDShareApiFactory;
import com.android.dingtalk.share.ddsharemodule.IDDAPIEventHandler;
import com.android.dingtalk.share.ddsharemodule.IDDShareApi;
import com.android.dingtalk.share.ddsharemodule.ShareConstant;
import com.android.dingtalk.share.ddsharemodule.message.BaseReq;
import com.android.dingtalk.share.ddsharemodule.message.BaseResp;
import com.android.dingtalk.share.ddsharemodule.message.SendAuth;
import com.sundary.cordova.dd.Dingding;

import org.apache.cordova.CallbackContext;
import android.widget.Toast;

/**
 * @author scofieldwenwen
 */
public class DDShareActivity extends Activity implements IDDAPIEventHandler {
    public static final String TAG = "DDShareActivity";

    private IDDShareApi mIDDShareApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate==========>");
        try {
            //activity的export为true，try起来，防止第三方拒绝服务攻击
            String appId = Dingding.getSavedAppId(this);
            Log.d(TAG, "appId = " + appId);
            mIDDShareApi = DDShareApiFactory.createDDShareApi(this, appId, false);
            mIDDShareApi.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "e===========>" + e.toString());
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG, "onReq=============>");
    }

    @Override
    public void onResp(BaseResp baseResp) {
        CallbackContext ctx = Dingding.getCurrentCallbackContext();
        if (ctx == null) {
            startMainActivity();
            return;
        }
        Log.d(TAG, "CallbackId=" + ctx.getCallbackId());


        int errCode = baseResp.mErrCode;
        Log.d(TAG, "errorCode==========>" + errCode);
        String errMsg = baseResp.mErrStr;
        Log.d(TAG, "errMsg==========>" + errMsg);
        if (baseResp.getType() == ShareConstant.COMMAND_SENDAUTH_V2 && (baseResp instanceof SendAuth.Resp)) {
            SendAuth.Resp authResp = (SendAuth.Resp) baseResp;
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Log.d(TAG, "授权成功，授权码为:" + authResp.code);
                    showToast("授权成功，授权码为:" + authResp.code);
                    ctx.success(authResp.code);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Log.d(TAG, errCode + "授权取消");
                    showToast("授权取消");
                    ctx.error("授权取消");
                    break;
                default:
                    Log.e(TAG, errCode + "授权异常" + baseResp.mErrStr);
                    showToast("授权异常" + baseResp.mErrStr);
                    ctx.error("授权异常");
                    break;
            }
        } else {
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Log.d(TAG, "分享成功");
                    showToast("分享成功");
                    ctx.success("分享成功"+errMsg);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Log.d(TAG, errCode + "分享取消");
                    showToast("分享取消");
                    ctx.error("分享取消"+errMsg);
                    break;
                default:
                    Log.e(TAG, errCode + "分享失败" + baseResp.mErrStr);
                    showToast("分享失败" + baseResp.mErrStr);
                    ctx.error("分享失败"+errMsg);
                    break;
            }
        }

        finish();
    }

    protected void startMainActivity() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage(getApplicationContext().getPackageName());
        getApplicationContext().startActivity(intent);
    }

    private void showToast(String msg) {
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}