package com.example.a5lites;

import android.provider.DocumentsContract;

import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;

public class RegisterNode {

    public static Document registerUSer(String phone, String status, String MACid){
        Document doc = new Document();
        doc.put("MACid", MACid);
        doc.put("phone", phone);
        doc.put("status", status);
        return doc;
    }


}
