var area, dat1 = null,
dat2 = null

d3.json(
  'data.json',
  function (dat) {
    dat1 = d3.layout.stack().offset("wiggle")(dat[0]);
    dat2 = d3.layout.stack().offset("wiggle")(dat[1]);
    dat3 = d3.layout.stack().offset("wiggle")(dat[2]);
    dat4 = d3.layout.stack().offset("wiggle")(dat[3]);
    dat5 = d3.layout.stack().offset("wiggle")(dat[4]);
    dat6 = d3.layout.stack().offset("wiggle")(dat[5]);
    dat7 = d3.layout.stack().offset("wiggle")(dat[6]);
    // create the chart here with
    // the returned data
var n = 20, // number of layers
    m = 200, // number of samples per layer
    data = d3.layout.stack().offset("wiggle")(dat1)
    color = d3.interpolateRgb("#aad", "#556");

var width = 7500,
    height = 400,
    mx = m - 1,
    my = d3.max(data, function(d) {
      return d3.max(d, function(d) {
        return d.y0 + d.y;
      });
    });

area = d3.svg.area().interpolate("basis")
    .x(function(d) { return d.x * width / mx; })
    .y0(function(d) { return height - d.y0 * height / my; })
    .y1(function(d) { return height - (d.y + d.y0) * height / my; });

var vis = d3.select("#chart")
  .append("svg")
    .attr("width", width)
    .attr("height", height);

vis.selectAll("path")
    .data(data)
  .enter().append("path")
    .style("fill", function(data, index) { 
                      var datum = data[0];
                      var colour = datum.colour;
                      return colour;
                  }
            )
    .attr("d", area);

 
  });

function transition(sin) {
 d3.select("#Title").text(sin);
  d3.selectAll("path")
      .data(function() {
        if(sin==='Lust') {
          return dat1;
        }
        if(sin==='Greed') {
          return dat2;
        } 
        if(sin==='Gluttony') {
          return dat3;
        } 
        if(sin==='Envy') {
          return dat4;
        }    
        if(sin==='Pride') {
          return dat5;
        }         
        if(sin==='Wrath') {
          return dat6;
        } 
        if(sin==='Sloth') {
          return dat7;
        } 
        
           })
    .transition()
      .duration(2500)
      .attr("d", area);
}
