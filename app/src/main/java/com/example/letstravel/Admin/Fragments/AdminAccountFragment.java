package com.example.letstravel.Admin.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letstravel.LoginActivity;
import com.example.letstravel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAccountFragment newInstance(String param1, String param2) {
        AdminAccountFragment fragment = new AdminAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseAuth firebaseAuth;

    String mUID = "uid";
    private ProgressDialog pd;

    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    String storagePath = "profile_images/";
    String profileorCoverPhoto;

    private Uri image_uri_camera, image_uri_gallery;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    //permission arrays
    private String[] cameraPermissions;
    private String[] storagePermissions;

    private ImageView profileIv;
    private TextView nameTv, emailTv, phoneTv, genderTv, editAdminInfoFab;
    private ImageButton backBtn;
    private TextView logoutBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_account, container, false);

        pd = new ProgressDialog(getActivity());
        pd.setTitle("Please Wait");
        pd.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        logoutBtn = view.findViewById(R.id.logoutBtn);

        profileIv = view.findViewById(R.id.profileIv);
        nameTv = view.findViewById(R.id.nameTv);
        emailTv = view.findViewById(R.id.emailTv);
        phoneTv = view.findViewById(R.id.phoneTv);
        genderTv = view.findViewById(R.id.genderTv);

        editAdminInfoFab = view.findViewById(R.id.editUserInfoFab);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        editAdminInfoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditProfileDialog();
            }
        });

        checkUserStatus();
        return view;
    }

    private void showEditProfileDialog() {
        String options[] = {"Edit Profile Picture", "Edit Name", "Edit Phone", "Change Password"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0) {
                    pd.setMessage("Updating Profile Picture");
                    profileorCoverPhoto = "image";
                    showImagePicDialog();
                } else if (which == 1) {
                    pd.setMessage("Updating Name");
                    showNamePhoneUpdateDialog("name");
                } else if (which == 2) {
                    pd.setMessage("Updating Phone");
                    showNamePhoneUpdateDialog("phone");
                } else if (which == 3) {
                    pd.setMessage("Changing Password ");
                    showChangePasswordDialog();
                }
            }
        });
        builder.create().show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Camera & Storage permissions, requests and setting Data//

    private void showImagePicDialog() {

        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            //camera clicked
                            if (checkCameraPermission()) {
                                pickFromCamera();
                            } else {
                                requestCameraPermission();
                            }
                        } else {
                            //gallery clicked
                            if (checkStoragePermission()) {
                                pickFromGallery();
                            } else {
                                requestStoragePermission();
                            }
                        }
                    }
                }).show();

    }

    private void pickFromCamera() {
//        ContentValues cv = new ContentValues();
//        cv.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
//        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");
//
//        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
//        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setActivityTitle("Crop Image")
                .setFixAspectRatio(true)
                .setCropMenuCropButtonTitle("Done")
                .start(getContext(), this);

    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Permission request and Activity result//


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Camera permission is required...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Storage permission is required...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
//                image_uri = data.getData();
//                profileIv.setImageURI(image_uri);
//                uploadProfileCoverPhoto(image_uri);

                image_uri_gallery = data.getData();
                CropImage.activity(image_uri_gallery)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setActivityTitle("Crop Image")
                        .setFixAspectRatio(true)
                        .setCropMenuCropButtonTitle("Done")
                        .start(getContext(), this);
            }
//            else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
//                //profileIv.setImageURI(image_uri);
//                uploadProfileCoverPhoto(image_uri);
//            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                image_uri_camera = result.getUri();
                uploadProfileCoverPhoto(image_uri_camera);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception e = result.getError();
                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();
            }

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                uploadProfileCoverPhoto(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {
        pd.show();
        /*Instead of creating separate function for Profile Picture and Cover Photo
        i'm doing work for both in same function
        To add check 1 - add a string variable and assign it value "image" when user clicks
        * "Edit Profile Pic", and assign it value "cover" in user clicks "Edit cover Photo"
        * Here: image is the key in each user containing uil of users profile picture
         cover is the key in each user containing url users cover photo */

        String filePathAndName = storagePath + "" + profileorCoverPhoto + "_" + user.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadUri = uriTask.getResult();

                if (uriTask.isSuccessful()) {
                    HashMap<String, Object> results = new HashMap<>();
                    results.put(profileorCoverPhoto, downloadUri.toString());

                    databaseReference.child(user.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Image Updated", Toast.LENGTH_SHORT).show();
                                    loadUserData();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Error Updating Image", Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    pd.show();
                    Toast.makeText(getActivity(), "Some error occurred", Toast.LENGTH_SHORT).show();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNamePhoneUpdateDialog(final String key) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Update " + key);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter" + key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value)) {
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                } else {
                    Toast.makeText(getActivity(), "Please enter " + key, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showChangePasswordDialog() {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_password, null);
        final EditText passwordEt = view.findViewById(R.id.passwordEt);
        final EditText newPasswordEt = view.findViewById(R.id.newPasswordEt);
        Button updatePasswordBtn = view.findViewById(R.id.updatePasswordBtn);

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setView(view);

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = passwordEt.getText().toString().trim();
                String newPassword = newPasswordEt.getText().toString().trim();
                if (TextUtils.isEmpty(oldPassword)) {
                    Toast.makeText(getActivity(), "Enter your current password...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.length() < 6) {
                    Toast.makeText(getActivity(), "Password length must atleast 6 characters...", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                updatePassword(oldPassword, newPassword);
            }
        });
    }

    private void updatePassword(String oldPassword, String newPassword) {
        pd.show();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void loadUserData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String name = "" + ds.child("name").getValue();
                            String email = "" + ds.child("email").getValue();
                            String phone = "" + ds.child("phone").getValue();
                            String deliveryAddress = "" + ds.child("address").getValue();
                            String profileImage = "" + ds.child("image").getValue();

                            nameTv.setText(name);
                            emailTv.setText(email);
                            phoneTv.setText(phone);

                            try {
                                Picasso.get().load(profileImage).placeholder(R.drawable.ic_person_white).into(profileIv);
                            } catch (Exception e) {
                                profileIv.setImageResource(R.drawable.admin_person_bg);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            mUID = user.getUid();
            loadUserData();

        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }

}