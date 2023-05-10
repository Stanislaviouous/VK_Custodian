package com.example.custodian.exout;

import com.example.custodian.data.SFile;

import java.io.File;

public interface OnSFileSelectListener {
    void onSFilePressed(SFile file);
    void onSFileLongPressed(SFile file);
}