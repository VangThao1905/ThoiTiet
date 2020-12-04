package vangthao.app.thoitiet.viewmodel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseSingleton {
    private static DatabaseReference myDatabase = null;

    private FirebaseDatabaseSingleton(){}

    public static DatabaseReference getInstance(){
        if(myDatabase == null){
            myDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return myDatabase;
    }
}
