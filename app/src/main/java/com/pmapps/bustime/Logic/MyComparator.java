package com.pmapps.bustime.Logic;

import com.pmapps.bustime.BusList.BusItem;

import java.util.Comparator;

import static com.pmapps.bustime.Logic.Logic.isNumeric;

public class MyComparator implements Comparator<BusItem> {

    @Override
    public int compare(BusItem o1, BusItem o2) {
        String o11 = o1.getBusNumber();
        String o22 = o2.getBusNumber();

        String firstChar_o1 = String.valueOf(o11.charAt(0));
        String firstChar_o2 = String.valueOf(o22.charAt(0));

        // case 0: CT18 type
        /*
         * CT8, 63
         * 63, CT8
         * CT8, NR7
         * */

        if (!isNumeric(firstChar_o1) && isNumeric(firstChar_o2)) {
            return 1;
        } else if (isNumeric(firstChar_o1) && !isNumeric(firstChar_o2)) {
            return -1;
        } else if (!isNumeric(firstChar_o1) && !isNumeric(firstChar_o2)) {
            return 0;
        }
        // case 1: both are numerical
        else if (isNumeric(o11) && isNumeric(o22)) {

            int o1_int = Integer.parseInt(o11);
            int o2_int = Integer.parseInt(o22);

            return Integer.compare(o1_int, o2_int);
        }
        // case 2: o1 is numeric only 63,43M
        else if (isNumeric(o11) && !isNumeric(o22)) {
            String[] o2_split = o22.split("(?<=\\d)(?=\\D)");
            String new_o2 = o2_split[0];

            int o1_int = Integer.parseInt(o11);
            int o2_int = Integer.parseInt(new_o2);

            return Integer.compare(o1_int, o2_int);
        }
        // case 3: o2 is numeric only
        else if (!isNumeric(o11) && isNumeric(o22)) {
            String[] o1_split = o11.split("(?<=\\d)(?=\\D)");
            String new_o1 = o1_split[0];

            int o1_int = Integer.parseInt(new_o1);
            int o2_int = Integer.parseInt(o22);

            return Integer.compare(o1_int, o2_int);
        }
        // case 4: both are non-numeric
        else {
            String[] o1_split = o11.split("(?<=\\d)(?=\\D)");
            String[] o2_split = o22.split("(?<=\\d)(?=\\D)");
            String new_o1 = o1_split[0];
            String new_o2 = o2_split[0];

            int o1_int = Integer.parseInt(new_o1);
            int o2_int = Integer.parseInt(new_o2);

            return Integer.compare(o1_int, o2_int);
        }

    }
}
