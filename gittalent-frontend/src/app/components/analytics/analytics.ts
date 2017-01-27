import { Component, OnInit } from '@angular/core';
import { Utility } from "../../utility";

declare var bootbox: any;

@Component({
    selector: 'analytics',
    templateUrl: './analytics.html',
})
export class AnalyticsComponent implements OnInit {

    public barChartOptions:any = {
      scaleShowVerticalLines: false,
      responsive: true
    };
    public barChartLabels:Array<any>;
    public barChartData:Array<any>;
    public barChartType:string = "bar";
    public barChartLegend:boolean = true;


    public chartData: Array<any>;
    public chartLabels: Array<any>;
    public chartOptions: any;
    public chartType: string;
    public isRequesting: boolean;
    public isRequestingPopLang: boolean;
    public isRequestingMonthlyLangTrend: boolean;


    public lineChartData:Array<any> = [];
    public lineChartLabelIds:Array<any> =["2016-01","2016-02","2016-03","2016-04","2016-05","2016-06","2016-07","2016-08","2016-09","2016-10","2016-01","2016-12"];
    public lineChartLabels:Array<any> = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December' ];
    public lineChartOptions:any = {
      animation: false,
      responsive: true
    };
    public lineChartLegend:boolean = true;
    public lineChartType:string = 'line';

  public constructor(private utility: Utility) {
        this.chartType = "pie";
        this.chartData = [];
        this.chartLabels = [];
        this.chartOptions = {
            tooltips: {
                callbacks: {
                    label: (tooltipItems, data) => {
                        console.log(tooltipItems);
                        console.log(data);
                        return data.labels[tooltipItems.index] + " Repositories: " +
                            data.datasets[tooltipItems.datasetIndex].data[tooltipItems.index];
                    }
                }
            }
        };
    }

    public ngOnInit() {
        // this.isRequesting = true;
        // this.utility.makeGetRequest("/analytics", []).then(result => {
        //     console.log(result.results);
        //     this.chartLabels = [];
        //     this.chartData = [];
        //     for(let i = 0; i < result.results.length; i++) {
        //         this.chartLabels.push(result.results[i].name);
        //         this.chartData.push(result.results[i].numberOfRepo);
        //     }
        //     console.log(this.chartLabels);
        //     console.log(this.chartData);
        //     this.isRequesting = false;
        // }, error => {
        //   this.isRequesting = false;
        //     console.error(error);
        // });
        // this.isRequestingPopLang = true;
        // this.utility.makeGetRequest("/analytics/languagePopularityRanking", []).then(result => {
        //   console.log(result.results);
        //   this.barChartLabels = [];
        //   this.barChartData = [];
        //   var data = [];
        //   for(let i = 0; i < result.results.length; i++) {
        //     data.push(result.results[i].commits);
        //     this.barChartLabels.push(result.results[i].language);
        //   }
        //   this.barChartData.push({"data": data,  label: 'Commits'});
        //   this.isRequestingPopLang = false;
        // }, error => {
        //   this.isRequestingPopLang = false;
        //   console.error(error);
        // });
        this.isRequestingMonthlyLangTrend = true;
        this.utility.makeGetRequest("/analytics/monthlyLangTrend", []).then(result => {
          console.log(result.results);
          this.lineChartData = [];
          for(let i = 0; i < result.results.length; i++) {
            var line = {"data" :[], "label":result.results[i].language};
            var data = [];
            for (var monthId of this.lineChartLabelIds) {
              var commits = 0;
              for(let j = 0; j < result.results[i].trend.length; j++) {
                if (result.results[i].trend[j].month == monthId) {
                  commits = result.results[i].trend[j].commits;
                }
              }
              data.push(commits);
            }
            line.data = data;
            this.lineChartData.push(line);
          }
          console.log(this.lineChartData);
          this.isRequestingMonthlyLangTrend = false;
        }, error => {
          this.isRequestingMonthlyLangTrend = false;
          console.error(error);
        });
    }

    public chartHovered(value: any) {

    }
    public chartClicked(value: any) {

    }


}
