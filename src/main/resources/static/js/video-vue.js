const videoVue = {
  template: `<div>
      <video :id="id" class="video-js vjs-default-skin vjs-big-play-centered" controls preload="auto" width="640" :poster="poster" data-setup="{}">
        <source v-for="source in sources" :src="source.src" :type="source.type" />
        <p class="vjs-no-js">
          你的浏览器不支持视频播放器！请开启JavaScript脚本或者更换浏览器以正常观看视频：<a href="https://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a>
        </p>
      </video>
    </div>`,
  props: ['sources', 'poster', 'autoplay', 'options'],
  data() {
    return {
      id: 'video' + (((1+Math.random())*0x10000)|0).toString(16).substring(1),
    }
  },
  watch: {
  },
  mounted: function () {
    const that = this;
    // const video = that.$el.children[0]
    videojs(that.id, that.options, function(){
      // 开始
      that.$emit('ready', this)

      // 获取保存的音量
      if (window.localStorage.volume) {
        this.volume(window.localStorage.volume)
      }
      // 尝试自动播放
      if (this.autoplay) {
        this.play().then().catch(err => {
          this.muted(true)
          this.play().then().catch(err => videojs.log('自动播放失败：静音才能播放或者设备不支持！'))
        })
      }
      // 常用的事件
      this.on('play', () => that.$emit('start', this)) // 开始播放
      this.on('pause', () => that.$emit('pause', this)) // 暂停播放
      this.on('ended', () => that.$emit('stop', this)) // 停止播放
      this.on('volumechange', () => {
        const volume = this.volume()
        that.$emit('volume', volume, this)
        window.localStorage.volume = volume
      }) // 调整音量
    })
  },
}

