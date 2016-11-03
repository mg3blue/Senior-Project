package layout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.seniorproject.mainseniorprojectpart.R;

public class AccountPage extends Fragment {

    private ImageView AccountimageView;
    View inflatedView = null;
    private static final int PICK_IMAGE = 100;

    public AccountPage() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        this.inflatedView = inflater.inflate(R.layout.fragment_account_page, container, false);

        AccountimageView = (ImageView) inflatedView.findViewById(R.id.account_Photo);

        AccountimageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        return inflatedView;
    }


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            AccountimageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            cursor.close();
        } else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}
