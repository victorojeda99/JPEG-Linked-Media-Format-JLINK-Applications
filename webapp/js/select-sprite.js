/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

var img = document.getElementById("img");

img.addEventListener("click", function (e) {
    var original_width  = img.naturalWidth;
    var original_height = img.naturalHeight;
    img.x = img.getBoundingClientRect().left;
    img.y = img.getBoundingClientRect().top;
    var result_x = Math.trunc((e.clientX - img.x)*original_width/img.width);
    var result_y = Math.trunc((e.clientY - img.y)*original_height/img.height);
    if (result_x < 0){
        result_x = 0;
    } else if (result_x > original_width){
        result_x = original_width;
    }
    if (result_y < 0){
        result_y = 0;
    } else if (result_y > original_height){
        result_y = original_height;
    }
    document.getElementById("output_x").innerHTML = result_x + ' px';
    document.getElementById("output_y").innerHTML = result_y + ' px';
    document.getElementById("sprite_x").value = 
            Math.trunc(result_x*100/original_width);
    document.getElementById("sprite_y").value = 
            Math.trunc(result_y*100/original_height);
    document.getElementById("sprite").style.left = e.clientX +  
            document.body.scrollLeft + 
            document.documentElement.scrollLeft + 'px';
    document.getElementById("sprite").style.top = e.clientY +  
            document.body.scrollTop + 
            document.documentElement.scrollTop + 'px';
    document.getElementById("sprite").style.display = "block";
    document.getElementById("selectSprite_button").style.pointerEvents = "auto";
});

function getImgSize() {
    var original_width  = img.naturalWidth;
    var original_height = img.naturalHeight;
    document.getElementById("img_width").innerHTML = original_width + ' px';
    document.getElementById("img_height").innerHTML = original_height + ' px';
}

