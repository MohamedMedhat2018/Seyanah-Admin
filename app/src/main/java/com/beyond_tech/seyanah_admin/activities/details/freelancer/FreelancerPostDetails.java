//package com.beyond_tech.seyanah_admin.activities.details.freelancer;
//
//import android.content.DialogInterface;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.text.style.StyleSpan;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.cardview.widget.CardView;
//import androidx.core.widget.NestedScrollView;
//import androidx.recyclerview.widget.DefaultItemAnimator;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.ahmed.homeservices.R;
//import com.ahmed.homeservices.adapters.rv.comments.CommentAdapter;
//import com.ahmed.homeservices.adapters.rv.comments.CommentFreelancerAdapter;
//import com.ahmed.homeservices.adapters.view_pager.DemoInfiniteAdapter;
//import com.ahmed.homeservices.constants.Constants;
//import com.ahmed.homeservices.fire_utils.RefBase;
//import com.ahmed.homeservices.models.Comment;
//import com.ahmed.homeservices.models.ConnectionFree;
//import com.ahmed.homeservices.models.Rate;
//import com.ahmed.homeservices.models.User;
//import com.ahmed.homeservices.models.orders.OrderRequest;
//import com.ahmed.homeservices.utils.Utils;
//import com.asksira.loopingviewpager.LoopingViewPager;
//import com.github.siyamed.shapeimageview.CircularImageView;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.ValueEventListener;
//import com.pixplicity.easyprefs.library.Prefs;
//import com.rd.PageIndicatorView;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import es.dmoral.toasty.Toasty;
//
//public class FreelancerPostDetails extends AppCompatActivity implements CommentAdapter.OnAcceptClicked {
//
//
//    private static final String TAG = "FreelancerPostDetails";
//    //    @BindView(R.id.tv_customer_order_category)
////    TextView tv_customer_order_category;
////    @BindView(R.id.tv_customer_order_coast)
////    TextView tv_customer_order_coast;
//    @BindView(R.id.add_comment)
//    LinearLayout llAddComment;
//    @BindView(R.id.iv_customer_post_img)
////    ImageView iv_customer_post_img;
//            CircularImageView iv_customer_post_img;
//    @BindView(R.id.tv_customer_post_name)
//    TextView tv_customer_post_name;
//    @BindView(R.id.tv_customer_post_city_loc)
//    TextView tv_customer_post_city;
//    @BindView(R.id.tv_customer_post_country_loc)
//    TextView tv_customer_post_country;
//    @BindView(R.id.looping_view_pager_post)  //need an adapter
//            LoopingViewPager looping_view_pager_post;
//    @BindView(R.id.tv_customer_post_category)
//    TextView tv_customer_post_category;
//    @BindView(R.id.tv_post_start_date)
//    TextView tv_post_start_date;
//    @BindView(R.id.tv_customer_post_time)
//    TextView tv_customer_post_time;
//    @BindView(R.id.tv_customer_order_description)
//    TextView tv_customer_order_description;
//    @BindView(R.id.rv_comments)
//    RecyclerView rv_comments;
//    @BindView(R.id.et_comment)
//    EditText etWriteComment;
//    @BindView(R.id.iv_send_commnet)
//    ImageView ivSendComment;
//    @BindView(R.id.tvNoCommentsYet)
//    TextView tvNoCommentsYet;
//    @BindView(R.id.cv_customer_order_choosen_comment)
//    CardView cv_customer_order_choosen_comment;
//    @BindView(R.id.tv_customer_order_choosen_comment)
//    TextView tv_customer_order_choosen_comment;
//    @BindView(R.id.ll_customer_order_rate)
//    LinearLayout ll_customer_order_rate;
//    @BindView(R.id.rb_customer_order_rate)
//    RatingBar rb_customer_order_rate;
//    @BindView(R.id.tv_customer_post_location_address)
//    TextView tv_customer_post_location_address;
//
////    @BindView(R.id.tv_customer_post_answer1)
////    Spinner tv_customer_post_answer1;
//
//    //    Bundle extras;
////    OrderRequest orderRequest;
////    DemoInfiniteAdapter demoInfiniteAdapter;
//    @BindView(R.id.tv_post_offers)
//    TextView tv_post_offers;
//    List<Comment> listComments = new ArrayList<>();
//    OrderRequest orderRequest;
//    User user;
//    @BindView(R.id.llComments)
//    LinearLayout llComments;
//    @BindView(R.id.tvOfferExist)
//    TextView tvOfferExist;
//    @BindView(R.id.scrollView)
//    NestedScrollView scrollView;
//    String exist;
//    @BindView(R.id.indicatorView)
//    PageIndicatorView indicatorView;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    Menu menu;
//    int numberOfRealComments = 0;
//    private DemoInfiniteAdapter demoInfiniteAdapter;
//    @BindView(R.id.cardNoImages)
//    View cardNoImages;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_freelancer_post_details);
//        ButterKnife.bind(this);
//        accessToolbar();
//
////        toolbar.setNavigationOnClickListener(view -> {
////            //finish();
////            onBackPressed();
////        });
////        setSupportActionBar(toolbar);
//
//    }
//
//    private void accessToolbar() {
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(view -> {
//            finish();
////            onBackPressed();
//        });
//        toolbar.setTitle(getString(R.string.details_post));
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_free_post_details, menu);
//        this.menu = menu;
//        return super.onCreateOptionsMenu(menu);
////        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.finish_work:
//                Log.e(TAG, "onOptionsItemSelected: ");
//                finishFreeWorkNow();
//                break;
//
//        }
//        return super.onOptionsItemSelected(item);
////        return true;
//    }
//
//    private void finishFreeWorkNow() {
////        RefBase.refRequests(orderRequest.getOrderId())
//        RefBase.refRequests()
//                .orderByChild(Constants.ORDER_ID)
//                .equalTo(orderRequest.getOrderId())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        dataSnapshot.getRef().removeEventListener(this);
//                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                            for (DataSnapshot dataSnapshot1 :
//                                    dataSnapshot.getChildren()) {
//                                Log.e(TAG, "onDataChange:iiiiii ");
//
////                            OrderRequest orderRequest1 = dataSnapshot.getValue(OrderRequest.class);
////                            orderRequest1.setState(Constants.FREELANCE_FINISHED);
//
//                                HashMap<String, Object> map = new HashMap<>();
//                                map.put(Constants.ORDER_STATE, Constants.FREELANCE_FINISHED);
//                                dataSnapshot1.getRef().updateChildren(map);
//
//                                RefBase.refFreelancersConnection()
//                                        .orderByChild(Constants.REQUEST_ID)
//                                        .equalTo(orderRequest.getOrderId())
//                                        .addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                                                    dataSnapshot.getRef().removeEventListener(this);
//                                                    for (DataSnapshot dataSnapshot1 :
//                                                            dataSnapshot.getChildren()) {
//                                                        ConnectionFree connectionFree = dataSnapshot1.getValue(ConnectionFree.class);
//                                                        if (connectionFree != null) {
//                                                            connectionFree.setState(Constants.FREELANCE_FINISHED);
//                                                            dataSnapshot1.getRef().setValue(connectionFree).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                @Override
//                                                                public void onSuccess(Void aVoid) {
////                                                                    Toasty.success(getApplicationContext(), "Updated success", Toast.LENGTH_SHORT)
////                                                                            .show();
//                                                                    menu.findItem(R.id.finish_work).setVisible(false);
//
//                                                                }
//                                                            });
//                                                        }
//                                                    }
//
//
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                            }
//                                        });
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        if (orderRequest != null) {
//            Log.e(TAG, "onPrepareOptionsMenu: " + orderRequest.getState());
//            switch (orderRequest.getState()) {
//                case Constants.POST:
//                case Constants.FREELANCE_FINISHED:
//                    Log.e(TAG, "777777777777: ");
//                    this.menu.findItem(R.id.finish_work).setVisible(false);
//                    break;
//                case Constants.FREELANCE_WORKING:
//                    Log.e(TAG, "333333333333333: ");
//                    this.menu.findItem(R.id.finish_work).setVisible(true);
//                    break;
//            }
//        } else {
//            Log.e(TAG, "222222222222: ");
//        }
//
//        return super.onPrepareOptionsMenu(menu);
////        return true;
//    }
//
//    @Override
//    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        accessFields(); //Get order REQUEST
//        addTextWatcherToEditTextSendComment();
//
//
//        if (orderRequest.getOrderPhotos().isEmpty()) {
//            Log.e(TAG, "accessViews: default img  ");
//            cardNoImages.setVisibility(View.VISIBLE);
//        } else {
//            cardNoImages.setVisibility(View.GONE);
//            demoInfiniteAdapter = new DemoInfiniteAdapter(this, orderRequest.getOrderPhotos(), true);
//            looping_view_pager_post.setAdapter(demoInfiniteAdapter);
//            Utils.attachIndicatiorToViewPager(looping_view_pager_post, indicatorView);
//        }
//
//
//        switch (orderRequest.getState()) {
//            case Constants.POST:
//                tvNoCommentsYet.setVisibility(View.VISIBLE);
//                llAddComment.setVisibility(View.VISIBLE);
//                loadPreviousCommentsIntoRecyclerView();
//                checkIfIamSentOfferAlreadyToThisRequest();
//
//                break;
//            case Constants.FREELANCE_WORKING:
//                llAddComment.setVisibility(View.GONE);
//                tvNoCommentsYet.setVisibility(View.GONE);
//                cv_customer_order_choosen_comment.setVisibility(View.VISIBLE);
//                loadPreviousCommentsIntoRecyclerView();
//                tv_post_offers.setVisibility(View.GONE);
//
//                break;
//            case Constants.FREELANCE_FINISHED:
//                llAddComment.setVisibility(View.GONE);
//                tvNoCommentsYet.setVisibility(View.GONE);
//                ll_customer_order_rate.setVisibility(View.GONE);
//                cv_customer_order_choosen_comment.setVisibility(View.VISIBLE); //not finished
////                getRateFromCustomer();
//                getRate();
////                loadRate();
//                loadPreviousCommentsIntoRecyclerView();
//                tv_post_offers.setVisibility(View.GONE);
//
//                break;
//        }
//
//        RefBase.refCategories()
////                    .orderByChild(C)
////                    .equalTo(orderRequest.getCategoryId())
//                .child(orderRequest.getCategoryId())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        dataSnapshot.getRef().removeEventListener(this);
//                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
////                                Category category = dataSnapshot.getValue(Category.class);
//                            HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
//                            if (map.get(Constants.CAT_NAME) != null) {
//                                String catName = (String) map.get(Constants.CAT_NAME);
//                                tv_customer_post_category.setText(catName);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//    }
//
//    private void checkIfIamSentOfferAlreadyToThisRequest() {
//        RefBase.refComments(orderRequest.getOrderId())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        dataSnapshot.getRef().removeEventListener(this);
//                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                            for (DataSnapshot dataSnapshot1 :
//                                    dataSnapshot.getChildren()) {
////                              HashMap<String, Object> dataSnapshot1.getValue();
//                                Comment comment1 = dataSnapshot1.getValue(Comment.class);
//                                String userId = Prefs.getString(Constants.FIREBASE_UID, "");
//                                if (comment1 != null) {
//                                    if (TextUtils.equals(comment1.getFreelancerId(), userId)) {
//                                        llAddComment.setVisibility(View.GONE);
//                                        tvOfferExist.setVisibility(View.VISIBLE);
//                                    } else {
////                                        llAddComment.setVisibility(View.VISIBLE);
////                                        tvOfferExist.setVisibility(View.GONE);
//                                    }
//                                }
//                            }
////                            dataSnapshot.getRef().removeEventListener(this);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//    }
//
//    private void loadRate() {
//        RefBase.rate().orderByChild(Constants.REQUEST_ID).equalTo(orderRequest.getOrderId()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    dataSnapshot.getRef().removeEventListener(this);
//                    Rate rate = dataSnapshot.getChildren().iterator().next().getValue(Rate.class);
//                    if (rate != null) {
//                        rb_customer_order_rate.setRating(Float.parseFloat(rate.getNumberOfStars()));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void addTextWatcherToEditTextSendComment() {
//        etWriteComment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String s = charSequence.toString();
//                if (s.length() > 0) {
//                    ivSendComment.setEnabled(true);
//                } else {
//                    ivSendComment.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//
//            }
//        });
//    }
//
//    private void accessFields() {
//
//        Bundle extras = getIntent().getExtras();
//        Log.e(TAG, "onCreate: " + extras.toString());
//        if (extras != null) {
//            Log.e(TAG, "onCreate: ");
//            orderRequest = (OrderRequest) extras.getSerializable(Constants.ORDER);
//            user = (User) extras.getSerializable(Constants.USER);
//
//
//            if (user != null) {
//                Picasso.get().load(user.getUserPhoto()).into(iv_customer_post_img, new Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                });
////            iv_customer_post_img.setImageResource(user.getUserPhoto());
//            }
//
//
//            ArrayList<String> staticPhotos = new ArrayList<>();
////            staticPhotos.add(R.drawable.customer1);
////            staticPhotos.add(R.drawable.customer2);
////            staticPhotos.add(R.drawable.customer3);
//
//            if (orderRequest.getOrderPhotos().isEmpty()) {
////                demoInfiniteAdapter = new DemoInfiniteAdapter(this, staticPhotos, true);
////                looping_view_pager_post.setAdapter(demoInfiniteAdapter);
//                Log.e(TAG, "onCreate:2 " + staticPhotos.isEmpty());
//                cardNoImages.setVisibility(View.VISIBLE);
//            } else {
//                cardNoImages.setVisibility(View.GONE);
//                demoInfiniteAdapter = new DemoInfiniteAdapter(this, orderRequest.getOrderPhotos(), true);
//                looping_view_pager_post.setAdapter(demoInfiniteAdapter);
//            }
//
//
////            tv_customer_post_name.setText(user.getUserName());
//            tv_customer_post_city.setText(orderRequest.getLocation().getCity());
//            tv_customer_post_country.setText(orderRequest.getLocation().getCountry());
//            tv_customer_post_location_address.setText(orderRequest.getLocationAddress());
//            tv_post_start_date.setText(orderRequest.getCreationDate());
//            tv_customer_post_time.setText(orderRequest.getCreationTime());
//            tv_customer_order_description.setText(orderRequest.getOrderDescription());
//            rv_comments.setHasFixedSize(false);
//            rv_comments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//            rv_comments.setItemAnimator(new DefaultItemAnimator());
//            Log.e(TAG, "accessFields: " + orderRequest.getOrderId());
//
//        }
//    }
//
//    private void loadPreviousCommentsIntoRecyclerView() {
//
////        RefBase.refComments(orderRequest.getOrderId())
////                .orderByChild(Constants.ACCEPTED_COMMENT)
////                .equalTo(true)
////                .addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//////                        dataSnapshot.getRef().removeEventListener(this);
////                        if (dataSnapshot.exists()) {
////                            exist = Constants.EXIST;
////                        } else {
////                            exist = null;
////
////                        }
//
//        RefBase.refComments(orderRequest.getOrderId())
//                .addValueEventListener(new ValueEventListener() {
//                                           @Override
//                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                                               dataSnapshot.getRef().removeEventListener(this);
//                                               if (dataSnapshot.exists()) {
//                                                   Log.e(TAG, "onDataChange: " + dataSnapshot.getKey());
//
//                                                   listComments.clear();
//                                                   numberOfRealComments = 0;
//
//                                                   for (DataSnapshot snapShot :
//                                                           dataSnapshot.getChildren()) {
//                                                       //Log.e(TAG, "comments " + dataSnapshot.toString());
//                                                       HashMap<String, Object> map = (HashMap<String, Object>) snapShot.getValue();
//                                                       if (map != null) {
//                                                           Comment comment = new Comment();
//                                                           if (map.get("comment") != null)
//                                                               comment.setComment(map.get("comment").toString());
//                                                           if (map.get("freelancerId") != null)
//                                                               comment.setFreelancerId(map.get("freelancerId").toString());
//                                                           comment.setCommentId(snapShot.getKey());
//                                                           if (map.get(Constants.ACCEPTED_COMMENT) != null) {
////                                                               comment.setSelected((boolean) map.get(Constants.ACCEPTED_COMMENT));
//                                                               comment.setSelected((boolean) map.get(Constants.ACCEPTED_COMMENT));
//                                                               Log.e(TAG, "fffffffsdfs: ");
//                                                           }
//                                                           numberOfRealComments++;
//                                                           if (TextUtils.equals(comment.getFreelancerId(),
//                                                                   Prefs.getString(Constants.FIREBASE_UID, ""))) {
//                                                               switch (orderRequest.getState()) {
//                                                                   case Constants.POST:
//                                                                       listComments.add(comment);
//                                                                       break;
//                                                                   case Constants.FREELANCE_WORKING:
//                                                                   case Constants.FREELANCE_FINISHED:
//                                                                       if (comment.isSelected()) {
//                                                                           listComments.add(comment);
//                                                                       }
//                                                                       break;
//                                                               }
//                                                           }
//
//
//                                                           Log.e(TAG, "getComment: " + comment.getComment());
//                                                           Log.e(TAG, "getFreelancerId: " + comment.getFreelancerId());
//
////                                                                           for (int i = 0; i < rv_comments.getChildCount(); i++) {
////                                                                               View viewGroup = rv_comments.getChildAt(i);
////                                                                               if (listComments.get(i).isSelected()) {
////                                                                                   viewGroup.findViewById(R.id.btn_prev).setVisibility(View.VISIBLE);
////                                                                                   viewGroup.findViewById(R.id.btn_accept).setVisibility(View.GONE);
////                                                                               } else {
////                                                                                   viewGroup.findViewById(R.id.btn_prev).setVisibility(View.GONE);
////                                                                                   viewGroup.findViewById(R.id.btn_accept).setVisibility(View.VISIBLE);
////                                                                               }
////                                                                           }
//
//                                                       }
//                                                   }
//
//
//                                                   StringBuilder stringBuilder = new StringBuilder();
////                                                   String txt1 = tv_post_offers.getText().toString();
//                                                   String txt1 = getString(R.string.post_offer);
//                                                   stringBuilder.append(txt1 + " ");
//                                                   String txt2 = getString(R.string.you_have) + " " + numberOfRealComments + " " +  getString(R.string.offer);
//                                                   SpannableString txtSpannable = new SpannableString(txt2);
//                                                   StyleSpan boldSpan = new StyleSpan(Typeface.NORMAL);
//                                                   txtSpannable.setSpan(boldSpan, 0, txt2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                                                   stringBuilder.append(txtSpannable);
//
//                                                   tv_post_offers.setText(stringBuilder, TextView.BufferType.SPANNABLE);
////                                                   tv_post_offers.setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
//
//                                                   rv_comments.setAdapter(new CommentFreelancerAdapter(getApplicationContext(),
//                                                           listComments));
//
//                                                   if (listComments.isEmpty()) {
////                                                                       rv_comments.setVisibility(View.GONE);
//                                                       llComments.setVisibility(View.GONE);
//                                                       tvNoCommentsYet.setVisibility(View.VISIBLE);
//                                                   } else {
////                                                                       rv_comments.setVisibility(View.VISIBLE);
//                                                       llComments.setVisibility(View.VISIBLE);
//                                                       tvNoCommentsYet.setVisibility(View.GONE);
//                                                   }
//                                               }
//                                           }
//
//                                           @Override
//                                           public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                           }
//                                       }
//                );
//
//
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                    }
////                });
//    }
//
//    @OnClick(R.id.iv_send_commnet)
//    public void sendCommentOfferToUser(View view) {
//
//        AlertDialog.Builder builder = new AlertDialog
//                .Builder(this, R.style.MyAlertDialogStyle)
//                .setTitle(getString(R.string.can_not_edit_offer))
//                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (orderRequest != null) {
//                            Comment comment = new Comment();
//                            if (etWriteComment.getText().toString().trim().length() != 0) {
//                                comment.setComment(etWriteComment.getText().toString());
//                                comment.setFreelancerId(Prefs.getString(Constants.FIREBASE_UID, ""));
//                                RefBase.refComments(orderRequest.getOrderId())
//                                        .push()
//                                        .setValue(comment)
//                                        .addOnSuccessListener(aVoid -> {
////                                Toasty.success(getApplicationContext(), "Sent success").show();
//                                            scrollView.fullScroll(View.FOCUS_DOWN);
//                                            etWriteComment.getEditableText().clear();
//
//                                            llAddComment.setVisibility(View.GONE);
//                                            tvOfferExist.setVisibility(View.VISIBLE);
//
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toasty.error(getApplicationContext(), e.getLocalizedMessage()).show();
//                                    }
//                                });
//
//                            }
//
//                        }
//                    }
//                });
//
//        builder.show();
//
//
//    }
//
//    @Override
//    public void OnAcceptClicked(Comment comment, int pos) {
//
//    }
//
//
//    public void getRate() {
//        if (orderRequest.getRate().length() != 1) {
//            RefBase.rate(orderRequest.getRate()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                    dataSnapshot.getRef().removeEventListener(this);
//                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                        Rate rate = dataSnapshot.getValue(Rate.class);
//                        if (rate != null) {
//                            ll_customer_order_rate.setVisibility(View.VISIBLE); //not finished
//                            rb_customer_order_rate.setRating(Float.parseFloat(rate.getNumberOfStars()));
//                            Log.e(TAG, "onDataChange: rate1 " + rate.getNumberOfStars());
//                        } else {
//                            ll_customer_order_rate.setVisibility(View.GONE);
//                            Log.e(TAG, "onDataChange: rate2 ");
//                        }
//                    } else {
//                        ll_customer_order_rate.setVisibility(View.GONE);
//                        Log.e(TAG, "onDataChange: rate3 ");
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        } else {
//            ll_customer_order_rate.setVisibility(View.GONE);
//            Log.e(TAG, " Test1 : ddddddd ");
//        }
//
//    }
//
//    public void getRateFromCustomer() {
//        RefBase.refRequests().
//                orderByChild(Constants.ORDER_STATE)
//                .equalTo(Constants.FREELANCE_FINISHED)
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        dataSnapshot.getRef().removeEventListener(this);
//                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                                OrderRequest orderRequest = dataSnapshot1.getValue(OrderRequest.class);
//                                Log.e(TAG, "onDataChange:333 " + orderRequest.getRate().length());
//                                if (orderRequest != null && orderRequest.getRate().length() != 1) {
////                                    Log.e(TAG, "onDataChange: "+ orderRequest.getRate() );
//                                    RefBase.rate(orderRequest.getRate()).addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
//                                                dataSnapshot.getRef().removeEventListener(this);
//                                                Rate rate = dataSnapshot.getValue(Rate.class);
//                                                if (rate != null) {
//                                                    ll_customer_order_rate.setVisibility(View.VISIBLE); //not finished
//                                                    rb_customer_order_rate.setRating(Float.parseFloat(rate.getNumberOfStars()));
//                                                    Log.e(TAG, "onDataChange: rate1 " + rate.getNumberOfStars());
//                                                } else {
//                                                    ll_customer_order_rate.setVisibility(View.GONE);
//                                                    Log.e(TAG, "onDataChange: rate2 ");
//
//                                                }
//                                            } else {
//                                                ll_customer_order_rate.setVisibility(View.GONE);
//                                                Log.e(TAG, "onDataChange: rate3 ");
//
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                } else {
//                                    ll_customer_order_rate.setVisibility(View.GONE);
//                                    Log.e(TAG, "onDataChange: rate4 ");
//
//                                }
//                            }
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//    }
//}