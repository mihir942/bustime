package com.pmapps.bustime.Logic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.pmapps.bustime.Enums.Decker;
import com.pmapps.bustime.Enums.Load;
import com.pmapps.bustime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logic {

    public static Drawable imgForDecker(Context context, Decker decker) {

        Drawable img = null;
        if (decker == Decker.SINGLE) {
            img = null;
            img = ResourcesCompat.getDrawable(context.getResources(), R.drawable.sd_bus_icon, null);
        } else if (decker == Decker.DOUBLE) {
            img = ResourcesCompat.getDrawable(context.getResources(), R.drawable.dd_bus_icon, null);
        } else {
            img = null;
        }
        return img;
    }

    public static int colorForLoad(Load load) {

        int color = 0;

        if (load == Load.LIGHT) {
            color = Color.parseColor("#FF00AB07");
        } else if (load == Load.MEDIUM) {
            color = Color.parseColor("#FFE77D00");
        } else if (load == Load.HEAVY) {
            color = Color.parseColor("#FFE70000");
        }

        return color;
    }

    //------------------------------------------------------------------------------------------------

    public static String timeForAPIHit(String utcTime) {

        long differenceInMinutes = 0;

        try {
            String busTime = utcTime.substring(11, 19);
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date1_bus = simpleDateFormat.parse(busTime);
            Date date2_current = simpleDateFormat.parse(currentTime);

            long differenceInMilliSeconds = Math.abs(date1_bus.getTime() - date2_current.getTime());
            differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (differenceInMinutes == 0) return "Arr";

        return String.valueOf(differenceInMinutes);
    }

    public static Load loadForAPIHit(String hit) {

        Load load = null;

        if (hit.equals("SEA")) {
            load = Load.LIGHT;
        } else if (hit.equals("SDA")) {
            load = Load.MEDIUM;
        } else if (hit.equals("LSD")) {
            load = Load.HEAVY;
        }

        return load;
    }

    public static Decker deckerForAPIHit(String hit) {
        Decker decker = null;

        if (hit.equals("SD")) {
            decker = Decker.SINGLE;
        } else if (hit.equals("DD")) {
            decker = Decker.DOUBLE;
        }
        return decker;
    }

    public static boolean isNumeric(String input) {
        boolean isit = true;

        try {
            int i = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            isit = false;
        }

        return isit;
    }

}
