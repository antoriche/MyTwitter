$(function(){
	$.ajax({
		url: "/",
		method:"POST",
		data:{
			action : "isLogged"
		},
		success: function(resultat){
			console.log(resultat);
			if(resultat == true){
				$("#login").text("Oui");
			}else{
				$("#login").text("Non");
			}
		}
	});
});