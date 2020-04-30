package com.bourgeois.lister;
import java.util.Date;

class Listing {

    private String title;
    private Integer price;
    private String desc;
    private String uid;
    private Date posted;
    private String email;

    // No-argument constructor is required to support conversion of Firestore document to POJO
    public Listing() {}

    // All-argument constructor is required to support conversion of Firestore document to POJO
    public Listing(String title, Integer price, String desc, String uid, Date posted, String email) {
        this.title = title;
        this.price = price;
        this.desc = desc;
        this.uid = uid;
        this.posted = posted;
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPrice() {
        return price;
    }

    public String getDesc() {
        return desc;
    }

    public String getUID(){
        return uid;
    }

    public Date getPosted(){
        return posted;
    }

    public String getEmail(){
        return email;
    }
}
