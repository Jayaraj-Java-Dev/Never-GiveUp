

package com.jayaraj.CrazyChat7.J;

import androidx.annotation.*;

import java.util.*;

import java.util.HashMap;
import java.util.ArrayList;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ChildEventListener;

import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;


public class getData {
    private final FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private DatabaseReference db = this._firebase.getReference("Receiver");

    private final ChildEventListener _db_child_listener;
    private ChildEventListener _dbSend_child_listener;
    private ChildEventListener _dbRand_child_listener;
    private ChildEventListener _dbOnOff_child_listener;
    private SharedPreferences msg;
    private SharedPreferences list;
    private SharedPreferences asp;
    private SharedPreferences imgurls;
    private final SharedPreferences ChatList;
    private SharedPreferences ChatRef;
    private final FirebaseAuth auth;
    private final String uidh = "";
	private ArrayList<HashMap<String, Object>> nlistmap = new ArrayList<>();

	public getData(String uid, Context appc, SharedPreferences spchat) {

        this.ChatList = spchat;

        this.auth = FirebaseAuth.getInstance();
        this.db = this._firebase.getReference("Profile/uid/".concat(uid));

        this._db_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot _param1, final String _param2) {
                final GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                String _childKey = _param1.getKey();
                HashMap<String, Object> _childValue = _param1.getValue(_ind);

                getData.this.nlistmap.clear();
				final String Tmp = getData.this.ChatList.getString("list", "");
				int N=-1;
                String NAME = "Not Found";
                String PHONE = "Not Found";
                String URL = "NOT Found";
                String UI = "NOT Found";
                boolean pb = false;


				assert _childValue != null;
				if (_childValue.containsKey("NAME")) {
                    NAME = Objects.requireNonNull(_childValue.get("NAME")).toString();
                    if (!getData.this.ChatList.getString(uid.concat("n"), "").equals(NAME)) {
                        getData.this.ChatList.edit().putString(Objects.requireNonNull(_childValue.get("CID")).toString().concat("n"), NAME).apply();
                        pb = true;
                    }
                } else {
                }
                if (_childValue.containsKey("PHONE")) {
                    PHONE = Objects.requireNonNull(_childValue.get("PHONE")).toString();
                    if (!getData.this.ChatList.getString(uid.concat("p"),"").equals(PHONE)){
                        getData.this.ChatList.edit().putString(Objects.requireNonNull(_childValue.get("CID")).toString().concat("p"), PHONE).apply();
                        pb = true;
                    }
                } else {
                }
                if (_childValue.containsKey("URL")) {
                    URL = Objects.requireNonNull(_childValue.get("URL")).toString();
                    if (!getData.this.ChatList.getString(uid.concat("i"),"").equals(URL)) {
                        getData.this.ChatList.edit().putString(Objects.requireNonNull(_childValue.get("CID")).toString().concat("i"), URL).apply();
                        pb = true;
                    }
                } else {
                }
                if (_childValue.containsKey("UI")) {
                    UI = Objects.requireNonNull(_childValue.get("UI")).toString();
                    getData.this.ChatList.edit().putString(Objects.requireNonNull(_childValue.get("CID")).toString().concat("ui"), UI).apply();
                } else {
                    getData.this.ChatList.edit().putString(Objects.requireNonNull(_childValue.get("CID")).toString().concat("ui"), "#4D4D4D/#212121/#FFFFFF/#212121/#212121").apply();
                }
                if(pb) {
                    if ("".equals(Tmp)) {
                        final HashMap<String, Object> _item = new HashMap<>();
                        _item.put("uid", Objects.requireNonNull(_childValue.get("CID")).toString());
                        getData.this.nlistmap.add(_item);
                        N = 0;
                    } else {
                        getData.this.nlistmap = new Gson().fromJson(Tmp, new TypeToken<ArrayList<HashMap<String, Object>>>() {
                        }.getType());
                        for (int i = 0; i < getData.this.nlistmap.size(); i++) {
                            if (Objects.requireNonNull(getData.this.nlistmap.get(i).get("uid")).toString().equals(Objects.requireNonNull(_childValue.get("CID")).toString())) {
                                N = i;
                                break;
                            }
                        }
                    }

                    if (N != -1) {
                        getData.this.nlistmap.get(N).put("img", URL);
                        getData.this.nlistmap.get(N).put("name", NAME);
                        getData.this.nlistmap.get(N).put("phone", PHONE);
                        getData.this.ChatList.edit().putString("list", new Gson().toJson(getData.this.nlistmap)).apply();
                    }
                    else
                    {
                        SketchwareUtil.showMessage(appc,"User Was Not Found in your list");
                    }
                }
                getData.this.db.removeEventListener(getData.this._db_child_listener);

            }

            @Override
            public void onChildChanged(@NonNull final DataSnapshot _param1, final String _param2) {

            }

            @Override
            public void onChildMoved(@NonNull final DataSnapshot _param1, final String _param2) {

            }

            @Override
            public void onChildRemoved(@NonNull final DataSnapshot _param1) {

            }

            @Override
            public void onCancelled(@NonNull final DatabaseError _param1) {

            }
        };
        this.db.addChildEventListener(this._db_child_listener);

    }

}
