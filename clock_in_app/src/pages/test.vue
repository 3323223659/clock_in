<template>
  <div>
    <button @click="getLocation">获取当前位置</button>
    <div v-if="currentPosition">
      当前位置: 经度 {{ currentPosition.lng }}, 纬度 {{ currentPosition.lat }}
    </div>
    <div v-if="errorMessage">{{ errorMessage }}</div>
  </div>
</template>

<script>
import AMapLoader from "@amap/amap-jsapi-loader";

export default {
  data() {
    return {
      currentPosition: null,
      errorMessage: null,
      AMap: null,
    };
  },
  methods: {
    initAMap() {
      AMapLoader.load({
        key: "b3ffe779e7e8620a015eaeb55e5f9876",
        version: "2.0",
        plugins: ["AMap.Geolocation", "AMap.CitySearch"],
      })
        .then((AMap) => {
          this.AMap = AMap;
        })
        .catch((e) => {
          this.errorMessage = "地图加载失败：" + e.message;
        });
    },
    getLocation() {
      this.errorMessage = null;
      this.currentPosition = null;

      if (this.AMap) {
        // 首先尝试使用浏览器的Geolocation API
        if ("geolocation" in navigator) {
          navigator.geolocation.getCurrentPosition(
            (position) => {
              const { longitude, latitude } = position.coords;
              this.currentPosition = { lng: longitude, lat: latitude };
              alert(this.currentPosition);
            },
            (error) => {
              console.error("浏览器定位失败", error);
              alert("浏览器定位失败");
              this.tryAMapGeolocation();
            },
            { enableHighAccuracy: true, timeout: 5000, maximumAge: 0 }
          );
        } else {
          this.tryAMapGeolocation();
        }
      } else {
        this.errorMessage = "地图未初始化，请刷新页面重试";
      }
    },
    tryAMapGeolocation() {
      console.log("进入定位");
      alert("进入定位");
      const geolocation = new this.AMap.Geolocation({
        enableHighAccuracy: true,
        timeout: 10000,
        zoomToAccuracy: true,
        useNative: true,
        GeoLocationFirst: true,
      });

      geolocation.getCurrentPosition((status, result) => {
        alert("status");
        if (status === "complete") {
          const { lng, lat } = result.position;
          this.currentPosition = { lng, lat };
        } else {
          console.error("高德地图定位失败", result);
          alert("高德地图定位失败");
          this.fallbackToIPLocation();
        }
      });
    },
    fallbackToIPLocation() {
      console.log("既然你");
      alert("既然你");
      const citySearch = new this.AMap.CitySearch();
      citySearch.getLocalCity((status, result) => {
        if (status === "complete" && result.info === "OK") {
          this.errorMessage = "精确定位失败，已使用IP定位：" + result.city;
          const [lng, lat] = result.rectangle.split(";")[0].split(",");
          this.currentPosition = { lng, lat };
        } else {
          this.errorMessage = "所有定位方法均失败，请检查网络连接或浏览器设置";
        }
      });
    },
  },
  mounted() {
    this.initAMap();
  },
};
</script>

<style>
/* 样式可以根据需要自行调整 */
</style>
