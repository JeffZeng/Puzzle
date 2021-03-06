package tw.jefftzeng.puzzle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by JeffTseng on 2018/2/5.
 */

public class ImageAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private Context mContext;
    ArrayList<Bitmap> splitImages = new ArrayList<Bitmap>();
    ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    char []serialArray;

    public ImageAdapter(Context c) {
        mContext = c;
        serialArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '*'};
        for(int times = 0; times < 20; times++) {
           for(int index = 0; index < serialArray.length; index++) {
               if(serialArray[index]=='*') {
                   int [] direction = new int []{1,-1,3,-3};
                   int num = (int)(Math.random()*4);
                   char temp;
                   if(index+direction[num] >=0 && index+direction[num] < serialArray.length) {
                       temp = serialArray[index+direction[num]];
                       serialArray[index+direction[num]] = serialArray[index];
                       serialArray[index] = temp;
                   }

               }
           }
        }

        Resources res = mContext.getResources();
        Display display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Bitmap srcBmp = BitmapFactory.decodeResource(res, R.drawable.background);
        Bitmap resizeBmp = null;
        double radio = 1.0f;
        int width = (int)((float)size.x);
        radio = width/srcBmp.getWidth();
        resizeBmp = Bitmap.createScaledBitmap(srcBmp, width, (int)(radio * srcBmp.getHeight()), false);
        int vScale = (int)((double)resizeBmp.getHeight() / 3);
        int hScale = (int)((double)resizeBmp.getWidth() / 3);
        for(int v =0; v <resizeBmp.getHeight(); v+=vScale) {
            for (int h = 0; h < resizeBmp.getWidth(); h += hScale) {
                Bitmap dstBmp = Bitmap.createBitmap(resizeBmp, h, v, hScale, vScale);
                splitImages.add(dstBmp);
            }
        }

        for(int index=0; index < serialArray.length; index++){
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(hScale, vScale));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            if(serialArray[index] != '*')
                imageView.setImageBitmap(splitImages.get(serialArray[index]-'0'));
            imageViews.add(imageView);
        }

    }
    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        return imageViews.get(position);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        if((position + 1) < serialArray.length && serialArray[position + 1] == '*') {
            imageViews.get(position).setImageDrawable(null);
            imageViews.get(position+1).setImageBitmap(splitImages.get(serialArray[position]-'0'));
            char temp;
            temp = serialArray[position];
            serialArray[position] = serialArray[position+1];
            serialArray[position+1] = temp;
            this.notifyDataSetChanged();
        }

        if((position -1) >= 0 && serialArray[position - 1] == '*') {
            imageViews.get(position).setImageDrawable(null);
            imageViews.get(position-1).setImageBitmap(splitImages.get(serialArray[position]-'0'));
            char temp;
            temp = serialArray[position];
            serialArray[position] = serialArray[position-1];
            serialArray[position-1] = temp;
            this.notifyDataSetChanged();
        }

        if((position +3) < serialArray.length && serialArray[position +3] == '*') {
            imageViews.get(position).setImageDrawable(null);
            imageViews.get(position+3).setImageBitmap(splitImages.get(serialArray[position]-'0'));
            char temp;
            temp = serialArray[position];
            serialArray[position] = serialArray[position+3];
            serialArray[position+3] = temp;
            this.notifyDataSetChanged();
        }

        if((position -3) >= 0 && serialArray[position -3] == '*') {
            imageViews.get(position).setImageDrawable(null);
            imageViews.get(position-3).setImageBitmap(splitImages.get(serialArray[position]-'0'));
            char temp;
            temp = serialArray[position];
            serialArray[position] = serialArray[position-3];
            serialArray[position-3] = temp;
            this.notifyDataSetChanged();
        }
        if(new String(serialArray).equals("01234567*"))
            Toast.makeText(mContext,"You win!", Toast.LENGTH_SHORT).show();
    }
}
