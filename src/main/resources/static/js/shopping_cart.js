$(document).ready(function () {
    $(".minusButton").on("click", function (evt) {
        evt.preventDefault();
        decreaseQuantity($(this));
    });
    $(".plusButton").on("click", function (evt) {
        evt.preventDefault();
        increaseQuantity($(this));
    });

    $(".link-remove").on("click", function (evt) {
        evt.preventDefault();
        removeFromCart($(this));
    })
    updateTotal();

});

function removeFromCart(link) {
    url = link.attr("href");

    $.ajax({
        type: "POST",
        url: url,

    }).done(function (response) {
        // alert(response);

        $("#modalTitle").text("Shopping Cart");
        if (response.includes("removed")) {
            $("#myModal").on("hide.bs.modal", function (e) {
                rowNumber = link.attr("rowNumber");
                removeBook(rowNumber);
                updateTotal();
            });
        }
        $("#modalBody").text(response);
        $("#myModal").modal();

    }).fail(function () {
        $("#modalTitle").text("Shopping Cart");
        $("#modalBody").text("Error while adding book to shopping cart");
        $("#myModal").modal();
    });

}

function removeBook(rowNumber) {
    rowId = "row" + rowNumber;
    $("#" + rowId).remove();
}

function decreaseQuantity(link) {
    bookId = link.attr("bookId");
    qtyInput = $("#quantity" + bookId);
    newQty = parseInt(qtyInput.val()) - 1;

    if (newQty > 0) {
        qtyInput.val(newQty);
        updateQuantity(bookId, newQty);
    }
}

function increaseQuantity(link) {
    bookId = link.attr("bookId");
    qtyInput = $("#quantity" + bookId);
    newQty = parseInt(qtyInput.val()) + 1;

    if (newQty < 10) {
        qtyInput.val(newQty);
        updateQuantity(bookId, newQty);
    }
}

function updateQuantity(bookId, quantity) {
    url = "/cart/update/" + bookId + "/" + quantity;
    console.log(url);
    $.ajax({
        type: "POST",
        url: url,
    }).done(function (newSubtotal) {
        console.log(newSubtotal);
        if (!newSubtotal){
            $("#modalTitle").text("Shopping Cart");
            $("#modalBody").text("Không đủ số lượng trong kho hàng");
            $("#myModal").modal();
        }
        else {
            updateSubtotal(newSubtotal, bookId);
            updateTotal();
        }
    }).fail(function () {
        $("#modalTitle").text("Shopping Cart");
        $("#modalBody").text("Error while adding book to shopping cart");
        $("#myModal").modal();
    });
};

function updateSubtotal(newSubtotal, bookId) {
    $("#subtotal" + bookId).text(newSubtotal).number(true,0,".",".");
};

function updateTotal() {
    total = 0;
    $('.bookSubtotal').each(function (index, element) {
        total = (total + parseFloat(element.innerHTML));
    });
    console.log(total);
    $('#totalAmount').text(total*1000 + " đ").addClass( "price" ).number(true,0,".",".");

};
