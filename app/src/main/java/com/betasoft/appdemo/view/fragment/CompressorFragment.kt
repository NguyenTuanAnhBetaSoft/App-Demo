package com.betasoft.appdemo.view.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.exifinterface.media.ExifInterface

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.betasoft.appdemo.R
import com.betasoft.appdemo.databinding.FragmentCompressorBinding
import com.betasoft.appdemo.utils.BitmapUtil
import com.betasoft.appdemo.utils.BitmapUtil.executeAsyncTask
import com.betasoft.appdemo.utils.ToastUtils
import com.betasoft.appdemo.utils.download.DownloadUrl
import com.betasoft.appdemo.view.base.AbsBaseFragment
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import id.zelory.compressor.loadBitmap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

class CompressorFragment : AbsBaseFragment<FragmentCompressorBinding>() {
    private val args: CompressorFragmentArgs by navArgs()
    private var compressedImage: File? = null

    override fun getLayout(): Int {
        return R.layout.fragment_compressor
    }

    override fun initView() {
        Log.d("54355435", "file = ${args.param.file.toString()}")
        binding.btnCompressed.setOnClickListener {
            lifecycleScope.launch {
                compressedImage = Compressor.compress(requireContext(), args.param.file!!) {
                    //resolution(1280, 720)
                    resolution(150, 150)
                    quality(50)
                    format(Bitmap.CompressFormat.PNG)
                    size(2_097_152) // 2 MB

                    setCompressedImage()
                }
                if (this@CompressorFragment.compressedImage != null) {
                    ToastUtils.getInstance(requireContext()).showToast("Compressed")
                }
                Log.d("54355435", "compressed = ${compressedImage.toString()}")
            }
        }

        binding.imgActual.setImageBitmap(loadBitmap(args.param.file!!))
        binding.tvSizeActual.text =
            String.format("Size : %s", getReadableFileSize(args.param.file!!.length()))

        binding.tvResolutionActual.text = "resolution: ${args.param.uri.getImageDimensions(requireContext())}"

        binding.btnDeleteCache.setOnClickListener {
            lifecycleScope.launch {
                File(requireContext().cacheDir, "compressor").deleteRecursively()
            }


        }

        binding.btnSaveImage.setOnClickListener {
            saveImage()
        }

    }

    private fun setCompressedImage() {
        lifecycleScope.launch {
            delay(1000)
            compressedImage?.let {
                binding.imgCompressed.setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))

                binding.tvSizeCompressed.text =
                    String.format("Size : %s", getReadableFileSize(it.length()))
                ToastUtils.getInstance(requireContext())
                    .showToast("Compressed image save in " + it.path)
                Log.d("Compressor", "Compressed image save in " + it.path)
            }
        }

    }

    private fun getReadableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

    private fun test() {
//         fun Bitmap.compress(cacheDir: File, f_name: String): File? {
//            val f = File(cacheDir, "user$f_name.jpg")
//            f.createNewFile()
//            ByteArrayOutputStream().use { stream ->
//                compress(Bitmap.CompressFormat.JPEG, 70, stream)
//                val bArray = stream.toByteArray()
//                FileOutputStream(f).use { os -> os.write(bArray) }
//            }//stream
//            return f
//        }
    }

    fun Uri.getImageDimensions(context: Context): String {
        val inputStream = context.contentResolver.openInputStream(this)!!
        val exif = ExifInterface(inputStream)

        val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 150)
        val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 150)

        return "$width, $height"
    }

    private fun saveImage() {
        /*lifecycleScope.executeAsyncTask(onPreExecute = {
            //savingLayout.visibility = View.VISIBLE
        }, doInBackground = {
            var result: String? = null
            var bitmap = BitmapFactory.decodeFile(compressedImage!!.absolutePath)
            if (bitmap != null) {
                BitmapUtil.savePhoto(requireContext(), bitmap)
                result = "OK"
            }
            result
        }, onPostExecute = {
            if (it != null) {

            } else {

            }
        })*/

        lifecycleScope.launch {
            compressedImage?.let {
                DownloadUrl.saveImage(
                    it,
                    "ahihihih",
                    true,
                    requireContext()
                )
            }
        }
    }

}