package com.example.doan.model;

import com.example.doan.Entity.User;
import lombok.Data;

@Data
public class UserOrderSave {
    private String phone;

    private String address;

    private String name;

    private long wardId;

    public UserOrderSave() {
    }

    public UserOrderSave(User user) {
        this.phone= user.getPhone();
        this.address=user.getAddress();
        this.name = user.getName();
    }

    public UserOrderSave(String phone, String address, String name, long wardId) {
        this.phone = phone;
        this.address = address;
        this.name = name;
        this.wardId = wardId;
    }
}
