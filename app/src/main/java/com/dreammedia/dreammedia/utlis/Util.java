package com.dreammedia.dreammedia.utlis;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Window;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    //____________
    public static String getPath(Uri uri ,Context context) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULL POINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String tPath = cursor.getString(column_index);
            cursor.close();
            return tPath;
        } else
            return null;
    }

    public static boolean isEmpty(Editable editText) {
        return TextUtils.isEmpty(editText);
    }

    public static boolean validateEmail(Editable email) {
        if (TextUtils.isEmpty(email)) {
            return true;
        } else return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
     /*
    public static boolean checkTokenValid(Context context, String message)  {
        return message.equals(context.getString(R.string.token_not_valid));
    }
*/
    /*Get Device Id*/
    public static String getDeviceId(Activity activity) {
        return Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /*Check Internet is Available*/
    public static boolean hasInternet(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    /*Get Local Ip*/
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces =
                    Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs =
                        Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                if (delim < 0) {
                                    return sAddr.toUpperCase();
                                } else {
                                    sAddr.substring(0, delim).toUpperCase();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    public static boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }


    public static String generateUUIDForRequest() {
        return UUID.randomUUID().toString();
    }

    public static boolean checkNullStatus(String fieldName) {
        return fieldName != null;
    }

    public static void changeStatusBarColor(Activity activity, final int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(color);
        }
    }
    
/*    public static void showMessageDialog(Context context, String message, Boolean isNotCancelable) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_password_change);
        // TextView titleTv  = dialog.findViewById(R.id.tv_otp_sent_label);
        // titleTv.setText(title);
        TextView messageTv = dialog.findViewById(R.id.tv_message);
        messageTv.setText(message);
        if (isNotCancelable) {
            dialog.setCancelable(false);
        }
        dialog.findViewById(R.id.btn_ok).setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }*/

    public static String getUTCDateTimeAsString() {
        return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT, Locale.ENGLISH).format(new Date());
    }


    public static int distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("Km")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }

            int b = (int) Math.round(dist);
            return (b);
        }
    }


    // GMT time formate  get gmt time and convert currenttime set hours and minute ago use posts-----------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static String DateTime(String createdAt) {

        String mTime = null;

        try {

            Date dt1 = null;
            SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");

            Calendar newcal = Calendar.getInstance();
            TimeZone newtz = newcal.getTimeZone();

            SimpleDateFormat newtimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            newtimeFormat.setTimeZone(newtz);

            SimpleDateFormat ndf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            String newformattedDate = ndf.format(newcal.getTime());

            //
            //convert to gmt
            SimpleDateFormat dateFormat2 =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            dateFormat2.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date date =  dateFormat2.parse(createdAt) ;
            Log.e("currnetDAteTime", "String To Date Conversion " +date +"");

            //

            Date d1 = null;
            Date d2 = null;

            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            d1  = format.parse(newformattedDate);
            d2  = dateFormat2.parse(createdAt);

            //   dt1 = sdfs.parse(String.valueOf(d2));

            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");

            long diff = d1.getTime() - d2.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours   = diff / (60 * 60 * 1000) % 24;
            long diffDays    = diff / (24 * 60 * 60 * 1000);

            Log.e("newtimeFormat", "onBindViewHolder: " + diffDays +
                    "\n" + diffHours +
                    "\n" + diffMinutes
            );

            if (diffDays == 0){

                if (diffHours >= 1){

                    mTime = diffHours+" hrs "+diffMinutes+" mins ago" ;

                }else{

                    if (diffMinutes <=3){
                        mTime = "just now" ;
                    }else{
                        mTime = diffMinutes+" mins ago" ;
                    }
                }

            } else if (diffDays == 1){

                //   mTime = diffMinutes+"Yesterday" + " at "+ sdfs.format(d2) ;
                mTime = "Yesterday" + " at "+ sdfs.format(d2) ;

            }else{

                SimpleDateFormat monthaNameFormate = new SimpleDateFormat("dd MMM yyyy");
                //  mTime = monthaNameFormate.format(d2) + " at "+ sdfs.format(d2) ;
                mTime = monthaNameFormate.format(d2) + " at "+ sdfs.format(d2) ;
            }

            Log.e("mmmmmminitLayoutIMAGE", "initLayoutIMAGE: "+mTime );

        }catch (Exception e){ Log.e("newtimeFormat", "initLayoutIMAGE: " + e.getMessage() ); }

        return mTime ;
    }

    //END-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //

    // check permissions-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //
    public static boolean hasReadPermissions(Context context) { return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED); }

    public static boolean hasWritePermissions(Context context) { return (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED); }

    public static boolean hasCameraPermissions(Context context) { return (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED); }

    public static boolean hasFineLocationPermissions(Context context) { return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED); }

    public static boolean hasCorePermissions(Context context) { return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED); }

    //END-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //get file orignal path-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

  /*  public static String getPath(Uri uri , Context context) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }*/

    // open image Chooser intent when choose photo use in dashboard fragment---------------------------------------------------------------------------------------------------------------------
    public static Intent getPickImageChooserIntent(Context context) {

        Uri outputFileUri = getCaptureImageOutputUri(context);
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        /*Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }*/

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select Image source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //when select imag eget orignal image path use in dashboard fragment---------------------------------------------------------------------------------------------------------------

    public static String getImageFromFilePath(Intent data , Context context) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri(context).getPath();
        else return getPathFromURI(data.getData() , context);

    }

    public static Uri getCaptureImageOutputUri(Context context) {
        Uri outputFileUri = null;
        File getImage = context.getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    public static String getPathFromURI(Uri contentUri , Context context) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String getImageFilePath(Intent data , Context context) {
        return getImageFromFilePath(data,context);
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------

    //set location custome icone----------------------------------------------------------------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------------------------------------------------------------


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        OutputStream out;
        File file = new File(getFilename(context));

        try {
            if (file.createNewFile()) {
                InputStream iStream = context != null ? context.getContentResolver().openInputStream(contentUri) : context.getContentResolver().openInputStream(contentUri);
                byte[] inputData = getBytes(iStream);
                out = new FileOutputStream(file);
                out.write(inputData);
                out.close();
                return file.getAbsolutePath();
            }
        } catch (IOException e) { e.printStackTrace();
        }
        return null;
    }

    private static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private static String getFilename(Context context) {
        File mediaStorageDir = new File(context.getExternalFilesDir(""), "patient_data");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        String mImageName = "IMG_" + String.valueOf(System.currentTimeMillis()) + ".png";
        return mediaStorageDir.getAbsolutePath() + "/" + mImageName;

    }

}

