package com.example.karmali.homexperts;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;
import android.app.ProgressDialog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppraisalActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, AppBarLayout.OnOffsetChangedListener {


    private String UrlDefaultAppraise = "https://intservices.iazi.ch/api/apps/v1/defaultOfferedRentAppraisal";
    private String UrlAppraise ="https://intservices.iazi.ch/api/apps/v1/OfferedRentAppraisal";
    String deviceId;
    OkHttpClient okHttpClient;
    MediaType JSON;
    private Request request;
    private Handler mHandler;
    TextView txtAppraisePrice, txtAddress;
    TextView livserfacevalue, textViewRoomsVal;
    SeekBar  seekBarLivSurf, seekBarRooms,roomSeekBar,surfaceSeekBar;
    Spinner yearspin,objectTypeSpinner;
    CheckBox lift,liftCheckBox;
    Button buttonA3, buttonA2;
    Button estimate;
    ArrayAdapter<String> adapter, adapterObjectType;
    private ImageView imageViewCapturedImage;

    Bitmap imageBitmap;
    private String requestZip,requestTown, requestStreet, requestCategory="6",requestlift="0",requestaddres,requestqualityMicro,requestortId;

    String userAddress;
    LatLng userAddressLatLng;

    private static final int REQUEST_CHECK_SETTINGS = 111;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiCLient;
    private static final int LOCATION_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    private GoogleMap mGoogleMap;
    private ProgressBar progressBarAppraisal;
    private ProgressBar progressBarAppraisalHeader;
    ////////////////////////
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.7f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private RelativeLayout mTitleContainer;
    private TextView mTitle, textViewAppraiseValueThumb,textViewsurfaceValueThumb,textViewroomsValueThumb,maintextviewsurfacetitle,maintextviewroomstitle,meter_squarethumb;
    private  ImageView mImage;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private Drawable BARS_DEFAULT_SURFACE, BARS_DEFAULT_ROOMS;
/////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraisal);
//////////////
        ///////////Action Bar//////////////
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.customactionbar, null);

        ImageView backButton = (ImageView) mCustomView.findViewById(R.id.backbutton);
        ImageButton imageButton = (ImageButton) mCustomView.findViewById(R.id.action_settings);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppraisalActivity.this, UserLocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AppraisalActivity.this, SettingsActivity.class));
            }
        });

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        ///////////Action Bar End//////////////

        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        startAlphaAnimation(mImage, 0, View.INVISIBLE);
        startAlphaAnimation(textViewAppraiseValueThumb,0,View.INVISIBLE);
        startAlphaAnimation(textViewroomsValueThumb,0,View.INVISIBLE);
        startAlphaAnimation(textViewsurfaceValueThumb,0,View.INVISIBLE);
        startAlphaAnimation(maintextviewsurfacetitle,0,View.INVISIBLE);
        startAlphaAnimation(maintextviewroomstitle,0,View.INVISIBLE);
        startAlphaAnimation(meter_squarethumb,0,View.INVISIBLE);
        ///////////
        txtAddress = findViewById(R.id.addressText);
        progressBarAppraisal = findViewById(R.id.progressBarAppraisal);
        progressBarAppraisal.setVisibility(View.INVISIBLE);
        progressBarAppraisalHeader = findViewById(R.id.progressBarAppraisalHeader);
        progressBarAppraisalHeader.setVisibility(View.INVISIBLE);

        Bundle bun = getIntent().getExtras();
        final String savedImageUrl = bun.getString("PhotoUrl");
        final String data = bun.getString("defValues");

        yearspin = (Spinner) findViewById(R.id.yearspin);
        objectTypeSpinner = (Spinner) findViewById(R.id.objectTypeSpinner);
        livserfacevalue = (TextView) findViewById(R.id.textViewLivSurfVal);
        seekBarLivSurf= (SeekBar) findViewById(R.id.seekBarLivSurf);
        textViewRoomsVal = (TextView) findViewById(R.id.textViewRoomsVal);
        seekBarRooms = (SeekBar) findViewById(R.id.seekBarRooms);


        lift = (CheckBox) findViewById(R.id.lift);
        txtAppraisePrice = (TextView) findViewById(R.id.textViewAppraiseValue);
        textViewAppraiseValueThumb = (TextView) findViewById(R.id.textViewAppraiseValueThumb);

        textViewsurfaceValueThumb = (TextView) findViewById(R.id.textViewsurfaceValueThumb);

        textViewroomsValueThumb = (TextView) findViewById(R.id.textViewroomsValueThumb);
        getAddress();

        //  ImageView img = (ImageView) findViewById(R.id.gotoloacationactivity);

        ArrayList<String> years = new ArrayList<String>();

        //Spinner For Build Year
        int thisYear = Calendar.getInstance().get(Calendar.YEAR) + 2;
        for (int i = 1900; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        Spinner spinYear = (Spinner)findViewById(R.id.yearspin);
        spinYear.setAdapter(adapter);

        adapterObjectType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.android_dropdown_objectType));


        /* Bitmap bitmap = (Bitmap) this.getIntent().getParcelableExtra("BitmapImage");
        ImageView imageview = (ImageView) findViewById(R.id.capturedImageView);
        imageview.setImageBitmap(bitmap);*/


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        buttonA2 = (Button) findViewById(R.id.buttonA2);
        buttonA3 = (Button) findViewById(R.id.buttonA3);
        buttonA2.setTransformationMethod(null);
        buttonA3.setTransformationMethod(null);
        buttonA2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonA2.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                buttonA3.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borderbottom, null));
                buttonA2.setTextColor(Color.WHITE);
                buttonA3.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                requestCategory = "5";

            }
        });
        buttonA3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonA3.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                buttonA2.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.borderbottom, null));
                buttonA3.setTextColor(Color.WHITE);
                buttonA2.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
                requestCategory = "6";
            }
        });
        estimate = (Button) findViewById(R.id.buttonAppraise);
        estimate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Toast.makeText(getBaseContext(), "Calculating...", Toast.LENGTH_LONG).show();
                AppraisalService();
            }

        });

        liftCheckBox =(CheckBox) findViewById(R.id.lift);
        lift.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                    requestlift = "1";
                else
                    requestlift = "0";
            }
        });

        roomSeekBar = (SeekBar) findViewById(R.id.seekBarRooms);
        BARS_DEFAULT_ROOMS = ContextCompat.getDrawable(AppraisalActivity.this.getApplicationContext(), R.drawable.round_button);
        roomSeekBar.setThumb(BARS_DEFAULT_ROOMS);
        roomSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int roomNo, boolean fromUser) {
                double rooms = ((double) roomNo)/2;
                textViewRoomsVal.setText(String.valueOf(rooms));
                textViewroomsValueThumb.setText(String.valueOf(rooms));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        surfaceSeekBar = (SeekBar) findViewById(R.id.seekBarLivSurf);
        BARS_DEFAULT_SURFACE = ContextCompat.getDrawable(AppraisalActivity.this.getApplicationContext(), R.drawable.round_button);
        surfaceSeekBar.setThumb(BARS_DEFAULT_SURFACE);
        surfaceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                livserfacevalue.setText(String.valueOf(progress));
                textViewsurfaceValueThumb.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        imageViewCapturedImage = findViewById(R.id.capturedImageView);
        imageViewCapturedImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fillImageView(savedImageUrl);
                imageViewCapturedImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        //fillImageView(savedImageUrl);
        //defaultAppraisal(savedImageUrl);
        SetDefaultValues(data);

        MapFragment mapFragment=(MapFragment)getFragmentManager().findFragmentById(R.id.mapViewCurrentLocation);
        mapFragment.getMapAsync(this);
    }

    private void getAddress(){
        final AddressGlobal addressGlobal = (AddressGlobal)getApplicationContext();
        userAddress=addressGlobal.getAddress();
        userAddressLatLng=addressGlobal.getAddresslatLng();
    }

    public void goToLocation() {
        Intent intent = new Intent(AppraisalActivity.this,UserLocationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    //To display image
    private void fillImageView(String savedImageUrl) {
        try {
            Uri tempUri = FileProvider.getUriForFile(this, "com.example.karmali.homexperts.fileprovider", new File(savedImageUrl));

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            Glide.with(this).load(savedImageUrl).apply(requestOptions).into(imageViewCapturedImage);
            ImageView capturedImageThumb  = (ImageView) findViewById(R.id.capturedImageThumb);
            Glide.with(this).load(savedImageUrl).apply(requestOptions).into(capturedImageThumb);

            /*
            imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tempUri);
            ImageView capturedImageThumb  = (ImageView) findViewById(R.id.capturedImageThumb);
            imageViewCapturedImage.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, imageViewCapturedImage.getWidth(), imageViewCapturedImage.getHeight(), false));
            capturedImageThumb.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 150, 120, false));
            */
           // defaultAppraisal(savedImageUrl);
        }
        catch (Exception ex) {
            Toast.makeText(this, "Error: "+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void SetDefaultValues(String defaultData)
    {
        try
        {
            final JSONObject json = new JSONObject(defaultData);
            final String category = json.getString("categoryCode");

            final String appvalue = json.getString("appraisalValue");
            final String surface = json.getString("surfaceContract");
            final String liftValue = json.getString("lift");
            final String year = json.getString("buildYear");
            final String roomNo = json.getString("roomNb");
            final String object = json.getString("objectType");
            requestZip = json.getString("zip");
            requestTown = json.getString("town");
            requestStreet = json.getString("street");
            requestaddres = requestStreet + ", " + requestZip + " " + requestTown;
            requestqualityMicro = json.getString("qualityMicro");
            requestortId = json.getString("ortId");


            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator('\'');
            DecimalFormat decimalFormat = new DecimalFormat("#,### CHF", symbols);
            final  String localeFormattedNumber = decimalFormat.format(Integer.parseInt(appvalue));

            runOnUiThread(new Runnable() {

                @Override
                public void run() {


                    txtAddress.setText(requestaddres);

                    //Spinner For Object Type
                    adapterObjectType = new ArrayAdapter<String>(AppraisalActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.android_dropdown_objectType));

                    objectTypeSpinner.setAdapter(adapterObjectType);
                    if (!object.equals(null)) {
                        int spinnerPosition = adapterObjectType.getPosition(object);
                        objectTypeSpinner.setSelection(spinnerPosition);
                    }

                    livserfacevalue.setText(surface);
                    seekBarLivSurf.setProgress(Integer.parseInt(surface));


                    int roomInt = (int) (Float.parseFloat(roomNo) * 2);
                    seekBarRooms.setProgress(roomInt);

                    if (!year.equals(null)) {
                        int spinnerPosition = adapter.getPosition(year);
                        yearspin.setSelection(spinnerPosition);
                    }

                    if (liftValue.trim().equals("1"))
                        lift.setChecked(true);
                    else
                        lift.setChecked(false);

                    if (category.trim().equals("5")) {
                        buttonA2.performClick();
                    } else {
                        buttonA3.performClick();
                    }


                    txtAppraisePrice.setText(localeFormattedNumber);
                    textViewAppraiseValueThumb.setText(localeFormattedNumber);

                    progressBarAppraisal.setVisibility(View.INVISIBLE);
                    progressBarAppraisalHeader.setVisibility(View.INVISIBLE);
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

    }
            private void AppraisalService() {
                try {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(this, "No network", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressBarAppraisal.setVisibility(View.VISIBLE);
                    if (mIsTheTitleVisible) progressBarAppraisalHeader.setVisibility(View.VISIBLE);

                    TextView roomNb = (TextView) findViewById(R.id.textViewRoomsVal);
                    TextView surfaceLiving = (TextView) findViewById(R.id.textViewLivSurfVal);
                    deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                    ;// "123XGH67";
                    Spinner spinner = (Spinner) findViewById(R.id.yearspin);
                    String buildYear = spinner.getSelectedItem().toString();


                    Spinner objectspinner = (Spinner) findViewById(R.id.objectTypeSpinner);
                    String objectType = objectspinner.getSelectedItem().toString();

                    //Spinner For Object Type
                    adapterObjectType = new ArrayAdapter<String>(AppraisalActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.android_dropdown_objectType));
                    objectTypeSpinner.setAdapter(adapterObjectType);

                    int spinnerPosition = adapterObjectType.getPosition(objectType);
                    objectTypeSpinner.setSelection(spinnerPosition);


                    String values[] = getResources().getStringArray(R.array.android_dropdown_objectValue);

                    String value = values[spinnerPosition];

                    RequestBody appraiseData = new FormBody.Builder()
                            .add("ortId", requestortId)
                            .add("categoryCode", requestCategory)
                            .add("objectTypeCode", value)
                            .add("qualityMicro", requestqualityMicro)
                            .add("surfaceContract", surfaceLiving.getText().toString())
                            .add("buildYear", buildYear)
                            .add("roomNb", roomNb.getText().toString())
                            .add("lift", requestlift)
                            .add("deviceId", deviceId.toString())
                            .add("address.address", requestaddres)
                            .add("address.lat", String.valueOf(userAddressLatLng.latitude))
                            .add("address.lng", String.valueOf(userAddressLatLng.longitude))
                            .add("address.street", requestStreet)
                            .add("address.zip", requestZip)
                            .add("address.town", requestTown)
                            .add("address.country", "Switzerland")
                            .build();

                    okHttpClient = new OkHttpClient();
                    request = new Request.Builder().url(UrlAppraise).build();
                    request = new Request.Builder().url(UrlAppraise)
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .post(appraiseData)
                            .build();
                    try {

                        okHttpClient.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                progressBarAppraisal.setVisibility(View.INVISIBLE);
                                progressBarAppraisalHeader.setVisibility(View.INVISIBLE);
                                Log.i("IN", e.getMessage());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    final String myResponse = response.body().string();

                                    final JSONObject json = new JSONObject(myResponse);
                                    final String appvalue = json.getString("appraisalValue");
                                    final String surface = json.getString("surfaceContract");
                                    final String liftValue = json.getString("lift");
                                    final String category = json.getString("categoryCode");
                                    final String object = json.getString("objectType");
                                    final String year = json.getString("buildYear");
                                    final String roomNo = json.getString("roomNb");
                                    requestZip = json.getString("zip");
                                    requestTown = json.getString("town");
                                    requestStreet = json.getString("street");
                                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                                    symbols.setGroupingSeparator('\'');
                                    DecimalFormat decimalFormat = new DecimalFormat("#,### CHF", symbols);
                                    final String localeFormattedNumber = decimalFormat.format(Integer.parseInt(appvalue));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            txtAddress.setText(requestStreet + ", " + requestZip + " " + requestTown);

                                            livserfacevalue.setText(surface);
                                            seekBarLivSurf.setProgress(Integer.parseInt(surface));
                                            textViewRoomsVal.setText(roomNo);

                                            int roomInt = (int) (Float.parseFloat(roomNo) * 2);
                                            seekBarRooms.setProgress(roomInt);

                                            if (!object.equals(null)) {
                                                int spinnerPosition = adapter.getPosition(object);
                                                objectTypeSpinner.setSelection(spinnerPosition);
                                            }


                                            if (!year.equals(null)) {
                                                int spinnerPosition = adapter.getPosition(year);
                                                yearspin.setSelection(spinnerPosition);
                                            }
                                   /* if (liftValue.trim().equals("1"))
                                        lift.setChecked(true);
                                    else
                                        lift.setChecked(false);*/

                                            if (category.trim().equals("5"))
                                                buttonA2.performClick();
                                            else
                                                buttonA3.performClick();

                                            txtAppraisePrice.setText(localeFormattedNumber);
                                            textViewAppraiseValueThumb.setText(localeFormattedNumber);

                                            progressBarAppraisal.setVisibility(View.INVISIBLE);
                                            progressBarAppraisalHeader.setVisibility(View.INVISIBLE);
                                        }
                                    });

                                } catch (Exception e) {
                                    Log.i("error", e.getMessage());
                                }
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } catch (Exception outerEx) {
                    Toast.makeText(this, "Error:" + outerEx.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            private boolean isNetworkAvailable() {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }

            @Override
            protected void onPostResume() {
                super.onPostResume();
                if (sentToSettings) {
                    if (ActivityCompat.checkSelfPermission(AppraisalActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //Got Permission
                        //Toast.makeText(getBaseContext(), "Returned from settings. Permission granted", Toast.LENGTH_LONG).show();
                    }
                }
            }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap=googleMap;
        setCurrentLocationOnMap();
    }

    private Marker mCurrLocationMarker;
    private void setCurrentLocationOnMap() {
        if (mGoogleMap!=null&&userAddressLatLng != null) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userAddressLatLng, 14);
            if (mCurrLocationMarker != null)
                mCurrLocationMarker.remove();
            MarkerOptions markerOptions=new MarkerOptions().position(userAddressLatLng).title("You're here");
            mCurrLocationMarker=mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }

    private void bindActivity() {
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_pricetitle);
        mTitleContainer = (RelativeLayout) findViewById(R.id.main_linearlayout_title);
        mImage  = (ImageView) findViewById(R.id.capturedImageThumb);
        textViewAppraiseValueThumb = (TextView) findViewById(R.id.textViewAppraiseValueThumb);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        textViewroomsValueThumb = (TextView) findViewById(R.id.textViewroomsValueThumb);
        textViewsurfaceValueThumb = (TextView) findViewById(R.id.textViewsurfaceValueThumb);
        maintextviewroomstitle = (TextView) findViewById(R.id.maintextviewroomstitle);
        maintextviewsurfacetitle = (TextView) findViewById(R.id.maintextviewsurfacetitle);
        meter_squarethumb = (TextView) findViewById(R.id.meter_squarethumb);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {

            int maxScroll = appBarLayout.getTotalScrollRange();
            float percentage = (float) Math.abs(offset) / (float) maxScroll;

            handleAlphaOnTitle(percentage);
            handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(mImage, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(textViewAppraiseValueThumb, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(textViewroomsValueThumb, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(textViewsurfaceValueThumb, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(maintextviewroomstitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(maintextviewsurfacetitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(meter_squarethumb, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mToolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.toolbarcolor, null));
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(mImage, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(textViewAppraiseValueThumb, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(textViewroomsValueThumb, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(textViewsurfaceValueThumb, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(maintextviewroomstitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(maintextviewsurfacetitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(meter_squarethumb, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mToolbar.setBackgroundColor(0x00000000);
                progressBarAppraisalHeader.setVisibility(View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
            //    startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);

                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
              //  startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);

                mIsTheTitleContainerVisible = true;
            }
        }
    }
    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

}