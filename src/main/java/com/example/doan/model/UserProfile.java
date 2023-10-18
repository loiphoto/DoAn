package com.example.doan.model;

import com.example.doan.Entity.User;
import com.example.doan.Utils.validator.CheckPhone;
import lombok.Data;

@Data
public class UserProfile {
//    private String password;
    @CheckPhone
    private String phone;
    private String address;
    private String name;
    private long wardId;

    public UserProfile() {
    }

    public UserProfile(String phone, String address, String name, long wardId) {
        this.phone = phone;
        this.address = address;
        this.name = name;
        this.wardId = wardId;
    }

    public UserProfile(User userCurrent) {
        phone = userCurrent.getPhone();
        address = userCurrent.getAddress();
        name = userCurrent.getName();
    }
}
