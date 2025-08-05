<template>
  <view class="dialog-mask" v-if="value" @click="close">
    <view class="dialog-box" @click.stop>
      <view id="qr-reader"></view>
    </view>
  </view>
</template>

<script>
import { Html5Qrcode } from "html5-qrcode";
export default {
  name: "Scan",
  model: { props: "value", event: "close" },
  props: {
    value: { type: Boolean, default: false },
  },
  watch: {
    value(val) {
      if (val) {
        this.$nextTick(() => {
          this.getCameras();
        });
      }
    },
  },
  data() {
    return {
      cameraId: "",
      html5QrCode: "",
    };
  },
  beforeDestroy() {
    this.stop();
  },
  methods: {
    getCameras() {
      uni.showLoading({ title: "相机启动中...", icon: "none" });
      Html5Qrcode.getCameras()
        .then((devices) => {
          /**
           * devices 是对象数组
           * 例如：[ { id: "id", label: "label" }]
           */
          if (devices && devices.length) {
            if (devices.length > 1) {
              this.cameraId = devices[1].id;
            } else {
              this.cameraId = devices[0].id;
            }
            console.log(this.cameraId, "cameraId");
            this.start();
          }
        })
        .catch((err) => {
          this.close();
          uni.showToast({ title: "启用相机失败", icon: "none" });
        });
    },
    start() {
      this.html5QrCode = new Html5Qrcode("qr-reader");
      setTimeout(() => {
        uni.hideLoading();
      }, 1500);
      this.html5QrCode
        .start(
          this.cameraId, // 传入cameraId参数，这个参数在之前的步骤中已经获取到.
          {
            fps: 10, // 设置摄像头的帧率为10帧每秒
            qrbox: { width: 300, height: 300 }, // 设置需要扫描的QR码区域，这里设置的是300x300的区域
            aspectRatio: 1, // 设置扫描结果的宽高比为1，即正方形
          },
          (qrCodeMessage) => {
            //  当成功读取到QR码时，执行此回调函数
            if (qrCodeMessage) {
              this.qrCodeMessage = qrCodeMessage;
              // console.log(this.qrCodeMessage);

              this.$emit("success", qrCodeMessage);
              this.close();
              this.stop();
            }
          },
          (errorMessage) => {}
        )
        .catch((err) => {
          // 如果扫描启动失败，执行此catch块中的代码
          uni.showToast({ title: `扫码失败：${err}`, icon: "none" });
        });
    },
    stop() {
      if (this.html5QrCode) {
        this.html5QrCode.stop().finally(() => {
          this.html5QrCode.clear();
          this.html5QrCode = null;
        });
      }
    },
    close() {
      this.stop(); // 确保在关闭组件时停止摄像头
      // this.value = false;
      this.$emit("close", false);
    },
  },
};
</script>

<style scoped>
.dialog-mask {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1000;
  height: 100vh;
  width: 100vw;
  background-color: rgba(0, 0, 0, 0.7);
}

.dialog-box {
  position: absolute;
  left: 50%;
  top: 40%;
  transform: translate(-50%, -50%);
  width: 70vw;
  height: 70vw;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fff;
  border-radius: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.2);
}

#qr-reader {
  width: 100%;
  height: 100%;
  border-radius: 20rpx;
}
</style>
