package top.alau.alcanderian.cloudi;

import android.content.*;
import android.support.design.widget.*;
import android.support.v7.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    ImageView i_image;
    Button b_sign_in, b_sign_up;
    RadioButton rb_stu, rb_tec;
    TextInputLayout txt_id, txt_pwd;
    AlertDialog.Builder ad_builder;
    String[] items = {"拍摄", "从相册选择"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        i_image = (ImageView) findViewById(R.id.icon);
        b_sign_in = (Button) findViewById(R.id.sign_in);
        b_sign_up = (Button) findViewById(R.id.sign_up);

        rb_stu = (RadioButton) findViewById(R.id.as_student);
        rb_tec = (RadioButton) findViewById(R.id.as_teacher);

        txt_id = (TextInputLayout) findViewById(R.id.txt_student_id);
        txt_pwd = (TextInputLayout) findViewById(R.id.txt_passwd);

        ad_builder = new AlertDialog.Builder(MainActivity.this);
        ad_builder
                .setTitle("上传图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        toastShow(MainActivity.this, "您选择了[" + items[i] + "]");
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        toastShow(MainActivity.this, "您选择了[取消]");
                    }
                }).create();

        i_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad_builder.show();
            }
        });

        b_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = txt_id.getEditText().getText().toString();
                String pwd = txt_pwd.getEditText().getText().toString();
                String notice;

                txt_id.setErrorEnabled(false);
                txt_pwd.setErrorEnabled(false);
                if (id.isEmpty()) {
                    txt_id.setErrorEnabled(true);
                    txt_id.setError("学号不能为空");
                } else if (pwd.isEmpty()) {
                    txt_pwd.setErrorEnabled(true);
                    txt_pwd.setError("密码不能为空");
                } else {
                    if (id.equals("123456") && pwd.equals("6666"))
                        notice = "登陆成功";
                    else
                        notice = "学号或密码错误";
                    snackShow(v, notice);
                }
            }
        });

        b_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb_stu.isChecked())
                    snackShow(v, "学生注册功能尚未启用");
                if (rb_tec.isChecked())
                    toastShow(MainActivity.this, "教职工注册功能尚未启用");
            }
        });

        rb_stu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackShow(v, "您选择了学生");
            }
        });

        rb_tec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackShow(v, "您选择了教职工");
            }
        });
    }

    protected void snackShow(View v, CharSequence cs) {
        Snackbar
                .make(v, cs, Snackbar.LENGTH_SHORT)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toastShow(MainActivity.this, "Snackbar的确定按钮被点击了");
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                .setDuration(5000)
                .show();
    }

    protected void toastShow(Context ctx, CharSequence cs) {
        Toast.makeText(ctx, cs, Toast.LENGTH_SHORT).show();
    }
}
