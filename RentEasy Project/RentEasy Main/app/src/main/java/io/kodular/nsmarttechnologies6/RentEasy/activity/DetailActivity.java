package io.kodular.nsmarttechnologies6.RentEasy.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.DetailsImages;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.DetailsRelated;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.DetailsImagesAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.DetailsRelatedAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    private AdView mAdView;
    private RewardedAd mRewardedAd;
    SharedPreferences sharedPreferences,sp2;
    private static final String SHARED_PREF_PAID = "myPaid";
    private static final String KEY_IS_PAID = "isPaid";
    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_PHONENO = "number";
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    List<DetailsRelated> list,temp,temp2;
    DetailsRelatedAdapter adapter;
    Activity mActivity;
    boolean isAdLoaded = false,isLiked = false,once=true,liked=true,paymentSucess=true,related=true,viewCount=true,isViewed=false,onceViewed=true,onceCLicked=true,onceRequest=true,sendRequest=false;
    String I1Url="",I2Url="No",I3Url="No",I4Url="No",I5Url="No",isPro,isCallorMessage="C",lat = "",lng = "",phNm = "",msg = "",pincode, listingId,userPhno,total,isAdsAvailable="f",currentLikedId=null;
    Dialog dialog;
    DatabaseReference getListingInfo,addReport,getRelatedListings,getLiked,getAds,setProUser,setTotalProUsers,viewedListings,getOwnerName;
    io.RentEasy.views.MediumTextView tvTitle,tvRate,tvBook;
    io.RentEasy.views.RegularTextView tvAddress,tvOwnerName,tvListingTitle,tvDescription,tvCategory,tvStatus2;
    ImageView ivLike,ivStatus2;
    TextView tvDate;
    int var = 0,varViewed=0;
    RelativeLayout verifiedLay;
    String Token = "cHTc0aPgSdacQCtBi7VNRr:APA91bH1ImfzYeRUKZ4r1RbmW7Zk1t8_ObxCQ1etT3FMqfkvE2Rs4n7PfVkdCjZtzRQJ8f71DlO0K2XTjYwtsHgAQjZ_ttBvLW2Ou2uP_T4XSUfodDjreFJ04-Zp3kebcCCbi-9mjAOM";

    List<DetailsImages> list2;
    SliderView sliderView;
    Animation scaleUp,scaleDown;
    String totalListingView = "0",totalCallCLicks="0",totalWhatsappClicks="0";

    io.RentEasy.views.RegularTextView th1,th2,th3,th4,th5,th6,th7,t1,t2,t3,t4,t5,t6,t7;
    RelativeLayout detailLay,CallLay;
    boolean isDatePicked=false;


    private ReviewManager reviewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dialog =new Dialog(this);
        openLoadingDialog();
        init();
        String uri2 = "@drawable/ic_close";

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        //loading animations
        scaleUp= AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown=AnimationUtils.loadAnimation(this,R.anim.scale_down);
        verifiedLay = findViewById(R.id.verified_lay);

        th1=findViewById(R.id.th_1);
        th2=findViewById(R.id.th_2);
        th3=findViewById(R.id.th_3);
        th4=findViewById(R.id.th_4);
        th5=findViewById(R.id.th_5);
        th6=findViewById(R.id.th_6);
        th7=findViewById(R.id.th_7);

        t1=findViewById(R.id.t_1);
        t2=findViewById(R.id.t_2);
        t3=findViewById(R.id.t_3);
        t4=findViewById(R.id.t_4);
        t5=findViewById(R.id.t_5);
        t6=findViewById(R.id.t_6);
        t7=findViewById(R.id.t_7);

        tvRate = findViewById(R.id.tv_rate);

        tvAddress = findViewById(R.id.tv_propertyAddress);
        tvOwnerName = findViewById(R.id.tv_owner_name);
        tvTitle = findViewById(R.id.PropertyTitle);
        ivStatus2 = findViewById(R.id.iv_status2);
        tvListingTitle = findViewById(R.id.tv_listing_title);
        tvDescription = findViewById(R.id.tv_description);
        tvDate = findViewById(R.id.tv_date);
        tvCategory = findViewById(R.id.tv_category);
        tvStatus2 = findViewById(R.id.tv_status2);
        detailLay=findViewById(R.id.detail_lay);

        Uri uri = getIntent().getData();
        if (uri!=null) {
            String raw = uri.toString();
            if (raw.contains("https://www.renteasyindia.com/ad/")) {
                String a = raw.replace("https://www.renteasyindia.com/ad/","");
                String[] split = a.split("/");
                pincode = split[0];
                listingId = split[1];
            }else {
                Intent intent = new Intent(DetailActivity.this,WebViewActivity.class);
                intent.putExtra("url",raw);
                startActivity(intent);
            }

        }else {
            listingId = getIntent().getStringExtra("listingId");
            pincode = getIntent().getStringExtra("pincode");
        }
        // getting listing data from database
        getListingInfo = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(listingId);
        getListingInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("is deleted")) {
                    String noImageUrl = "https://res.cloudinary.com/nsmarttechnologies/image/upload/v1642161658/no_image_pokwhn.png";
                    String cloudinary = "https://res.cloudinary.com/nsmarttechnologies/image/upload/";
                    if (dataSnapshot.hasChild("0")) {
                        I1Url = dataSnapshot.child("0").getValue().toString();
                    }else {I1Url="No";}
                    if (dataSnapshot.hasChild("1")) {
                        I2Url = dataSnapshot.child("1").getValue().toString();
                    }else {I2Url="No";}
                    if (dataSnapshot.hasChild("2")) {
                        I3Url = dataSnapshot.child("2").getValue().toString();
                    }else{I3Url="No";}
                    if (dataSnapshot.hasChild("3")) {
                        I4Url = dataSnapshot.child("3").getValue().toString();
                    }else {I4Url="No";}
                    if (dataSnapshot.hasChild("4")) {
                        I5Url = dataSnapshot.child("4").getValue().toString();
                    }else {I5Url="No";}

                    list2 = new ArrayList<>();
                    list2.add(new DetailsImages(I1Url));
                    list2.add(new DetailsImages(I2Url));
                    list2.add(new DetailsImages(I3Url));
                    list2.add(new DetailsImages(I4Url));
                    list2.add(new DetailsImages(I5Url));
                    setImages();

                    String amountRaw = dataSnapshot.child("5").getValue().toString();
                    String[] split = amountRaw.split(",");
                    String amount = split[0];
                    String cycle = split[1];
                    if (cycle.equals("m")) { tvRate.setText("₹"  + amount + " per Month");
                    }else if (cycle.equals("d")) {tvRate.setText("₹"  + amount + " per Day");
                    }else if (cycle.equals("h")) {tvRate.setText("₹"  + amount + " per Hour");}

                    String title = dataSnapshot.child("6").getValue().toString();
                    tvListingTitle.setText(title);

                    String description = dataSnapshot.child("7").getValue().toString();
                    tvDescription.setText(description);

                    String date = dataSnapshot.child("8").getValue().toString();
                    tvDate.setText("posted on "+date);

                    String status = dataSnapshot.child("9").getValue().toString(); // not completed
                    int imageResource2 = getResources().getIdentifier(uri2, null, getPackageName());
                    Drawable res2 = getResources().getDrawable(imageResource2);
                    if ( status.equals("N") ){
                        ivStatus2.setBackground(res2);
                        ivStatus2.setBackgroundTintList(getResources().getColorStateList(R.color.red));;
                        tvStatus2.setText("Temporarily not-available for rent");
                    }

                    String Category = dataSnapshot.child("10").getValue().toString();
                    if (Category.equals("1")) {
                        //category 1 houses
                        tvCategory.setText("House");
                        String s1 = dataSnapshot.child("23").child("1").getValue().toString();
                        if (s1.equals("1")) {
                            t1.setText("Individual");
                        }
                        String s2 = dataSnapshot.child("23").child("2").getValue().toString();
                        if (s2.equals("1")) {
                            t2.setText("1BHK");
                        }else if (s2.equals("2")) {
                            t2.setText("2BHK");
                        }else if (s2.equals("3")) {
                            t2.setText("3BHK");
                        }
                        String s3 = dataSnapshot.child("23").child("3").getValue().toString();
                        if (s3.equals("1")) {
                            t3.setText("2-wheeler");
                        }else if (s3.equals("2")) {
                            t3.setText("4-wheeler");
                        }
                        String s4 = dataSnapshot.child("23").child("4").getValue().toString();
                        if (s4.equals("0")) {
                            t4.setText("Furnished");
                        }else if (s4.equals("1")) {
                            t4.setText("Semi-Furnished");
                        }
                        String s5 = dataSnapshot.child("23").child("5").getValue().toString();
                        if (s5.equals("1")) {
                            t5.setText("North");
                        }else if (s5.equals("2")) {
                            t5.setText("South");
                        }else if (s5.equals("3")) {
                            t5.setText("East");
                        }
                        String s6 = dataSnapshot.child("23").child("6").getValue().toString();
                        if (s6.equals("1")) {
                            t6.setText("Ground Floor");
                        }else if (s6.equals("2")) {
                            t6.setText("1st Floor");
                        }else if (s6.equals("3")) {
                            t6.setText("2nd Floor");
                        }else if (s6.equals("4")) {
                            t6.setText("3rd Floor");
                        }
                        String securityDeposit = dataSnapshot.child("22").getValue().toString();
                        t7.setText("₹"+securityDeposit);
                    }else if (Category.equals("2")) {
                        //category 2 shops

                        tvCategory.setText("Shop");
                        String s1 = dataSnapshot.child("23").child("1").getValue().toString();
                        if (s1.equals("1")) {
                            t1.setText("Individual");
                        }
                        String s3 = dataSnapshot.child("23").child("3").getValue().toString();
                        if (s3.equals("1")) {
                            t3.setText("2-wheeler");
                        }else if (s3.equals("2")) {
                            t3.setText("4-wheeler");
                        }
                        String s5 = dataSnapshot.child("23").child("5").getValue().toString();
                        if (s5.equals("1")) {
                            t5.setText("North");
                        }else if (s5.equals("2")) {
                            t5.setText("South");
                        }else if (s5.equals("3")) {
                            t5.setText("East");
                        }
                        String s6 = dataSnapshot.child("23").child("6").getValue().toString();
                        if (s6.equals("1")) {
                            t6.setText("Ground Floor");
                        }else if (s6.equals("2")) {
                            t6.setText("1st Floor");
                        }else if (s6.equals("3")) {
                            t6.setText("2nd Floor");
                        }else if (s6.equals("4")) {
                            t6.setText("3rd Floor");
                        }
                        String securityDeposit = dataSnapshot.child("22").getValue().toString();
                        t7.setText("₹"+securityDeposit);
                        t2.setVisibility(View.GONE);
                        th2.setVisibility(View.GONE);
                        t4.setVisibility(View.GONE);
                        th4.setVisibility(View.GONE);
                    }else if (Category.equals("3")) {
                        //category 3 photografers
                        tvCategory.setText("Photography");
                        CallLay.setVisibility(View.GONE);
                        tvBook.setVisibility(View.VISIBLE);
                        checkApplyData();
                        //displaying photography type
                        th1.setText("Photography/Videography");
                        if (dataSnapshot.child("23").child("1").getValue().toString().equals("0")) {
                            t1.setText("Only Photography");
                        }else if (dataSnapshot.child("23").child("1").getValue().toString().equals("1")) {
                            t1.setText("Only Videography");
                        }else {
                            t1.setText("Both");
                        }
                        //displaying copies type
                        th2.setText("Copies Type");
                        if (dataSnapshot.child("23").child("2").getValue().toString().equals("0")) {
                            t2.setText("Only Hard Copy");
                        }else if (dataSnapshot.child("23").child("2").getValue().toString().equals("1")) {
                            t2.setText("Only Soft Copy");
                        }else {
                            t2.setText("Both Hard & Soft Copies");
                        }
                        //displaying editing type
                        th3.setText("Is editing available");
                        if (dataSnapshot.child("23").child("3").getValue().toString().equals("0")) {
                            t3.setText("Yes");
                        }else {
                            t3.setText("No");
                        }
                        //displaying album type
                        th4.setText("Album Type");
                        if (dataSnapshot.child("23").child("4").getValue().toString().equals("0")) {
                            t4.setText("Karizma");
                        }else {
                            t4.setText("Canvera");
                        }
                        th5.setVisibility(View.GONE);
                        t5.setVisibility(View.GONE);
                        th6.setVisibility(View.GONE);
                        t6.setVisibility(View.GONE);
                        th7.setVisibility(View.GONE);
                        t7.setVisibility(View.GONE);
                        //check wheather promo video avialble or not
                        if (dataSnapshot.child("23").hasChild("vid")) {
                            RelativeLayout promoLay = findViewById(R.id.promo_vid_lay);
                            promoLay.setVisibility(View.VISIBLE);
                            ImageView ivPromo = findViewById(R.id.iv_promo_video);
                            Glide
                                    .with(DetailActivity.this)
                                    .load(dataSnapshot.child("23").child("vid").child("0").getValue().toString())
                                    .centerCrop()
                                    .into(ivPromo);
                            ivPromo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    gotoUrl(dataSnapshot.child("23").child("vid").child("1").getValue().toString());
                                }
                            });
                        }
                    }else if (Category.equals("4")) {
                        //category 4 function halls
                        String s1 = dataSnapshot.child("23").child("1").getValue().toString();
                        th1.setText("Type");
                        if (s1.equals("1")) {
                            t1.setText("Ac");
                        }else if (s1.equals("2")) {
                            t1.setText("Non-Ac");
                        }
                        t2.setVisibility(View.GONE);
                        th2.setVisibility(View.GONE);
                        t3.setVisibility(View.GONE);
                        th3.setVisibility(View.GONE);
                        t4.setVisibility(View.GONE);
                        th4.setVisibility(View.GONE);
                        t5.setVisibility(View.GONE);
                        th5.setVisibility(View.GONE);
                        t6.setVisibility(View.GONE);
                        th6.setVisibility(View.GONE);
                        tvCategory.setText("Function Hall/Banquet");
                        String securityDeposit = dataSnapshot.child("22").getValue().toString();
                        t7.setText("₹"+securityDeposit);

                        CallLay.setVisibility(View.GONE);
                        tvBook.setVisibility(View.VISIBLE);
                        checkApplyData();
                    }else if (Category.equals("5")) {
                        //category 5 bikes
                        String b = dataSnapshot.child("23").child("b").getValue().toString();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference docRef = db.collection("bike").document(b);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        t1.setText(document.get("n").toString());
                                        th1.setText("Brand/Model");
                                    } else {
                                        Toast.makeText(mActivity, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        t2.setVisibility(View.GONE);
                        th2.setVisibility(View.GONE);
                        t3.setVisibility(View.GONE);
                        th3.setVisibility(View.GONE);
                        t4.setVisibility(View.GONE);
                        th4.setVisibility(View.GONE);
                        t5.setVisibility(View.GONE);
                        th5.setVisibility(View.GONE);
                        t6.setVisibility(View.GONE);
                        th6.setVisibility(View.GONE);
                        tvCategory.setText("Bike");
                        String securityDeposit = dataSnapshot.child("22").getValue().toString();
                        t7.setText("₹"+securityDeposit);
                    }else if (Category.equals("6")) {
                        //category 6 cars
                        if (dataSnapshot.child("23").child("r").getValue().toString().equals("N")) {
                            //when car has no sun roof
                            t4.setVisibility(View.GONE);
                            th4.setVisibility(View.GONE);
                        }else {
                            //when car has sun roof
                            th4.setText("Sunroof");
                            t4.setText("Yes");
                        }
                        String b = dataSnapshot.child("23").child("b").getValue().toString();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference docRef = db.collection("car").document(b);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        t1.setText(document.get("n").toString());
                                        th1.setText("Brand/Model");
                                    } else {
                                        Toast.makeText(mActivity, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                        th2.setText("Fuel Type");
                        if (dataSnapshot.child("23").child("1").getValue().toString().equals("0")) {
                            t2.setText("Petrol");
                            th3.setText("Transmission");
                            if (dataSnapshot.child("23").child("2").getValue().toString().equals("0")) {
                                t3.setText("Manual");
                            }else {
                                t3.setText("Automatic");
                            }
                        }else if (dataSnapshot.child("23").child("1").getValue().toString().equals("1")) {
                            t2.setText("Diesel");
                            th3.setText("Transmission");
                            if (dataSnapshot.child("23").child("2").getValue().toString().equals("0")) {
                                t3.setText("Manual");
                            }else {
                                t3.setText("Automatic");
                            }
                        }else {
                            t2.setText("Electric");
                            t3.setVisibility(View.GONE);
                            th3.setVisibility(View.GONE);
                        }
                        t5.setVisibility(View.GONE);
                        th5.setVisibility(View.GONE);
                        t6.setVisibility(View.GONE);
                        th6.setVisibility(View.GONE);
                        tvCategory.setText("Car");
                        String securityDeposit = dataSnapshot.child("22").getValue().toString();
                        t7.setText("₹"+securityDeposit);
                    }


                    if (onceViewed == true) {
                        if (dataSnapshot.hasChild("12")) {
                            totalListingView = dataSnapshot.child("12").getValue().toString();
                        }
                    }

                    String address = dataSnapshot.child("13").getValue().toString();
                    tvAddress.setText(address);

                    lat = dataSnapshot.child("14").getValue().toString();
                    lng = dataSnapshot.child("15").getValue().toString();

                    phNm = dataSnapshot.child("17").getValue().toString();

                    if (dataSnapshot.hasChild("24")) {
                        verifiedLay.setVisibility(View.VISIBLE);
                    }

                    getOwnerName = FirebaseDatabase.getInstance().getReference().child("users data").child(phNm).child("0");
                    getOwnerName.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            tvOwnerName.setText(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    if (onceCLicked==true) {
                        onceCLicked=false;
                        if (dataSnapshot.hasChild("19")) {
                            String raw = dataSnapshot.child("19").getValue().toString();
                            String[] split3 = raw.split(",");
                            totalCallCLicks=split3[0];
                            totalWhatsappClicks=split3[1];
                        }
                    }

                }
                else {
                    Utils.startActivity(DetailActivity.this,HomeActivity.class);
                    Toast.makeText(mActivity, "Sorry this listings is deleted by owner.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //getting and checking users favorite listings
        getLiked = FirebaseDatabase.getInstance().getReference().child("users data").child(userPhno);
        getLiked.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("liked listings")) {
                    total = String.valueOf(dataSnapshot.child("liked listings").getChildrenCount());
                    for (DataSnapshot ds : dataSnapshot.child("liked listings").getChildren()) {
                        if (ds.hasChild("is deleted")) {

                        }else {
                            String Pincode = ds.child("0").getValue().toString();
                            String id = ds.child("1").getValue().toString();
                            if (Pincode.equals(pincode)) {
                                if (id.equals(listingId)) {
                                    //indicates that this listings is already a users favorite listing
                                    ivLike.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                                    ivLike.setBackgroundResource(R.drawable.ic_fill_heart);
                                    isLiked = true;
                                    currentLikedId = ds.getKey();
                                }
                            }
                        }
                    }
                }else {
                    total = "0";
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //CountView();
        dialog.dismiss();

    }

    private void setImages() {
        sliderView = findViewById(R.id.details_images_slider);
        DetailsImagesAdapter detailsImagesAdapter = new DetailsImagesAdapter(list2,mActivity);
        sliderView.setSliderAdapter(detailsImagesAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideKeyboard(mActivity);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {

        mActivity = this;
        // setting onClick Listeners
        RelativeLayout backLay = findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);
        io.RentEasy.views.RegularTextView tvCall = findViewById(R.id.tv_call);
        tvCall.setOnClickListener(this);
        io.RentEasy.views.RegularTextView tvMessage = findViewById(R.id.tv_message);
        tvMessage.setOnClickListener(this);
        CardView cvMap = findViewById(R.id.cv_map);
        cvMap.setOnClickListener(this);
        ivLike = findViewById(R.id.iv_like);
        ivLike.setOnClickListener(this);

        ImageView ivReport = findViewById(R.id.iv_report);
        ivReport.setOnClickListener(this);
        ImageView ivShare=findViewById(R.id.iv_share);
        ivShare.setOnClickListener(this);

        tvBook = findViewById(R.id.tv_book);
        CallLay=findViewById(R.id.call_message_lay);

        tvBook.setOnClickListener(this);

        //load ads
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getAds = FirebaseDatabase.getInstance().getReference().child("Server");
        getAds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isAdsAvailable = dataSnapshot.child("is ads available").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        loadRewardAd();

        reviewManager = ReviewManagerFactory.create(this);

        sharedPreferences = this.getSharedPreferences(SHARED_PREF_PAID,MODE_PRIVATE);
        sp2 = this.getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        isPro = sharedPreferences.getString(KEY_IS_PAID,null);
        userPhno = sp2.getString(KEY_PHONENO,null);

        checkRequestsData();
    }

    private void showBottomSheetDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        TextInputEditText tvDes = dialog.findViewById(R.id.input_describe);
        io.RentEasy.views.RegularTextView tvDateSelected = dialog.findViewById(R.id.tv_selected_date);

        LinearLayout book = dialog.findViewById(R.id.book_lay);
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDatePicked) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("d", tvDateSelected.getText().toString());
                    data.put("i", listingId);
                    data.put("p", pincode);
                    data.put("n",userPhno);
                    if (!tvDes.getText().toString().isEmpty()) {
                        data.put("r", tvDes.getText().toString());
                    }

                    DocumentReference dbApply = db.collection("booking applications").document();
                    dbApply.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //close the dialog on sucess
                            dialog.dismiss();
                            Toast.makeText(mActivity, "Successfully applied for booking. It generally takes nearly 24 hours to call you.", Toast.LENGTH_LONG).show();
                        }
                    });
                }else {
                    Toast.makeText(mActivity, "Please select date", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Calendar calendar = Calendar.getInstance();
        final int Year = calendar.get(Calendar.YEAR);
        final int Month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        RelativeLayout dateLay = dialog.findViewById(R.id.date_pick_lay);
        dateLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog2 = new DatePickerDialog(mActivity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String date = dayOfMonth+"/"+month+"/"+year;

                        if (year==Year) {
                            if (month==Month) {
                                if (dayOfMonth<day+2) {
                                    Toast.makeText(mActivity, "You have to select date at least after 2 days from today. Since we take nearly 24 hours to get back to you.", Toast.LENGTH_LONG).show();
                                }else {
                                    tvDateSelected.setText(date);
                                    isDatePicked=true;
                                }
                            }else if (month<Month) {
                                Toast.makeText(mActivity, "Please select date in future.", Toast.LENGTH_SHORT).show();
                            }else {
                                tvDateSelected.setText(date);
                                isDatePicked=true;
                            }
                        }else {
                            Toast.makeText(mActivity, "Please select this year.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },Year,Month,day);
                dialog2.show();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialoAnimation;

    }

    /*
    private void CountView() {
        // getting all listings view by user and checking them with this listing
        viewedListings=FirebaseDatabase.getInstance().getReference().child("users data").child(userPhno);
        viewedListings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (viewCount==true) {
                    viewCount=false;
                    if (dataSnapshot.hasChild("2")) {
                        int total = (int) dataSnapshot.child("2").getChildrenCount();
                        for (DataSnapshot ds : dataSnapshot.child("2").getChildren()) {
                            varViewed++;
                            if (ds.child("0").getValue().toString().equals(pincode)){
                                if (ds.child("1").getValue().toString().equals(listingId)) {
                                    isViewed=true;
                                }
                            }

                            if (varViewed==total) {
                                if (isViewed==false) {
                                    int add = total+1;
                                    viewedListings.child("2").child(String.valueOf(add)).child("0").setValue(pincode);
                                    viewedListings.child("2").child(String.valueOf(add)).child("1").setValue(listingId);
                                    getListingInfo.child("12").setValue(Integer.parseInt(totalListingView)+1);
                                }
                            }
                        }
                    }else {
                        viewedListings.child("2").child("1").child("0").setValue(pincode);
                        viewedListings.child("2").child("1").child("1").setValue(listingId);
                        getListingInfo.child("12").setValue(Integer.parseInt(totalListingView)+1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

     */

    /*
    private void getRelatedListings() {
        list = new ArrayList<>();
        temp = new ArrayList<>();
        temp2 = new ArrayList<>();
        getRelatedListings = FirebaseDatabase.getInstance().getReference().child("listings data");
        getRelatedListings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (related==true) {
                    related=false;
                    if (dataSnapshot.hasChild(pincode)) {
                        String total = String.valueOf(dataSnapshot.child(pincode).getChildrenCount());
                        for (DataSnapshot ds : dataSnapshot.child(pincode).getChildren()) {
                            var++;
                            if (ds.hasChild("is deleted")) {

                            } else {
                                String imageUrl, Rate = "";
                                if (ds.hasChild("0")) {
                                    imageUrl = ds.child("0").getValue().toString();
                                } else {
                                    imageUrl = "No";
                                }
                                String amountRaw = ds.child("5").getValue().toString();
                                String[] split = amountRaw.split(",");
                                String amount = split[0];
                                String cycle = split[1];
                                if (cycle.equals("m")) {
                                    Rate = "₹" + amount + " per Month";
                                } else if (cycle.equals("d")) {
                                    Rate = "₹" + amount + " per Day";
                                } else if (cycle.equals("h")) {
                                    Rate = "₹" + amount + " per Hour";
                                }
                                String promoted = "null";
                                if (ds.hasChild("18")) {
                                    promoted="true";
                                }
                                String title = ds.child("6").getValue().toString()+" • "+ds.child("10").getValue().toString();
                                if (ds.getKey().equals(listingId)) {

                                }else {
                                    if (ds.hasChild("18")) {
                                        temp2.add(new DetailsRelated(Rate+" • ",title,imageUrl,pincode, String.valueOf(var), promoted));
                                    }else{
                                        temp.add(new DetailsRelated(Rate,title,imageUrl,pincode, String.valueOf(var), promoted));
                                    }
                                }

                                if (Integer.parseInt(total) == var) {
                                    list.addAll(temp);
                                    list.addAll(temp2);
                                    setDataToAdapter();
                                    io.RentEasy.views.MediumTextView related = findViewById(R.id.tvRelated);
                                    RecyclerView recyclerView = findViewById(R.id.recycler_related_postings);
                                    if (list.isEmpty()) {
                                        related.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }
                    }else {

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDataToAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recycler_related_postings);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        Collections.reverse(list);
        adapter=new DetailsRelatedAdapter(list, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

     */

    private void openLoadingDialog() {
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void loadRewardAd() {
        AdRequest adRequest2 = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-1019423261424750/4136891252",
                adRequest2, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        isAdLoaded=true;
                    }
                });
    }

    private void openAdChooseDialog() {
        dialog.setContentView(R.layout.watch_or_block_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView watchAd = dialog.findViewById(R.id.tv_ad);  //when user click watch ad to get owner contact details
        watchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdsAvailable.equals("t")) {
                    if (mRewardedAd != null) {
                        Activity activityContext = DetailActivity.this;
                        mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                // Handle the reward.
                                int rewardAmount = rewardItem.getAmount();
                                String rewardType = rewardItem.getType();
                                if (isCallorMessage.equals("C")) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:"+phNm));
                                    startActivity(intent);

                                    int add = Integer.parseInt(totalCallCLicks)+1;
                                    totalCallCLicks= String.valueOf(add);
                                    getListingInfo.child("19").setValue(add+","+totalWhatsappClicks);
                                } else if (isCallorMessage.equals("M")) {
                                    gotoUrl("https://api.whatsapp.com/send?phone="+ "+91" + phNm + "&text=I want to rent your posting.");
                                    int add = Integer.parseInt(totalWhatsappClicks)+1;
                                    totalWhatsappClicks= String.valueOf(add);
                                    getListingInfo.child("19").setValue(totalCallCLicks+","+add);
                                }
                                dialog.dismiss();
                                //load ad
                                loadRewardAd();
                            }
                        });
                    } else {
                        Toast.makeText(DetailActivity.this,"Sorry ad failed to show, try after some time.", Toast.LENGTH_SHORT).show();
                        loadRewardAd();
                    }
                }else {
                    if (isCallorMessage.equals("C")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+phNm));
                        startActivity(intent);

                        int add = Integer.parseInt(totalCallCLicks)+1;
                        totalCallCLicks= String.valueOf(add);
                        getListingInfo.child("19").setValue(add+","+totalWhatsappClicks);
                    } else if (isCallorMessage.equals("M")) {
                        gotoUrl("https://api.whatsapp.com/send?phone="+ "+91" + phNm + "&text=I want to rent your posting.");

                        int add = Integer.parseInt(totalWhatsappClicks)+1;
                        totalWhatsappClicks= String.valueOf(add);
                        getListingInfo.child("19").setValue(totalCallCLicks+","+add);
                    }
                    dialog.dismiss();
                }
            }
        });

        TextView blockAd = dialog.findViewById(R.id.tv_block); //when user click block ad payment to get owner contact details
        blockAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = "50";
                int amount = Math.round(Float.parseFloat(price)*100);
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_live_g7i7onGCVMJrUN");
                JSONObject object = new JSONObject();
                try {
                    object.put("name","RentEasy Pro");
                    object.put("description","Go Ad-Free with just Rs.50.00");
                    object.put("currency","INR");
                    object.put("amount",amount);
                    checkout.open(DetailActivity.this,object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_lay:
                onBackPressed();
                break;
            case R.id.tv_call:
                // tempory monitization removal.
                /*
                if (isPro == null) {
                    isCallorMessage="C";
                    openAdChooseDialog();
                }else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+phNm));
                    startActivity(intent);

                    int add = Integer.parseInt(totalCallCLicks)+1;
                    totalCallCLicks= String.valueOf(add);
                    getListingInfo.child("19").setValue(add+","+totalWhatsappClicks);
                }
                 */

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phNm));
                startActivity(intent);

                int add1 = Integer.parseInt(totalCallCLicks)+1;
                totalCallCLicks= String.valueOf(add1);
                //getListingInfo.child("19").setValue(add1+","+totalWhatsappClicks);

                if(!sendRequest==true) {
                    sendRequest();
                }
                break;
            case R.id.tv_message:
                // tempory monitization removal.
                /*
                if (isPro == null) {
                    isCallorMessage="M";
                    openAdChooseDialog();
                }else {
                    gotoUrl("https://api.whatsapp.com/send?phone="+ "+91" + phNm + "&text=I want to rent your "+tvListingTitle.getText().toString()+" which I found in RentEasy app.");

                    int add = Integer.parseInt(totalWhatsappClicks)+1;
                    totalWhatsappClicks= String.valueOf(add);
                    getListingInfo.child("19").setValue(totalCallCLicks+","+add);
                }
                 */

                gotoUrl("https://api.whatsapp.com/send?phone="+ "+91" + phNm + "&text=I want to rent your "+tvListingTitle.getText().toString()+" which I found in RentEasy app.");

                int add = Integer.parseInt(totalWhatsappClicks)+1;
                totalWhatsappClicks= String.valueOf(add);
                //getListingInfo.child("19").setValue(totalCallCLicks+","+add);

                if(!sendRequest==true) {
                    sendRequest();
                }

                break;
            case R.id.cv_map:
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng ;
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                intent2.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent2);
                break;
            case R.id.iv_like:
                if (isLiked == false) {
                    if (total.equals("0")) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("0",pincode);
                        hashMap.put("1",listingId);
                        getLiked.child("liked listings").child("1").updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(@NonNull Object o) {
                                currentLikedId="1";
                                Toast.makeText(mActivity, "Added to your favorite ads.", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        String temp = "1";
                        int sum = Integer.parseInt(total) + Integer.parseInt(temp);
                        HashMap hashMap = new HashMap();
                        hashMap.put("0",pincode);
                        hashMap.put("1",listingId);
                        getLiked.child("liked listings").child(String.valueOf(sum)).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(@NonNull Object o) {
                                currentLikedId=String.valueOf(sum);
                                Toast.makeText(mActivity, "Added to your favorite ads.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }else {
                    HashMap hashMap = new HashMap();
                    hashMap.put("is deleted","t");
                    getLiked.child("liked listings").child(currentLikedId).setValue(null);
                    getLiked.child("liked listings").child(currentLikedId).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(@NonNull Object o) {
                            Toast.makeText(mActivity, "Removed from your favorite ads.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    ivLike.setBackgroundResource(R.drawable.ic_empty_heart);
                    ivLike.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4c4b4b")));
                    isLiked=false;
                }
                ivLike.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction()==MotionEvent.ACTION_DOWN) {
                            ivLike.startAnimation(scaleUp);
                        }else if (event.getAction()==MotionEvent.ACTION_UP) {
                            ivLike.startAnimation(scaleDown);
                        }
                        return false;
                    }
                });
                break;
            case R.id.iv_report:
                openReportDialog();
                break;
            case R.id.iv_share:
                //on user clicked share option opens url provide posting url
                String url = "https://www.renteasyindia.com/ad/"+pincode+"/"+listingId;
                shareIntent(url);
                break;
            case R.id.tv_book:
                showBottomSheetDialog();
                break;
        }
    }

    private void checkRequestsData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection("requests data");
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot q1 : value.getDocuments()) {
                    String number = String.valueOf(q1.get("n"));
                    String pin = String.valueOf(q1.get("p"));
                    String i = String.valueOf(q1.get("i"));

                    if (number.equals(userPhno)) {
                        if (pin.equals(pincode)) {
                            if (i.equals(listingId)) {
                                sendRequest=true;
                            }
                        }
                    }

                }
            }
        });
    }

    private void checkApplyData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = db.collection("booking applications");
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot q1 : value.getDocuments()) {
                    String number = String.valueOf(q1.get("n"));
                    String pin = String.valueOf(q1.get("p"));
                    String i = String.valueOf(q1.get("i"));

                    if (number.equals(userPhno)) {
                        if (pin.equals(pincode)) {
                            if (i.equals(listingId)) {
                                tvBook.setEnabled(false);
                                tvBook.setText("Checking in Progress");
                                tvBook.setBackground(getResources().getDrawable(R.drawable.dissabled_bg));
                            }
                        }
                    }

                }
            }
        });
    }

    private void sendRequest() {
        if (onceRequest==true) {
            onceRequest=false;

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.hh.mm.ss");
            String currentDateandTime = sdf.format(new Date());

            Map<String, Object> data = new HashMap<>();
            data.put("d", currentDateandTime);
            data.put("i", listingId);
            data.put("p", pincode);
            data.put("n",userPhno);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference dbApply = db.collection("requests data").document();
            dbApply.set(data);

        }
    }


    private void openImage(String ivUrl) {
        Intent intent = new Intent(DetailActivity.this,ImageDetailViewerActivity.class);
        intent.putExtra("image url",ivUrl);
        startActivity(intent);
    }


    private void shareIntent(String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I've found something for rent. Click the below link to see \n" + url + "\nor if you not yet installed the app click below \nhttps://play.google.com/store/apps/details?id=io.kodular.nsmarttechnologies6.RentEasy");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    private void openReportDialog() {
        dialog.setContentView(R.layout.status_off_survey_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        io.RentEasy.views.MediumTextView close = dialog.findViewById(R.id.tv_close);
        close.setVisibility(View.VISIBLE);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        io.RentEasy.views.MediumTextView title = dialog.findViewById(R.id.tv_title);
        title.setText("Report Posting");
        io.RentEasy.views.MediumTextView description = dialog.findViewById(R.id.tv_displayRooms);
        description.setText("Tell us what's wrong with this Posting.");

        String selection[] = {"select reason","Fake Posting","Sexual","Misleading"};
        ArrayAdapter<String> spinnerArrayAdapter;
        Spinner spinner = dialog.findViewById(R.id.Spinner);

        spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, selection);
        spinner.setAdapter(spinnerArrayAdapter);


        io.RentEasy.views.MediumTextView okbtn = dialog.findViewById(R.id.tv_ok);
        okbtn.setText("Report");
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem().equals("select reason")) {
                    Toast.makeText(getApplicationContext(),"Please select a reason",Toast.LENGTH_SHORT).show();
                } else {
                    if (spinner.getSelectedItem().equals("Fake Posting")) {
                        spamReport("1");
                    }else if (spinner.getSelectedItem().equals("Sexual")) {
                        spamReport("2");
                    }else if (spinner.getSelectedItem().equals("Misleading")) {
                        spamReport("3");
                    }
                }
            }
        });

        dialog.show();
    }

    private void spamReport(String s) {
        addReport = FirebaseDatabase.getInstance().getReference();
        addReport.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (once==true) {
                once=false;
                if(dataSnapshot.hasChild("spam reports")) {
                    addReport.child("spam reports").push().setValue(pincode+","+listingId+","+s+","+userPhno);
                }else {
                    addReport.child("spam reports").push().setValue(pincode+","+listingId+","+s+","+userPhno);
                }
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dialog.dismiss();
        Toast.makeText(getApplicationContext(),"Thanks! for Reporting.",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    /*public void showRateApp() {
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Getting the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();

                Task <Void> flow = reviewManager.launchReviewFlow(this, reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown.
                });
            }
        });
    }

     */

    @Override
    public void onPaymentSuccess(String s) {
        //adding user as pro user
        setProUser = FirebaseDatabase.getInstance().getReference().child("users data").child(userPhno);
        HashMap hashMap = new HashMap();
        hashMap.put("pro","t");
        setProUser.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(@NonNull Object o) {
            setTotalProUsers = FirebaseDatabase.getInstance().getReference().child("Server");
            setTotalProUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String total = dataSnapshot.child("total pro users").getValue().toString();
                int sum = Integer.parseInt(total) + 1;
                HashMap hashMap1 = new HashMap();
                hashMap1.put("total pro users",sum);
                if (paymentSucess==true) {
                    paymentSucess=false;
                    setTotalProUsers.updateChildren(hashMap1).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(@NonNull Object o) {
                            String ok = "true";
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(KEY_IS_PAID,ok);
                            editor.apply();
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+phNm));
                            startActivity(intent);
                            isPro = "true";
                            Toast.makeText(DetailActivity.this,"Success! you are a pro user. Please re-start the app if the ads are not blocked.",Toast.LENGTH_LONG).show();
                        }
                    });
                }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(DetailActivity.this,"Sorry! your payment has failed.",Toast.LENGTH_SHORT).show();
    }
    private void gotoUrl(String s) {
        Uri uri= Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}