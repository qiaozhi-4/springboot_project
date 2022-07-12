const videoUploadVue = {
  template: `<div>
<!-- 上传部分 -->
<el-upload ref="upload" drag :action="action" 
  :accept="accept" :file-list="imageList" :limit="uploadLimit" 
  :before-upload="beforeUpload" :on-exceed="handleExceed" :on-success="handleSuccess" 
  :on-preview="handlePreview" :before-upload="beforeUpload" :before-remove="beforeRemove" :on-remove="handleRemove">
    <i class="el-icon-upload"></i>
    <div class="el-upload__text">将视频文件拖到此处，或<em>点击上传</em></div>
    <div class="el-upload__tip" slot="tip">支持常见的视频文件（mp4/mpeg/3gp等），最大不超过{{ uploadLimit }}M</div>
</el-upload>
<!-- 缩略图 -->
<el-divider v-if="showImagePreview"></el-divider>
<el-upload v-show="showImagePreview" disabled list-type="picture-card" :auto-upload="false" :file-list="imageList">
    <i slot="default" class="el-icon-plus"></i>
    <div slot="file" slot-scope="{file}">
      <img class="el-upload-list__item-thumbnail" :src="file.url" alt="file.name">
      <span class="el-upload-list__item-actions">
        <span class="el-upload-list__item-preview" @click="handlePreview(file)"><i class="el-icon-video-play"></i></span>
        <span class="el-upload-list__item-delete" @click="handleImagePreview(file)"><i class="el-icon-zoom-in"></i></span>
        <span class="el-upload-list__item-delete" @click="handlePreviewRemove(file)"><i class="el-icon-delete"></i></span>
      </span>
    </div>
</el-upload>
<!-- 预览部分 -->
<el-dialog v-if="selectedVideo" :visible.sync="dialogVideoVisible" @close='stopVideo'>
  <video ref="preview" width="100%" :src="selectedVideo.video" controls />
</el-dialog>
<el-dialog v-if="selectedVideo" :visible.sync="dialogImageVisible">
  <img width="100%" :src="selectedVideo.url" />
</el-dialog>
<!-- 画布剪裁 -->
<div v-if="captureVideo" style="position:absolute; right: 999999px;">
  <canvas id="videoCanvas" ref="videoCanvas" />
  <video id="canvasVideo" ref="canvasVideo" :width="captureWidth" :height="captureHeight" controls="controls"/>
</div>
<!-- 测试 -->
  </div>`,

  props: {
    uploadImageApi: {
      type: String,
      default: 'http://localhost:8080/upload/image',
    },
    deleteImageApi: {
      type: String,
      default: 'http://localhost:8080/upload/video/',
    },
    accept: {
      type: String,
      default: 'video/*',
    },
    action: {
      type: String,
      default: '#',
    },
    captureVideoTime: {
      type: Number,
      default: 1,
    },
    uploadLimit: {
      type: Number,
      default: 1000,
    },
    captureWidth: {
      type: Number | String,
      default: 480,
    },
    captureHeight: {
      type: Number | String,
      default: 'auto',
    },
  },
  data() {
    return {
      imageList: [],
      selectedVideo: null,
      captureVideo: false,
      dialogVideoVisible: false,
      dialogImageVisible: false,
      dialogVideoUrl: '',
    }
  },
  computed: {
    showImagePreview: function () {
      return this.imageList && this.imageList.length > 0
    }
  },
  watch: {},
  methods: {
    /*
    File结构：
      name: "协程第一话.mp4", percentage: 100, size, status, uid,
      raw: File, response: {name success suffix type url}
     */
    handleExceed() {
      this.$alert('超过最大上传数量！', '提示')
    },
    beforeUpload(file) {
      const isVideo = ['video/mp4', 'video/mpeg',].some(t => t === file.type);
      const isLimitSize = file.size / 1024 / 1024 < this.uploadLimit;
      if (!isVideo) {
        this.$message.error('必须上传视频文件!');
      }
      if (isVideo && !isLimitSize) {
        this.$message.error('上传视频最大支持1G!');
      }
      return isVideo && isLimitSize
    },
    handleSuccess(data, file, fileList) {
      if (!data.success) {
        this.$message.error('文件上传失败，请重试!');
        return
      }
      this.captureVideo = true
      this.$nextTick(() => this.captureVideoFrame(file))
    },
    async handleRemove(file, fileList) {
      try{
        let video = this.imageList.find(item => item.uid === file.uid)
        if (video == null) {
          this.$message.error('文件移除失败，可能已经被删除。');
        }

        // TODO 后台删除，改成自己的链接，选择自己的拼接方式
        /*await axios.delete(this.deleteImageApi, {
          params: {id: video.id},
        })*/

        let index = this.imageList.indexOf(video)
        this.imageList.splice(index, 1)
        this.$message.success('文件移除移除成功。');
      }catch {
        this.$alert('删除失败，可能是网络问题或者已经删除，请重试!');
      }
    },
    handlePreviewRemove(file) {
      this.beforeRemove(file)
        .then(_ => this.handleRemove(file))
        .catch()
    },
    beforeRemove(file, fileList) {
      return new Promise((resolve, reject) => {
        this.$confirm('该视频已经上传，移除后需要重新上传，确认移除？', '提示', {type: 'warning',})
          .then(_ => {
            this.$message({type: 'success', message: '视频被删除'})
            resolve()
          })
          .catch(_ => reject())
      })
    },
    handlePreview(video) {
      this.selectedVideo = video
      this.dialogVideoVisible = true
      this.dialogImageVisible = false
      this.$nextTick(() => this.$refs.preview.play())
    },
    handleImagePreview(video) {
      this.selectedVideo = video
      this.dialogVideoVisible = false
      this.dialogImageVisible = true
    },
    stopVideo() {
      this.$refs.preview.pause()
      this.selectedVideo = null
      this.dialogVideoVisible = false
    },
    captureVideoFrame(videoFile) {
      const videoData = videoFile.response
      const canvasVideo = this.$refs.canvasVideo
      canvasVideo.src = videoData.url
      const canvas = this.$refs.videoCanvas
      canvasVideo.crossOrigin = 'anonymous'
      canvasVideo.currentTime = this.captureVideoTime
      canvasVideo.oncanplay = () => {
        canvas.width = canvasVideo.clientWidth
        canvas.height = canvasVideo.clientHeight
        canvas.getContext('2d').drawImage(canvasVideo, 0, 0, canvasVideo.clientWidth, canvasVideo.clientHeight)
        // 截取后的视频封面
        let dialogImageUrl = canvas.toDataURL('image/png')
        const imagePreview = {
          uid: videoFile.uid,
          id: videoData.id,
          name: videoData.name,
          src: dialogImageUrl,
          url: dialogImageUrl,
          video: videoData.url,
        }
        this.imageList.push(imagePreview)
        // 这里可以多截几张图
        const file = this.dataURLtoFile(dialogImageUrl, videoData.name + '.jpg')
        let count = 1
        this.uploadVideoImage(file, videoData, count, imagePreview)
          .then(url => imagePreview.url = url)
          .catch()

        this.captureVideo = false
      }
    },
    async uploadVideoImage(file, videoData, count) {
      const formData = new FormData()
      formData.append('file', file)
      formData.append('videoId', videoData.id)
      formData.append('videoName', videoData.name)
      formData.append('count', count) // 第几张，暂时就1张
      try{
        const { data } = await axios.post(this.uploadImageApi, formData) // 填写自己的图片上传路径
        return data.url // 真实地址
      }catch {
        return null
      }
    },
    dataURLtoFile(dataUrl, filename) {
      const array = dataUrl.split(',')
      const mime = array[0].match(/:(.*?);/)[1]
      const baseString = window.atob(array[1])
      // const baseString = Buffer.from(array[1], 'base64').toString('utf8');
      let len = baseString.length
      const u8arr = new Uint8Array(len)
      while (len--) {
        u8arr[len] = baseString.charCodeAt(len)
      }
      return new File([u8arr], filename, {type: mime})
    },
  },
  mounted: function () {
  },
}

