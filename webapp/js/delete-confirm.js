/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


function deleteImg(obj) {
    if (confirm("Are you sure to delete this file?") == true) {
        window.location='Delete?file_name=' + obj.getAttribute('data-file'); + '';
    } 
}