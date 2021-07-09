package com.app.ebook.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.fragment.app.Fragment;

import com.app.ebook.R;
import com.app.ebook.models.BoardListResponse;
import com.app.ebook.models.ClassListResponse;
import com.app.ebook.models.UserDetailsResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;


public class AppUtilities {
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    private static String uniqueID = null;
    private static AppUtilities comminInstance = null;
    private Context mContext;

    private AppUtilities() {
    }

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
    }

    public static AppUtilities getInstance(Context mContext) {
        if (comminInstance == null) {
            comminInstance = new AppUtilities();
        }
        comminInstance.mContext = mContext;
        return comminInstance;
    }

    /**
     * Get EditText value
     *
     * @param editText
     * @return
     */
    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * Get TextView value
     *
     * @param textView
     * @return
     */
    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * Validate Email
     *
     * @param target
     * @return
     */
    public final static boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidPhoneNo(CharSequence target) {
        return Patterns.PHONE.matcher(target).matches();
    }

    /**
     * Show toast message
     *
     * @param mContext
     * @param message
     */
    public static void showToast(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBar(View parentLayout, String message) {
        Snackbar snackbar = Snackbar.make(parentLayout, message, BaseTransientBottomBar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.parseColor("#C3AAE7"));
        textView.setTextSize(15);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER);
        }
        snackbar.show();
    }

    public static ListPopupWindow showAnchoredPopup(Context context, ArrayList popupList, int popupLayout, int popUpView, View anchorView) {
        ListPopupWindow popupWindow = new ListPopupWindow(context);
        ArrayAdapter adapter = new ArrayAdapter<>(context, popupLayout, popUpView, popupList);
        popupWindow.setAnchorView(anchorView); //this let as set the popup below the EditText
        popupWindow.setAdapter(adapter);
        return popupWindow;
    }

    /**
     * Is Fragment Visible
     *
     * @return
     */
    public static boolean isSafe(Fragment f) {
        return !(f.isRemoving() || f.getActivity() == null || f.isDetached() || !f.isAdded() || f.getView() == null);
    }

    public static void hideKeyBoard(View rootLayout, Context context) {
        if (rootLayout != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rootLayout.getWindowToken(), 0);
        }
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    /**
     * Check Online Status
     *
     * @return
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Lock Screen while network calling
     *
     * @param shouldLock
     */
    public void lockView(Boolean shouldLock) {
        if (shouldLock)
            try {
                ((Activity) mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        else
            try {
                ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * Load image in normal shape with center fit
     *
     * @param mContext
     * @param imageView
     * @param source
     */
    public static void loadImage(Context mContext, ImageView imageView, String source) {
        Glide.with(mContext)
                .load(source)
                .placeholder(R.drawable.download)
                .error(R.drawable.download)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static String convertDateFormat_String(String value, String oldFormat, String newFormat) {
        try {
            if (TextUtils.isEmpty(value) || value.equalsIgnoreCase("Invalid date")) {
                return "";
            }
            String formatedDate = "";
            SimpleDateFormat dateFormat;
            dateFormat = new SimpleDateFormat(oldFormat);
            dateFormat.setTimeZone(TimeZone.getDefault());
            Date myDate = null;
            try {
                myDate = dateFormat.parse(value);
            } catch (ParseException e) {
                Log.e("DateException", "convertDateFormat_String: " + e.getMessage());
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat(newFormat);
            timeFormat.setTimeZone(TimeZone.getDefault());
            if (myDate != null) {
                formatedDate = timeFormat.format(myDate);
            }
            return formatedDate;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getName(Context mContext, String name, int position) {
        String firstName = "", lastName = "";
        if (name.split("\\w+").length > 1) {
            lastName = name.substring(name.lastIndexOf(" ") + 1);
            firstName = name.substring(0, name.lastIndexOf(' '));
        } else {
            firstName = name;
        }

        return position == 0 ? firstName : lastName;
    }

    public static List<BoardListResponse> getBoardList(Context mContext) {
        List<BoardListResponse> boardListResponseList = new ArrayList<>();
        String apiResponse = new SessionManager(mContext).getSession(Constants.BOARD_LIST);
        if (!apiResponse.equals("")) {
            Gson mGson = new GsonBuilder().create();
            boardListResponseList = mGson.fromJson(apiResponse,
                    new TypeToken<List<BoardListResponse>>() {
                    }.getType());
        }
        return boardListResponseList;
    }

    public static List<ClassListResponse> getClassList(Context mContext) {
        List<ClassListResponse> classListResponseList = new ArrayList<>();
        String apiResponse = new SessionManager(mContext).getSession(Constants.CLASS_LIST);
        if (!apiResponse.equals("")) {
            Gson mGson = new GsonBuilder().create();
            classListResponseList = mGson.fromJson(apiResponse,
                    new TypeToken<List<ClassListResponse>>() {
                    }.getType());
        }
        return classListResponseList;
    }

    public static String getClassName(Context mContext, String id) {
        List<ClassListResponse> classListResponseList = getClassList(mContext);
        String className = "";
        for (ClassListResponse classListResponse : classListResponseList) {
            if (classListResponse.classId.equals(id)) {
                className = classListResponse.className;
                break;
            }
        }
        return className;
    }

    public static UserDetailsResponse getUser(Context mContext) {
        UserDetailsResponse userDetailsResponse = null;
        String apiResponse = new SessionManager(mContext).getSession(Constants.CURRENT_USER);
        if (!apiResponse.equals("")) {
            Gson mGson = new GsonBuilder().create();
            userDetailsResponse = mGson.fromJson(apiResponse, UserDetailsResponse.class);
        }
        return userDetailsResponse;
    }

}
