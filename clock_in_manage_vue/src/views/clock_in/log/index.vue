<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="上课时间">
        <el-date-picker
          v-model="queryParams.time"
          type="datetime"
          placeholder="上课时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          v-hasPermi="['clock_in:log:getClass']"
          @change="handleDateChange"
        >
        </el-date-picker>
      </el-form-item>
      <!-- 课堂名称选择器 -->
      <el-form-item label="选择课堂">
        <el-select
          v-model="queryParams.courseName"
          filterable
          placeholder="请选择课堂"
          class="custom-select"
          :disabled="courseOptions.length === 0"
          @clear="resetCourseSelection"
          @change="isCourseSelected = true"
        >
          <el-option
            v-for="item in filteredCourseOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <!-- 班级名称选择器 -->
      <el-form-item label="选择班级">
        <el-select
          v-model="queryParams.className"
          filterable
          placeholder="请选择班级"
          class="custom-select"
          :disabled="classOptions.length === 0"
          @clear="resetClassSelection"
          @change="isClassSelected = true"
        >
          <el-option
            v-for="item in filteredClassOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--      <el-col :span="1.5">-->
      <!--        <el-button-->
      <!--          type="danger"-->
      <!--          plain-->
      <!--          icon="el-icon-delete"-->
      <!--          size="mini"-->
      <!--          :disabled="multiple"-->
      <!--          @click="handleDelete"-->
      <!--          v-hasPermi="['clock_in:log:remove']"-->
      <!--        >删除</el-button>-->
      <!--      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['clock_in:log:export']"
          >导出
        </el-button>
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table :data="logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="id" />
      <el-table-column
        prop="username"
        align="center"
        label="学号"
      ></el-table-column>
      <el-table-column
        prop="name"
        align="center"
        label="姓名"
      ></el-table-column>

      <el-table-column
        prop="status"
        align="center"
        label="打卡状态"
      ></el-table-column>
      <el-table-column
        prop="clockInTime"
        align="center"
        label="打卡时间"
      ></el-table-column>
      <!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-button-->
      <!--            size="mini"-->
      <!--            type="text"-->
      <!--            icon="el-icon-delete"-->
      <!--            @click="handleDelete(scope.row)"-->
      <!--            v-hasPermi="['clock_in:log:remove']"-->
      <!--          >删除</el-button>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改打卡记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课堂名称" prop="sourceName">
          <el-input v-model="form.sourceName" placeholder="请输入课堂名称" />
        </el-form-item>
        <el-form-item label="上课时间" prop="classTime">
          <el-date-picker
            clearable
            v-model="form.classTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择上课时间"
          >
          </el-date-picker>
        </el-form-item>
        <el-form-item label="班级名称" prop="className">
          <el-input v-model="form.className" placeholder="请输入班级名称" />
        </el-form-item>
        <el-form-item label="打卡人数" prop="clockCount">
          <el-input v-model="form.clockCount" placeholder="请输入打卡人数" />
        </el-form-item>
        <el-form-item label="未打卡人数" prop="noClock">
          <el-input v-model="form.noClock" placeholder="请输入未打卡人数" />
        </el-form-item>
        <el-form-item label="迟到人数" prop="belated">
          <el-input v-model="form.belated" placeholder="请输入迟到人数" />
        </el-form-item>
        <el-form-item label="请假人数" prop="leaved">
          <el-input v-model="form.leaved" placeholder="请输入请假人数" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listLog,
  getLog,
  delLog,
  addLog,
  updateLog,
  getClassAndCourse,
} from "@/api/clock_in/log";
import axios from "axios";

export default {
  name: "Log",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 打卡记录表格数据
      logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseName: "",
        classTime: null,
        className: "",
        clockCount: null,
        noClock: null,
        belated: null,
        leaved: null,
        time: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      classOptions: [], // 班级选项数据
      courseOptions: [], // 课堂选项数据
      tableData: [], // 表格数据
      isCourseSelected: false, // 记录课堂是否被选择
      isClassSelected: false, // 记录班级是否被选择
    };
  },
  created() {
    //this.getList();
  },
  computed: {
    // 班级名称过滤后的选项
    filteredClassOptions() {
      // 如果没有选中班级且有输入值时进行过滤
      if (!this.isClassSelected && this.queryParams.className !== "") {
        return this.classOptions.filter((option) =>
          option.label.includes(this.queryParams.className)
        );
      }
      // 如果选中了班级，显示所有选项
      return this.classOptions;
    },
    // 课堂名称过滤后的选项
    filteredCourseOptions() {
      // 如果没有选中课堂且有输入值时进行过滤
      if (!this.isCourseSelected && this.queryParams.courseName !== "") {
        return this.courseOptions.filter((option) =>
          option.label.includes(this.queryParams.courseName)
        );
      }
      // 如果选中了课堂，显示所有选项
      return this.courseOptions;
    },
  },
  methods: {
    // 重置课堂选项状态
    resetCourseSelection() {
      this.isCourseSelected = false;
      this.queryParams.courseName = ""; // 清除已选中的课堂
    },

    // 重置班级选项状态
    resetClassSelection() {
      this.isClassSelected = false;
      this.queryParams.className = ""; // 清除已选中的班级
    },
    /** 查询打卡记录列表 */
    getList() {
      this.loading = true;
      listLog(this.queryParams).then((response) => {
        if (response.data.total === 0) {
          this.$message.info("没有数据");
          this.loading = false;
          return;
        }
        this.logList = response.data.list;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        sourceName: null,
        classTime: null,
        className: null,
        clockCount: null,
        noClock: null,
        belated: null,
        leaved: null,
        createTime: null,
        updateTime: null,
        isDelete: null,
        time: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
      // this.queryParams = {
      //   pageNum: 1,
      //   pageSize: 10,
      //   courseName: '',
      //   classTime: null,
      //   className: '',
      //   clockCount: null,
      //   noClock: null,
      //   belated: null,
      //   leaved: null,
      //   time: null
      // }
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      // this.handleQuery();
      this.queryParams.pageNum = 1;
      this.logList = [];
      // this.getList();
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        courseName: "",
        classTime: null,
        className: "",
        clockCount: null,
        noClock: null,
        belated: null,
        leaved: null,
        time: null,
      };
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加打卡记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getLog(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改打卡记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateLog(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLog(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除打卡记录编号为"' + ids + '"的数据项？')
        .then(function () {
          return delLog(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const requestData = {
        // 将你的 classStudentLogDTO 对象数据放在这里
        className: this.queryParams.className,
        courseName: this.queryParams.courseName,
        time: this.queryParams.time,
        pageNum: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize,
      };
      this.download(
        "/clock_in/log/export",
        requestData,
        `${requestData.className}_${requestData.courseName}的打卡信息.xlsx`
      );
    },
    // 日期时间选择后，请求课堂和班级选项
    handleDateChange(val) {
      if (val) {
        // 假设调用后端API获取课堂和班级列表
        this.getClassAndGradeOptions(val);
      }
    },

    //请求后端接口获取课堂和班级列表
    getClassAndGradeOptions(dateTime) {
      this.form.time = dateTime;
      this.queryParams.time = dateTime;
      const classStudentLogDTO = {
        time: dateTime,
      };
      //这个是接口请求
      getClassAndCourse(classStudentLogDTO).then((response) => {
        if (response.data) {
          this.courseOptions = response.data.courseNames.map((courseName) => ({
            value: courseName,
            label: courseName,
          }));
          this.classOptions = response.data.classNames.map((className) => ({
            value: className,
            label: className,
          }));
          console.log("班级：", this.classOptions);
          console.log("课堂：", this.courseOptions);
        } else {
          console.error("没有返回数据");
        }
      });
    },
  },
};
</script>
<style scoped>
.custom-select .el-select-dropdown {
  max-height: 200px; /* 你可以根据需求调整这个高度 */
  overflow-y: auto;
}
</style>
