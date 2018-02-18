package luigi.casciaro.cityparty.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.controller.RealmController;
import luigi.casciaro.cityparty.model.Ad;
import luigi.casciaro.cityparty.model.Category;
import luigi.casciaro.cityparty.util.MyDateUtil;
import luigi.casciaro.cityparty.util.MyUtil;

public class FilterActivity extends BaseActivity {

    public final static int FILTER_RESULT_CODE = 69;

    @BindView(R.id.radioAdType)
    RadioGroup radioAdType;

    @BindView(R.id.spinnerCategory)
    Spinner spinnerCategory;

    @BindView(R.id.spinnerCity)
    Spinner spinnerCity;

    @BindView(R.id.radioFree)
    RadioButton radioFree;

    @BindView(R.id.radioPaid)
    RadioButton radioPaid;

    @BindView(R.id.giorno)
    TextView giorno;

    @BindView(R.id.mese)
    TextView mese;

    @BindView(R.id.anno)
    TextView anno;

    @OnClick(R.id.cardData)
    void openDob(){
        datePicker.show();
    }

    @OnClick({R.id.radioFree, R.id.radioPaid, R.id.radioAll})
    public void onRadioButtonClicked(RadioButton radioButton) {

        // is the radio checked?
        boolean checked = radioButton.isChecked();

        // Check which radio button was clicked
        switch (radioButton.getId()) {
            case R.id.radioAll:
                if (checked) {
                    MyUtil.print("Radio -> all");
                    eventType = null;
                }
                break;
            case R.id.radioFree:
                if (checked) {
                    MyUtil.print("Radio -> free");
                    eventType = Ad.EVENT_TYPE_FREE;
                }
                break;
            case R.id.radioPaid:
                if (checked) {
                    MyUtil.print("Radio -> paid");
                    eventType = Ad.EVENT_TYPE_PAID;
                }
                break;
        }
    }

    DatePickerDialog datePicker;
    String[] date;

    String eventType;
    Date dateTime;
    Category category;
    String city;

    @OnClick(R.id.textViewSubmit)
    public void submit(){
        // hide
        MyUtil.hideKeyboard(this);
        // save
        AppController.getInstance().setFilterByEventType(eventType);
        AppController.getInstance().setFilterByDate(dateTime);
        AppController.getInstance().setFilterByCategory(category);
        AppController.getInstance().setFilterByCity(city);
        // go to
        Intent intent = new Intent();
        setResult(FILTER_RESULT_CODE, intent);
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        MyUtil.setToolbar(this);

        // tipo di evento -> radio group

        // data
        date = MyDateUtil.getTodayDateArray();

        // create date start picker
        if (MyUtil.isBrokenSamsungDevice()) {
            // |- owner
            datePicker = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, dateDobPickerListener, Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));
        } else {
            // |- owner
            datePicker = new DatePickerDialog(this, dateDobPickerListener, Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));
        }

        // categoria
        ArrayList<Category> categories = new ArrayList();
        categories.add(new Category("Nessuna", R.drawable.background_apericena));
        categories.addAll(RealmController.getCategories());
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(dataAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = categories.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // città
        ArrayList<String> cities = new ArrayList();
        cities.add("Nessuna");
        cities.addAll(RealmController.getCityOfAds());
        ArrayAdapter<String> cityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = cities.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // check active filter
        if(AppController.getInstance().isFilterByEventTypeSetted()){
            eventType = AppController.getInstance().getFilterByEventType();
            if(eventType.equalsIgnoreCase(Ad.EVENT_TYPE_FREE)) radioFree.setChecked(true);
            if(eventType.equalsIgnoreCase(Ad.EVENT_TYPE_PAID)) radioPaid.setChecked(true);
        }
        if(AppController.getInstance().isFilterByDateSetted()){
            dateTime = AppController.getInstance().getFilterByDate();
            date = MyDateUtil.getDateArrayFromDate(dateTime);
            giorno.setText(date[0]);
            mese.setText(date[1]);
            anno.setText(date[2]);
        }
        if(AppController.getInstance().isFilterByCategorySetted()){
            category = AppController.getInstance().getFilterByCategory();
            for (int i=0; i<categories.size(); i++)
                if(categories.get(i).getName().equalsIgnoreCase(category.getName()))
                    spinnerCategory.setSelection(i);
        }
        if(AppController.getInstance().isFilterByCitySetted()){
            city = AppController.getInstance().getFilterByCity();
            for (int i=0; i<cities.size(); i++)
                if(cities.get(i).equalsIgnoreCase(city))
                    spinnerCity.setSelection(i);
        }
    }

    /**
     * DatePicker Listeners
     */
    // owner -> DATE OF BIRTH
    DatePickerDialog.OnDateSetListener dateDobPickerListener = (view, year, monthOfYear, dayOfMonth) -> {

        if (dayOfMonth < 10) {
            date[0] = "0" + String.valueOf(dayOfMonth);
        } else {
            date[0] = String.valueOf(dayOfMonth);
        }

        if ((monthOfYear + 1) < 10) {
            date[1] = "0" + String.valueOf(monthOfYear + 1);
        } else {
            date[1] = String.valueOf(monthOfYear + 1);
        }

        if (year < 10) {
            date[2] = "0" + String.valueOf(year);
        } else {
            date[2] = String.valueOf(year);
        }

        // datetime
        dateTime = MyDateUtil.getDateFromArray(date);
        AppController.getInstance().setFilterByDate(dateTime);
        // date
        giorno.setText(date[0]);
        mese.setText(date[1]);
        anno.setText(date[2]);
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_reset){
            AppController.getInstance().resetFilter();
            // hide
            MyUtil.hideKeyboard(this);
            // go to
            Intent intent = new Intent();
            setResult(FILTER_RESULT_CODE, intent);
            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_down);
        }
        if(item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}