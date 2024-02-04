<template>
<div>
  <!--头部标签-->
  <el-breadcrumb separator="/">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item><a href="/">用户中心</a></el-breadcrumb-item>
  </el-breadcrumb>
  <!--          搜索框-->
  <div style="padding: 10px 0">
    <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
    <el-button class="ml-5" style="background-color: #409EFF;color: white" @click="load">搜索</el-button>
    <el-button class="ml-5" type="warning" @click="reset">重置</el-button>
  </div>

  <!--增删改查按钮-->
  <div style="margin: 10px 0">
    <el-button type="primary" size="medium" @click="handleAdd()">添加 <i class="el-icon-circle-plus-outline"></i></el-button>
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
<!--    <el-upload action="http://localhost:9090/user/import"-->
<!--               style="display: inline-block;" :show-file-list="false"-->
<!--               accept="xlsx" :on-success="handleImportSuccess">-->
<!--      <el-button type="primary" size="medium" class="ml-5">导入 <i class="el-icon-download"></i></el-button>-->
<!--    </el-upload>-->
<!--    <el-button type="primary" size="medium" @click="exp" class="ml-5">导出 <i class="el-icon-upload2"></i></el-button>-->
  </div>

  <el-table :data="tableData" border stripe
            row-key="id" default-expand-all  @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <!--            <el-table-column prop="id" label="id" width="80"></el-table-column>-->
    <el-table-column prop="name" label="名称" width="150"></el-table-column>
    <el-table-column prop="path" label="路径" width="150"></el-table-column>
    <el-table-column prop="pagePath" label="页面路径" width="150"></el-table-column>
    <el-table-column prop="icon" label="图标" width="150" align="center" class-name="fontSize18">
      <template slot-scope="scope">
        <span :class="scope.row.icon"></span>
      </template>
    </el-table-column>
    <el-table-column prop="description" label="描述" width="200"></el-table-column>
    <el-table-column label="操作" >
      <template slot-scope="scope">
        <el-button type="primary" size="small" @click="handleAdd(scope.row.id)" v-if="!scope.row.pid">新增子菜单 <i class="el-icon-menu"></i></el-button>
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



  <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="50%">
    <el-form label-width="100px">
      <el-form-item label="名称" :label-width="formLabelWidth">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="路径" :label-width="formLabelWidth">
        <el-input v-model="form.path" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="页面路径" :label-width="formLabelWidth">
        <el-input v-model="form.pagePath" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="图标" :label-width="formLabelWidth">
        <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
          <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
            <i :class="item.value" />{{ item.name }}
          </el-option>
        </el-select>
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
  name: "User",
  data(){
    return{
      tableData:[],
      total:5,
      name:"",
      path:"",
      icon:"",
      description:"",
      form:{},
      dialogFormVisible:false,
      multipleSelection:[],
      options:[]
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods:{
    load(){
      this.request.get("/menu",{
        params:{
          name:this.name
        }
      }).then(res =>{
        //将表格绑定的tableData属性赋值后端请求后的对象数组
        this.tableData = res.data
      })
    },
    reset(){
      this.name = ""
      this.path = ""
      this.icon = ""
      this.description = ""
      this.load()
    },
    handleAdd(pid){
      this.dialogFormVisible=true
      this.form={}
      if (pid){
        this.form.pid = pid
      }
    },
    save(){
      this.request.post("/menu",this.form).then(res =>{
        if (res.code === '200'){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible=true

      //请求数据
      this.request.get("/menu/icons").then(res =>{
        this.options = res.data
      })
    },
    handleDel(id){
      this.request.delete("/menu/" + id).then(res =>{
        if (res.code === '200'){
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
      this.request.post("/menu/del/batch",ids).then(res =>{
        if (res.code === '200'){
          this.$message.success("批量删除成功")
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    }
  }
}
</script>

<style>
.fontSize18{
  font-size: 18px;
}
</style>