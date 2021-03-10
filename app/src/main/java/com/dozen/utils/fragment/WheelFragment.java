package com.dozen.utils.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dozen.commonbase.act.BaseFragment;
import com.dozen.commonbase.view.wheel.base.WheelItem;
import com.dozen.commonbase.view.wheel.base.WheelItemView;
import com.dozen.commonbase.view.wheel.base.WheelView;
import com.dozen.commonbase.view.wheel.dialog.ColumnWheelDialog;
import com.dozen.commonbase.view.wheel.dialog.DateTimeWheelDialog;
import com.dozen.utils.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * @author: Dozen
 * @description:
 * @time: 2021/2/19
 */
public class WheelFragment extends BaseFragment {

    public static final String KEY_TEXT = "wheel";

    public static WheelFragment newInstance(String text) {
        WheelFragment mineFragment = new WheelFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TEXT, text);
        mineFragment.setArguments(args);
        return mineFragment;
    }

    private TextView tvResult;
    private TextView tvResult1;
    private WheelItemView wheelViewLeft;
    private WheelItemView wheelViewCenter;
    private WheelItemView wheelViewRight;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_wheel;
    }

    @Override
    protected void setUpView() {
        Bundle bundle = this.getArguments();
        assert bundle != null;
        String name=bundle.getString(KEY_TEXT);

        tvResult = getContentView().findViewById(R.id.wheel_text);
        tvResult1 = getContentView().findViewById(R.id.wheel_text1);

        btnSetClick(R.id.wheel_btn1);
        btnSetClick(R.id.wheel_btn2);
        btnSetClick(R.id.wheel_btn3);
        btnSetClick(R.id.wheel_btn4);
        btnSetClick(R.id.wheel_btn5);
        btnSetClick(R.id.wheel_btn6);
        btnSetClick(R.id.wheel_btn7);
        btnSetClick(R.id.wheel_btn8);
        btnSetClick(R.id.wheel_btn9);
        btnSetClick(R.id.wheel_btn10);

        wheelViewLeft = getContentView().findViewById(R.id.wheel_view_left);
        wheelViewCenter = getContentView().findViewById(R.id.wheel_view_center);
        wheelViewRight = getContentView().findViewById(R.id.wheel_view_right);
        wheelViewLeft.setOnSelectedListener(listener);
        wheelViewCenter.setOnSelectedListener(listener);
        wheelViewRight.setOnSelectedListener(listener);
    }

    @Override
    protected void setUpData() {
        loadData(wheelViewLeft, "很长的左边菜单");
        loadData(wheelViewCenter, "中间菜单");
        loadData(wheelViewRight,  "很长的右边边菜单");
    }

    WheelView.OnSelectedListener listener = new WheelView.OnSelectedListener() {
        @Override
        public void onSelected(Context context, int selectedIndex) {
            tvResult1.setText(String.format(Locale.CHINA, "{left:%d, center:%d, right:%d}", wheelViewLeft.getSelectedIndex(), wheelViewCenter.getSelectedIndex(),  wheelViewRight.getSelectedIndex()));
        }
    };

    private void btnSetClick(int id){
        Button btn = getContentView().findViewById(id);
        btn.setOnClickListener(btnClickListener);
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            DateTimeWheelDialog dialog;
            ColumnWheelDialog columnWheelDialog;
            switch (view.getId()){
                case R.id.wheel_btn1:
                    dialog = createDialog(1);
                    dialog.show();
                    break;
                case R.id.wheel_btn2:
                    dialog = createDialog(2);
                    dialog.show();
                    break;
                case R.id.wheel_btn3:
                    dialog = createDialog(3);
                    dialog.show();
                    break;
                case R.id.wheel_btn4:
                    dialog = createDialog(4);
                    dialog.show();
                    break;
                case R.id.wheel_btn5:
                    dialog = createDialog(5);
                    dialog.show();
                    break;
                case R.id.wheel_btn6:
                    columnWheelDialog = createColumnDialog(1);
                    columnWheelDialog.show();
                    break;
                case R.id.wheel_btn7:
                    columnWheelDialog = createColumnDialog(2);
                    columnWheelDialog.show();
                    break;
                case R.id.wheel_btn8:
                    columnWheelDialog = createColumnDialog(3);
                    columnWheelDialog.show();
                    break;
                case R.id.wheel_btn9:
                    columnWheelDialog = createColumnDialog(4);
                    columnWheelDialog.show();
                    break;
                case R.id.wheel_btn10:
                    columnWheelDialog = createColumnDialog(5);
                    columnWheelDialog.show();
                    break;
            }
        }
    };

    private DateTimeWheelDialog createDialog(int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date startDate = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 3000);
        Date endDate = calendar.getTime();

        DateTimeWheelDialog dialog = new DateTimeWheelDialog(getActivity());
        dialog.setShowCount(7);
        dialog.setItemVerticalSpace(24);
        dialog.show();
        dialog.setTitle("选择时间");
        int config = DateTimeWheelDialog.SHOW_YEAR_MONTH_DAY_HOUR_MINUTE;
        switch (type) {
            case 1:
                config = DateTimeWheelDialog.SHOW_YEAR;
                break;
            case 2:
                config = DateTimeWheelDialog.SHOW_YEAR_MONTH;
                break;
            case 3:
                config = DateTimeWheelDialog.SHOW_YEAR_MONTH_DAY;
                break;
            case 4:
                config = DateTimeWheelDialog.SHOW_YEAR_MONTH_DAY_HOUR;
                break;
            case 5:
                config = DateTimeWheelDialog.SHOW_YEAR_MONTH_DAY_HOUR_MINUTE;
                break;
        }
        dialog.configShowUI(config);
        dialog.setCancelButton("取消", null);
        dialog.setOKButton("确定", new DateTimeWheelDialog.OnClickCallBack() {
            @Override
            public boolean callBack(View v, @NonNull Date selectedDate) {
                tvResult.setText(SimpleDateFormat.getInstance().format(selectedDate));
                return false;
            }
        });
        dialog.setDateArea(startDate, endDate, true);
        dialog.updateSelectedDate(new Date());
        return dialog;
    }

    private ColumnWheelDialog createColumnDialog(int type) {
        ColumnWheelDialog<WheelItem, WheelItem, WheelItem, WheelItem, WheelItem> dialog = new ColumnWheelDialog<>(getActivity());
        dialog.show();
        dialog.setTitle("选择菜单");
        dialog.setCancelButton("取消", null);
        dialog.setOKButton("确定", new ColumnWheelDialog.OnClickCallBack<WheelItem, WheelItem, WheelItem, WheelItem, WheelItem>() {
            @Override
            public boolean callBack(View v, @Nullable WheelItem item0, @Nullable WheelItem item1, @Nullable WheelItem item2, @Nullable WheelItem item3, @Nullable WheelItem item4) {
                String result = "";
                if (item0 != null)
                    result += item0.getShowText() + "\n";
                if (item1 != null)
                    result += item1.getShowText() + "\n";
                if (item2 != null)
                    result += item2.getShowText() + "\n";
                if (item3 != null)
                    result += item3.getShowText() + "\n";
                if (item4 != null)
                    result += item4.getShowText() + "\n";
                tvResult.setText(result);
                return false;
            }
        });
        switch (type) {
            case 1:
                dialog.setItems(
                        initItems("选项一"),
                        null,
                        null,
                        null,
                        null
                );
                dialog.setSelected(
                        new Random().nextInt(50),
                        0,
                        0,
                        0,
                        0
                );
                break;
            case 2:
                dialog.setItems(
                        initItems("选项一"),
                        initItems("选项二"),
                        null,
                        null,
                        null
                );
                dialog.setSelected(
                        new Random().nextInt(50),
                        new Random().nextInt(50),
                        0,
                        0,
                        0
                );
                break;
            case 3:
                dialog.setItems(
                        initItems("选项一"),
                        initItems("选项二"),
                        initItems("选项三"),
                        null,
                        null
                );
                dialog.setSelected(
                        new Random().nextInt(50),
                        new Random().nextInt(50),
                        new Random().nextInt(50),
                        0,
                        0
                );
                break;
            case 4:
                dialog.setItems(
                        initItems("选项一"),
                        initItems("选项二"),
                        initItems("选项三"),
                        initItems("选项四"),
                        null
                );
                dialog.setSelected(
                        new Random().nextInt(50),
                        new Random().nextInt(50),
                        new Random().nextInt(50),
                        new Random().nextInt(50),
                        0
                );
                break;
            case 5:
                dialog.setItems(
                        initItems("选项一"),
                        initItems("选项二"),
                        initItems("选项三"),
                        initItems("选项四"),
                        initItems("选项五")
                );
                dialog.setSelected(
                        new Random().nextInt(50),
                        new Random().nextInt(50),
                        new Random().nextInt(50),
                        new Random().nextInt(50),
                        new Random().nextInt(50)
                );
                break;
        }
        return dialog;
    }

    private WheelItem[] initItems(String label) {
        final WheelItem[] items = new WheelItem[50];
        for (int i = 0; i < 50; i++) {
            items[i] = new WheelItem(label + (i < 10 ? "0" + i : "" + i));
        }
        return items;
    }


    private void loadData(WheelItemView wheelItemView) {
        String[] randomShowText = {"菜单", "子菜单", "父系菜单", "很长的家族菜单", "ScrollMenu"};
        Random random = new Random();
        WheelItem[] items = new WheelItem[50];
        for (int i = 0; i < 50; i++) {
            items[i] = new WheelItem(randomShowText[random.nextInt(5)] + (i < 10 ? "0" + i : "" + i));
        }
        wheelItemView.setItems(items);
    }

    private void loadData(WheelItemView wheelItemView, String label) {
        WheelItem[] items = new WheelItem[50];
        for (int i = 0; i < 50; i++) {
            items[i] = new WheelItem(label + (i < 10 ? "0" + i : "" + i));
        }
        wheelItemView.setItems(items);
    }

}
