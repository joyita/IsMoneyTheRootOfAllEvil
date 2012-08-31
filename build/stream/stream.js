d3.json(
  'data.json',
  function (data) {

    // create the chart here with
    // the returned data

var n = 20, // number of layers
    m = 200, // number of samples per layer
    data = d3.layout.stack().offset("wiggle")(data),
    color = d3.interpolateRgb("#aad", "#556");

var width = 960,
    height = 500,
    mx = m - 1,
    my = d3.max(data, function(d) {
      return d3.max(d, function(d) {
        return d.y0 + d.y;
      });
    });

var area = d3.svg.area().interpolate("cardinal")
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