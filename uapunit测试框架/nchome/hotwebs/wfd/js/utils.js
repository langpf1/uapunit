/*IE9在developer tools没有启动的状态下不存在"console"对象,
并且异常不会被抛出；异常出现后会导致程序不能正常运行;其他浏览器没有问题
*/
if (Function.prototype.bind && console && typeof console.log == "object") {
  [
    "log","info","warn","error","assert","dir","clear","profile","profileEnd"
  ].forEach(function (method) {
    console[method] = this.call(console[method], console);
  }, Function.prototype.bind);
}
function guidGenerator() {
    var S4 = function() {
       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    };
    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
}
//timestap
function IDGenserator(factor)

{
var Factor=factor||"ID";
var date = new Date(); var components = [ date.getYear(), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds(), date.getMilliseconds() ];
var id =defaultFactor +components.join("");
return id; }