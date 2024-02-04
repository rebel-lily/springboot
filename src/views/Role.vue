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
<!--    <el-upload action="http://localhost:9090/user/import"-->
<!--               style="display: inline-block;" :show-file-list="false"-->
<!--               accept="xlsx" :on-success="handleImportSuccess">-->
<!--      <el-button type="primary" size="medium" class="ml-5">导入 <i class="el-icon-download"></i></el-button>-->
<!--    </el-upload>-->
<!--    <el-button type="primary" size="medium" @click="exp" class="ml-5">导出 <i class="el-icon-upload2"></i></el-button>-->
  </div>

  <el-table :data="tableData" border stripe  @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <!--            <el-table-column prop="id" label="id" width="80"></el-table-column>-->
    <el-table-column prop="name" label="名称" width="250"></el-table-column>
    <el-table-column prop="description" label="描述" width="250"></el-table-column>
    <el-table-column label="操作" >
      <template slot-scope="scope">
        <el-button type="primary" size="small" @click="selectMenu(scope.row)">分配 <i class="el-icon-menu"></i></el-button>
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

  <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="50%">
    <el-form label-width="100px">
      <el-form-item label="用户名" :label-width="formLabelWidth">
        <el-input v-model="form.name" autocomplete="off"></el-input>
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

  <el-dialog title="菜单信息" :visible.sync="menuDialogVis" width="50%">
    <el-tree
        :props="props"
        :data="menuData"
        show-checkbox
        node-key="id"
        :default-expanded-keys="expends"
        :default-checked-keys="checks"
        ref="tree">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span><i :class="data.icon"></i>{{ data.name }}</span>
      </span>
    </el-tree>
    <div slot="footer" class="dialog-footer">
      <el-button @click="menuDialogVis = false">取 消</el-button>
      <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
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
      pageNum:0,
      pageSize:5,
      name:"",
      description:"",
      form:{},
      dialogFormVisible:false,
      multipleSelection:[],
      menuDialogVis:false,
      menuData:[],
      props:{
        label:'name'
      },
      expends:[],
      checks:[],
      roleId:0,
      roleFlag:''
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods:{
    load(){
      this.request.get("/role/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          name:this.name,
          description:this.description
        }
      }).then(res =>{
        //将表格绑定的tableData属性赋值后端请求后的对象数组
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset(){
      this.name = ""
      this.description = ""
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
      this.request.post("/role",this.form).then(res =>{
        if (res.code === '200'){
          this.$message.success("保存成功")
          this.dialogFormVisible=false
          this.load()
        }else {
          this.$message.error("保存失败")
        }
      })
    },
    saveRoleMenu(){
      this.request.post("/role/roleMenu/" + this.roleId,this.$refs.tree.getCheckedKeys()).then(res =>{
        if (res.code === '200'){
          this.$message.success("绑定成功")
          this.menuDialogVis=false

          //操作管理员角色后需要重新登录
          if (this.roleFlag === 'ADMIN'){
            this.$store.commit("logout")
          }
        }else {
          this.$message.error("绑定失败")
        }
      })
    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible=true
    },
    handleDel(id){
      this.request.delete("/role/" + id).then(res =>{
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
      this.request.post("/role/del/batch",ids).then(res =>{
        if (res.code === '200'){
          this.$message.success("批量删除成功")
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    exp(){
      window.open("http://localhost:9090/role/export")
    },
    handleImportSuccess(){
      this.$message.success("文件导入成功")
      this.load()
    },
  selectMenu(role){
    this.roleId = role.id
    this.roleFlag = role.flag

    //请求菜单数据
    this.request.get("/menu",{
      params:{
        name:'',
      }
    }).then(res =>{
      this.menuData = res.data
      //把后台的数组返回成id数组
      this.expends = this.menuData.map(v => v.id)
    })

    this.request.get("/role/roleMenu/" + this.roleId,{
      params:{
        name:'',
      }
    }).then(res =>{
      this.checks = res.data
    })

    this.menuDialogVis = true
    this.request.get("/menu/ids").then(r =>{
      const ids = r.data
      ids.forEach(id =>{
        //this.menuDialogVis = true如果放在后面可能会报错，$refs.tree未定义，建议放在开头先打开弹窗再渲染元素
        if (!this.checks.includes(id)){
          this.$refs.tree.setChecked(id,false)
        }
      })
    })
   },
  }
}
</script>

<style>

</style>