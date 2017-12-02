package com.berstek.veripay.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileUploader {

    public void openFileChooser(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooserIntent = Intent.createChooser(intent, "Select File");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        activity.startActivityForResult(chooserIntent, IntentMarker.RC_OPEN_FILE);
    }

    private StorageReference reference;

    public FileUploader() {
        reference = FirebaseStorage.getInstance().getReference();
    }

    public UploadTask uploadFile(Intent data,
                                 Activity activity) throws FileNotFoundException {
        UploadTask uploadTask = null;

        if (data != null) {

            Uri selectedImageUri = data.getData();
            String file_path = RealFilePath.getPath(activity, selectedImageUri);
            String[] file_names = file_path.split("/");
            String file_name = file_names[file_names.length - 1];
            StorageReference uploadRef = reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            InputStream inputStream = activity.getContentResolver().openInputStream(data.getData());
            if (inputStream != null) {
                uploadTask = uploadRef.child(file_name).putStream(inputStream);
            }
        }

        return uploadTask;
    }
}
