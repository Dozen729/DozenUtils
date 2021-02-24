package com.dozen.commonbase.view;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

/**
 * @author: Dozen
 * @description: 每隔4位显示 用于显示银行卡之类
 * @time: 2020/9/29
 */
public class TextSpaceView implements TextWatcher {
	private String TAG = "SpaceText";
	private EditText etSpace;
	int beforeTextLength = 0;
	int onTextLength = 0;
	int i = 0;
	int start;
	private String beforeText;
	private boolean isContinue = true;

	public TextSpaceView(EditText etSpace) {
		super();
		this.etSpace = etSpace;
	}

	public String removeAllSpace(String str) {
		String tmpstr = str.replace(" ", "");
		return tmpstr;
	}

	@Override
	public void afterTextChanged(Editable s) {
		try {
			if (!isContinue) {
				isContinue = true;
				return;
			}
			isContinue = false;

			String str = etSpace.getText().toString();
			Log.d(TAG, "mEditText = " + removeAllSpace(str) + ".");
			onTextLength = str.length();
			Log.d(TAG, "beforeLen = " + beforeTextLength + "afterLen = " + onTextLength);
			str = str.substring(0, this.start) + "X" + str.substring(this.start);

			// 每四位添加空格
			String regex = "(.{4})";
			str = str.replaceAll(" ", "").replaceAll(regex, "$1 ");
			int sectionIndex = str.indexOf("X");
			str = str.replace("X", "").replaceAll(" ", "").replaceAll(regex, "$1 ");
			if (str.endsWith(" ")) {
				etSpace.setText(str.trim());
				etSpace.setSelection(sectionIndex - 1);
			} else if (0 == sectionIndex) {
				etSpace.setText(str.trim());
				etSpace.setSelection(sectionIndex);
			} else if (" ".equals(str.substring(sectionIndex - 1, sectionIndex))) {
				etSpace.setText(str.trim());
				etSpace.setSelection(sectionIndex - 1);
			} else {
				etSpace.setText(str.trim());
				etSpace.setSelection(sectionIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		beforeTextLength = s.length();
		this.beforeText = s.toString();
		if (count < after) {
			this.start = start + after;
		} else if (count > after) {
			this.start = start;
		} else {// ==
			this.start = start + count;
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		System.out.println("");
	}

}