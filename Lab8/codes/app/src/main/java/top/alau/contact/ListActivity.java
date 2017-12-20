package top.alau.contact;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {
    private ContactDb dbo;
    private Button btn_add;
    private ListView lv_contact;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (Permission.verify(ListActivity.this,
                Manifest.permission.READ_CONTACTS, 0))
            runActivity();
    }

    private void runActivity() {
        dbo = new ContactDb(ListActivity.this);
        btn_add = findViewById(R.id.btn_add);
        adapter = new ContactAdapter(ListActivity.this, dbo.queryAll());
        lv_contact = findViewById(R.id.contact_list);
        lv_contact.addHeaderView(LayoutInflater.from(ListActivity.this).inflate(R.layout.list_header, null));
        lv_contact.setAdapter(adapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, AddActivity.class);
                startActivityForResult(intent, 1000);
            }
        });

        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final ContactItem item = adapter.getItem(position);
                final View ad_edit = LayoutInflater.from(ListActivity.this).inflate(R.layout.dialog_style, null);
                ((TextView) ad_edit.findViewById(R.id.txt_name)).setText(item.name);
                ((TextView) ad_edit.findViewById(R.id.et_birth)).setText(item.birth);
                ((TextView) ad_edit.findViewById(R.id.et_gift)).setText(item.gift);
                ((TextView) ad_edit.findViewById(R.id.txt_phone)).setText(getTelByName(item.name));

                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setView(ad_edit)
                        .setPositiveButton("确认修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                item.birth = ((TextView) ad_edit.findViewById(R.id.et_birth)).getText().toString();
                                item.gift = ((TextView) ad_edit.findViewById(R.id.et_gift)).getText().toString();
                                dbo.update(item.cid, item.name, item.birth, item.gift);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("放弃修改", null)
                        .show();
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                final ContactItem item = adapter.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setTitle(String.format("是否删除%s？", item.name))
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbo.delete(item.cid);
                                adapter.data.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
            }
        });
    }

    private String getTelByName(String name) {
        try {
            Cursor cursor = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    String.format("%s = '%s'", ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, name),
                    null, null
            );
            StringBuilder ret = new StringBuilder();
            while (cursor.moveToNext()) {
                ret.append(cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                )));
                if (!cursor.isLast())
                    ret.append('\n');
            }
            cursor.close();
            String s = ret.toString();
            return s.isEmpty() ? "无" : s;
        } catch (Exception e) {
            e.printStackTrace();
            return "无";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            System.exit(0);
        else
            runActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1000 && data.hasExtra("cid")) {
            adapter.data.add(dbo.select(data.getIntExtra("cid", -1)));
            adapter.notifyDataSetChanged();
        }
    }
}
