package top.alau.alcanderian.shoplist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    MyShopApp my_app;
    TextView t_price;
    TextView t_name;
    TextView t_type;
    TextView t_info;
    Button b_addcart;
    Button b_back;
    Button b_star;
    ImageView i_img;
    ListView lv_more;
    ListView lv_op;
    int idx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        my_app = (MyShopApp) getApplication();
        int iid = getIntent().getIntExtra("itemid", 0);

        idx = my_app.good_data.getIndex(iid);
        t_price = (TextView) findViewById(R.id.price);
        t_name = (TextView) findViewById(R.id.name);
        t_type = (TextView) findViewById(R.id.type);
        t_info = (TextView) findViewById(R.id.ifo);
        b_addcart = (Button) findViewById(R.id.addcart);
        b_back = (Button) findViewById(R.id.back);
        b_star = (Button) findViewById(R.id.star);
        i_img = (ImageView) findViewById(R.id.itemimg);
        lv_more = (ListView) findViewById(R.id.more);
        lv_op = (ListView) findViewById(R.id.operation);

        t_price.setText(my_app.good_data.data.get(idx).get("price").toString());
        t_name.setText(my_app.good_data.data.get(idx).get("name").toString());
        t_type.setText(my_app.good_data.data.get(idx).get("type").toString());
        t_info.setText(my_app.good_data.data.get(idx).get("info").toString());
        i_img.setImageResource(my_app.good_data.imgid.get(idx));

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update_star();
        b_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int val = my_app.good_data.stared.get(idx);
                if (val == 1) val = 0;
                else val = 1;
                my_app.good_data.stared.set(idx, val);
                update_star();
            }
        });
        b_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_app.cart_data.add(
                        my_app.good_data.data.get(idx),
                        my_app.good_data.stared.get(idx),
                        my_app.good_data.itemid.get(idx),
                        my_app.good_data.imgid.get(idx)
                );
                my_app.lvad.notifyDataSetChanged();
                Toast.makeText(DetailActivity.this,
                        "商品已经添加到购物车", Toast.LENGTH_SHORT).show();
            }
        });
        final String[] s_more = {"更多产品信息"};
        final String[] s_op = {"一键下单", "分享产品", "不感兴趣", "查看更多产品促销消息"};
        lv_more.setAdapter(new ArrayAdapter<>(DetailActivity.this, R.layout.view_text, s_more));
        lv_op.setAdapter(new ArrayAdapter<>(DetailActivity.this, R.layout.view_text, s_op));
    }

    public void update_star() {
        if (my_app.good_data.stared.get(idx) == 0) {
            b_star.setBackgroundResource(R.mipmap.empty_star);
        } else {
            b_star.setBackgroundResource(R.mipmap.full_star);
        }
    }
}
