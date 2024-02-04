<template>
  <div>
    <!--头部标签-->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item><a href="/">用户中心</a></el-breadcrumb-item>
    </el-breadcrumb>

    <!--搜索框-->
    <div style="padding: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="activityName"></el-input>
      <el-button class="ml-5" style="background-color: #409EFF;color: white" @click="load">搜索</el-button>
      <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
    </div>

    <!--增删改查按钮-->
    <div v-if="role === 'ADMIN'" style="margin: 10px 0">
      <el-button type="primary" size="medium" @click="handleAdd">添加 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定删除吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" size="medium" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <!--字段信息-->
    <el-table :data="tableData"  border stripe  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="activityName" label="活动名称" width="140"></el-table-column>
      <el-table-column prop="date" label="日期" width="160" :formatter="formatDate"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="organizer" label="主办方" width="80"></el-table-column>
      <el-table-column prop="theme" label="主题" width="80"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column>
        <template slot-scope="scope">

          <!--管理员按钮 start-->
          <el-button v-if="role === 'ADMIN'" type="success" size="small" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit-outline"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="handleDel(scope.row.id)"
          >
            <el-button v-if="role === 'ADMIN'" type="danger" size="small" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
          <!--管理员按钮 end-->

          <!--明天开始做-->
          <!--员工按钮 start-->
          <el-button v-if="role === 'EMPLOYEE'" type="warning" size="small" @click="handleCount(scope.row.id)">查看<i class="el-icon-remove-outline"></i></el-button>
          <!--员工按钮 end-->

          <!--普通用户按钮 start-->
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定参加吗？"
              @confirm="handleAttend(scope.row.id)"
          >
            <el-button v-if="role === 'USER'" type="success" size="small" slot="reference">参加<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定退出吗？"
              @confirm="handleAct(scope.row.id)"
          >
            <el-button v-if="role === 'USER'" type="danger" size="small" slot="reference">退出<i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
          <!--普通用户按钮 end-->

        </template>
      </el-table-column>
    </el-table>

    <!--          分页插件-->
    <div style="padding: 0 10px">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="活动信息" :visible.sync="dialogFormVisible" width="50%">
      <el-form label-width="100px">
        <el-form-item label="活动名称" :label-width="formLabelWidth">
          <el-input v-model="form.activityName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker v-model="form.date" autocomplete="off" value-format="yyyy-MM-dd" ></el-date-picker>
        </el-form-item>
        <el-form-item label="地址" :label-width="formLabelWidth">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="主办方" :label-width="formLabelWidth">
          <el-input v-model="form.organizer" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="主题" :label-width="formLabelWidth">
          <el-input v-model="form.theme" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述" :label-width="formLabelWidth">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Activity",
  data(){
    return{
      userId:JSON.parse(localStorage.getItem("user")).id,
      tableData:[],
      total:5,
      pageNum:0,
      pageSize:5,
      activityName:'',
      multipleSelection:[],
      form:{},
      dialogFormVisible:false,
      originalData: {},
      role:JSON.parse(localStorage.getItem("user")).role
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    //加载页面
    load() {
      this.request.get("/activity/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          activityName: this.activityName
        }
      }).then(res => {
        //将表格绑定的tableData属性赋值后端请求后的对象数组
        this.tableData = res.data.records
        console.log(this.tableData)
        this.total = res.data.total
      })
    },
    //重置页面
    reset() {
      this.activityName = ""
      this.load()
    },
    //批量删除
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)
      //delete不能传参数，改用post
      this.request.post("/activity/del/batch/", ids).then(res => {
        if (res.data) {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    //查看参加活动人数
    handleCount(id){
      this.request.post("/activity/countAct/" + id).then(res =>{
        const countAll = JSON.stringify(res.data)
        console.log(countAll)
        if (res.code === '200'){
          if ( countAll > 0){
            this.$message.success("参与活动人数:" + countAll)
          }else {
            this.$message.warning("该活动没有人参加")
          }
        }else {
          this.$message.error("活动人数参数错误")
        }
      })
    },
    //删除指定活动
    handleDel(id) {
      this.request.delete("/activity/" + id).then(res => {
        if (res.data) {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    //用户退出活动
    handleAct(id){
      this.request.delete("/activity/delAct/" + id + "/" + this.userId).then(res =>{
        if (res.data) {
          this.$message.success("退出成功")
          this.load()
        } else {
          this.$message.error("退出失败")
        }
      })
    },
    //用户参加活动
    async handleAttend(id){
      const isAttended = await this.isUserAttended(this.userId, id);
      if (isAttended) {
        console.log(id);
        this.$message.error("您已参加活动");
        return;
      } else {
        this.request.post("/activity/attend/" + id,this.userId).then(res =>{
          if(res.code === '200'){
            this.$message.success("参加成功")
          }else{
            this.$message.error("参加失败")
          }
        })
      }

    },
    //判断用户已参加活动
    isUserAttended(userId, activityId) {
      return new Promise((resolve, reject) => {
        this.request.post("/activity/isAttended/" + userId, activityId).then((res) => {
              if (res.code === "200") {
                resolve(true);
              } else {
                resolve(false);
              }
            })
            .catch((error) => {
              reject(error);
            });
      });
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    //修改活动信息
    handleEdit(row) {
      //this.from = row为浅拷贝
      //JSON.parse(JSON.stringify(row))为深拷贝解决表单未提交前端页面数据更改bug
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    //保存数据
    save() {
      this.request.post("/activity", this.form).then(res => {
        if (res.data) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    formatDate(row, column) {
      // 获取日期值
      let dateValue = row[column.property];

      if (dateValue) {
        //获取日期
        const date = new Date(dateValue);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");

        return `${year}-${month}-${day}`;
      } else {
        return "";
      }
    }
  }
}
</script>

<style scoped>

</style>