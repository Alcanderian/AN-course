package top.alau.contact;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    private Button btn_add;
    private EditText et_name;
    private EditText et_birth;
    private EditText et_gift;
    private ContactDb dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbo = new ContactDb(AddActivity.this);
        btn_add = findViewById(R.id.btn_add);
        et_name = findViewById(R.id.et_name);
        et_birth = findViewById(R.id.et_birth);
        et_gift = findViewById(R.id.et_gift);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                if (name.trim().isEmpty()) {
                    Toast.makeText(AddActivity.this, "名字不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Integer cid = dbo.insert(name, et_birth.getText().toString(), et_gift.getText().toString());
                    Intent intent = new Intent(AddActivity.this, ListActivity.class);
                    intent.putExtra("cid", cid);
                    setResult(1000, intent);
                    finish();
                }
            }
        });
    }
}
