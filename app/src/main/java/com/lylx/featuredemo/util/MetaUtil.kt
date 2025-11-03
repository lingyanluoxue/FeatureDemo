package com.lylx.featuredemo.util

import android.util.Log
import com.beaglebuddy.id3.enums.Language
import com.beaglebuddy.mp3.MP3
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import org.jaudiotagger.tag.id3.AbstractID3v2Frame
import org.jaudiotagger.tag.id3.AbstractID3v2Tag
import org.jaudiotagger.tag.id3.ID3v24Frame
import org.jaudiotagger.tag.id3.ID3v24Frames
import org.jaudiotagger.tag.id3.framebody.FrameBodySYLT
import org.jaudiotagger.tag.id3.framebody.FrameBodyUSLT
import org.jaudiotagger.tag.id3.valuepair.TextEncoding
import java.io.ByteArrayOutputStream
import java.io.File


object MetaUtil {

  private const val lyrics = "[00:31.02]雾夜四周是蒙\n" +
      "[00:35.87]蒙胧像过往的梦\n" +
      "[00:41.43]未能睡去\n" +
      "[00:46.23]徘徊路中\n" +
      "[00:47.24]想不到竟跟你又再逢\n" +
      "[00:47.89]霓虹雾夜暗闪动\n" +
      "[00:58.53]像想看清你面容\n" +
      "[01:03.64]自说早已将你淡忘\n" +
      "[01:07.25]心窝却听不懂\n" +
      "[01:12.21]仍然未肯冰冻\n" +
      "[01:17.32]遥望你洒脱面容\n" +
      "[01:22.23]仍是觉心动\n" +
      "[01:28.72]但你的臂弯有别人\n" +
      "[01:31.93]她使你再不有空\n" +
      "[01:38.92]然后我双眼变红\n" +
      "[01:44.21]长夜变黑洞\n" +
      "[01:49.67]尽快躲进漆黑街角\n" +
      "[01:53.75]不阻你与她抱拥\n" +
      "[01:58.33]泪暗涌\n" +
      "[02:01.23]泪之中\n" +
      "[02:03.69]恨爱几多重\n" +
      "[02:09.60]泪暗涌\n" +
      "[02:11.78]泪之中\n" +
      "[02:14.29]恨爱几多重\n" +
      "[02:27.99]雾夜四周是蒙\n" +
      "[02:33.67]蒙胧像过往的梦\n" +
      "[02:39.57]逐游荡去\n" +
      "[02:40.15]徘徊路中\n" +
      "[02:40.87]一刻瘦小猫却共我逢\n" +
      "[02:49.30]迷途像碎了的梦\n" +
      "[02:51.17]夜猫瘦得带病容\n" +
      "[02:58.28]在我的脚边不舍去\n" +
      "[03:00.47]仿佛怕冷亲晚空\n" +
      "[03:06.64]垂头拾起它相看\n" +
      "[03:11.58]还是我今晚独行\n" +
      "[03:17.39]流浪似飘梦\n" +
      "[03:22.68]若怕孤寡请结伴行\n" +
      "[03:25.81]小猫你可听懂\n" +
      "[03:32.47]然后我双眼又变红\n" +
      "[03:40.69]长夜变黑洞\n" +
      "[03:45.37]又再忆记起爱人\n" +
      "[03:49.85]抛低我于黑暗中\n" +
      "[03:53.16]泪暗涌\n" +
      "[03:54.87]泪之中\n" +
      "[03:58.33]恨爱几多重\n" +
      "[04:04.56]泪暗涌\n" +
      "[04:08.06]泪之中\n" +
      "[04:12.10]恨爱几多重\n" +
      "[04:15.51]泪暗涌\n" +
      "[04:19.15]泪之中\n" +
      "[04:20.09]恨爱几多重\n" +
      "[04:26.28]泪暗涌\n" +
      "[04:28.56]泪之中\n" +
      "[04:31.00]恨爱几多重\n"

  fun writeMetadataToMp3File(path:String) {
    val startTime = System.currentTimeMillis()
    val mp3File = MP3(path)
    mp3File.setTitle("test")
    mp3File.setAlbum("Album")
    mp3File.setLyrics(Language.ZHO, lyrics)
//    mp3File.setLyrics(Language.ENG, "Get a little frightened sometimes\nA little cold inside\nCatching bad news on the radio\n")
    mp3File.save()
    Log.e("lylx", "time:${System.currentTimeMillis() - startTime}")

    Log.e("lylx", "lyrics:${mp3File.lyrics}")
    Log.e("lylx", "lyrics:${mp3File.getLyrics(Language.ZHO)}")
  }

  fun writeLyrics(file: File) {
    val startTime = System.currentTimeMillis()
    // 读取文件
    val audioFile = AudioFileIO.read(file)
    // 获取标签
    val tag = audioFile.getTagOrCreateAndSetDefault()
    // 3. 读取或修改标签
//    tag.setField(FieldKey.TITLE, "test")
//    tag.setField(FieldKey.ARTIST, "artist")
//    tag.setField(FieldKey.ALBUM, "album")
    tag.setField(FieldKey.LYRICS, lyrics)

    // 4. 保存更改
    audioFile?.commit()
    Log.e("lylx", "time:${System.currentTimeMillis() - startTime}")
  }

  fun deleteLyrics(file: File) {
    // 读取文件
    val audioFile = AudioFileIO.read(file)
    // 获取标签
    val tag = audioFile.getTagOrCreateAndSetDefault()
    tag.deleteField(FieldKey.LYRICS)
    audioFile?.commit()
  }

  fun writeSyncLyrics(file:File, language: String?,
    description: String?, lyricLines: MutableList<SyncLyricLine>) {
    runCatching {
      // 读取文件
      val audioFile = AudioFileIO.read(file)
      // 获取标签
      val tag = audioFile.getTagOrCreateAndSetDefault()
      val lyricsData: ByteArray? = createSyncLyricsData(lyricLines)

      val body = FrameBodySYLT(
        TextEncoding.UTF_16.toInt(),  // textEncoding
        language,  // language
        2,  // timeStampFormat (milliseconds)
        1,  // contentType (lyrics)
        description,  // description
        lyricsData // lyrics data
      )

      val frame: AbstractID3v2Frame = ID3v24Frame(ID3v24Frames.FRAME_ID_SYNC_LYRIC)
      frame.setBody(body)
      tag.setField(frame)
//      (tag as? AbstractID3v2Tag)?.setFrame(frame)
      audioFile?.commit()
    }.getOrElse {
      Log.e("lylx", "e:$it")
    }
  }


  private fun createSyncLyricsData(lyricLines: MutableList<SyncLyricLine>): ByteArray {
    try {
      val baos = ByteArrayOutputStream()

      for (line in lyricLines) {
        if (line.text?.isNotEmpty() == true) {
          // 添加歌词文本（UTF-16BE，不带 BOM）
          val textBytes = line.text.toByteArray(charset("UTF-16BE"))
          baos.write(textBytes)


          // 添加终止符（UTF-16 需要两个 0x00）
          baos.write(0x00)
          baos.write(0x00)
        } else {
          // 空行也需要终止符
          baos.write(0x00)
          baos.write(0x00)
        }


        // 添加时间戳（4字节，大端序）
        val timestamp = line.timestamp
        baos.write((timestamp shr 24) and 0xFF)
        baos.write((timestamp shr 16) and 0xFF)
        baos.write((timestamp shr 8) and 0xFF)
        baos.write(timestamp and 0xFF)
      }

      return baos.toByteArray()
    } catch (e: java.lang.Exception) {
      e.printStackTrace()
      return ByteArray(0)
    }
  }

  fun writeStaticLyrics(file:File, language: String?, description: String?, lyrics: String?) {
    // 读取文件
    val audioFile = AudioFileIO.read(file)
    // 获取标签
    val tag = audioFile.getTagOrCreateAndSetDefault()
    runCatching {
      val body = FrameBodyUSLT()
      body.textEncoding = TextEncoding.UTF_8
      body.language = language
      body.description = description
      body.lyric = lyrics

      val frame = ID3v24Frame(ID3v24Frames.FRAME_ID_UNSYNC_LYRICS)
      frame.setBody(body)
      tag.setField(frame)
//      (tag as? AbstractID3v2Tag)?.setFrame(frame)
      audioFile?.commit()
    }.getOrElse {
      Log.e("lylx", "e:$it")
    }
  }

  // 获取所有静态歌词
  fun getAllStaticLyrics(file: File): MutableMap<String?, String?> {
    // 读取文件
    val audioFile = AudioFileIO.read(file)
    // 获取标签
    val tag = audioFile?.getTagOrCreateAndSetDefault()
    val lyricsMap: MutableMap<String?, String?> = hashMapOf()

    val usltFrames: MutableList<AbstractID3v2Frame> = getFrame(tag, "USLT")
    for (frame in usltFrames) {
      if (frame.getBody() is FrameBodyUSLT) {
        val body = frame.getBody() as FrameBodyUSLT
        val key = body.getLanguage() + ":" + body.getDescription()
        lyricsMap.put(key, body.lyric)
      }
    }

    return lyricsMap
  }

  // 获取特定语言的静态歌词
  fun getStaticLyricsByLanguage(file: File, language: String): String? {
    // 读取文件
    val audioFile = AudioFileIO.read(file)
    // 获取标签
    val tag = audioFile?.getTagOrCreateAndSetDefault()
    val usltFrames: MutableList<AbstractID3v2Frame> = getFrame(tag, "USLT")

    for (frame in usltFrames) {
      if (frame.getBody() is FrameBodyUSLT) {
        val body = frame.getBody() as FrameBodyUSLT
        if (language == body.getLanguage()) {
          return body.lyric
        }
      }
    }

    return null
  }

  // 删除特定语言的歌词
  fun removeLyricsByLanguage(file: File, language: String) {
    // 删除静态歌词
    removeFramesByLanguage(file, "USLT", language)


    // 删除同步歌词
    removeFramesByLanguage(file, "SYLT", language)
  }

  private fun removeFramesByLanguage(file: File, frameType: String, language: String) {
    // 读取文件
    val audioFile = AudioFileIO.read(file)
    // 获取标签
    val tag = audioFile?.getTagOrCreateAndSetDefault() as? AbstractID3v2Tag
    val frames: MutableList<AbstractID3v2Frame> = getFrame(tag, frameType)

    for (frame in frames) {
      if (frameType == "USLT" && frame.getBody() is FrameBodyUSLT) {
        val body = frame.getBody() as FrameBodyUSLT
        if (language == body.getLanguage()) {
          tag?.removeFrame(frame.identifier)
        }
      } else if (frameType == "SYLT" && frame.getBody() is FrameBodySYLT) {
        val body = frame.getBody() as FrameBodySYLT
        if (language == java.lang.String(body.getLanguage()) as String) {
          tag?.removeFrame(frame.identifier)
        }
      }
    }
  }

  fun readAllLyrics(file: File) {
    try {
      val audioFile = AudioFileIO.read(file)
      val tag: Tag? = audioFile.getTag()

      if (tag != null) {
        // 读取静态歌词 (USLT)
        readUSLTFrames(tag)


        // 读取同步歌词 (SYLT)
        readSYLTFrames(tag)


        // 读取标准歌词字段
        readStandardLyrics(tag)
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  private fun readUSLTFrames(tag: Tag?) {
    Log.e("lylx", "--------静态歌词 Start--------")
    getFrame(tag, ID3v24Frames.FRAME_ID_UNSYNC_LYRICS).forEach {
      (it.body as? FrameBodyUSLT)?.let {
        Log.e("lylx", "语言: " + it.language)
        Log.e("lylx", "描述: " + it.description)
        Log.e("lylx", "内容: " + it.lyric)
      }
    }
    Log.e("lylx", "--------静态歌词 End--------")
  }

  private fun readSYLTFrames(tag: Tag?) {
    Log.e("lylx", "--------动态歌词 Start--------")
    getFrame(tag, "SYLT").forEach {
      (it.body as? FrameBodySYLT)?.let {
        Log.e("lylx", "语言: " + it.language)
        Log.e("lylx", "描述: " + it.description)
        Log.e("lylx", "内容类型: " + it.contentType)
        Log.e("lylx", "文件编码: " + it.textEncoding)
      }
      Log.e("lylx", "--------动态歌词 End--------")
    }
  }

  private fun readStandardLyrics(tag: Tag) {
    // 读取标准歌词字段
    Log.e("lylx", "--------标准歌词 Start--------")
    val lyrics: String? = tag.getFirst(FieldKey.LYRICS)
    if (lyrics != null && !lyrics.trim { it <= ' ' }.isEmpty()) {
      Log.e("lylx", "title:${tag?.getFirst(FieldKey.TITLE)} --- album:${tag?.getFirst(FieldKey.ALBUM)} -- lyrics:$lyrics")

    }
    Log.e("lylx", "--------标准歌词 End--------")
  }

  // 方法1：使用 getFields() 并过滤
  fun getFramesByType(tag: Tag?, frameType: String): MutableList<AbstractID3v2Frame> {
    val frames = mutableListOf<AbstractID3v2Frame>()

    if (tag is AbstractID3v2Tag) {
      val id3v2Tag = tag


      // 获取所有字段并过滤
      val fieldIterator = id3v2Tag.getFields()
      while (fieldIterator.hasNext()) {
        val field = fieldIterator.next()
        if (field is AbstractID3v2Frame) {
          val frame = field
          if (frameType == frame.getId()) {
            frames.add(frame)
          }
        }
      }
    }

    return frames
  }

  // 方法2：使用 frameOfType 迭代器（推荐）
  fun getFrame(tag: Tag?, frame: String?): MutableList<AbstractID3v2Frame> {
    val frames= mutableListOf<AbstractID3v2Frame>()
    (tag as? AbstractID3v2Tag)?.getFrame(frame)?.forEach {
      (it as? AbstractID3v2Frame)?.let {
        frames.add(it)
      }
    }

//    if (tag is AbstractID3v2Tag) {
//      val id3v2Tag = tag
//
//
//      // 使用 getFrameOfType 迭代器
//
//      val frameIterator = id3v2Tag.getFrameOfType(frameType)
//      while (frameIterator.hasNext()) {
//        (frameIterator.next() as? AbstractID3v2Frame)?.let {
//          frames.add(it)
//        }
//      }
//    }

    return frames
  }

  fun setCustomField(audioFile: File?, value: String?) {
    try {
      val file = AudioFileIO.read(audioFile)
      val tag = file.getTagOrCreateAndSetDefault()

      tag.setField(FieldKey.CUSTOM1, value);

      file.commit()
    } catch (e: java.lang.Exception) {
      e.printStackTrace()
    }
  }

  fun getCustomField(audioFile: File?): String? {
    try {
      val file = AudioFileIO.read(audioFile)
      val tag = file.getTag()

      if (tag != null) {
        return tag.getFirst(FieldKey.CUSTOM1)
      }
    } catch (e: java.lang.Exception) {
      e.printStackTrace()
    }
    return null
  }

}

data class SyncLyricLine(
  val timestamp: Int,// 毫秒
  val text: String?,
)