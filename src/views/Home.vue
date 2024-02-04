<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 40px">
      <el-col :span="6">
        <el-card style="color: #409EFF">
          <div><i class="el-icon-user"></i>用户数量</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F37335">
          <div><i class="el-icon-bank-card"></i>销售总量</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">
            ￥1000000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #8360c3">
          <div><i class="el-icon-coin"></i>收益总额</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">
            ￥200000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #2ebf91">
          <div><i class="el-icon-office-building"></i>店面数量</div>
          <div style="padding: 10px 0;text-align: center;font-weight: bold">
            20
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px; height: 400px"></div>
      </el-col>

      <el-col :span="12">
        <div id="pie" style="width: 500px; height: 400px"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
export default {
  name: "Home",
  data(){
    return{

    }
  },
  mounted() {//页面元素渲染之后再触发
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option;

    option = {
      title: {
        text: "各季度会员数量统计",
        subtext: '趋势图',
        left: 'center'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度","第二季度","第三季度","第四季度"]
      },
      yAxis: {
        type: 'value',
        minInterval: 1//设置成1保证坐标轴分割刻度显示成整数。
      },
      series: [
        {
          data: [],
          type: 'line'
        }
      ]
    };
    this.request.get("/echarts/members").then(res =>{
      //option.xAxis.data = res.data
      option.series[0].data = res.data

      myChart.setOption(option)
    })



    //饼图
    var pieOption = {
      title: {
        text: 'Referer of a Website',
        subtext: 'Fake Data',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: 'Access From',
          type: 'pie',
          radius: '50%',
          data: [
            { value: 1048, name: 'Search Engine' },
            { value: 735, name: 'Direct' },
            { value: 580, name: 'Email' },
            { value: 484, name: 'Union Ads' },
            { value: 300, name: 'Video Ads' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };
    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);
    pieChart.setOption(pieOption)
  }


}
</script>

<style scoped>

</style>