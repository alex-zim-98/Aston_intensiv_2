package com.aston.drum

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: RainbowDrumViewModel

    private lateinit var rainbowDrumView: RainbowDrumView
    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private lateinit var tvColor: TextView
    private lateinit var ivLoaded: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var pbLoad: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        viewModel = ViewModelProvider(this).get(RainbowDrumViewModel::class.java)
        observers()
        clickListeners()
    }

    private fun observers() {
        viewModel.currentContent.observe(this) { content ->
            when(content) {
                is String -> {
                    ivLoaded.setImageBitmap(null)
                    ivLoaded.visibility = View.GONE

                    tvColor.visibility = View.VISIBLE
                    tvColor.text = content
                }
                is Bitmap -> {
                    tvColor.text = ""
                    tvColor.visibility = View.GONE

                    ivLoaded.visibility = View.VISIBLE
                    ivLoaded.setImageBitmap(content)
                }
            }
        }
        rainbowDrumView.onColorSelected = { color ->
            viewModel.onColorSelected(color)
        }
        viewModel.isLoading.observe(this) {
            pbLoad.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun clickListeners() {
        startButton.setOnClickListener {
            viewModel.resetContent()
            rainbowDrumView.spinAndStop()
        }

        resetButton.setOnClickListener {
            viewModel.resetContent()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val scaleFactor = progress / 50f
                rainbowDrumView.adjustSize(scaleFactor)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun initView() {
        rainbowDrumView = findViewById(R.id.rainbowDrumView)
        startButton = findViewById(R.id.startButton)
        resetButton = findViewById(R.id.resetButton)
        tvColor = findViewById(R.id.tvColor)
        ivLoaded = findViewById(R.id.ivLoaded)
        seekBar = findViewById(R.id.sizeSeekBar)
        pbLoad = findViewById(R.id.pbLoad)
    }
}
