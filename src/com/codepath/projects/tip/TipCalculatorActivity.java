package com.codepath.projects.tip;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TipCalculatorActivity extends Activity {
	private static BigDecimal HUNDRED = new BigDecimal("100");

	private TextView tvTip;
	private EditText etTotal;
	private EditText etCustom;
	private int fractionDigits;
	private String tipPrefix;
	private BigDecimal currentTip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calculator);
		tvTip = (TextView) findViewById(R.id.tvTip);
		etTotal = (EditText) findViewById(R.id.etTotal);
		etTotal.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				calculateTip(currentTip);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

		});

		etCustom = (EditText) findViewById(R.id.etCustom);
		etCustom.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				BigDecimal tipPercent = null;
				if (etCustom.length() > 0) {
					tipPercent = new BigDecimal(etCustom.getText().toString());
					tipPercent = tipPercent.setScale(fractionDigits, RoundingMode.HALF_EVEN).divide(HUNDRED);
				}
				calculateTip(tipPercent);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		Locale currentLocale = getResources().getConfiguration().locale;
		fractionDigits = Currency.getInstance(currentLocale).getDefaultFractionDigits();
		tipPrefix = getResources().getString(R.string.tip_is);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View view) {
		etCustom.setText("");
		etCustom.clearFocus();
		etTotal.clearFocus();
		view.requestFocus();
		BigDecimal percentage = new BigDecimal((String) view.getTag());
		calculateTip(percentage);
	}

	private void calculateTip(BigDecimal tipRate) {
		currentTip = tipRate;
		if (currentTip == null || etTotal.length() == 0) {
			tvTip.setText(tipPrefix);
		} else {
			BigDecimal total = new BigDecimal(etTotal.getText().toString());
			total = total.setScale(fractionDigits, RoundingMode.HALF_EVEN);
			BigDecimal tip = total.multiply(currentTip).setScale(fractionDigits, RoundingMode.HALF_EVEN);

			// output
			String tipStr = NumberFormat.getPercentInstance().format(currentTip);
			tvTip.setText(tipStr + " tip is " + NumberFormat.getCurrencyInstance().format(tip));
		}
	}
}
