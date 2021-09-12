$(document).ready(function () {
    $("#btnAddToCart").on("click", function (e){
        addToCart();
    })
});

function addToCart(){
    quantity = $("#quantity" + bookId).val();

    console.log(quantity);
    url = "/add-to-cart/" + bookId + "/" + quantity;
    console.log(url);
    $.ajax({
        type: "POST",
        url: url,
    }).done(function (response){
        $("#modalTitle").text("Shopping Cart");
        console.log(response);
        $("#modalBody").text(response);
        $("#myModal").modal();
    }).fail(function (){
        $("#modalTitle").text("Shopping Cart");
        $("#modalBody").text("Error while adding book to shopping cart");
        $("#myModal").modal();
    });
};