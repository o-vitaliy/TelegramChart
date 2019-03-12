package ru.ovi.telegram.chart

import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.ovi.telegram.chart.data.AssetsFileReader
import ru.ovi.telegram.chart.data.ChartData
import ru.ovi.telegram.chart.data.Parser

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoadChartDataTask().execute();
    }

   inner class LoadChartDataTask: AsyncTask<Unit, Unit, List<ChartData>>(){
        override fun doInBackground(vararg params: Unit?): List<ChartData> {
            return Parser().parse(AssetsFileReader().parse(this@MainActivity, "chart_data.json"))
        }

       override fun onPostExecute(result: List<ChartData>?) {
           super.onPostExecute(result)
           result?.get(0)?.let {
               (findViewById<ChartView>(R.id.chartView)).setChartData(it)
           }
       }
    }
}
