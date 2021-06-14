function splitWords(top) {
    var node = top.firstChild, words, newNode, idCntr = 1, skipChild;
    var re = /\S/;
    while(node && node != top) {
        skipChild = false;
        // if text node, check for our text
        if (node.nodeType == 3) {
            if (re.test(node.nodeValue)) {
                newNode = null;
                words = node.nodeValue.split(" ");
                for (var i = 0; i < words.length; i++) {
                    if (words[i] === "") {
                        newNode = document.createTextNode(" ");
                        node.parentNode.insertBefore(newNode, node);
                    } else {
                        newNode = document.createElement("span");
						newNode.className = "customTextStyle";
                        newNode.id = "wordID-" + idCntr++;
                        newNode.innerHTML = htmlEncode(words[i])+" ";
                        node.parentNode.insertBefore(newNode, node);
//                        if (i < words.length - 1) {
//                            newNode = document.createTextNode(" ");
//                            node.parentNode.insertBefore(newNode, node);
//                        }
                    }
                }
                if (newNode) {
                    node.parentNode.removeChild(node);
                    node = newNode;
                    // don't go into the children of this node
                    skipChild = true;
                }
            }
        } else if (node.nodeType == 1) {
            if (node.tagName == "SCRIPT") {
                skipChild = true;
            }
        }        
        
        if (!skipChild && node.firstChild) {
            // if it has a child node, traverse down into children
            node = node.firstChild;
        } else if (node.nextSibling) {
            // if it has a sibling, go to the next sibling
            node = node.nextSibling;
        } else {
            // go up the parent chain until we find a parent that has a nextSibling
            // so we can keep going
            while ((node = node.parentNode) != top) {
                if (node.nextSibling) {
                    node = node.nextSibling;
                    break;
                }
            }
        }
    }
}

var htmlEncode = function (source, display, tabs) {
	var i, s, ch, peek, line, result,
		next, endline, push,
		spaces;
	
	// Stash the next character and advance the pointer
	next = function () {
		peek = source.charAt(i);
		i += 1;
	};
	
	// Start a new "line" of output, to be joined later by <br />
	endline = function () {
		line = line.join('');
		if (display) {
			// If a line starts or ends with a space, it evaporates in html
			// unless it's an nbsp.
			line = line.replace(/(^ )|( $)/g, '&nbsp;');
		}
		result.push(line);
		line = [];
	};
	
	// Push a character or its entity onto the current line
	push = function () {
		if (ch < ' ' || ch > '~') {
			line.push('&#' + ch.charCodeAt(0) + ';');
		} else {
			line.push(ch);
		}
	};
	
	// Use only integer part of tabs, and default to 4
	tabs = (tabs >= 0) ? Math.floor(tabs) : 4;
	
	result = [];
	line = [];

	i = 0;
	next();
	while (i <= source.length) { // less than or equal, because i is always one ahead
		ch = peek;
		next();
		
		// HTML special chars.
		switch (ch) {
		case '<':
			line.push('&lt;');
			break;
		case '>':
			line.push('&gt;');
			break;
		case '&':
			line.push('&amp;');
			break;
		case '"':
			line.push('&quot;');
			break;
		case "'":
			line.push('&#39;');
			break;
		default:
			// If the output is intended for display,
			// then end lines on newlines, and replace tabs with spaces.
			if (display) {
				switch (ch) {
				case '\r':
					// If this \r is the beginning of a \r\n, skip over the \n part.
					if (peek === '\n') {
						next();
					}
					endline();
					break;
				case '\n':
					endline();
					break;
				case '\t':
					// expand tabs
					spaces = tabs - (line.length % tabs);
					for (s = 0; s < spaces; s += 1) {
						line.push(' ');
					}
					break;
				default:
					// All other characters can be dealt with generically.
					push();
				}
			} else {
				// If the output is not for display,
				// then none of the characters need special treatment.
				push();
			}
		}
	}
	endline();
	
	// If you can't beat 'em, join 'em.
	result = result.join('<br />');

	if (display) {
		// Break up contiguous blocks of spaces with non-breaking spaces
		result = result.replace(/ {2}/g, ' &nbsp;');
	}
	
	// tada!
	return result;
};

$(function() {
  splitWords(document.body);
  });
