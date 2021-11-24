package com.lylx.featuredemo

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.lylx.featuredemo.provider.getUriForFile
import com.lylx.featuredemo.util.createImageFile

class ResultActivity : AppCompatActivity() {

  companion object {
    private const val TAG = "ResultActivity"
    private const val MIME_IMAGE = "image/*"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_result)

    findViewById<View>(R.id.selectPhoto).setOnClickListener {
      photoLauncher.launch(MIME_IMAGE)
    }

    findViewById<View>(R.id.takePicture).setOnClickListener {
//      Environment.getExternalStorageDirectory()?.resolve("${Environment.DIRECTORY_PICTURES}/image.png")?.let {
//        takePictureLauncher.launch(getUriForFile(this, it))
//      }
      createImageFile(this)?.let {
        takePictureLauncher.launch(getUriForFile(this, it))
      }
    }

    findViewById<View>(R.id.requestPermission).setOnClickListener {
      requestPermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
    }

    findViewById<View>(R.id.requestMultiplePermissions).setOnClickListener {
      Log.d(TAG, "${ActivityCompat.shouldShowRequestPermissionRationale(this, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)Manifest.permission.READ_MEDIA_AUDIO else Manifest.permission.READ_EXTERNAL_STORAGE)}")
      val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        arrayOf(Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO)
      } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
      }
      requestMultiplePermissionsLauncher.launch(permissions)
    }
  }

  /**
   * GetContent : 通过 Intent.ACTION_GET_CONTENT 获取一个文件
   * @param : MIME 类型
   * @return : 文件 Uri
   */
  private val photoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
    Log.d(TAG, "select photo uri=$it")
  }

  /**
   * TakePicture : 通过 MediaStore.ACTION_IMAGE_CAPTURE 拍照并保存
   * @param : 保存文件的 Uri
   * @return : 是否保存成功
   */
  private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
      Log.d(TAG, "take picture result : $it")
  }

  /**
   * TakePicture : 请求单个权限
   * @param : Manifest.permission.*
   * @return : 用户是否授予权限
   */
  private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
    Log.d(TAG, "permission is granted : $it")
  }

  /**
   * TakePicture : 请求多个权限
   * @param : Array<Manifest.permission.*>
   * @return : Map<Manifest.permission.*, 用户是否授予权限>
   */
  private val requestMultiplePermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
    it.map { permissionResult ->
      Log.d(TAG, "${permissionResult.key} is granted : ${permissionResult.value}")
    }
  }
}