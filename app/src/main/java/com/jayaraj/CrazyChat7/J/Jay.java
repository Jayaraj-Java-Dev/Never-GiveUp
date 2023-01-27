package com.jayaraj.CrazyChat7.J;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/*

private Jay jay;
jay = new Jay(getSharedPreferences("MODE", Activity.MODE_PRIVATE));

public void _Bg() {
        //linearBackGround.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        if (FileUtil.isFile(FileUtil.getPackageDataDir(getApplicationContext()).concat("/bg.png"))) {
            imageviewBackGround.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(FileUtil.getPackageDataDir(getApplicationContext()).concat("/bg.png"), 1024, 1024));
        } else {
            imageviewBackGround.setImageResource(R.drawable.bgi);
        }
        Jay jay = new Jay(getSharedPreferences("MODE", Activity.MODE_PRIVATE));
        //linear1BG.setBackgroundColor(Color.parseColor(jay.col(0,2)));
        _SetGradientView(linear1BG,jay.col(0,3),jay.col(0,4));
        Window w = getWindow();
        w.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        w.setStatusBarColor(Color.parseColor(jay.col(0,3)));

    }

public void _SetGradientView(@NonNull final View _view, final String _color_a, final String _color_b) {
        _view.setBackground(new GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(_color_a), Color.parseColor(_color_b)}) { public GradientDrawable getIns(int a) { this.setCornerRadius(a);return this; } }.getIns((int)0));
    }

*/
public class Jay {
    public String MyWeb = "https://jay87coder.web.app/";
    public String PrivacyPolicy = "https://www.privacypolicies.com/live/dc7ddb16-169c-4124-aec5-93a7ef2629f8";
    private final SharedPreferences MODE;
    private final String EMOJI = "[{\"v\":\"\uD83D\uDE04\"},{\"v\":\"\uD83D\uDE0E\"},{\"v\":\"\uD83D\uDE01\"},{\"v\":\"\uD83D\uDE05\"},{\"v\":\"\uD83D\uDE02\"},{\"v\":\"\uD83E\uDD23\"},{\"v\":\"\uD83D\uDE2D\"},{\"v\":\"\uD83D\uDE09\"},{\"v\":\"\uD83D\uDE17\"},{\"v\":\"\uD83D\uDE18\"},{\"v\":\"\uD83E\uDD70\"},{\"v\":\"\uD83D\uDE0D\"},{\"v\":\"\uD83E\uDD29\"},{\"v\":\"\uD83E\uDD73\"},{\"v\":\"\uD83D\uDE0B\"},{\"v\":\"\uD83E\uDD2A\"},{\"v\":\"\uD83D\uDE07\"},{\"v\":\"\uD83D\uDE0F\"},{\"v\":\"\uD83D\uDE31\"},{\"v\":\"\uD83E\uDD2C\"},{\"v\":\"\uD83E\uDD10\"},{\"v\":\"\uD83E\uDD2F\"},{\"v\":\"\uD83E\uDD15\"},{\"v\":\"\uD83E\uDD13\"}]";

    public Jay(final SharedPreferences s) {
        this.MODE = s;
    }

    public boolean isDefaultEmoji() {
        return this.MODE.getString("EMOJI", "").equals("");
    }
    public ArrayList<HashMap<String, Object>> getDefaultEmoji() {
        return (new Gson().fromJson(this.EMOJI, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType()));
    }

    public ArrayList<HashMap<String, Object>> Emoji() {
        try{
            return (new Gson().fromJson(this.MODE.getString("EMOJI", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType()));
        } catch (final Exception e) {
            return (new Gson().fromJson(this.EMOJI, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType()));
        }
    }

    public String col(final int v, final int color) {
        //
        //
        final String COLOR;
        String vl;
        String vd;
        String f;
        String g1;
        final String g2;
        vl = this.MODE.getString("light", "#4D4D4D");
        vd = this.MODE.getString("dark", "#212121");
        f = this.MODE.getString("text", "#FFFFFF");
        g1 = this.MODE.getString("g1", "#212121");
        g2 = this.MODE.getString("g2", "#212121");

        if (v == 0) {
            switch (color) {
                case 1:
                    COLOR = vl;
                    break;
                case 2:
                    COLOR = vd;
                    break;
                case 3:
                    COLOR = g1;
                    break;
                case 4:
                    COLOR = g2;
                    break;
                default:
                    COLOR = "#212121";
            }
        } else {
            COLOR = f;
        }
        return COLOR;
    }
    public String getCurrentTheme() {
        String COLOR;
        String vl;
        String vd;
        String f;
        String g1;
        final String g2;
        vl = this.MODE.getString("light", "#4D4D4D");
        vd = this.MODE.getString("dark", "#212121");
        f = this.MODE.getString("text", "#FFFFFF");
        g1 = this.MODE.getString("g1", "#212121");
        g2 = this.MODE.getString("g2", "#212121");
        return(vl+"/"+vd+"/"+f+"/"+g1+"/"+g2);
    }
    public String getdefaultTheme() {
        return "#4D4D4D/#212121/#FFFFFF/#212121/#212121";
    }

    public void setTheme(final String thm){
        final String[] cols = this.parseCol(thm);
        this.MODE.edit().putString("light",cols[0]).apply();
        this.MODE.edit().putString("dark",cols[1]).apply();
        this.MODE.edit().putString("text",cols[2]).apply();
        this.MODE.edit().putString("g1",cols[3]).apply();
        this.MODE.edit().putString("g2",cols[4]).apply();
    }

    public String[] parseCol(final String thm) {
        if(thm.equals("")){
            return this.parseCol(this.getdefaultTheme());
        }
        String c1 = "",c2="",c3="",c4="",c5="";
        int num = 1;
        for(int i = 0;i<thm.length();i++){
            if(thm.charAt(i) == '/'){
                num++;
                continue;
            }
            if(num==1) {
                c1=c1.concat(String.valueOf(thm.charAt(i)));
            }
            if(num==2) {
                c2=c2.concat(String.valueOf(thm.charAt(i)));
            }
            if(num==3) {
                c3=c3.concat(String.valueOf(thm.charAt(i)));
            }
            if(num==4) {
                c4=c4.concat(String.valueOf(thm.charAt(i)));
            }
            if(num==5) {
                c5=c5.concat(String.valueOf(thm.charAt(i)));
            }
        }
        return new String[]{c1,c2,c3,c4,c5};
    }


}

/*



    private ObjectAnimator openAnim = new ObjectAnimator();
    private ObjectAnimator closeAnim = new ObjectAnimator();
    private LinearLayout bottom,btr1;
    private TextView btr1t1,btr1t2,btr1t3;
    private boolean isopen = false;

        //BOTTOM LAYOUTS
        bottom = findViewById(R.id.bottom);
        btr1 = findViewById(R.id.brow1);
        btr1t1 = findViewById(R.id.br1t1);
        btr1t2 = findViewById(R.id.br1t2);
        btr1t3 = findViewById(R.id.br1t3);

    public void _openBottom () {
        isopen = true;
        openAnim.setTarget(bottom);
        openAnim.setPropertyName("translationY");
        openAnim.setFloatValues((float)(-300));
        openAnim.setDuration((int)(500));
        openAnim.start();
    }


    public void _closeBottom () {
        isopen = false;
        closeAnim.setTarget(bottom);
        closeAnim.setPropertyName("translationY");
        closeAnim.setFloatValues((float)(0));
        closeAnim.setDuration((int)(500));
        closeAnim.start();
    }



 */