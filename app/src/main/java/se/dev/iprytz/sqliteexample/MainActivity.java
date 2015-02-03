package se.dev.iprytz.sqliteexample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    EditText textInput;
    ListView listView;
    DBHandler myDBHandler;
    ArrayList<String> mProductsNames;
    ArrayAdapter<String> mArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInput = (EditText) findViewById(R.id.inputText);
        listView = (ListView) findViewById(R.id.list_view);
        myDBHandler = new DBHandler(this, null, null, 1);
        mProductsNames = new ArrayList<String>();
        updateProductListFromDB();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mProductsNames);
        listView.setAdapter(mArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String productName = String.valueOf(parent.getItemAtPosition(position));
                deleteProduct(productName);
            }
        });
    }

    public void addButtonClicked(View v) {

        String input = textInput.getText().toString();
        if (input != null && !("".equals(input))) {
            Product product = new Product(input);
            myDBHandler.addProduct(product);
            updateProductListFromDB();
        } else {
            Toast.makeText(this, "No Input!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteButtonClicked(View v) {
        String input = textInput.getText().toString();
        deleteProduct(input);
    }

    private void updateProductListFromDB() {
        mProductsNames.clear();
        mProductsNames.addAll(myDBHandler.databaseToStringArray());

        if (mArrayAdapter != null) {
            mArrayAdapter.notifyDataSetChanged();
        }
        textInput.setText("");
    }

    private void deleteProduct(String input){
        if (input != null && !("".equals(input))) {
            int rowsAffected = myDBHandler.deleteProduct(input);
            if (rowsAffected < 1) {
                Toast.makeText(this, "No match in DB", Toast.LENGTH_SHORT).show();
            } else {
                String rowDeleted = (rowsAffected > 1) ? " rows deleted" : " row deleted";
                Toast.makeText(this, rowsAffected + rowDeleted, Toast.LENGTH_SHORT).show();
            }
            updateProductListFromDB();
        } else {
            Toast.makeText(this, "No Input!", Toast.LENGTH_SHORT).show();
        }
    }

}
