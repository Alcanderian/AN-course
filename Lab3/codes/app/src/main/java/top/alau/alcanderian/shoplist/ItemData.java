package top.alau.alcanderian.shoplist;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemData {

    public ArrayList<Map<String, Object>> data;
    public ArrayList<Integer> stared;
    public ArrayList<Integer> itemid;
    public ArrayList<Integer> imgid;
    String[] name = {"Enchated Forest", "Arla Milk", "Devondale Milk", "Kindle Oasis",
            "waitrose 早餐麦片", "Mcvitie's 饼干", "Ferrero Rocher", "Maltesers", "Lindt",
            "Borggreve"};
    String[] price = {"¥5.00", "¥59.00", "¥79.00", "¥2399.00", "¥179.00", "¥14.90",
            "¥132.59", "¥141.43", "¥139.43", "¥28.90"};
    String[] type = {"作者", "产地", "产地", "版本", "重量", "产地", "重量", "重量", "重量",
            "重量"};
    String[] info = {"Johanna Basford", "德国", "澳大利亚", "8GB", "2Kg", "英国", "300g",
            "118g", "249g", "640g"};
    String[] first = {"E", "A", "D", "K", "W", "M", "F", "M", "L", "B"};
    Integer[] resimg = {R.mipmap.enchated_forest, R.mipmap.arla, R.mipmap.devondale,
            R.mipmap.kindle, R.mipmap.waitrose, R.mipmap.mcvitie, R.mipmap.ferrero,
            R.mipmap.maltesers, R.mipmap.lindt, R.mipmap.borggreve};

    public ItemData(boolean what) {
        data = new ArrayList<>();
        stared = new ArrayList<>();
        itemid = new ArrayList<>();
        imgid = new ArrayList<>();
        if (what) {
            for (int i = 0; i < name.length; ++i) {
                stared.add(0);
                itemid.add(i);
                imgid.add(resimg[i]);
                Map<String, Object> tmp = new LinkedHashMap<>();
                tmp.put("name", name[i]);
                tmp.put("price", price[i]);
                tmp.put("type", type[i]);
                tmp.put("info", info[i]);
                tmp.put("first", first[i]);
                data.add(tmp);
            }
        } else {
            Map<String, Object> tmp = new LinkedHashMap<>();
            stared.add(-1);
            itemid.add(-1);
            imgid.add(-1);
            tmp.put("name", "购物车");
            tmp.put("price", "价格");
            tmp.put("type", "title");
            tmp.put("info", "title");
            tmp.put("first", "*");
            data.add(tmp);
        }
    }

    public void remove(int pos) {
        data.remove(pos);
        stared.remove(pos);
        itemid.remove(pos);
        imgid.remove(pos);
    }

    public void add(Map<String, Object> m, int st, int iid, int im_id) {
        data.add(m);
        stared.add(st);
        itemid.add(iid);
        imgid.add(im_id);
    }

    public int getIndex(int iid) {
        return itemid.indexOf(iid);
    }
}
