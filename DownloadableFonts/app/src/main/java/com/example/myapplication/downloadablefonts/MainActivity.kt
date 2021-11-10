package com.example.myapplication.downloadablefonts

import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.text.Editable
import android.text.TextWatcher
import android.util.ArraySet
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import com.example.myapplication.downloadablefonts.Constants.ITALIC_DEFAULT
import com.example.myapplication.downloadablefonts.Constants.WEIGHT_DEFAULT
import com.example.myapplication.downloadablefonts.Constants.WEIGHT_MAX
import com.example.myapplication.downloadablefonts.Constants.WIDTH_DEFAULT
import com.example.myapplication.downloadablefonts.Constants.WIDTH_MAX
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit private var mHandler: Handler

    lateinit private var mDownloadableFontTextView: TextView
    lateinit private var mWidthSeekBar: SeekBar
    lateinit private var mWeightSeekBar: SeekBar
    lateinit private var mItalicSeekBar: SeekBar
    lateinit private var mBestEffort: CheckBox
    lateinit private var mRequestDownloadButton: Button

    lateinit private var mFamilyNameSet: ArraySet<String>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handlerThread = HandlerThread("fonts")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)
        initializeSeekBars()
        mFamilyNameSet = ArraySet<String>()
        //*: spread operator
        mFamilyNameSet.addAll(listOf(*(resources.getStringArray(R.array.family_names))))

        mDownloadableFontTextView = findViewById(R.id.textview)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            resources.getStringArray(R.array.family_names)
        )
        val familyNameInput = findViewById<TextInputLayout>(R.id.auto_complete_family_name_input)
        val autoCompleteFamilyName =
            findViewById<AutoCompleteTextView>(R.id.auto_complete_family_name)
        autoCompleteFamilyName.setAdapter(adapter)
        autoCompleteFamilyName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //No op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isValidFamilyName(s.toString())) {
                    familyNameInput.isErrorEnabled = false
                    familyNameInput.error = ""
                } else {
                    familyNameInput.isErrorEnabled = true
                    familyNameInput.error = getString(R.string.invalid_family_name)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //No op
            }
        })

        mRequestDownloadButton = findViewById(R.id.button_request)
        mRequestDownloadButton.setOnClickListener(View.OnClickListener {
            val familyName = autoCompleteFamilyName.getText().toString()
            if (!isValidFamilyName(familyName)) {
                familyNameInput.isErrorEnabled = true
                familyNameInput.error = getString(R.string.invalid_family_name)
                Toast.makeText(
                    this@MainActivity,
                    R.string.invalid_input,
                    Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            requestDownload(familyName)
            mRequestDownloadButton.isEnabled = false
        })

        mBestEffort = findViewById(R.id.checkbox_best_effort)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestDownload(familyName: String) {
        val queryBuilder = QueryBuilder(
            familyName,
            width = progressToWidth(mWidthSeekBar.progress),
            weight = progressToWeight(mWeightSeekBar.progress),
            italic = progressToItalic(mItalicSeekBar.progress),
            bestefoort = mBestEffort.isChecked
        )

        val query = queryBuilder.build()

        Log.d(TAG, "Requesting a font. Query")
        val request = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            query,
            R.array.com_google_android_gms_fonts_certs
        )

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val callback = object : FontsContractCompat.FontRequestCallback() {
            override fun onTypefaceRetrieved(typeface: Typeface?) {
                mDownloadableFontTextView.typeface = typeface
                progressBar.visibility = View.GONE
                mRequestDownloadButton.isEnabled = true
            }

            override fun onTypefaceRequestFailed(reason: Int) {
                Toast.makeText(
                    this@MainActivity, getString(R.string.request_failed, reason),
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.GONE
                mRequestDownloadButton.isEnabled = true
            }
        }

        FontsContractCompat
            .requestFont(this@MainActivity, request, callback, mHandler)
    }

    private fun initializeSeekBars() {
        mWidthSeekBar = findViewById(R.id.seek_bar_width)
        val widthValue = (100 * WIDTH_DEFAULT.toFloat() / WIDTH_MAX.toFloat()).toInt()
        mWidthSeekBar.progress = widthValue
        val widthTextView = findViewById<TextView>(R.id.textview_width)
        widthTextView.text = widthValue.toString()
        mWidthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                widthTextView
                    .setText(progressToWeight(progress).toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        mWeightSeekBar = findViewById(R.id.seek_bar_weight)
        val weightValue = WEIGHT_DEFAULT.toFloat() / WEIGHT_MAX.toFloat() * 100
        mWeightSeekBar.progress = weightValue.toInt()
        val weightTextView = findViewById<TextView>(R.id.textview_weight)
        weightTextView.text = WEIGHT_DEFAULT.toString()
        mWeightSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                weightTextView
                    .setText(progressToWeight(progress).toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        mItalicSeekBar = findViewById<SeekBar>(R.id.seek_bar_italic)
        mItalicSeekBar.progress = ITALIC_DEFAULT.toInt()
        val italicTextView = findViewById<TextView>(R.id.textview_italic)
        italicTextView.text = ITALIC_DEFAULT.toString()
        mItalicSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                italicTextView.setText(progressToItalic(progress).toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun isValidFamilyName(familyName: String?): Boolean {
        return familyName != null && mFamilyNameSet.contains(familyName)
    }

    private fun progressToWidth(progress: Int): Float {
        return (if (progress == 0) 1 else progress * WIDTH_MAX / 100).toFloat()
    }

    private fun progressToWeight(progress: Int): Int {
        if (progress == 0) {
            return 1
        } else if (progress == 100)
            return WEIGHT_MAX - 1
        else return WEIGHT_MAX * progress / 100
    }

    private fun progressToItalic(progress: Int): Float {
        return progress.toFloat() / 100f
    }

    companion object {
        private val TAG = "MainActivity"
    }
}

