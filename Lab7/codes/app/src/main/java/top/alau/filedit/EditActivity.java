package top.alau.filedit;

import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class EditActivity extends AppCompatActivity {
    private TextInputLayout til_filename;
    private TextInputLayout til_filectx;
    private Button btn_save;
    private Button btn_load;
    private Button btn_clear;
    private Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        til_filename = findViewById(R.id.til_filename);
        til_filectx = findViewById(R.id.til_filectx);
        btn_save = findViewById(R.id.btn_save);
        btn_load = findViewById(R.id.btn_load);
        btn_clear = findViewById(R.id.btn_clear);
        btn_delete = findViewById(R.id.btn_delete);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                til_filectx.getEditText().setText("");
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = til_filename.getEditText().getText().toString().trim();
                String filectx = til_filectx.getEditText().getText().toString();

                if (checkFilename(filename)) {
                    try (FileOutputStream out = openFileOutput(filename, MODE_PRIVATE)) {
                        out.write(filectx.getBytes());
                        out.close();
                        makeToast("Save successfully");
                    } catch (Exception e) {
                        e.printStackTrace();
                        makeToast("Failed to save file");
                    }
                }
            }
        });

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = til_filename.getEditText().getText().toString().trim();

                if (checkFilename(filename)) {
                    try (FileInputStream in = openFileInput(filename)) {
                        byte[] context = new byte[in.available()];
                        in.read(context);
                        til_filectx.getEditText().setText(new String(context));
                        in.close();
                        makeToast("Save successfully");
                    } catch (Exception e) {
                        e.printStackTrace();
                        makeToast("Failed to load file");
                    }
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = til_filename.getEditText().getText().toString().trim();

                if (checkFilename(filename)) {
                    Boolean ok = deleteFile(filename);
                    if (ok)
                        makeToast("Delete successfully");
                    else
                        makeToast("Failed to delete file");

                }
            }
        });
    }

    private Boolean checkFilename(String filename) {
        if (filename.isEmpty()) {
            makeToast("File name cannot be empty");
            return false;
        } else if (filename.contains("/")) {
            makeToast("File name cannot contains \"/\"");
            return false;
        }
        return true;
    }

    private void makeToast(String content) {
        Toast.makeText(EditActivity.this, content, Toast.LENGTH_SHORT).show();
    }
}
