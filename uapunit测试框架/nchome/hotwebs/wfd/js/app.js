// Filename: app.js
define([
  'jquery',
  'underscore',
  'backbone',
  'bootstrap',
  'bootbox',
  'highcharts',
  'fullcalendar'
  ], function() {
    $("#logout").click(function(){
      var date=new Date();
      date.setTime(date.getTime()-10000);
      document.cookie="name=a;expires="+date.toGMTString();
      $("#user-name").text(document.cookie);
      window.location.href="login.html";
      });
    $(document).ready(function(){
      //editables
      $("#user-name").text(document.cookie.substring(5,document.cookie.length));
      
      });
  var initialize = function() {
      // Pass in our Router module and call it's initialize function
      //Router.initialize();

      // var now = new Date;
      // console.log("init at:" + now.getTime());

      
      $(function() {

        // var now = new Date;
        // console.log("document ready at :" + now.getTime());

        /**   carousel  ***/
        $('#myCarousel').carousel({
          interval: 5000
        })

        /* ***********************
    charts 
    ************************* */
        var chart;
        chart = new Highcharts.Chart({
          chart: {
            renderTo: 'container0',
            type: 'spline'
          },
          title: {
            text: '2008-2012年财务收支'
          },
          subtitle: {
            text: '数据信息截至日期为:2012-07-27'
          },
          xAxis: {
            type: 'datetime',
            dateTimeLabelFormats: { // don't display the dummy year
              month: '%e. %b',
              year: '%b'
            }
          },
          yAxis: {
            title: {
              text: 'Snow depth (m)'
            },
            min: 0
          },
          tooltip: {
            formatter: function() {
              return '<b>' + this.series.name + '</b><br/>' + Highcharts.dateFormat('%e. %b', this.x) + ': ' + this.y + ' m';
            }
          },

          series: [{
            name: 'Winter 2007-2008',
            // Define the data points. All series have a dummy year
            // of 1970/71 in order to be compared on the same x axis. Note
            // that in JavaScript, months start at 0 for January, 1 for February etc.
            data: [
              [Date.UTC(1970, 9, 27), 0],
              [Date.UTC(1970, 10, 10), 0.6],
              [Date.UTC(1970, 10, 18), 0.7],
              [Date.UTC(1970, 11, 2), 0.8],
              [Date.UTC(1970, 11, 9), 0.6],
              [Date.UTC(1970, 11, 16), 0.6],
              [Date.UTC(1970, 11, 28), 0.67],
              [Date.UTC(1971, 0, 1), 0.81],
              [Date.UTC(1971, 0, 8), 0.78],
              [Date.UTC(1971, 0, 12), 0.98],
              [Date.UTC(1971, 0, 27), 1.84],
              [Date.UTC(1971, 1, 10), 1.80],
              [Date.UTC(1971, 1, 18), 1.80],
              [Date.UTC(1971, 1, 24), 1.92],
              [Date.UTC(1971, 2, 4), 2.49],
              [Date.UTC(1971, 2, 11), 2.79],
              [Date.UTC(1971, 2, 15), 2.73],
              [Date.UTC(1971, 2, 25), 2.61],
              [Date.UTC(1971, 3, 2), 2.76],
              [Date.UTC(1971, 3, 6), 2.82],
              [Date.UTC(1971, 3, 13), 2.8],
              [Date.UTC(1971, 4, 3), 2.1],
              [Date.UTC(1971, 4, 26), 1.1],
              [Date.UTC(1971, 5, 9), 0.25],
              [Date.UTC(1971, 5, 12), 0]
            ]
          }, {
            name: 'Winter 2008-2009',
            data: [
              [Date.UTC(1970, 9, 18), 0],
              [Date.UTC(1970, 9, 26), 0.2],
              [Date.UTC(1970, 11, 1), 0.47],
              [Date.UTC(1970, 11, 11), 0.55],
              [Date.UTC(1970, 11, 25), 1.38],
              [Date.UTC(1971, 0, 8), 1.38],
              [Date.UTC(1971, 0, 15), 1.38],
              [Date.UTC(1971, 1, 1), 1.38],
              [Date.UTC(1971, 1, 8), 1.48],
              [Date.UTC(1971, 1, 21), 1.5],
              [Date.UTC(1971, 2, 12), 1.89],
              [Date.UTC(1971, 2, 25), 2.0],
              [Date.UTC(1971, 3, 4), 1.94],
              [Date.UTC(1971, 3, 9), 1.91],
              [Date.UTC(1971, 3, 13), 1.75],
              [Date.UTC(1971, 3, 19), 1.6],
              [Date.UTC(1971, 4, 25), 0.6],
              [Date.UTC(1971, 4, 31), 0.35],
              [Date.UTC(1971, 5, 7), 0]
            ]
          }, {
            name: 'Winter 2010-2011',
            data: [
              [Date.UTC(1970, 9, 9), 0],
              [Date.UTC(1970, 9, 14), 0.15],
              [Date.UTC(1970, 10, 28), 0.35],
              [Date.UTC(1970, 11, 12), 0.46],
              [Date.UTC(1971, 0, 1), 0.59],
              [Date.UTC(1971, 0, 24), 0.58],
              [Date.UTC(1971, 1, 1), 0.62],
              [Date.UTC(1971, 1, 7), 0.65],
              [Date.UTC(1971, 1, 23), 0.77],
              [Date.UTC(1971, 2, 8), 0.77],
              [Date.UTC(1971, 2, 14), 0.79],
              [Date.UTC(1971, 2, 24), 0.86],
              [Date.UTC(1971, 3, 4), 0.8],
              [Date.UTC(1971, 3, 18), 0.94],
              [Date.UTC(1971, 3, 24), 0.9],
              [Date.UTC(1971, 4, 16), 0.39],
              [Date.UTC(1971, 4, 21), 0]
            ]
          }]
        });

        //3
        var colors = Highcharts.getOptions().colors,
          categories = ['MSIE', 'Firefox', 'Chrome', 'Safari', 'Opera'],
          name = 'Browser brands',
          data = [{
            y: 35.11,
            color: colors[0],
            drilldown: {
              name: 'MSIE versions',
              categories: ['MSIE 6.0', 'MSIE 7.0', 'MSIE 8.0', 'MSIE 9.0'],
              data: [1.85, 7.35, 20.06, 2.81],
              color: colors[0]
            }
          }, {
            y: 18.63,
            color: colors[1],
            drilldown: {
              name: 'Firefox versions',
              categories: ['Firefox 3.0', 'Firefox 3.5', 'Firefox 3.6', 'Firefox 4.0'],
              data: [1.03, 1.58, 9.12, 5.43],
              color: colors[1]
            }
          }, {
            y: 34.94,
            color: colors[2],
            drilldown: {
              name: 'Chrome versions',
              categories: ['Chrome 5.0', 'Chrome 6.0', 'Chrome 7.0', 'Chrome 8.0', 'Chrome 9.0', 'Chrome 10.0', 'Chrome 11.0', 'Chrome 12.0'],
              data: [0.12, 0.19, 0.12, 0.36, 0.32, 9.91, 0.50, 20.22],
              color: colors[2]
            }
          }, {
            y: 7.15,
            color: colors[3],
            drilldown: {
              name: 'Safari versions',
              categories: ['Safari 5.0', 'Safari 4.0', 'Safari Win 5.0', 'Safari 4.1', 'Safari/Maxthon', 'Safari 3.1', 'Safari 4.1'],
              data: [4.55, 1.42, 0.23, 0.21, 0.20, 0.19, 0.14],
              color: colors[3]
            }
          }, {
            y: 2.14,
            color: colors[4],
            drilldown: {
              name: 'Opera versions',
              categories: ['Opera 9.x', 'Opera 10.x', 'Opera 11.x'],
              data: [0.12, 0.37, 1.65],
              color: colors[4]
            }
          }];


        // Build the data arrays
        var browserData = [];
        var versionsData = [];
        for(var i = 0; i < data.length; i++) {

          // add browser data
          browserData.push({
            name: categories[i],
            y: data[i].y,
            color: data[i].color
          });

          // add version data
          for(var j = 0; j < data[i].drilldown.data.length; j++) {
            var brightness = 0.2 - (j / data[i].drilldown.data.length) / 5;
            versionsData.push({
              name: data[i].drilldown.categories[j],
              y: data[i].drilldown.data[j],
              color: Highcharts.Color(data[i].color).brighten(brightness).get()
            });
          }
        }

        // Create the chart
        chart = new Highcharts.Chart({
          chart: {
            renderTo: 'container1',
            type: 'pie'
          },
          title: {
            text: '同类产品市场占有率, 四月, 2011'
          },
          yAxis: {
            title: {
              text: 'Total percent market share'
            }
          },
          plotOptions: {
            pie: {
              shadow: false
            }
          },
          tooltip: {
            formatter: function() {
              return '<b>' + this.point.name + '</b>: ' + this.y + ' %';
            }
          },
          series: [{
            name: 'Browsers',
            data: browserData,
            size: '60%',
            dataLabels: {
              formatter: function() {
                return this.y > 5 ? this.point.name : null;
              },
              color: 'white',
              distance: -30
            }
          }, {
            name: 'Versions',
            data: versionsData,
            innerSize: '60%',
            dataLabels: {
              formatter: function() {
                // display only if larger than 1
                return this.y > 1 ? '<b>' + this.point.name + ':</b> ' + this.y + '%' : null;
              }
            }
          }]
        });

        //4
        chart = new Highcharts.Chart({
          chart: {
            renderTo: 'container2'
          },
          title: {
            text: '人员项目完成情况'
          },
          xAxis: {
            categories: ['Apples', 'Oranges', 'Pears', 'Bananas', 'Plums']
          },
          tooltip: {
            formatter: function() {
              var s;
              if(this.point.name) { // the pie chart
                s = '' + this.point.name + ': ' + this.y + ' fruits';
              } else {
                s = '' + this.x + ': ' + this.y;
              }
              return s;
            }
          },
          labels: {
            items: [{
              html: 'Total fruit consumption',
              style: {
                left: '40px',
                top: '8px',
                color: 'black'
              }
            }]
          },
          series: [{
            type: 'column',
            name: 'Jane',
            data: [3, 2, 1, 3, 4]
          }, {
            type: 'column',
            name: 'John',
            data: [2, 3, 5, 7, 6]
          }, {
            type: 'column',
            name: 'Joe',
            data: [4, 3, 3, 9, 0]
          }, {
            type: 'spline',
            name: 'Average',
            data: [3, 2.67, 3, 6.33, 3.33]
          }, {
            type: 'pie',
            name: 'Total consumption',
            data: [{
              name: 'Jane',
              y: 13,
              color: '#4572A7' // Jane's color
            }, {
              name: 'John',
              y: 23,
              color: '#AA4643' // John's color
            }, {
              name: 'Joe',
              y: 19,
              color: '#89A54E' // Joe's color
            }],
            center: [100, 80],
            size: 100,
            showInLegend: false,
            dataLabels: {
              enabled: false
            }
          }]
        });
        $('a.style').click(function() {
          var style = $(this).attr('href');
          $('.links-css').attr('href', 'css/' + style);
          return false;
        });

        // full calendar JS 
        /* initialize the external events
    -----------------------------------------------------------------*/

        $('#external-events div.external-event').each(function() {

          // create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
          // it doesn't need to have a start or end
          var eventObject = {
            title: $.trim($(this).text()) // use the element's text as the event title
          };

          // store the Event Object in the DOM element so we can get to it later
          $(this).data('eventObject', eventObject);

          // make the event draggable using jQuery UI
          $(this).draggable({
            zIndex: 999,
            revert: true,
            // will cause the event to go back to its
            revertDuration: 0 //  original position after the drag
          });

        });


        /* initialize the calendar
    -----------------------------------------------------------------*/

        var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();
        $('#calendar').fullCalendar({
          header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek,agendaDay'
          },
          selectable: true,
          selectHelper: true,
          select: function(start, end, allDay) {
            var title = prompt('Event Title:');
            if(title) {
              calendar.fullCalendar('renderEvent', {
                title: title,
                start: start,
                end: end,
                allDay: allDay
              }, true // make the event "stick"
              );
            }
            calendar.fullCalendar('unselect');
          },
          editable: true,
          monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
          monthNamesShort: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
          dayNamesShort: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
          weekMode: 'liquid',
          events: [{
            title: 'All Day Event',
            start: new Date(y, m, 1)
          }, {
            title: 'Long Event',
            start: new Date(y, m, d - 5),
            end: new Date(y, m, d - 2)
          }, {
            id: 999,
            title: 'Repeating Event',
            start: new Date(y, m, d - 3, 16, 0),
            allDay: false
          }, {
            id: 999,
            title: 'Repeating Event',
            start: new Date(y, m, d + 4, 16, 0),
            allDay: false
          }],
          droppable: true,
          // this allows things to be dropped onto the calendar !!!
          drop: function(date, allDay) { // this function is called when something is dropped
            // retrieve the dropped element's stored Event Object
            var originalEventObject = $(this).data('eventObject');

            // we need to copy it, so that multiple events don't have a reference to the same object
            var copiedEventObject = $.extend({}, originalEventObject);

            // assign it the date that was reported
            copiedEventObject.start = date;
            copiedEventObject.allDay = allDay;

            // render the event on the calendar
            // the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
            $('#calendar').fullCalendar('renderEvent', copiedEventObject, true);

            // is the "remove after drop" checkbox checked?
            if($('#drop-remove').is(':checked')) {
              // if so, remove the element from the "Draggable Events" list
              $(this).remove();
            }

          }
        });
        setControls();
        // Portlets (boxes)
        $(".column").sortable({
          connectWith: '.column',
          cancel: ".box-content",
          iframeFix: false,
          //forceHelperSize:true,
          //forcePlaceholderSizeType:true,
          items: 'div.box',
          opacity: 0.8,
          helper: 'original',
          revert: true,
          forceHelperSize: true,
          placeholder: 'box-placeholder round-all',
          forcePlaceholderSize: true,
          tolerance: 'pointer'
        });
        // Store portlet update (move) in cookie
        $(".column").bind('sortupdate', function() {
          $('.column').each(function() {
            //$.cookie(boxPositionCookiePrefix + $("body").attr("id") + ($(this).attr('id')), $(this).sortable('toArray'), { expires: cookieExpiration });
            console.log($(this).attr('id'));
          });
        });
        // Control funtion for portlet (box) buttons clicks

        function setControls(ui) {
          //$('[class="box-btn"][title="toggle"]').click(function() {
          $('.box-btn').click(function() {
            var e = $(this);
            //var p = b.next('a');
            // Control functionality
            switch(e.attr('title').toLowerCase()) {
            case 'config':
              widgetConfig(e);
              break;

            case 'toggle':
              widgetToggle(e);
              break;

            case 'close':
              widgetClose(e);
              break;
            }
          });
        }

        // Toggle button widget

        function widgetToggle(e) {
          // Make sure the bottom of the box has rounded corners
          e.parent().toggleClass("round-all");
          e.parent().toggleClass("round-top");

          // replace plus for minus icon or the other way around
          if(e.html() == "<i class=\"icon-plus\"></i>") {
            e.html("<i class=\"icon-minus\"></i>");
          } else {
            e.html("<i class=\"icon-plus\"></i>");
          }

          // close or open box  
          e.parent().next(".box-container-toggle").toggleClass("box-container-closed");

          // store closed boxes in cookie
          var closedBoxes = [];
          var i = 0;
          $(".box-container-closed").each(function() {
            closedBoxes[i] = $(this).parent(".box").attr("id");
            i++;
          });
          //$.cookie(closedBoxesCookiePrefix + $("body").attr("id"), closedBoxes, { expires: cookieExpiration });
          //Prevent the browser jump to the link anchor
          return false;

        }

        // Close button widget with dialog

        function widgetClose(e) {
          // get box element
          var box = e.parent().parent();

          // prompt user to confirm
          bootbox.confirm("是否关闭?", function(confirmed) {
            // remove box
            if(confirmed) box.remove();

            // store removal in cookie
            //$.cookie(deletedBoxesCookiePrefix + $("body").attr("id") + "_" + box.attr('id'), "yes", { expires: cookieExpiration });
          });
        }

        $('#box-close-modal .btn-success').click(function(e) {
          // e is the element that triggered the event
          console.log(e.target);
          // outputs an Object that you can then explore with Firebug/developer tool.
          // for example e.target.firstChild.wholeText returns the text of the button
        });

        // Modify button widget

        function widgetConfig(e) {
          // bootbox.dialog("Plenty of buttons...", [{
          //     "label" : "Success!",
          //     "class" : "success",
          //     "callback": function() {
          //         console.log("great success");
          //     }
          // }, {
          //     "label" : "Danger!",
          //     "class" : "danger",
          //     "callback": function() {
          //         console.log("uh oh, look out!");
          //     }
          // }, {
          //     "label" : "Click ME!",
          //     "class" : "primary",
          //     "callback": function() {
          //         console.log("Primary button");
          //     }
          // }]);
        }
      });
    } //end init

  return {
    initialize: initialize
  };
});