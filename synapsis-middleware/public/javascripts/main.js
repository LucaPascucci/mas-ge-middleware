var ctxMyChart = document.getElementById('myChart');
var ctxMixedChart = document.getElementById('mixedChart');
var myChart = new Chart(ctxMyChart, {
   type: 'bar',
   data: {
      labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
      datasets: [{
         label: '# of Votes',
         data: [12, 19, 3, 5, 2, 3],
         backgroundColor: [
            'rgba(255, 99, 132, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(255, 206, 86, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(153, 102, 255, 0.2)',
            'rgba(255, 159, 64, 0.2)'
         ],
         borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)'
         ],
         borderWidth: 1
      }]
   },
   options: {
      scales: {
         yAxes: [{
            ticks: {
               beginAtZero: true
            }
         }]
      }
   }
});

var mixedChart = new Chart(ctxMixedChart, {
   type: 'bar',
   data: {
      datasets: [{
         label: 'Bar Dataset',
         data: [10, 20, 30, 40]
      }, {
         label: 'Line Dataset',
         data: [50, 50, 50, 50],

         // Changes this dataset to become a line
         type: 'line'
      }],
      labels: ['January', 'February', 'March', 'April']
   },
   options: {
      tooltips: {
         mode: 'index'
      }
   }
});
