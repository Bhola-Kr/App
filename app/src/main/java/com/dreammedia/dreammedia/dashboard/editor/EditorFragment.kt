package com.dreammedia.dreammedia.dashboard.editor

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dreammedia.dreammedia.R
import com.dreammedia.dreammedia.activity.DashboardActivity
import com.dreammedia.dreammedia.adapter.ColorAdapter
import com.dreammedia.dreammedia.adapter.FontAdapter
import com.dreammedia.dreammedia.customWigits.viewPager.edittext.RotatableAutofitEditText
import com.dreammedia.dreammedia.customWigits.viewPager.imageview.ImageDrageListener
import com.dreammedia.dreammedia.customWigits.viewPager.imageview.TopCropImageView
import com.dreammedia.dreammedia.databinding.FragmentEditorBinding
import com.dreammedia.dreammedia.model.ColorModel
import com.dreammedia.dreammedia.model.FontModel
import com.dreammedia.dreammedia.utlis.Util

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditorFragment : Fragment() ,FontAdapter.ClickListener ,ColorAdapter.ClickListener {

    lateinit var binding : FragmentEditorBinding
    var MP: MediaPlayer? = null
    private var flag     = 0
    var linearLayoutManager: GridLayoutManager? = null
    var linearLM: LinearLayoutManager? = null

    var txtFlag : Boolean = false
    var frmFlag : Boolean = false
    var tempid: String = ""
    var mview: View? = null

    lateinit var tvT0 : RotatableAutofitEditText
    lateinit var tvT1 : RotatableAutofitEditText
    lateinit var tvT2 : RotatableAutofitEditText
    lateinit var tvT3 : RotatableAutofitEditText
    lateinit var tvT4 : RotatableAutofitEditText
    lateinit var tvT5 : RotatableAutofitEditText

    lateinit var selectedTextView : RotatableAutofitEditText

    lateinit var imgFram : ImageView
    lateinit var img1 : TopCropImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_editor, container, false)

        tempid = requireArguments().getString("tempid").toString()

        layoutGraber()

        return binding.root

    }

    private class GestureListener : SimpleOnGestureListener() {
        // event when double tap occurs
        override fun onDoubleTap(e: MotionEvent): Boolean {
            return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.text.setOnClickListener {

            binding.lyText.visibility        = View.VISIBLE
            binding.fontRecycler.visibility  = View.GONE
            binding.fram1.visibility         = View.GONE
            binding.colorRecycler.visibility = View.VISIBLE

            if (txtFlag){
                txtFlag = false
                binding.lyText.visibility        = View.GONE
                binding.colorRecycler.visibility = View.GONE
            }else{
                txtFlag = true
                binding.lyText.visibility        = View.VISIBLE
                binding.colorRecycler.visibility = View.VISIBLE
            }
        }

        binding.image.setOnClickListener {

            GalleryOnclick()

            binding.lyText.visibility        = View.GONE
            binding.fontRecycler.visibility  = View.GONE
            binding.colorRecycler.visibility = View.GONE
            binding.fram1.visibility         = View.GONE
        }

        binding.frame.setOnClickListener {

            binding.lyText.visibility        = View.GONE
            binding.fontRecycler.visibility  = View.GONE
            binding.colorRecycler.visibility = View.GONE

            if (frmFlag){
                frmFlag = false
                binding.fram1.visibility         = View.GONE
            }else{
                frmFlag = true
                binding.fram1.visibility         = View.VISIBLE
            }
        }

        binding.fram1.setOnClickListener {

           imgFram.background = resources.getDrawable(R.drawable.fram2)

        }

        binding.fonStyle.setOnClickListener {
            binding.lyText.visibility = View.GONE
            binding.fontRecycler.visibility = View.VISIBLE
            binding.colorRecycler.visibility = View.VISIBLE
        }

        binding.fontSeek.setMax( (400 - 130) / 1 )

        linearLayoutManager = GridLayoutManager(activity , 2)
        binding.fontRecycler.setLayoutManager(linearLayoutManager)
        binding.fontRecycler.setHasFixedSize(true)
        binding.fontRecycler.isNestedScrollingEnabled = false

        linearLM = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.colorRecycler.setLayoutManager(linearLM)
        binding.colorRecycler.setHasFixedSize(true)
        binding.colorRecycler.isNestedScrollingEnabled = false

        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "roboto_bold.ttf"),true))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f1.ttf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f2.otf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f3.otf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f4.otf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f5.ttf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f6.ttf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f7.ttf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f8.ttf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f9.ttf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f10.ttf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f11.ttf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f12.otf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f13.otf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f14.ttf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f15.ttf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f16.otf"),false))
        fontList.add(FontModel(Typeface.createFromAsset(activity?.resources?.assets, "f17.otf"),false))

        adapter = FontAdapter(fontList , this , this.requireActivity())
        binding.fontRecycler.setAdapter(adapter)

        colorList.add(ColorModel(R.color.c0,false))
        colorList.add(ColorModel(R.color.c1,false))
        colorList.add(ColorModel(R.color.c2,false))
        colorList.add(ColorModel(R.color.c3,false))
        colorList.add(ColorModel(R.color.c4,false))
        colorList.add(ColorModel(R.color.c6,false))
        colorList.add(ColorModel(R.color.c7,false))
        colorList.add(ColorModel(R.color.c8,false))
        colorList.add(ColorModel(R.color.c9,false))
        colorList.add(ColorModel(R.color.c10,false))
        colorList.add(ColorModel(R.color.c11,false))
        colorList.add(ColorModel(R.color.c12,false))
        colorList.add(ColorModel(R.color.c13,false))
        colorList.add(ColorModel(R.color.c14,false))
        colorList.add(ColorModel(R.color.c15,false))
        colorList.add(ColorModel(R.color.c16,false))
        colorList.add(ColorModel(R.color.c17,false))
        colorList.add(ColorModel(R.color.c18,false))
        colorList.add(ColorModel(R.color.c19,false))
        colorList.add(ColorModel(R.color.c20,false))
        colorList.add(ColorModel(R.color.c21,false))
        colorList.add(ColorModel(R.color.c22,false))
        colorList.add(ColorModel(R.color.c23,false))
        colorList.add(ColorModel(R.color.c24,false))
        colorList.add(ColorModel(R.color.c25,false))
        colorList.add(ColorModel(R.color.c26,false))

        coloradapter = ColorAdapter(colorList , this , this.requireActivity())
        binding.colorRecycler.setAdapter(coloradapter)

       // tvT1.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.fontSeek.getProgress().toFloat())

        binding.fontSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                selectedTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, seekBar.getProgress().toFloat())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

    }

    val fontList: MutableList<FontModel>   = ArrayList()
    val colorList: MutableList<ColorModel> = ArrayList()
    var adapter: FontAdapter?              = null
    var coloradapter: ColorAdapter?        = null

    override fun onResume() {
        super.onResume()
        (activity as DashboardActivity?)!!.getSupportActionBar()?.hide()
        (activity as DashboardActivity?)!!.mainMenu.visibility = View.GONE

        (activity as DashboardActivity?)!!.mainLy.visibility     = View.GONE
        (activity as DashboardActivity?)!!.tvTileMain.visibility = View.GONE
        (activity as DashboardActivity?)!!.tvTitle.setText("")
    }

    override fun onClickListener(pos: Int, id: Int, list: FontModel) {

        for (i in 0 until fontList.size) {
            fontList.get(i).setmSelectd(false)
        }

        fontList.get(pos).setmSelectd(true)
        adapter!!.notifyDataSetChanged()

        selectedTextView.setTypeface(list.face)

    }

    override fun onClickListener(pos: Int, id: Int, list: ColorModel) {

        for (i in 0 until colorList.size) {
            colorList.get(i).setmSelected(false)
        }

        colorList.get(pos).setmSelected(true)
        coloradapter!!.notifyDataSetChanged()

        selectedTextView.setTextColor(resources.getColor(list.color))
    }

    // open gallery
    private fun GalleryOnclick() {
        // if user declin Deny permission then
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Util.hasReadPermissions(activity) && Util.hasWritePermissions(activity) && Util.hasCameraPermissions(activity)) {
                val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            } else {
                requestAppPermissions()
            }
        } else {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, 1)
        }
    }

    private fun requestAppPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return
        }
        if (Util.hasReadPermissions(activity) && Util.hasWritePermissions(activity) && Util.hasCameraPermissions(activity)) {
            return
        }
        ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA), 100) // your request code
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != Activity.RESULT_CANCELED) {

            Log.e("onActivityResult", "onActivityResult: " + "success" )

            when (requestCode) {

                1 -> if (resultCode == Activity.RESULT_OK) {

                    Log.e("onActivityResult", "onActivityResult: " + "0" )

                    //    val selectedImageUri = data!!.data
                    val selectedImageUri = data!!.data
                    val imageEncoded: String = Util.getRealPathFromURI(activity, selectedImageUri)
                    val selectedImage: Bitmap = BitmapFactory.decodeFile(imageEncoded)

                    img1.setImageBitmap(selectedImage)

                }
              }
           }
        }

    var childView: View? = null

    private fun layoutGraber(){

        Log.e("tempid", "layoutGraber: " + tempid )


        if(tempid.equals("temp_5")){

            childView = layoutInflater.inflate(R.layout.temp_5 , null)
            binding.mainFrame.removeAllViews()
            binding.mainFrame.addView(childView)

            tvT1    = childView!!.findViewById(R.id.tvT1)
            tvT2    = childView!!.findViewById(R.id.tvT2)

            img1    = childView!!.findViewById(R.id.img1)
            img1.setOnTouchListener(ImageDrageListener())
            imgFram = childView!!.findViewById(R.id.imgFram)

            selectedTextView = tvT1

            tvT1.setOnTouchListener(OnTouchListener { v, event ->

                selectedTextView = tvT1
                tvT1.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);
                tvT2.setBackgroundResource(R.drawable.drawable_null);
                imgFram.isClickable = true

                false })

            tvT2.setOnTouchListener(OnTouchListener { v, event ->

                selectedTextView = tvT2
                tvT2.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);
                tvT1.setBackgroundResource(R.drawable.drawable_null);
                imgFram.isClickable = true

                false })

            imgFram.setOnClickListener {

                tvT1.setBackgroundResource(R.drawable.drawable_null);
                tvT2.setBackgroundResource(R.drawable.drawable_null);
                imgFram.isClickable = false

            }

        }else if (tempid.equals("temp_4")){

            childView = layoutInflater.inflate(R.layout.temp_4 , null)
            binding.mainFrame.removeAllViews()
            binding.mainFrame.addView(childView)

            try {
                tvT1    = childView!!.findViewById(R.id.tvT1)

                imgFram = childView!!.findViewById(R.id.imgFram)

                selectedTextView = tvT1

                tvT1.setOnTouchListener(OnTouchListener { v, event ->

                    selectedTextView = tvT1
                    tvT1.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);
                    imgFram.isClickable = true

                    false })

                imgFram.setOnClickListener {

                    tvT1.setBackgroundResource(R.drawable.drawable_null);
                    imgFram.isClickable = false

                }

            } catch (e: Exception) { }

            img1    = childView!!.findViewById(R.id.img1)
            img1.setOnTouchListener(ImageDrageListener())



        }else if (tempid.equals("temp_3")){

            childView = layoutInflater.inflate(R.layout.temp_3 , null)
            binding.mainFrame.removeAllViews()
            binding.mainFrame.addView(childView)

            tvT1    = childView!!.findViewById(R.id.tvT1)
            tvT2    = childView!!.findViewById(R.id.tvT2)

            imgFram = childView!!.findViewById(R.id.imgFram)
            img1    = childView!!.findViewById(R.id.img1)
            img1.setOnTouchListener(ImageDrageListener())

            selectedTextView = tvT1

           // tvT1.setOnTouchListener(SnapDrag(this.requireContext()))

            tvT1.setOnTouchListener(OnTouchListener { v, event ->

                selectedTextView = tvT1
                imgFram.isClickable = true

                tvT1.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);
                tvT2.setBackgroundResource(R.drawable.drawable_null);

                false })


            tvT2.setOnTouchListener(OnTouchListener { v, event ->

                selectedTextView = tvT2
                imgFram.isClickable = true

                tvT1.setBackgroundResource(R.drawable.drawable_null);
                tvT2.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);

                false })



            imgFram.setOnClickListener {

                tvT1.setBackgroundResource(R.drawable.drawable_null)
                tvT2.setBackgroundResource(R.drawable.drawable_null)
                imgFram.isClickable = false

            }

        }else if (tempid.equals("temp_2")){


            childView = layoutInflater.inflate(R.layout.temp_2 , null)
            binding.mainFrame.removeAllViews()
            binding.mainFrame.addView(childView)

            tvT0    = childView!!.findViewById(R.id.tvT0)
            tvT1    = childView!!.findViewById(R.id.tvT1)
            tvT2    = childView!!.findViewById(R.id.tvT2)
            tvT3    = childView!!.findViewById(R.id.tvT3)

            imgFram = childView!!.findViewById(R.id.imgFram)

            img1    = childView!!.findViewById(R.id.img1)

            img1.setOnTouchListener(ImageDrageListener())

            selectedTextView = tvT0

            tvT0.setOnTouchListener(OnTouchListener { v, event ->

                selectedTextView = tvT0
                imgFram.isClickable = true


                tvT0.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);
                tvT1.setBackgroundResource(R.drawable.drawable_null);
                tvT2.setBackgroundResource(R.drawable.drawable_null);
                tvT3.setBackgroundResource(R.drawable.drawable_null);

                false })


            tvT1.setOnTouchListener(OnTouchListener { v, event ->

                selectedTextView = tvT1
                imgFram.isClickable = true

                tvT0.setBackgroundResource(R.drawable.drawable_null);
                tvT1.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);
                tvT2.setBackgroundResource(R.drawable.drawable_null);
                tvT3.setBackgroundResource(R.drawable.drawable_null);

                false })


            tvT2.setOnTouchListener(OnTouchListener { v, event ->

                selectedTextView = tvT2
                imgFram.isClickable = true

                tvT0.setBackgroundResource(R.drawable.drawable_null);
                tvT1.setBackgroundResource(R.drawable.drawable_null);
                tvT2.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);
                tvT3.setBackgroundResource(R.drawable.drawable_null);

                false })


            tvT3.setOnTouchListener(OnTouchListener { v, event ->

                selectedTextView = tvT3
                imgFram.isClickable = true

                tvT0.setBackgroundResource(R.drawable.drawable_null);
                tvT1.setBackgroundResource(R.drawable.drawable_null);
                tvT2.setBackgroundResource(R.drawable.drawable_null);
                tvT3.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);

                false })


            imgFram.setOnClickListener {

                tvT0.setBackgroundResource(R.drawable.drawable_null)
                tvT1.setBackgroundResource(R.drawable.drawable_null)
                tvT2.setBackgroundResource(R.drawable.drawable_null)
                tvT3.setBackgroundResource(R.drawable.drawable_null)
                imgFram.isClickable = false

            }

        }else if (tempid.equals("temp_1")){

            childView = layoutInflater.inflate(R.layout.temp_1 , null)
            binding.mainFrame.removeAllViews()
            binding.mainFrame.addView(childView)

            imgFram = childView!!.findViewById(R.id.imgFram)
            img1    = childView!!.findViewById(R.id.img1)
            img1.setOnTouchListener(ImageDrageListener())


            tvT1    = childView!!.findViewById(R.id.tvT1)
            tvT2    = childView!!.findViewById(R.id.tvT2)

            selectedTextView = tvT1

            tvT1.setOnTouchListener(OnTouchListener { v, event ->

                selectedTextView = tvT1
                imgFram.isClickable = true

                tvT1.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);
                tvT2.setBackgroundResource(R.drawable.drawable_null);

                false })

            tvT2.setOnTouchListener(OnTouchListener { v, event ->

                selectedTextView = tvT2
                imgFram.isClickable = true

                tvT1.setBackgroundResource(R.drawable.drawable_null);
                tvT2.setBackgroundResource(R.drawable.drawable_templat_corner_boarder);

                false })

            imgFram.setOnClickListener {

                tvT1.setBackgroundResource(R.drawable.drawable_null)
                tvT2.setBackgroundResource(R.drawable.drawable_null)
                imgFram.isClickable = false

            }
        }
    }

    private fun editTitleDialog(tvT1 :RotatableAutofitEditText) {
        val dialog = Dialog(this.requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_edit_)

        val editTitle = dialog.findViewById<EditText>(R.id.editTitle)
        val ok        = dialog.findViewById<Button>(R.id.ok)
        val cancel    = dialog.findViewById<Button>(R.id.cancel)

        editTitle.setText(tvT1.text.toString())
        ok.setOnClickListener {
            tvT1.setText(editTitle.text.toString())
            dialog.cancel()
        }

        cancel.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    public class SnapDrag(requireContext: Context) : OnTouchListener {

        private val isTextMode = true

        private var dX     = 0f
        private var dY     = 0f
        private var result = false

        var gestureDetecture: GestureDetector? = null

        override fun onTouch(v: View, event: MotionEvent): Boolean {

           // gestureDetecture = GestureDetector(requireContext, GestureListener())

            event.action
          //  result = gestureDetecture!!.onTouchEvent(event)

          //  if (result == true) {
               // showDeleteDialog(v)
           // }

            return if (isTextMode) {
                event.rawX
                event.rawY
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = v.x - event.rawX
                        dY = v.y - event.rawY
                    }
                    MotionEvent.ACTION_UP -> {
                    }
                    MotionEvent.ACTION_POINTER_DOWN -> {
                    }
                    MotionEvent.ACTION_POINTER_UP -> {
                    }
                    MotionEvent.ACTION_MOVE -> v.animate().x(event.rawX + dX)
                        .y(event.rawY + dY).setDuration(0).start()
                }
                true
            } else {
                false
            }
        }
    }


}