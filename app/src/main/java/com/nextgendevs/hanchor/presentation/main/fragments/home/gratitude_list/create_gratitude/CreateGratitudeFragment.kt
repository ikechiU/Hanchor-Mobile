package com.nextgendevs.hanchor.presentation.main.fragments.home.gratitude_list.create_gratitude

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import com.nextgendevs.hanchor.business.domain.models.Gratitude
import com.nextgendevs.hanchor.business.domain.utils.AreYouSureCallback
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentCreateGratitudeBinding
import com.nextgendevs.hanchor.presentation.main.fragments.home.gratitude_list.viewmodel.GratitudeViewModel
import com.nextgendevs.hanchor.presentation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutionException

class CreateGratitudeFragment : BaseCreateGratitudeFragment() {
    private var _binding: FragmentCreateGratitudeBinding? = null
    private val binding: FragmentCreateGratitudeBinding get() = _binding!!
    private val viewModel: GratitudeViewModel by viewModels()

    private var gratitude: Gratitude? = null
    private val PERMISSIONS_REQUEST_READ_STORAGE = 716236
    private var shouldObserveOnce = true

    private var uri: Uri? = null
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    private var _id = 0L
    private var title = "Blessing"
    private var message = ""
    private var imageUrl = ""
    private var imageId = "0"
    private var hasChangedImage = false


    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage
                .activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(context)
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uriContent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                areYouSure()
            }
        })
    }

    private fun OnBackPressedCallback.areYouSure() {
        activity?.areYouSureDialog("Navigate to Count Your Blessing", object : AreYouSureCallback {
            override fun proceed() {
                this@areYouSure.isEnabled = false
                activity?.onBackPressed()
            }

            override fun cancel() {
                Log.d(TAG, "cancel: pressed.")
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateGratitudeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments ?: return
        val args = CreateGratitudeFragmentArgs.fromBundle(bundle)
        gratitude = args.gratitude

        if (gratitude != null)
            setUpExistingGratitude(gratitude!!)

        binding.btnSave.setOnClickListener {
            saveGratitude()
        }

        binding.deleteGratitude.setOnClickListener {
            deleteGratitude()
        }

        binding.navigateUp.setOnClickListener {
            navigateToGratitudeFragment()
        }

        binding.addImage.setOnClickListener {
            if (isStoragePermissionGranted()) {
                cropActivityResultLauncher.launch(null)
            }
        }

        binding.addImage2.setOnClickListener {
            if (isStoragePermissionGranted()) {
                cropActivityResultLauncher.launch(null)
            }
        }

        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) { uri ->
            if (uri != null) {
                hasChangedImage = true
                this.uri = uri
                glideRequestManager.load(uri).into(binding.image)
                binding.imageContainer.visibility = View.VISIBLE
            }
        }

    }

    private fun saveGratitude() {
        if (binding.blessing.text.isNotEmpty() && binding.image.drawable != null) {
            val titleRequestBody = toRequestBody(title)
            val messageRequestBody = toRequestBody(binding.blessing.text.toString())
            val imageIdRequestBody = toRequestBody(imageId)

            if (_id == 0L) { //INSERT
                val file: MultipartBody.Part? = toMultipartBody(createTemporaryFile(uri!!))
                if (file != null) {
                    viewModel.insertGratitude(titleRequestBody, messageRequestBody, file)
                    subscribeObservers()
                } else {
                    getContext.displayToast("Image required")
                }
            } else { //UPDATE
                if(hasChangedImage) { //User has changed image their is Uri
                    val file = toMultipartBody(createTemporaryFile(uri!!))
                    if (file != null) {
                        viewModel.updateGratitude(_id, titleRequestBody, messageRequestBody, imageIdRequestBody,file)
                        subscribeObservers()
                    } else {
                        getContext.displayToast("Image required")
                    }
                } else { //User has not changed existing image from url
                    CoroutineScope(IO).launch {
                        try {
                            val cachedFile =
                                glideRequestManager.asFile().load(imageUrl).submit().get()
                            withContext(Dispatchers.Main) {
                                Log.d(TAG, "saveGratitude 1: ${Uri.parse(cachedFile.toString())}")
                                Log.d(TAG, "saveGratitude 2: ${Uri.parse(cachedFile.toString())}")

                                val file = toMultipartBody(createTemporaryFile(Uri.fromFile(cachedFile)))
                                if (file != null) {
                                    viewModel.updateGratitude(_id, titleRequestBody, messageRequestBody, imageIdRequestBody,file)
                                    subscribeObservers()
                                } else {
                                    getContext.displayToast("Image required")
                                }
                            }
                        } catch (e: ExecutionException) {
                            withContext(Dispatchers.Main) {
                                getContext.displayToast("Failed to get image file, check network and try again.")
                            }
                        }
                    }
                }
            }

        } else {
            getContext.toastMessage("Graditude message required and Image Required")
        }

    }

    private fun deleteGratitude() {
        viewModel.deleteGratitude(_id, imageId)
        subscribeObservers()
    }

    private fun navigateToGratitudeFragment() {
        val directions =
            CreateGratitudeFragmentDirections.actionCreateGratitudeFragmentToGratitudeFragment()
        Navigation.findNavController(binding.root).safeNavigate(directions)
    }

    private fun setUpExistingGratitude(gratitude: Gratitude) {
        binding.deleteGratitude.visibility = View.VISIBLE
        _id = gratitude.id
        title = gratitude.gratitudeTitle
        message = gratitude.gratitudeMessage
        imageUrl = gratitude.gratitudeImageUrl
        imageId = gratitude.gratitudeImageId
        binding.blessing.setText(message)
        glideRequestManager.load(imageUrl).into(binding.image)
    }

    private fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner) { gratitudeState ->
            uiCommunicationListener.displayProgressBar(gratitudeState.isLoading)

            if(gratitudeState.tokenExpired) {
                activity?.logoutUser(mySharedPreferences)
            }

            if (gratitudeState.insertResult != 0L) {
                if (shouldObserveOnce) {
                    shouldObserveOnce = false
                    navigateToGratitudeFragment()
                }
            }

            if (gratitudeState.updateResult != 0) {
                if (shouldObserveOnce) {
                    shouldObserveOnce = false
                    navigateToGratitudeFragment()
                }
            }

            if (gratitudeState.deleteResult != 0) {
                if (shouldObserveOnce) {
                    shouldObserveOnce = false
                    navigateToGratitudeFragment()
                }
            }

            processQueue(
                context = context,
                queue = gratitudeState.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onRemoveHeadFromQuery()
                    }
                })
        }
    }

    private fun isStoragePermissionGranted(): Boolean {
        if (
            ContextCompat.checkSelfPermission(
                getContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                getContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    PERMISSIONS_REQUEST_READ_STORAGE
                )
            }

            return false
        } else {
            // Permission has already been granted
            return true
        }
    }

    private fun toRequestBody(string: String): RequestBody {
        return string.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }

    private fun toMultipartBody(file: File?): MultipartBody.Part? {
        if (file != null) {
            if (file.exists()) {
                return MultipartBody.Part.createFormData(
                    "file",
                    file.name,
                    file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            }
        }
        return null
    }

    private fun createTemporaryFile(uri: Uri): File? {
        val inputStream = context?.contentResolver?.openInputStream(uri)
        val storageDir = getContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val tempFile =
            File.createTempFile(System.currentTimeMillis().toString(), ".jpg", storageDir)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return tempFile
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}