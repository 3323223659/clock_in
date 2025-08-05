<template>
  <div>
    <el-select
      style="width: 120%"
      v-model="value"
      filterable
      placeholder="请选择教室"
      @change="selectClass"
    >
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      >
      </el-option>
    </el-select>
  </div>
</template>

<script>
import { getClassLocationList } from "../../api/course";

export default {
  data() {
    return {
      dialogVisible: true,
      options: [],
      value: null,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    selectClass() {
      if (this.value !== null) {
        this.$emit("class-selected", this.value);
        this.dialogVisible = false;
      } else {
        this.$message.warning("请选择教室");
      }
    },
    async getList() {
      const data = await getClassLocationList();
      this.options = data.map((item) => ({
        value: item,
        label: item,
      }));
    },
  },
};
</script>

<style>
</style>