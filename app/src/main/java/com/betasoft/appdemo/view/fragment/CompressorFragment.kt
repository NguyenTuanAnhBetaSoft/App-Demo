package com.betasoft.appdemo.view.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.betasoft.appdemo.R
import com.betasoft.appdemo.data.model.MediaModel
import com.betasoft.appdemo.databinding.FragmentCompressorBinding
import com.betasoft.appdemo.utils.ToastUtils
import com.betasoft.appdemo.utils.download.DownloadUrl
import com.betasoft.appdemo.view.base.AbsBaseFragment
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.size
import id.zelory.compressor.loadBitmap
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.DecimalFormat
import java.util.Date
import kotlin.math.log10
import kotlin.math.pow

class CompressorFragment : AbsBaseFragment<FragmentCompressorBinding>() {
    private val args: CompressorFragmentArgs by navArgs()
    private var compressedImage: File? = null

    private val imagePathList = mutableListOf<MediaModel>()

    private val compressedImagePathList = ArrayList<File?>()


    override fun getLayout(): Int {
        return R.layout.fragment_compressor
    }

    override fun initView() {
        imagePathList.addAll(args.param.listMedeaModel)
        Log.d("54355435", "file = ${imagePathList[0].toString()}")
        binding.btnCompressed.setOnClickListener {
            compressImageClick(true, 50)
        }

        binding.imgActual.setImageBitmap(loadBitmap(imagePathList[0].file!!))
        binding.tvSizeActual.text =
            String.format(
                "Size : %s",
                getReadableFileSize(imagePathList[0].file!!.length())
            )

        binding.tvResolutionActual.text =
            "resolution: ${imagePathList[0].uri.getImageDimensions(requireContext())}"

        binding.btnDeleteCache.setOnClickListener {
            lifecycleScope.launch {
                File(requireContext().cacheDir, "compressor").deleteRecursively()
            }


        }

        binding.btnSaveImage.setOnClickListener {

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

    private fun Uri.getImageDimensions(context: Context): String {
        val inputStream = context.contentResolver.openInputStream(this)!!
        val exif = ExifInterface(inputStream)

        val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 150)
        val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 150)

        return "$width, $height"
    }

    private fun saveImage(file: File, isKeepImage: Boolean, quality: Int) {
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
        var fileName = ""
        if (isKeepImage) {
            val now = Date()
            fileName = now.time.toString()
        } else {

        }

        DownloadUrl.saveImage(
            quality,
            file,
            fileName,
            true,
            requireContext()
        )


    }

    private fun compressImageClick(isKeepImage: Boolean, quality: Int) {
        var compressJob: Job? = null
        try {
            for (i in args.param.listMedeaModel) {
                var file: File?

                compressJob = lifecycleScope.launch {
                    file = Compressor.compress(
                        requireContext(),
                        i.file!!
                    ) {
                        //destination(filePath.absoluteFile)
                        //resolution(1280, 720)
                        //calculateInSampleSize(BitmapFactory.Options(),50,50)
                        //resolution(507, 675)
                        //destination(file)
                        quality(quality)
                        format(Bitmap.CompressFormat.JPEG)
                        size(2_097_152) // 2 MB

                        setCompressedImage()

                    }

                    if (this@CompressorFragment.compressedImage != null) {
                        compressedImagePathList.add(file)

                    }
                }
                compressJob.invokeOnCompletion {
                    for (c in compressedImagePathList) {
                        c?.let {
                            saveImage(it, isKeepImage, 50)
                        }
                    }


                }
            }


        } catch (_: Exception) {

        }


    }


}