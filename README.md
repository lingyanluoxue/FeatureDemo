# LearnFeature
**学习 Android 新特性**
## startActivityForResult
[Activity Result API](https://developer.android.com/training/basics/intents/result#kotlin)

### ActivityResultContract
|ActivityResultContract|description|param|return|
|:--:|:--:|:--:|:--:|
|StartActivityForResult|同 startActivityForResult|Intent|ActivityResult(resultCode, intent)|
|RequestPermission|请求单个权限|Manifest.permission.*|用户是否授予权限|
|RequestMultiplePermissions|请求多个权限|Array<Manifest.permission.*>|Map<Manifest.permission.*, 用户是否授予权限>|
|TakeVideo|通过 MediaStore.ACTION_VIDEO_CAPTURE 拍摄视频并保存|保存文件的 Uri|视频缩略图 Bitmap(缩略图 Bitmap 返回不稳定)|
|TakePicture|通过 MediaStore.ACTION_IMAGE_CAPTURE 拍照并保存|保存文件的 Uri|是否保存成功|
|TakePicturePreview|通过 MediaStore.ACTION_IMAGE_CAPTURE 拍照|Void|图片的 Bitmap|
|GetContent|通过 Intent.ACTION_GET_CONTENT 获取一个文件|MIME 类型|文件 Uri|
|GetMultipleContents|通过 Intent.ACTION_GET_CONTENT 及 Intent.EXTRA_ALLOW_MULTIPLE 获取一个或多个文件|MIME 类型|文件 List|
|OpenDocument|通过 Intent.ACTION_OPEN_DOCUMENT 获取文件|MIME 类型|文件 Uri|
|OpenMultipleDocuments|通过 Intent.ACTION_OPEN_DOCUMENT 及 Intent.EXTRA_ALLOW_MULTIPLE 获取一个或多个文件|MIME 类型|文件 List|
|OpenDocumentTree|通过 Intent.ACTION_OPEN_DOCUMENT_TREE 选择一个目录，获取目录 Uri 及该目录下的全部文件管理权|目录初始位置 Uri|选择目录 Uri|
|CreateDocument|通过 Intent.ACTION_CREATE_DOCUMENT 创建一个文件|默认文件名|选择目录后返回该文件的 Uri|
|PickContact|通过 Intent.ACTION_PICK 从系统通讯录中获取联系人|Void|联系人 Uri|
