function ricerca(str) {
   var datalist=document.getElementById('ricerca-datalist');
   if(str.length==0){
       //toglie i suggerimenti esistenti
       datalist.innerHTML='';
       return;
   }
    var xmlHttpReq=new XMLHttpRequest();
    xmlHttpReq.responseType = "json";
    xmlHttpReq.onreadystatechange= function () {
        if(this.readyState==4 && this.status==200){
            
            datalist.innerHTML=''; //toglie i suggerimenti esistenti
            //console.log(this.response);
            for(var i in this.response){
                var option=document.createElement('option');
                option.value=this.response[i];
                //aggiunge option a datalist
                datalist.appendChild(option);
            }
        }

    }
    xmlHttpReq.open("GET","JSONSuggerimentiRicerca?query="+encodeURIComponent(str),true);
    xmlHttpReq.send();

}

