package com.iss.auth.adapter;

import com.iss.auth.domain.po.User;

public interface Login3rdAdapter {
    User login(String authCode);
}
