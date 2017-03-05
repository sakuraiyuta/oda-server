$(document).ready(
  function() {
    var w = 800;
    var h = 800;

    var nodes = {{nodes}};
    var links = {{links}};

    var svg = d3.select("body")
    .append("svg")
    .attr({width: w, height: h});

    var force = d3.layout.force()
    .nodes(nodes)
    .links(links)
    .size([w, h])
    .linkStrength(0.1)
    .friction(0.9)
    .distance(400)
    .charge(-450)
    .gravity(0.1)
    .theta(0.8)
    .alpha(0.1)
    .start();

    var link = svg.selectAll("line")
    .data(links)
    .enter()
    .append("line")
    .style({stroke: "#ccc",
        "stroke-width": 1});
    var node = svg.selectAll("circle")
    .data(nodes)
    .enter()
    .append("circle")
    .attr({r: 40,
        opacity: 0.6})
    .style({fill: "red"})
    .call(force.drag);

    force.on("tick", function() {
      link.attr({x1: function(d) { return d.source.x; },
                 y1: function(d) { return d.source.y; },
                 x2: function(d) { return d.target.x; },
                 y2: function(d) { return d.target.y; }});
      node.attr({cx: function(d) { return d.x; },
                 cy: function(d) { return d.y; }});
      label.attr({x: function(d) { return d.x; },
                  y: function(d) { return d.y; }});
    });

    var label = svg.selectAll('text')
    .data(nodes)
    .enter()
    .append('text')
    .attr({"text-anchor":"middle",
        "fill":"black"})
    .style({"font-size":15})
    .text(function(d) { return d.name; });

//    console.log(nodes);
    svg.selectAll("circle").on("click", function(d){
        location.href = d.href;
//        var elm = d3.select(this);
//        console.log(elm, d);
      });
  }
);

