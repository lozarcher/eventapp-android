package com.loz.iyaf.mainmenu;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loz.R;
import com.loz.iyaf.imagehelpers.Utils;

import static android.content.Context.WINDOW_SERVICE;

public class MenuAdapter extends BaseAdapter {
    private Context mContext;
    private static LayoutInflater inflater=null;

    public MenuAdapter(Context c) {
        mContext = c;
        inflater = (LayoutInflater)mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
        ImageView spoonImg;
        ImageView teaspoonImg;
        ImageView chopsticksImg;
        ImageView knifeImg;
        ImageView forkImg;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View rowView = inflater.inflate(R.layout.menulist, null);

        holder.tv = (TextView) rowView.findViewById(R.id.textView);
        holder.img = (ImageView) rowView.findViewById(R.id.menuImage);
        holder.teaspoonImg = (ImageView) rowView.findViewById(R.id.teaspoonImage);
        holder.spoonImg = (ImageView) rowView.findViewById(R.id.spoonImage);
        holder.chopsticksImg = (ImageView) rowView.findViewById(R.id.chopsticksImage);
        holder.knifeImg = (ImageView) rowView.findViewById(R.id.knifeImage);
        holder.forkImg = (ImageView) rowView.findViewById(R.id.forkImage);

        Double imageWidth = getScreenSizeInPixels(mContext).widthPixels/2.3;
        Double imageHeight = getScreenSizeInPixels(mContext).heightPixels/5.0;

        Double imageReferenceSize = (imageWidth < imageHeight) ? imageWidth : imageHeight;
        holder.img.setPadding(0,0,0,0);
        setPlateLayout(holder.img, imageReferenceSize, imageReferenceSize);



        Utils.loadImage("drawable://"+mThumbIds[position], holder.img, null, 0);

        holder.tv.setTextSize(convertPixelsToDp((float)(imageReferenceSize/9), mContext));
        holder.tv.setText(labels[position]);

        holder.forkImg.setVisibility(View.INVISIBLE);
        holder.knifeImg.setVisibility(View.INVISIBLE);
        holder.teaspoonImg.setVisibility(View.INVISIBLE);
        holder.spoonImg.setVisibility(View.INVISIBLE);
        holder.chopsticksImg.setVisibility(View.INVISIBLE);

        switch (position) {
            case 1: // KNIFE AND FORK
                //setImageLayout(holder.forkImg, imageReferenceSize*0.3, imageReferenceSize*1.1, 0.0, 0.0, imageReferenceSize*-0.3, 0.0, false);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.width=(int)(imageReferenceSize*0.3);
                lp.height=(int)(imageReferenceSize*0.9);
                lp.setMargins(0, 0, (int)(imageReferenceSize*-0.2), 0);
                lp.addRule(RelativeLayout.LEFT_OF, R.id.menuImage);
                lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                holder.forkImg.setLayoutParams(lp);
                Utils.loadImage("drawable://"+R.id.forkImage, holder.forkImg, null, 0);
                holder.forkImg.setVisibility(View.VISIBLE);

                lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.width=(int)(imageReferenceSize*0.2);
                lp.height=(int)(imageReferenceSize*1.0);
                lp.setMargins((int)(imageReferenceSize*-0.15), 0, 0, 0);
                lp.addRule(RelativeLayout.RIGHT_OF, R.id.menuImage);
                lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                holder.knifeImg.setLayoutParams(lp);
                Utils.loadImage("drawable://" + R.id.knifeImage, holder.knifeImg, null, 0);
                holder.knifeImg.setVisibility(View.VISIBLE);
                break;
            case 2: // TEASPOON
                Utils.loadImage("drawable://" + R.id.teaspoonImage, holder.teaspoonImg, null, 0);
                holder.teaspoonImg.setVisibility(View.VISIBLE);
                lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.width=(int)(imageReferenceSize*0.6);
                lp.height=(int)(imageReferenceSize*0.6);
                lp.setMargins((int)(imageReferenceSize*-0.8), (int)(imageReferenceSize*0.05), 0, 0);
                lp.addRule(RelativeLayout.RIGHT_OF, R.id.menuImage);
                lp.addRule(RelativeLayout.ALIGN_TOP, R.id.menuImage);
                lp.addRule(RelativeLayout.ALIGN_RIGHT, R.id.menuImage);
                holder.teaspoonImg.setLayoutParams(lp);
                break;
            case 4: // SPOON
                lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.width=(int)(imageReferenceSize*0.3);
                lp.height=(int)(imageReferenceSize*1.0);
                lp.setMargins((int)(imageReferenceSize*-0.1), 0, 0, 0);
                lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                lp.addRule(RelativeLayout.RIGHT_OF, R.id.menuImage);
                holder.spoonImg.setLayoutParams(lp);
                Utils.loadImage("drawable://" + R.id.spoonImage, holder.spoonImg, null, 0);
                holder.spoonImg.setVisibility(View.VISIBLE);
                break;
            case 5:  // CHOPSTICKS
                lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.width=(int)(imageReferenceSize*0.3);
                lp.height=(int)(imageReferenceSize*0.9);
                lp.setMargins((int)(imageReferenceSize*-0.1), 0, 0, 0);
                lp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                lp.addRule(RelativeLayout.RIGHT_OF, R.id.menuImage);
                holder.chopsticksImg.setLayoutParams(lp);
                Utils.loadImage("drawable://" + R.id.chopsticksImage, holder.chopsticksImg, null, 0);
                holder.chopsticksImg.setVisibility(View.VISIBLE);
                break;
        }

        return rowView;
    }


    private void setPlateLayout(ImageView image, Double width, Double height) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.width=width.intValue();
        lp.height=height.intValue();

        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        image.setLayoutParams(lp);
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.magenta_plate, R.drawable.orange_plate,
            R.drawable.blue_plate, R.drawable.red_plate,
            R.drawable.green_plate, R.drawable.indigo_plate
    };

    // references to our image labels
    private String[] labels = {
            "Events", "Gallery",
            "News", "Traders",
            "Get\nInvolved", "Twitter"
    };

    protected static DisplayMetrics getScreenSizeInPixels(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

}
