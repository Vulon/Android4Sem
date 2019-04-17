package com.example.myanimeschedule.Utills;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FileManager implements Serializable{

    private static final String filename = "authData";
    private static final long serialVersionUID = -4228462333579246283L;
    HTTPMessenger messenger;

    public boolean saveData(Context context){
        messenger = HTTPMessenger.getInstance(context);
        boolean result  = true;
        try{
            FileOutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);

            AuthDataObject data = new AuthDataObject(messenger.getUsername(), messenger.getUserkey(),
                    messenger.getToken(), messenger.getTokenExpDate().getTime());
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
            objStream.writeObject(data);
            Log.i("FILE WRITE", "WRITTEN BYTES");
            out.write(byteStream.toByteArray());
            out.close();

        }catch (Exception e){
            result = false;
            Log.i("FILE SAVE ERROR", e.toString());
        }
        return result;
    }
    public boolean loadData(Context context){
        messenger = HTTPMessenger.getInstance(context);
        for (String s:context.fileList()) {
            Log.i("FILE FOUND", s);
        }
        boolean result = true;
        try{
            FileInputStream in = context.openFileInput(filename);
            ObjectInputStream objSream = new ObjectInputStream(in);
            AuthDataObject data = (AuthDataObject) objSream.readObject();
            messenger.loadData(data.username, data.userkey, data.token, data.expDate);
            in.close();
        }catch (Exception e){
            Log.e("AUTH DATA LOAD FAIL", e.toString());
            result = false;
        }

        return result;
    }
    private class AuthDataObject implements Serializable {

        private static final long serialVersionUID = 6429966187405578163L;
        public String username;
        public String userkey;
        public String token;
        public long expDate;
        public AuthDataObject(String username, String userkey, String token, long expDate) {
            this.username = username;
            this.userkey = userkey;
            this.token = token;
            this.expDate = expDate;
        }

        public AuthDataObject(){

        }
    }
}
