const barrageStream = {
    template: `<div>
          <div class="barrage-stream" ref="barrageStream">
             <div class="barrage-block" v-for="(stream, streamIndex) of barrageStreamList" :key="'barrageStreamList' + streamIndex">
                <div class="barrage-block-item" v-for="(item, index) of stream" :key="'barrage-block-item' + index">
                    <span :style="{'color': item.color, 'backgroundColor': item.bgColor,}">{{ item.content }}</span>
                </div>
             </div>
           </div>
           <video ref="videoPlayer" :id="vid" class="video-custom video-js vjs-default-skin vjs-big-play-centered" 
                    controls preload="auto" data-setup='{}' :poster="poster">
                <source v-for="source in sources" :src="source.src" :type="source.type" />
                <p class="vjs-no-js">
                    你的浏览器不支持视频播放器！请开启JavaScript脚本或者更换浏览器以正常观看视频：
                    <a href="https://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a>
                </p>
           </video>
         </div>`,
    props: {
        // 视频相关属性 ====================
        // 视频源，可以有多个视频段，格式 {src, type}
        sources: {
            type: Array,
            default: () => [],
        },
        // 封面
        poster: {
            type: String,
            default: '',
        },
        // 自动播放
        autoplay: {
            type: Boolean,
            default: true,
        },
        // 一些配置，参考官网
        options: {
            type: Object,
            default: () => {},
        },
        // 弹幕相关属性 ====================
        // 弹幕列表
        barrageMsgList: {
            type: Array,
            default: () => [],
        },
        // 弹幕框下边预留位置
        bottomMargin: {
            type: Number,
            default: 100,
        },
        // 每行弹幕文字间隙
        lineHeight: {
            type: Number,
            default: 36,
        },
    },
    data() {
        return {
            vid: 'video' + (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1),
            videoPlayer: null, // 视频播放器对象引用
            barrageStreamRect: {}, // 弹幕尺寸
            barrageStreamList: [], // 弹幕轨道
            barrageStreamListNum: null, // 弹幕轨道数量
        }
    },
    watch: {
        barrageMsgList: {
            handler(newval) {
                if (newval && newval.length) {
                    // 获取随机轨道下标
                    let randomNum = Math.floor(Math.random() * this.barrageStreamListNum)
                    // 将弹幕推送到随机轨道中
                    let list = this.barrageStreamList[randomNum]
                    list && list.push(newval[newval.length - 1])
                    // 延时5s后，删除该弹幕。（时间与弹幕的播放时间有关）
                    setTimeout(() => list && list.shift(), 5000)
                }
            },
            deep: true,
        },
    },
    methods: {
        // 获取弹幕轨道播放流的数量
        getBarrageStreamList() {
            // 获取dom元素的信息
            this.barrageStreamRect = this.$refs.barrageStream.getBoundingClientRect();
            // 设置视频播放器尺寸（无法修改，样式有问题）
            this.$refs.videoPlayer.style.width = this.barrageStreamRect.width + 'px'
            this.$refs.videoPlayer.parentElement.style.width = this.barrageStreamRect.width + 'px'
            // 计算轨道的数量
            this.barrageStreamListNum = Math.floor((this.barrageStreamRect.height - this.bottomMargin) / this.lineHeight)
            // 初始化轨道
            this.barrageStreamList = []
            for (let i = 0; i < this.barrageStreamListNum; i++) {
                this.barrageStreamList.push([]);
            }
        },
        // 配置视频
        setupVideo() {
            const that = this;
            videojs(that.vid, that.options, function () {
                // 引用
                that.videoPlayer = this
                // 开始
                that.$emit('ready', this)
                // 获取保存的音量
                if (window.localStorage.volume) {
                    this.volume(window.localStorage.volume)
                }
                // 尝试自动播放
                if (this.autoplay) {
                    this.play().then(_ => that.getBarrageStreamList()).catch(_ => {
                        this.muted(true)
                        this.play().then(_ => that.getBarrageStreamList()).catch(err => videojs.log('自动播放失败：静音才能播放或者设备不支持！'))
                    })
                }

                /*
                TODO 如果要实现B站效果：
                    不要直接弹出弹幕，而是添加到消息中，并匹配视频时间再弹出弹幕
                    在play事件中开启轮询，找到视频当前时间下的弹幕并弹出
                    在stop/pause事件中关闭轮询
                 */

                // 常用的事件，可以自定义添加更多
                this.on('play', () => that.$emit('start', this))  // 开始播放
                this.on('pause', () => that.$emit('pause', this)) // 暂停播放
                this.on('ended', () => that.$emit('stop', this))  // 停止播放
                this.on('volumechange', () => {                     // 调整音量
                    const volume = this.volume()
                    that.$emit('volume', volume, this)
                    window.localStorage.volume = volume
                })
            })
        },
        // 配置弹幕
        setupBarrage() {
            this.getBarrageStreamList()
            window.addEventListener("resize", this.getBarrageStreamList);
            this.$refs.barrageStream.onclick = _ => {
                console.log('点击了弹幕', this.videoPlayer.paused)
                this.videoPlayer.paused() ? this.videoPlayer.play() : this.videoPlayer.pause()
            }
        },
    },
    mounted: function () {
        this.setupVideo()
        this.setupBarrage()
    },
    beforeDestroy: function () {
        window.removeEventListener("resize", this.getBarrageStreamList)
    },
}

