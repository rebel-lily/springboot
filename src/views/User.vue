<template>
<div>
  <!--头部标签-->
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item><a href="/">用户中心</a></el-breadcrumb-item>
  </el-breadcrumb>
  <!--          搜索框-->
  <div style="padding: 10px 0">
    <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
    <el-input style="width: 200px" placeholder="请输入邮箱" suffix-icon="el-icon-message" class="ml-5" v-model="email"></el-input>
    <el-input style="width: 200px" placeholder="请输入地址" suffix-icon="el-icon-location-outline" class="ml-5" v-model="address"></el-input>
    <el-button class="ml-5" style="background-color: #409EFF;color: white" @click="load">搜索</el-button>
    <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
  </div>

  <!--增删改查按钮-->
  <div style="margin: 10px 0">
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
    <el-upload action="http://localhost:9090/user/import"
               style="display: inline-block;" :show-file-list="false"
               accept="xlsx" :on-success="handleImportSuccess">
      <el-button type="primary" size="medium" class="ml-5">导入 <i class="el-icon-download"></i></el-button>
    </el-upload>
    <el-button type="primary" size="medium" @click="exp" class="ml-5">导出 <i class="el-icon-upload2"></i></el-button>
  </div>

  <!--字段信息-->
  <el-table :data="tableData" border stripe  @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <!--            <el-table-column prop="id" label="id" width="80"></el-table-column>-->
    <el-table-column prop="username" label="用户" width="140"></el-table-column>
    <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
    <el-table-column prop="email" label="邮箱"></el-table-column>
    <el-table-column prop="phone" label="手机" ></el-table-column>
    <el-table-column prop="address" label="地址"></el-table-column>
    <el-table-column prop="headName" label="负责人"></el-table-column>
    <el-table-column>
      <template slot-scope="scope">
        <el-button type="success" size="small" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit-outline"></i></el-button>
        <el-popconfirm
            class="ml-5"
            confirm-button-text='确定'
            cancel-button-text='我再想想'
            icon="el-icon-info"
            icon-color="red"
            title="您确定删除吗？"
            @confirm="handleDel(scope.row.id)"
        >
          <el-button type="danger" size="small" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
        </el-popconfirm>
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

  <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="50%">
    <el-form label-width="100px">
      <el-form-item label="用户名" :label-width="formLabelWidth">
        <el-input v-model="form.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="角色">
        <el-select clearable v-model="form.role" placeholder="请选择" style="width: 80%">
          <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="昵称" :label-width="formLabelWidth">
        <el-input v-model="form.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" :label-width="formLabelWidth">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话" :label-width="formLabelWidth">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址" :label-width="formLabelWidth">
        <el-input v-model="form.address" autocomplete="off"></el-input>
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
  name: "User",
  data(){
    return{
      name:JSON.parse(localStorage.getItem("user")).username,
      role:JSON.parse(localStorage.getItem("user")).role,
      tableData:[],
      total:5,
      pageNum:0,
      pageSize:5,
      username:"",
      email:"",
      address: "",
      form:{},
      dialogFormVisible:false,
      multipleSelection:[],
      roles:[]
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods:{
    load(){
      this.request.get("/user/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          username:this.username,
          email:this.email,
          address:this.address,
          name:this.name,
          role:this.role
        }
      }).then(res =>{
        //将表格绑定的tableData属性赋值后端请求后的对象数组
        this.tableData = res.data.records
        console.log(this.tableData)
        this.total = res.data.total
      })

      this.request.get("/role").then(res =>{
        this.roles = res.data
      })

    },
    reset(){
      this.username = ""
      this.email = ""
      this.address = ""
      this.load()
    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },
    handleAdd(){
      this.dialogFormVisible=true
      this.form={}
    },
    save(){
      this.request.post("/user",this.form).then(res =>{
        if (res.data){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    handleEdit(row){
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible=true
    },
    handleDel(id){
      this.request.delete("/user/" + id).then(res =>{
        if (res.data){
          this.$message.success("删除成功")
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val){
      this.multipleSelection = val
    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id)
      //delete不能传参数，改用post
      this.request.post("/user/del/batch/" , ids).then(res =>{
        if (res.data){
          this.$message.success("批量删除成功")
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    exp(){
      window.open("http://localhost:9090/user/export")
    },
    handleImportSuccess(){
      this.$message.success("文件导入成功")
      this.load()
    }
  }
}

</script>

<style>

</style>