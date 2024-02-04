<template>
  <div>
   <div style="margin: 10px 0">
    <el-upload action="http://localhost:9090/file/upload"
               style="display: inline-block;" :show-file-list="false"
               accept="xlsx" :on-success="handleFileUploadSuccess">
      <el-button type="primary" size="medium" class="ml-5">上传文件<i class="el-icon-top"></i></el-button>
    </el-upload>
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

   <el-table :data="tableData" border stripe  @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <!--            <el-table-column prop="id" label="id" width="80"></el-table-column>-->
    <el-table-column prop="name" label="文件名" ></el-table-column>
    <el-table-column prop="type" label="文件类型" ></el-table-column>
    <el-table-column prop="size" label="文件大小(kb)"></el-table-column>
    <el-table-column label="下载" >
      <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
      </template>
    </el-table-column>
    <el-table-column label="启用">
      <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="200" align="center">
      <template slot-scope="scope">
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
  </div>
</template>

<script>
export default {
  name: "File",
  data(){
    return{
      tableData:[],
      name:'',
      multipleSelection:[],
      pageNum:1,
      pageSize:10,
      total:0
    }
  },
  created() {
    this.load()
  },
  methods:{
    load(){
      this.request.get("/file/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          name:this.name
        }
      }).then(res =>{
        //将表格绑定的tableData属性赋值后端请求后的对象数组
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset(){
      this.name = " "
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
    handleDel(id){
      this.request.delete("/file/" + id).then(res =>{
        console.log(res.code)
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
      this.request.post("/file/del/batch",ids).then(res =>{
        if (res.code === '200'){
          this.$message.success("批量删除成功")
          this.load()
        }else {
          this.$message.error("删除失败")
        }
      })
    },
    handleFileUploadSuccess(){
        this.load()
    },
    download(url){
      window.open(url)
    },
    changeEnable(row){
      this.request.post("/file/update",row).then(res =>{
        if (res.code === '200'){
          this.$message.success("更新成功")
        }else {
          this.$message.error("更新失败")
        }
      })
    }
  }
}
</script>

<style scoped>

</style>