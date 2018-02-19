package luigi.casciaro.cityparty.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;
import com.kbeanie.imagechooser.exceptions.ChooserException;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmList;
import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.contract.AdCreateContract;
import luigi.casciaro.cityparty.controller.RealmController;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.model.AdImage;
import luigi.casciaro.cityparty.model.Category;
import luigi.casciaro.cityparty.util.MyDateUtil;
import luigi.casciaro.cityparty.util.MyDialogUtil;
import luigi.casciaro.cityparty.util.MyImageUtil;
import luigi.casciaro.cityparty.util.MyUtil;

public class AdCreateActivity extends BaseActivity implements ImageChooserListener, AdCreateContract {

    @BindView(R.id.editTextName)
    EditText editTextName;

    @BindView(R.id.editTextCategory)
    EditText editTextCategory;

    @BindView(R.id.editTextTypology)
    EditText editTextTypology;

    @BindView(R.id.editTextDate)
    EditText editTextDate;

    @BindView(R.id.editTextHashTags)
    EditText editTextHashTags;

    @BindView(R.id.editTextCity)
    EditText editTextCity;

    @BindView(R.id.editTextAddress)
    EditText editTextAddress;

    @BindView(R.id.editTextPhone)
    EditText editTextPhone;

    @BindView(R.id.editTextDescrizion)
    EditText editTextDescrizion;

    @BindView(R.id.textViewCamera)
    TextView textViewCamera;

    @BindView(R.id.textViewGallery)
    TextView textViewGallery;

    @OnClick(R.id.editTextDate)
    void openDriverDob(){
        datePicker.show();
    }

    @OnClick(R.id.editTextCategory)
    public void openCategories(){

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.text_select_category);
        b.setItems(categoriesArray, (dialog, which) -> {
            categorySelected = categories.get(which);
            editTextCategory.setText(categories.get(which).getName());
            dialog.dismiss();
        });
        b.show();
    }

    @OnClick(R.id.editTextTypology)
    public void openTypologies(){

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.text_select_typology);
        b.setItems(typologiesArray, (dialog, which) -> {
            typologySelected = typologies.get(which);
            editTextTypology.setText(typologiesArray[which]);
            dialog.dismiss();
        });
        b.show();
    }

    @OnClick(R.id.textViewGallery)
    public void onGallery(){
        isCamera = false;
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE, true);

        // multiple
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);

        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();

        checkPermissions();
    }

    @OnClick(R.id.textViewCamera)
    public void onCamera(){
        isCamera = true;
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);

        checkPermissions();
    }

    void checkPermissions() {
        // read, write and camera
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        } else {
            showImagePicker();
        }
    }

    void showImagePicker(){
        try {
            filePath = imageChooserManager.choose();
        } catch (ChooserException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.textViewSubmit)
    void addAd(){

        if (categorySelected == null){
            MyDialogUtil.showDialog(this, "Campo obbligatorio", "Selezionare categoria!");
            return;
        }

        if (typologySelected == null){
            MyDialogUtil.showDialog(this, "Campo obbligatorio", "Selezionare tipologia!");
            return;
        }

        if (date == null){
            MyDialogUtil.showDialog(this, "Campo obbligatorio", "Selezionare le date!");
            return;
        }

        if (images.size() == 0){
            MyDialogUtil.showDialog(this, "Immagine", "Selezionare almeno 1 immagine!");
            return;
        }

        Ad adToCreate = new Ad();

        adToCreate.setName(editTextName.getText().toString());
        adToCreate.setCategory(categorySelected);
        adToCreate.setCategoryName(categorySelected.getName());
        adToCreate.setEventType(typologySelected);
        adToCreate.setDateFrom(date);
        adToCreate.setDateTo(date);
        adToCreate.setImages(images);
        adToCreate.setHashTags(editTextHashTags.getText().toString());
        adToCreate.setCity(editTextCity.getText().toString());
        adToCreate.setAddress(editTextAddress.getText().toString());
        adToCreate.setNumberPhone(editTextPhone.getText().toString());
        adToCreate.setDescriptionEvent(editTextDescrizion.getText().toString());

        RealmController.addAdToPublisherUser(this, adToCreate);
    }

    String filePath;
    ImageChooserManager imageChooserManager;
    RealmList<AdImage> images = new RealmList();
    int imageIndex = 1;
    boolean isCamera = true;
    Category categorySelected;
    String typologySelected = Ad.EVENT_TYPE_FREE;
    Date date;

    ArrayList<Category> categories;
    ArrayList<String> typologies;
    String[] categoriesArray;
    String[] typologiesArray;

    DatePickerDialog datePicker;
    String[] dateArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_create);
        MyUtil.setToolbar(this);
        ButterKnife.bind(this);

        // fix exposed beyond app through ClipData.Item.getUri() by ImageChooserManager
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());

        categories = RealmController.getCategories();
        // array
        categoriesArray = new String[categories.size()];
        for (int i=0; i<categories.size(); i++) {
            categoriesArray[i] = categories.get(i).getName();
        }

        typologies = new ArrayList();
        typologies.add(Ad.EVENT_TYPE_FREE);
        typologies.add(Ad.EVENT_TYPE_PAID);
        // array
        typologiesArray = new String[typologies.size()];
        typologiesArray[0] = AppController.getInstance().getString(R.string.text_free);
        typologiesArray[1] = AppController.getInstance().getString(R.string.text_paid);

        date = new Date();
        dateArray = MyDateUtil.getTodayDateArray();

        // create date start picker
        if (MyUtil.isBrokenSamsungDevice()) {
            datePicker = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, dateDriverDobPickerListener, Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]) - 1, Integer.parseInt(dateArray[0]));
        } else {
            datePicker = new DatePickerDialog(this, dateDriverDobPickerListener, Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[1]) - 1, Integer.parseInt(dateArray[0]));
        }
    }

    /**
     * ImageChooserListener
     */
    @Override
    public void onImageChosen(ChosenImage chosenImage) {

        runOnUiThread(() -> {

            if (chosenImage != null) {

                Bitmap bitmap = MyImageUtil.getBitmapFromPath(chosenImage.getFileThumbnail());
                byte[] bytes = MyImageUtil.convertBitmapToByte(bitmap);

                images.add(new AdImage(chosenImage.getFileThumbnail(), bytes, imageIndex));

                // increment
                imageIndex = imageIndex + 1;

                if(isCamera) {
                    textViewCamera.setText("Immagini selezionate");
                    textViewCamera.setTypeface(null, Typeface.BOLD);
                }else{
                    textViewGallery.setText("Immagini selezionate");
                    textViewGallery.setTypeface(null, Typeface.BOLD);
                }
            }
        });
    }

    @Override
    public void onImagesChosen(ChosenImages chosenImages) {

        runOnUiThread(() -> {
            for (int i=0; i<chosenImages.size(); i++) onImageChosen(chosenImages.getImage(i));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            try {
                imageChooserManager.submit(requestCode, data);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Dare le permissions", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * AdCreateContract
     */
    @Override
    public void onAdCreated() {
        MyDialogUtil.showDialog_finishActivityOneButton(this, "Annuncio creato!", "Il tuo annuncio è stato creato con successo");
    }

    @Override
    public void onError(String reason) {
        runOnUiThread(() -> {
            Toast.makeText(this, reason, Toast.LENGTH_LONG).show();
        });
    }

    DatePickerDialog.OnDateSetListener dateDriverDobPickerListener = (view, year, monthOfYear, dayOfMonth) -> {

        if (dayOfMonth < 10) {
            dateArray[0] = "0" + String.valueOf(dayOfMonth);
        } else {
            dateArray[0] = String.valueOf(dayOfMonth);
        }

        if ((monthOfYear + 1) < 10) {
            dateArray[1] = "0" + String.valueOf(monthOfYear + 1);
        } else {
            dateArray[1] = String.valueOf(monthOfYear + 1);
        }

        if (year < 10) {
            dateArray[2] = "0" + String.valueOf(year);
        } else {
            dateArray[2] = String.valueOf(year);
        }

        // datetime start
        date = MyDateUtil.getDateFromArray(dateArray);
        // date start
        editTextDate.setText(dateArray[0] + "/" + dateArray[1] + "/" + dateArray[2]);
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}