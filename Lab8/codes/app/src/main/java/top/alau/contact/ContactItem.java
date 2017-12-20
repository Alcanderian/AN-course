package top.alau.contact;

/**
 * Created by alcanderian on 2017/12/19.
 */

public class ContactItem {
    public Integer cid;
    public String name;
    public String birth;
    public String gift;

    public ContactItem(Integer cid, String name, String birth, String gift) {
        this.cid = cid;
        this.name = name;
        this.birth = birth;
        this.gift = gift;
    }
}
