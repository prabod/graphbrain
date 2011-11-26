// Super node
var SNode = function(id) {
    this.id = id;
    this.x = 0;
    this.y = 0;
    this.vX = 0;
    this.vY = 0;
    this.nodes = [];
    this.subNodes = [];
    this.parent = 'unknown';
}

SNode.prototype.moveTo = function(x, y, redraw) {
    redraw = typeof(redraw) !== 'undefined' ? redraw : true;
    this.x = x;
    this.y = y;
    this.x0 = this.x - (this.width / 2);
    this.y0 = this.y - (this.height / 2);
    this.x1 = this.x + (this.width / 2);
    this.y1 = this.y + (this.height / 2);

    $('div#' + this.id).css('left', (this.x - (this.width / 2)) + 'px');
    $('div#' + this.id).css('top', (this.y - (this.height / 2)) + 'px');
    
    // update positions for nodes contained in this super node
    for (var key in this.nodes) {
        this.nodes[key].updatePos();
    }

    if (redraw) {
        g.drawLinks();
    }
}

SNode.prototype.place = function() {
    var snode = document.createElement('div');
    
    var nodesCount = 0;
    for (var key in this.nodes) {
        if (this.nodes.hasOwnProperty(key))
            nodesCount++;
    }
    if (nodesCount > 1) {
        snode.setAttribute('class', 'snode');
    }
    else {
        snode.setAttribute('class', 'snode1');   
    }
    snode.setAttribute('id', this.id);
    
    var nodesDiv = document.getElementById("nodesDiv");
    nodesDiv.appendChild(snode);

    // place nodes contained in this super node
    for (var key in this.nodes) {
        this.nodes[key].place();
    }

    var width = $('div#' + this.id).outerWidth();
    var height = $('div#' + this.id).outerHeight();
    
    this.width = width;
    this.height = height;
    this.moveTo(this.x, this.y);

    var nodeObj = this;

    $("div#" + this.id).bind("mousedown", function(e) {
        if (uiMode === 'drag') {
            draggedNode = nodeObj;
            return false;
        }
        else {
            newLink = new Link(0, nodeObj, false, '...');
            newLink.tx = e.pageX;
            newLink.ty = e.pageY;
            return false;
        }
    });

    $("div#" + this.id).bind("click", function(e) {
        if (dragging) {
            dragging = false;
            return false;
        }
        else {
            return true;
        }
    });

    $("div#" + this.id).hover(
    function(e) {
        if (newLink) {
            newLink.targ = nodeObj;
        }
    },
    function(e) {});
}
