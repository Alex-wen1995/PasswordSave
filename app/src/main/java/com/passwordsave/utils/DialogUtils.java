package com.passwordsave.utils;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.passwordsave.R;

/**
 * 对话框工具箱
 */

public class DialogUtils {
    /**
     * 显示一个对话框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param title 标题
     * @param message 消息
     * @param confirmButton 确认按钮
     * @param confirmButtonClickListener 确认按钮点击监听器
     * @param centerButton 中间按钮
     * @param centerButtonClickListener 中间按钮点击监听器
     * @param cancelButton 取消按钮
     * @param cancelButtonClickListener 取消按钮点击监听器
     * @param onShowListener 显示监听器
     * @param cancelable 是否允许通过点击返回按钮或者点击对话框之外的位置关闭对话框
     * @param onCancelListener 取消监听器
     * @param onDismissListener 销毁监听器
     * @return 对话框
     */
    public static AlertDialog showAlert(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener, String centerButton, DialogInterface.OnClickListener centerButtonClickListener, String cancelButton, DialogInterface.OnClickListener cancelButtonClickListener, DialogInterface.OnShowListener onShowListener, boolean cancelable, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog.Builder promptBuilder = new AlertDialog.Builder(context);
        if (title != null) {
            promptBuilder.setTitle(title);
        }
        if (message != null) {
            promptBuilder.setMessage(message);
        }
        if (confirmButton != null) {
            promptBuilder.setPositiveButton(confirmButton,
                    confirmButtonClickListener);
        }
        if (centerButton != null) {
            promptBuilder.setNeutralButton(centerButton,
                    centerButtonClickListener);
        }
        if (cancelButton != null) {
            promptBuilder.setNegativeButton(cancelButton,
                    cancelButtonClickListener);
        }
        promptBuilder.setCancelable(cancelable);
        if (onCancelListener!= null) {
            promptBuilder.setOnCancelListener(onCancelListener);
        }

        AlertDialog alertDialog = promptBuilder.create();
        if (!(context instanceof Activity)) {
            alertDialog.getWindow()
                       .setType(WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG);
        }
        alertDialog.setOnDismissListener(onDismissListener);
        alertDialog.setOnShowListener(onShowListener);

        alertDialog.show();
        return alertDialog;
    }


    /**
     * 显示一个对话框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param title 标题
     * @param message 消息
     * @param confirmButton 确认按钮
     * @param confirmButtonClickListener 确认按钮点击监听器
     * @param cancelButton 取消按钮
     * @param cancelButtonClickListener 取消按钮点击监听器
     * @return 对话框
     */
    public static AlertDialog showAlert(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener, String cancelButton, DialogInterface.OnClickListener cancelButtonClickListener) {
        return showAlert(context, title, message, confirmButton,
                confirmButtonClickListener, null, null, cancelButton,
                cancelButtonClickListener, null, true, null, null);
    }


    /**
     * 显示一个提示框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message 提示的消息
     * @param confirmButton 确定按钮的名字
     */
    public static AlertDialog showPrompt(Context context, String message, String confirmButton) {
        return showAlert(context, null, message, confirmButton, null, null,
                null, null, null, null, true, null, null);
    }
    /**
     * 显示一个提示框带点击逻辑处理
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message 提示的消息
     * @param confirmButton 确定按钮的名字
     */
    public static AlertDialog showPrompt(Context context, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener) {
        return showAlert(context, null, message, confirmButton, confirmButtonClickListener, null,
                null, null, null, null, true, null, null);
    }

    public static AlertDialog showPrompt(Context context, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener, boolean iscancel) {
        return showAlert(context, null, message, confirmButton, confirmButtonClickListener, null,
                null, null, null, null, iscancel, null, null);
    }


    /**
     * 显示一个提示框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message 提示的消息
     */
    public static AlertDialog showPrompt(Context context, String message) {
        return showAlert(context, null, message, "OK", null, null, null, null,
                null, null, true, null, null);
    }


    /** 加载数据对话框 */
    private Dialog mLoadingDialog;
    /**
     * 显示加载对话框
     * @param context 上下文
     * @param msg 对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public static Dialog showDialogForLoading(Context context, String msg, boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(R.layout.base_dialog_loading, null);
        TextView loadingText = (TextView)view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText(msg);

        Dialog mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        return  mLoadingDialog;
    }

    public static Dialog showDialogForContent(Context context, String title, String msg, boolean cancelable) {
        CardView cardView=new CardView(context);
        cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
       LinearLayout linearLayout=new LinearLayout(context);
       linearLayout.setOrientation(LinearLayout.VERTICAL);
       linearLayout.setMinimumHeight(DisplayManager.INSTANCE.dip2px(R.dimen.dp_60));
       linearLayout.setMinimumWidth(DisplayManager.INSTANCE.dip2px(R.dimen.dp_200));
        TextView tv_title = new TextView(context);
        TextView tv_content=new TextView(context);
        tv_title.setText(title);
        tv_content.setText(msg);
        linearLayout.addView(tv_title);
        linearLayout.addView(tv_content);
        cardView.addView(linearLayout);
        Dialog mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(cardView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        return  mLoadingDialog;
    }

    public static Dialog showDialogForLoading(Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.base_dialog_loading, null);
        TextView loadingText = (TextView)view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText("Loading...");

       Dialog mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        return  mLoadingDialog;
    }

    /**
     * 关闭加载对话框
     */
    public static void cancelDialogForLoading(Dialog loadingDialog, Context context) {
        if(loadingDialog != null) {
            loadingDialog.cancel();
        }
    }
}
