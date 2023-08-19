<template>
  <div>
    <q-card :class="$q.dark.isActive?'bg-dark':''" style="background-color: #FAFAFA">
      <q-card-section class="text-h6">
        Line Chart
      </q-card-section>
      <q-card-section>
        <ECharts ref="chart"
                 :option="options"
                 class="q-mt-md"
                 :resizable="true"
                 autoresize style="height: 250px;"
        />
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
import axios from 'axios'
import Env from 'src/env';
import ECharts from 'vue-echarts'
import * as echarts from 'echarts' // DONT DELETE IT !!!

export default {
  name: "LineChart",
  components: {
    ECharts
  },
  data() {
    const xAxisData = new Array(600).fill("未记录")
    const yAxisData = new Array(600).fill(0)

    const interval = setInterval(async () => {
      const response = await axios.get(`${Env.API_URI}/system/info`)
      console.log(response)
      const randomData = Math.random();
      this.updateData(randomData);
    }, 1000);

    return {
      interval,
      xAxisData,
      yAxisData,
      model: false,
      options: {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'line'
          },
          formatter: function (params) {
            return `Time: ${params[0].axisValue}</br>Used: ${params[0].data}`
          }
        },
        xAxis: {
          type: 'category',
          data: xAxisData,
          boundaryGap: false,
          axisTick: false,
          axisLine: false,
          axisLabel: false
        },
        yAxis: {
          type: 'value',
          splitLine: false,
          axisLine: false,
          axisTick: false,
          axisLabel: false,
        },
        lineStyle: {
          color: '#0E5FFF'
        },
        series: [
          {
            symbol: 'none',
            data: yAxisData,
            type: 'line',
            smooth: true,
          },
        ],
      },
      line_chart: null
    }
  },
  methods: {
    updateData(data) {
      const date = new Date();
      this.xAxisData.push(`${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}`);
      this.xAxisData.shift();
      this.yAxisData.push(data);
      this.yAxisData.shift();
    }
  },
  beforeUnmount() {
    clearInterval(this.interval);
  }
}
</script>

<style scoped>

</style>
