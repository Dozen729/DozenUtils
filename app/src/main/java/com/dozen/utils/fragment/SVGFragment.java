package com.dozen.utils.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.view.svg.OnSvgElementListener;
import com.dozen.commonbase.view.svg.Sharp;
import com.dozen.commonbase.view.svg.SharpDrawable;
import com.dozen.commonbase.view.svg.SharpPicture;
import com.dozen.commonbase.view.zoomage.ZoomImageView;
import com.dozen.utils.R;

import java.util.Random;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class SVGFragment extends BaseFragment {

    public static final String KEY_TEXT = "svg";

    public static SVGFragment newInstance(String text) {
        SVGFragment mineFragment = new SVGFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    private Sharp mSvg;
    private ZoomImageView svgView1;
    private Button mButton;
    private boolean mRenderBitmap = true;

    private ImageView svgView2;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_svg;
    }

    @Override
    protected void setUpView() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name = bundle.getString(KEY_TEXT);

        svgView1 = getContentView().findViewById(R.id.svg_view1);
        mButton = getContentView().findViewById(R.id.svg_btn1);

        Sharp.setLogLevel(Sharp.LOG_LEVEL_INFO);

        mSvg = Sharp.loadResource(getResources(), R.raw.mother);
        // If you want to load typefaces from assets:
        //          .withAssets(getAssets());

        // If you want to load an SVG from assets:
        //mSvg = Sharp.loadAsset(getAssets(), "cartman.svg");

        loadSvg(R.id.svg_view2,R.raw.mother);
        loadSvg(R.id.svg_view3,R.raw.jiucai);
        loadSvg(R.id.svg_view4,R.raw.wuxi);
        loadSvg(R.id.svg_view5,R.raw.times);
        loadSvg(R.id.svg_view6,R.raw.longpicture);
        loadSvg(R.id.svg_view7,R.raw.xin);
    }

    @Override
    protected void setUpData() {
        reloadSvg(false);
    }

    private void reloadSvg(final boolean changeColor) {
        mSvg.setOnElementListener(new OnSvgElementListener() {

            @Override
            public void onSvgStart(@NonNull Canvas canvas,
                                   @Nullable RectF bounds) {
            }

            @Override
            public void onSvgEnd(@NonNull Canvas canvas,
                                 @Nullable RectF bounds) {
            }

            @Override
            public <T> T onSvgElement(@Nullable String id,
                                      @NonNull T element,
                                      @Nullable RectF elementBounds,
                                      @NonNull Canvas canvas,
                                      @Nullable RectF canvasBounds,
                                      @Nullable Paint paint) {
                if (changeColor && paint != null && paint.getStyle() == Paint.Style.FILL &&
                        ("shirt".equals(id) || "hat".equals(id) || "pants".equals(id))) {
                    Random random = new Random();
                    paint.setColor(Color.argb(255, random.nextInt(256),
                            random.nextInt(256), random.nextInt(256)));
                }
                return element;
            }

            @Override
            public <T> void onSvgElementDrawn(@Nullable String id,
                                              @NonNull T element,
                                              @NonNull Canvas canvas,
                                              @Nullable Paint paint) {
            }

        });
        mSvg.getSharpPicture(new Sharp.PictureCallback() {
            @Override
            public void onPictureReady(SharpPicture picture) {
                Drawable drawable = picture.getDrawable();
                if (mRenderBitmap) {
                    // Create a bitmap with a size that is somewhat arbitrarily determined by SharpDrawable
                    // This will no doubt look bad when scaled up, so perhaps a different dimension would be used in practice
                    int width = Math.max(1, drawable.getIntrinsicWidth());
                    int height = Math.max(1, drawable.getIntrinsicHeight());
                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    // Draw SharpDrawable onto this bitmap
                    Canvas canvas = new Canvas(bitmap);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);

                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

                    // You could do some bitmap operations here that aren't supported by Picture
                    //bitmapDrawable.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                    //bitmapDrawable.setAlpha(100);

                    // Use the BitmapDrawable instead of the SharpDrawable
                    drawable = bitmapDrawable;
                } else {
                    SharpDrawable.prepareView(svgView1);
                }
                svgView1.setImageDrawable(drawable);

                // We don't want to use the same drawable, as we're specifying a custom size; therefore
                // we call createDrawable() instead of getDrawable()
                int iconSize = getResources().getDimensionPixelSize(R.dimen.icon_size);
                mButton.setCompoundDrawables(
                        picture.createDrawable(mButton, iconSize),
                        null, null, null);
            }
        });
    }

    private void loadSvg(int id,int raw){
        ImageView imageView = getContentView().findViewById(id);
        Sharp shape = Sharp.loadResource(getResources(), raw);
        imageView.setImageDrawable(shape.getDrawable().getCurrent());
    }
}
