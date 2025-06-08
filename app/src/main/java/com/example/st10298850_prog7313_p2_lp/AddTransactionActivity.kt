package com.example.st10298850_prog7313_p2_lp

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.st10298850_prog7313_p2_lp.data.AppDatabase
import com.example.st10298850_prog7313_p2_lp.data.Transaction
import com.example.st10298850_prog7313_p2_lp.databinding.ActivityAddTransactionBinding
import com.example.st10298850_prog7313_p2_lp.repositories.AccountRepository
import com.example.st10298850_prog7313_p2_lp.repositories.CategoryRepository
import com.example.st10298850_prog7313_p2_lp.repositories.TransactionRepository
import com.example.st10298850_prog7313_p2_lp.utils.ImagePreviewUtil
import com.example.st10298850_prog7313_p2_lp.utils.UserSessionManager
import com.example.st10298850_prog7313_p2_lp.viewmodels.AddTransactionViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding
    private lateinit var viewModel: AddTransactionViewModel
    private var receiptImageUri: Uri? = null
    private lateinit var photoUploadImage: ImageView
    private var currentPhotoPath: String = ""
    private var photoUri: Uri? = null
    private var currentReceiptPath: String? = null

    // NEW: Declare adapters here
    private lateinit var categoryAdapter: ArrayAdapter<String>
    private lateinit var accountAdapter: ArrayAdapter<String>

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            showImageSourceDialog()
        } else {
            Toast.makeText(this, "Permissions required to upload receipt", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            photoUploadImage.setImageURI(photoUri)
            receiptImageUri = photoUri
            currentReceiptPath = currentPhotoPath // From createImageFile()
            Log.d("CameraResult", "Photo URI: $photoUri, Path: $currentReceiptPath")
        } else {
            Log.e("CameraResult", "Camera capture failed or cancelled")
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri = result.data?.data
            if (selectedImageUri != null) {
                photoUri = selectedImageUri
                photoUploadImage.setImageURI(photoUri)
                receiptImageUri = photoUri
                currentReceiptPath = getRealPathFromUri(photoUri!!)
                Log.d("GalleryResult", "Picked URI: $photoUri, Real Path: $currentReceiptPath")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupViewModel()
        setupDropdowns()
        setupDatePickers()
        setupTabLayoutListener()
        setupReceiptPreview()
    }

    private fun setupUI() {
        binding.btnClose.setOnClickListener { finish() }
        photoUploadImage = binding.receiptImage
        photoUploadImage.setOnClickListener { checkPermissionsAndShowOptions() }
        binding.btnAddTransaction.setOnClickListener { addTransaction() }
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(applicationContext)
        val transactionRepository = TransactionRepository(database.transactionDao())
        val categoryRepository = CategoryRepository(database.categoryDao())
        val accountRepository = AccountRepository(database.accountDao(), database.userDao())

        val factory = AddTransactionViewModel.Factory(transactionRepository, categoryRepository, accountRepository)
        viewModel = ViewModelProvider(this, factory)[AddTransactionViewModel::class.java]

        val currentUserId = getCurrentUserId()
        viewModel.loadCategoriesForUser(currentUserId)
        viewModel.loadAccountsForUser(currentUserId)
    }


    private fun setupDropdowns() {
        // Initialize adapters once with empty lists
        categoryAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf())
        binding.etCategory.setAdapter(categoryAdapter)
        binding.etCategory.threshold = 1
        binding.etCategory.setOnClickListener { binding.etCategory.showDropDown() }

        accountAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf())
        binding.etAccount.setAdapter(accountAdapter)
        binding.etAccount.threshold = 1
        binding.etAccount.setOnClickListener { binding.etAccount.showDropDown() }

        // Observe categories and update adapter
        viewModel.categories.observe(this) { categories ->
            if (categories.isEmpty()) {
                lifecycleScope.launch {
                    viewModel.insertDefaultCategories(getCurrentUserId())
                    viewModel.loadCategoriesForUser(getCurrentUserId())
                }
            } else {
                // Clear old data and add fresh categories without duplicates
                val categoryNames = categories.map { it.name }.distinct()
                categoryAdapter.clear()
                categoryAdapter.addAll(categoryNames)
                categoryAdapter.notifyDataSetChanged()
            }
        }

        // Observe accounts and update adapter
        viewModel.accounts.observe(this) { accounts ->
            if (accounts.isEmpty()) {
                lifecycleScope.launch {
                    viewModel.loadAccountsForUser(getCurrentUserId())
                }
            } else {
                val accountNames = accounts.map { it.name }.distinct()
                accountAdapter.clear()
                accountAdapter.addAll(accountNames)
                accountAdapter.notifyDataSetChanged()
            }
        }
    }


    private fun setupDatePickers() {
        val calendar = Calendar.getInstance()
        val dateSetListener = { field: EditText ->
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                field.setText(dateFormat.format(calendar.time))
            }
        }

        binding.etStartDate.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener(binding.etStartDate),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.etEndDate.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener(binding.etEndDate),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupTabLayoutListener() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupReceiptPreview() {
        photoUploadImage.setOnClickListener {
            if (receiptImageUri != null || currentReceiptPath != null) {
                val previewPath = currentReceiptPath ?: receiptImageUri?.path
                previewPath?.let { ImagePreviewUtil.showImagePreviewDialog(this, it) }
            } else {
                checkPermissionsAndShowOptions()
            }
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        AlertDialog.Builder(this)
            .setTitle("Upload Receipt")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> takePhoto()
                    1 -> chooseFromGallery()
                    2 -> dialog.dismiss()
                }
            }
            .show()
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show()
            null
        }

        photoFile?.also {
            photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", it)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            takePictureLauncher.launch(takePictureIntent)
            Log.d("ImageSave", "Photo saved to: ${it.absolutePath}")
        }
    }

    private fun chooseFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir!!).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun addTransaction() {
        val amountText = binding.tvAmount.text.toString()
        val amount = amountText.toDoubleOrNull()
        val category = binding.etCategory.text.toString()
        val account = binding.etAccount.text.toString()
        val description = binding.etDescription.text.toString()
        val startDateStr = binding.etStartDate.text.toString()
        val endDateStr = binding.etEndDate.text.toString()

        if (amount == null || amount <= 0 || category.isEmpty() || account.isEmpty() || startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show()
            return
        }

        val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(startDateStr)?.time ?: return
        val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(endDateStr)?.time ?: return
        val accountId = viewModel.accounts.value?.find { it.name == account }?.accountId ?: return

        val transaction = Transaction(
            userId = getCurrentUserId(),
            type = if (binding.tabLayout.selectedTabPosition == 0) "Expense" else "Income",
            amount = amount,
            accountId = accountId,
            startDate = startDate,
            endDate = endDate,
            description = description,
            receiptPath = currentReceiptPath,
            category = category
        )

        viewModel.addTransaction(transaction)
        Log.d(TAG, "Transaction saved with path: ${transaction.receiptPath}")
        Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getCurrentUserId(): Long {
        val userId = UserSessionManager.getUserId(this)
        if (userId == -1L) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        return userId
    }

    private fun checkPermissionsAndShowOptions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionsNeeded = mutableListOf<String>()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.CAMERA)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES)
            }

            if (permissionsNeeded.isNotEmpty()) {
                requestPermissionLauncher.launch(permissionsNeeded.toTypedArray())
                return
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionsNeeded = mutableListOf<String>()
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.CAMERA)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (permissionsNeeded.isNotEmpty()) {
                requestPermissionLauncher.launch(permissionsNeeded.toTypedArray())
                return
            }
        }

        showImageSourceDialog()
    }


    private fun getRealPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        }
        return null
    }

    companion object {
        private const val TAG = "AddTransactionActivity"
    }
}
